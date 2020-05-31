package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU20 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.jpeg""", """.*.css""", """.*.ico""", """.*.js""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_8 = Map("Accept" -> "image/webp,*/*")

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(5)
	}

	object LoginOwner {
		val loginOwner = exec(
			http("LoginOwner")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(7)
		.exec(http("LoggedOwner")
			.post("/login")
			.headers(headers_0)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}

	object ListOwnerPickUpRequests {
		val listOwnerPickUpRequests = exec(http("ListOwnerPickUpRequests")
			.get("/owners/owner1/pick-up-requests")
			.headers(headers_0))
		.pause(4)
	}

	object CreatePickUpRequest {
		val createPickUpRequest= exec(http("FormCreatePickUpRequest")
			.get("/owners/1/pick-up-requests/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(4)
		.exec(http("CreatePickUpRequest")
			.post("/owners/1/pick-up-requests/new")
			.headers(headers_2)
			.formParam("description", "sadf")
			.formParam("petType", "cat")
			.formParam("physicalStatus", "asdf")
			.formParam("address", "asdf")
			.formParam("isAccepted", "false")
			.formParam("isClosed", "false")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}

	object Logout {
		val logout = exec(http("LogoutForm")
			.get("/logout")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(4)
		.exec(http("Logout")
			.post("/logout")
			.headers(headers_2)
			.formParam("_csrf", "${stoken}"))
		.pause(4)
	}

	object LoginVet {
		val loginVet = exec(
			http("LoginVet")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(7)
		.exec(http("LoggedVet")
			.post("/login")
			.headers(headers_0)
			.formParam("username", "vet1")
			.formParam("password", "v3t")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}

	object ListAllPickUpRequests {
		val listAllPickUpRequests = exec(http("ListAllPickUpRequests")
			.get("/vets/pick-up-requests")
			.headers(headers_0))
		.pause(5)
	}

	object AcceptPickUpRequest {
		val acceptPickUpRequest = exec(http("AcceptPickUpRequest")
			.post("/vets/pick-up-requests/5/update")
			.headers(headers_2)
			.formParam("description", "sadf")
			.formParam("petType", "cat")
			.formParam("physicalStatus", "asdf")
			.formParam("address", "asdf")
			.formParam("isAccepted", "true")
			.formParam("_isAccepted", "on")
			.formParam("contact", "asdf")
			.formParam("isClosed", "false")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
	}

	object DeletePickUpRequest {
		val deletePickUpRequest = exec(http("DeletePickUpRequest")
			.get("/vets/pick-up-requests/5/delete")
			.headers(headers_0))
		.pause(7)
	}

		object DeletePickUpRequestNoPermission {
		val deletePickUpRequestNoPermission = exec(http("DeletePickUpRequestNoPermission")
			.get("/vets/pick-up-requests/4/delete")
			.headers(headers_0))
		.pause(7)
	}


	val deletePickUpRequestScenario = scenario("DeletePickUpRequest").exec(Home.home,
														LoginOwner.loginOwner,
														ListOwnerPickUpRequests.listOwnerPickUpRequests,
														CreatePickUpRequest.createPickUpRequest,
														Logout.logout,
														LoginVet.loginVet,
														ListAllPickUpRequests.listAllPickUpRequests,
														AcceptPickUpRequest.acceptPickUpRequest,
														DeletePickUpRequest.deletePickUpRequest
														)
		
	val deletePickUpRequestNoPermissionScenario = scenario("DeletePickUpRequestNoPermission").exec(Home.home,
														LoginOwner.loginOwner,
														DeletePickUpRequestNoPermission.deletePickUpRequestNoPermission
														)
	


	setUp(deletePickUpRequestScenario.inject(rampUsers(9500) during (100 seconds)),
	      deletePickUpRequestNoPermissionScenario.inject(rampUsers(9500) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )

}
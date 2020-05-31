package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia16Escenarios extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(8)
	}
	object Login{
		val login = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(8).exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}") )
		.pause(6)
	}
	object PickUpRequests{
		val pickUpRequests = exec(http("PickUpRequests")
			.get("/owners/owner1/pick-up-requests")
			.headers(headers_0))
		.pause(10)
	}
	object CreatePickUpRequestSuccess{
		val createPickUpRequestSuccess = exec(http("CreatePickUpRequest")
			.get("/owners/1/pick-up-requests/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(5)
		.exec(http("PickUpRequestSuccess")
			.post("/owners/1/pick-up-requests/new")
			.headers(headers_3)
			.formParam("description", "Peticion de recogida")
			.formParam("petType", "dog")
			.formParam("physicalStatus", "Bueno")
			.formParam("address", "Calle Prueba")
			.formParam("isAccepted", "false")
			.formParam("isClosed", "false")
			.formParam("_csrf", "${stoken}") )
		.pause(29)
	}
	object CreatePickUpRequestFail{
		val createPickUpRequestFail = exec(http("CreatePickUpRequest")
			.get("/owners/1/pick-up-requests/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(5).exec(http("PickUpRequestFail")
			.post("/owners/1/pick-up-requests/new")
			.headers(headers_3)
			.formParam("description", "Peticion")
			.formParam("petType", "dog")
			.formParam("physicalStatus", "Bueno")
			.formParam("address", "")
			.formParam("isAccepted", "false")
			.formParam("isClosed", "false")
			.formParam("_csrf", "${stoken}") )
		.pause(8)
	}
	val successScn = scenario("Success").exec(Home.home,
									  Login.login,
									  PickUpRequests.pickUpRequests,
									  CreatePickUpRequestSuccess.createPickUpRequestSuccess)
	val failScn = scenario("Fail").exec(Home.home,
									  Login.login,
									  PickUpRequests.pickUpRequests,
									  CreatePickUpRequestFail.createPickUpRequestFail)


	setUp(
		successScn.inject(rampUsers(30) during (60 seconds)),
		failScn.inject(rampUsers(30) during (60 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
	
	
}
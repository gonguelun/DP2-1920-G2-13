package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU19 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.ico""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:68.0) Gecko/20100101 Firefox/68.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map("Accept" -> "image/webp,*/*")


	object Home {
	  val home = exec(http("Home")
			.get("/"))
		.pause(6)
	  
	}
	
	object Login {
	  val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
 .exec(http("Logged")
			.post("/login")
			.headers(headers_0)
			.formParam("username", "vet1")
			.formParam("password", "v3t")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}
	
	
	object PickUpList {
	  val pickUpList = exec(http("PickUpList")
			.get("/vets/pick-up-requests")
			.headers(headers_0))
		.pause(31)
	}
	
	object UpdatePickUpForm {
	  val updatePickUpForm = exec(http("UpdatePickUpForm")
			.get("/vets/pick-up-requests/3/update")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(21)
	.exec(http("Updated")
			.post("/vets/pick-up-requests/3/update")
			.headers(headers_0)
			.formParam("description", "prueba1")
			.formParam("petType", "cat")
			.formParam("physicalStatus", "prueba2")
			.formParam("address", "prueba3")
			.formParam("isAccepted", "true")
			.formParam("_isAccepted", "on")
			.formParam("contact", "prueba4")
			.formParam("isClosed", "false")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
	}
	
	object CantUpdateFinal {
	  val cantUpdateFinal = exec(http("CantUpdateFinal")
			.get("/vets/pick-up-requests/3/update")
			.headers(headers_0))
		.pause(15)
	}

	
	val scnCorrectUpdated = scenario("CorrectUpdated").exec(Home.home, Login.login,
	    PickUpList.pickUpList, UpdatePickUpForm.updatePickUpForm)
		
val scnCantUpdate = scenario("CantUpdate").exec(Home.home, Login.login,
	    PickUpList.pickUpList, CantUpdateFinal.cantUpdateFinal)
		
	setUp(scnCorrectUpdated.inject(rampUsers(3500) during (100 seconds)),
	      scnCantUpdate.inject(rampUsers(3500) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )
}
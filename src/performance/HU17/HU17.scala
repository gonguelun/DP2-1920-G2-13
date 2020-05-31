package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU17 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.ico""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:68.0) Gecko/20100101 Firefox/68.0")


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
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}

	
	object PickUpForm {
	  val pickUpForm = exec(http("PickUpForm")
			.get("/owners/1/pick-up-requests/new")
			check(css("input[name=_csrf]", "value").saveAs("stoken")))
	.exec(http("PickUpCreated")
			.post("/owners/1/pick-up-requests/new")
			.formParam("description", "pruebaaa")
			.formParam("petType", "bird")
			.formParam("physicalStatus", "dadffad")
			.formParam("address", "adfsfdfsdf")
			.formParam("isAccepted", "false")
			.formParam("isClosed", "false")
			.formParam("_csrf", "${stoken}"))
		.pause(16)
	}
	
	object PickUpList {
	  val pickUpList = exec(http("PickUpList")
			.get("/owners/owner1/pick-up-requests"))
		.pause(12)
	}
	
	object PickUpDeleted {
	  val pickUpDeleted = exec(http("PickUpDeleted")
			.get("/owners/owner1/pick-up-requests/5/delete"))
		.pause(36)
	}
	
	object DeletedOtherError {
	  val deletedOtherError = exec(http("DeleteOtherError")
			.get("/owners/owner2/pick-up-requests/1/delete"))
		.pause(12)
	}
	
		
val scnCorrectDeleted = scenario("CorrectDeleted").exec(Home.home, Login.login,
	    PickUpList.pickUpList, PickUpForm.pickUpForm, PickUpList.pickUpList, PickUpDeleted.pickUpDeleted)
		
val scnCantDelete = scenario("CantDelete").exec(Home.home, Login.login,
	    PickUpList.pickUpList, DeletedOtherError.deletedOtherError)
		
	setUp(scnCorrectDeleted.inject(rampUsers(2000) during (100 seconds)),
	      scnCantDelete.inject(rampUsers(2000) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )
}
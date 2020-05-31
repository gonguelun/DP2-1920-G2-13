package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU8 extends Simulation {

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
	
	object SearchBeautyCenters {
	  val searchBeautyCenters = exec(http("SearchBeautyCenters")
			.get("/owners/search-beauty-center"))
		.pause(15)
	}

	object SearchedBeautyCenter {
	  val searchedBeautyCenter = exec(http("SearchedBeautyCenter")
			.get("/owners/beauty-centers/5"))
		.pause(14)
	}
	
	object BookDateForm {
	  val bookDateForm = exec(http("BookDateForm")
			.get("/owners/owner1/beauty-centers/4/5/beauty-dates/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
.exec(http("NoPetError")
			.post("/owners/owner1/beauty-centers/4/5/beauty-dates/new")
			.formParam("description", "hahahaha")
			.formParam("startDate", "2020/06/29 19:00")
			.formParam("_products", "1")
		.formParam("_csrf", "${stoken}"))
		.pause(21)
	}
	
	object FindOwners {
	  val findOwners = exec(http("FindOwners")
			.get("/owners/find"))
		.pause(12)
	}
	
	object OwnerList {
	  val ownerList = exec(http("OwnerList")
			.get("/owners?lastName="))
		.pause(12)
	}
	
	object OwnerDetails {
	  val ownerDetails = exec(http("OwnerDetails")
			.get("/owners/1"))
		.pause(11)
	}
	
	object AddPetForm {
	  val addPetForm = exec(http("AddPetForm")
			.get("/owners/1/pets/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(15)
	.exec(http("PetAdded")
			.post("/owners/1/pets/new")
			.formParam("id", "")
			.formParam("name", "prueba")
			.formParam("birthDate", "2020/05/12")
			.formParam("type", "bird")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}
	
	object Booked {
	 val booked = exec(http("BookDateForm")
			.get("/owners/owner1/beauty-centers/4/5/beauty-dates/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(30)
		.exec(http("Booked")
			.post("/owners/owner1/beauty-centers/4/5/beauty-dates/new")
			.formParam("description", "jadjdjsjsjs")
			.formParam("startDate", "2020/06/29 19:00")
			.formParam("pet", "prueba - 14")
			.formParam("_products", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(5)
	}
	
		
  val scnCorrectBooked = scenario("CorrectBooked").exec(Home.home, Login.login,
	    FindOwners.findOwners, OwnerList.ownerList, OwnerDetails.ownerDetails, AddPetForm.addPetForm,
	    SearchBeautyCenters.searchBeautyCenters, SearchedBeautyCenter.searchedBeautyCenter, Booked.booked)
		
	val scnCantBook = scenario("CantBook").exec(Home.home, Login.login,
	    SearchBeautyCenters.searchBeautyCenters, SearchedBeautyCenter.searchedBeautyCenter, BookDateForm.bookDateForm)
		
	setUp(scnCorrectBooked.inject(rampUsers(500) during (100 seconds)),
	      scnCantBook.inject(rampUsers(500) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )
}
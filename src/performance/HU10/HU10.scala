package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU10 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.jpeg""", """.*.css""", """.*.ico""", """.*.js""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")


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
			.headers(headers_1)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	object SearchBeautyCenterForm {
		val searchBeautyCenterForm = exec(http("SearchBeautyCenterForm")
			.get("/owners/search-beauty-center")
			.headers(headers_0))
		.pause(6)
	}

	object BeautyCenterList {
		val beautyCenterList = exec(http("BeautyCenterList")
			.get("/owners/beauty-centers/1")
			.headers(headers_0))
		.pause(7)
	}

	val searchBeautyCenterFormScenario = scenario("SearchBeautyCenterFrom").exec(Home.home,
														LoginOwner.loginOwner,
														SearchBeautyCenterForm.searchBeautyCenterForm
														)
		
	val beautyCenterListScenario = scenario("BeautyCenterList").exec(Home.home,
														LoginOwner.loginOwner,  
														BeautyCenterList.beautyCenterList
														)
	


	setUp(searchBeautyCenterFormScenario.inject(rampUsers(9000) during (100 seconds)),
	      beautyCenterListScenario.inject(rampUsers(9000) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )
}
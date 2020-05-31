package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU9 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.jpeg""", """.*.css""", """.*.ico""", """.*.js""", """.*.png"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")



	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(5)
	}

	object LoginBeautician {
		val loginBeautician = exec(
			http("LoginBeautician")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("LoggedBeautician")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "f")
			.formParam("password", "f")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	object SearchAndListBeautyDates {
		val searchBeautyDates = exec(http("SearchBeautyDates")
			.get("/beauticians/searchBeautyDates/f")
			.headers(headers_0))
		.pause(9)		
		.exec(http("ListBeautyDates")
			.get("/beauticians/1/beautyDates/2021-01-01/16")
			.headers(headers_0))
		.pause(10)
	}

	object LoginNoBeauticianRol {
		val loginNoBeauticianRol = exec(
			http("LoginNoBeautician")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("LoggedNoBeautician")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	val searchAndShowBeautyDatesScenario = scenario("SearchAndShowBeautiyDates").exec(Home.home,
														LoginBeautician.loginBeautician,
														SearchAndListBeautyDates.searchBeautyDates)
		
	val searchAndShowBeautyDatesNoPermissionScenario = scenario("ListProducts").exec(Home.home,
														LoginNoBeauticianRol.loginNoBeauticianRol,
														SearchAndListBeautyDates.searchBeautyDates)
	


	setUp(searchAndShowBeautyDatesScenario.inject(rampUsers(9000) during (100 seconds)),
	      searchAndShowBeautyDatesNoPermissionScenario.inject(rampUsers(9000) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )


}
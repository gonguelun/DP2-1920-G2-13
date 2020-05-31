package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia11Escenarios extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(11)
	}
	object Login{
		val login = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(11).exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}") )
		.pause(9)
	}
	object SearchBeautyCenters{
		val searchBeautyCenters = exec(http("SearchBeautyCenters")
			.get("/owners/search-beauty-center")
			.headers(headers_0))
		.pause(16)	
		}
	object BeautyCentersCat{
		val beautyCentersCat = exec(http("BeautyCentersCat")
			.get("/owners/beauty-centers/1")
			.headers(headers_0))
		.pause(16)
	}
	object BookDateCorrect{
		val bookDateSuccess = exec(http("BookDate")
			.get("/owners/owner1/beauty-centers/1/1/beauty-dates/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(31).exec(http("DateBooked")
			.post("/owners/owner1/beauty-centers/1/1/beauty-dates/new")
			.headers(headers_3)
			.formParam("description", "Reserva")
			.formParam("startDate", "2020/06/24 16:00")
			.formParam("pet", "Leo - 1")
			.formParam("products", "prueba1 - 1")
			.formParam("_products", "1")
			.formParam("_csrf", "${stoken}") )
		.pause(201)
	}

	object BookDateFail{
		val bookDateFail = exec(http("BookDate")
			.get("/owners/owner1/beauty-centers/1/1/beauty-dates/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(31).exec(http("DateBookedFail")
			.post("/owners/owner1/beauty-centers/1/1/beauty-dates/new")
			.headers(headers_3)
			.formParam("description", "Reserva")
			.formParam("pet", "Leo - 1")
			.formParam("products", "prueba1 - 1")
			.formParam("_products", "1")
			.formParam("_csrf", "${stoken}") )
		.pause(12)
	}
	val successScn = scenario("Success").exec(Home.home,
									 	 Login.login,
									  	SearchBeautyCenters.searchBeautyCenters,
										  BeautyCentersCat.beautyCentersCat,
										  BookDateCorrect.bookDateSuccess)
	val failScn = scenario("Fail").exec(Home.home,
									 	Login.login,
										 SearchBeautyCenters.searchBeautyCenters,
										 BeautyCentersCat.beautyCentersCat,
										 BookDateFail.bookDateFail)
	

	setUp(
		successScn.inject(rampUsers(3500) during (100 seconds)),
		failScn.inject(rampUsers(3500) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
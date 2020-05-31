package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteBeautyDateOwnerDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.jpeg""", """.*.ico""", """.*.js"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(8)
	}

	object Login {
    val login = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(9)
    .exec(
      http("Logged")
        .post("/login")
        .headers(headers_3)
        .formParam("username", "owner1")
        .formParam("password", "0wn3r")        
        .formParam("_csrf", "${stoken}")
    ).pause(7)
  }

	object ShowBeautyDateForm {
		val showBeautyDateForm = exec(http("ShowBeautyDateForm")
			.get("/owners/owner1/beauty-dates")
			.headers(headers_0))
		.pause(9)
	}

	object DeleteBeautyDate {
		val deleteBeautyDate = exec(http("request_5")
			.get("/owners/owner1/beauty-dates/1/delete")
			.headers(headers_0))
		.pause(11)
	}

	object DeleteBeautyDateError {
		val deleteBeautyDateError = exec(http("DeleteBeautyDateError")
			.get("/owners/owner2/beauty-dates/2/delete")
			.headers(headers_0))
		.pause(3)
	}


	val ownersSuccessScn = scenario("OwnersSuccess").exec(Home.home,
													  Login.login,
													  ShowBeautyDateForm.showBeautyDateForm,
													  DeleteBeautyDate.deleteBeautyDate)
													  
	val ownersErrorScn = scenario("OwnersError").exec(Home.home,
													  Login.login,
													   ShowBeautyDateForm.showBeautyDateForm,
													   DeleteBeautyDateError.deleteBeautyDateError)

	setUp(
		ownersSuccessScn.inject(rampUsers(3500) during (100 seconds)),
		ownersErrorScn.inject(rampUsers(3500) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
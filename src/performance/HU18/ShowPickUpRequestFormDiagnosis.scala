package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ShowPickUpRequestFormDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.jpeg""", """.*.ico""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

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

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(8)
	}

	object LoginVet {
    val loginVet = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(9)
    .exec(
      http("Logged")
        .post("/login")
        .headers(headers_3)
        .formParam("username", "vet1")
        .formParam("password", "v3t")        
        .formParam("_csrf", "${stoken}")
    ).pause(6)
  }

	object ShowPickUpRequestForm {
		val showPickUpRequestForm = exec(http("ShowPickUpRequestForm")
			.get("/vets/pick-up-requests")
			.headers(headers_0))
		.pause(9)
	}

	object LoginOwner {
    val loginOwner = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(11)
    .exec(
      http("Logged")
        .post("/login")
        .headers(headers_3)
        .formParam("username", "owner1")
        .formParam("password", "0wn3r")        
        .formParam("_csrf", "${stoken}")
    ).pause(9)
  }

	object AccessDeniedOwner {
		val accessDeniedOwner = exec(http("AccessDeniedOwner")
			.get("/vets/pick-up-requests")
			.headers(headers_0))
		.pause(8)
	}




	val vetsScn = scenario("Vets").exec(Home.home,
										LoginVet.loginVet,
										ShowPickUpRequestForm.showPickUpRequestForm)
													  
	val ownersScn = scenario("Owners").exec(Home.home,
											LoginOwner.loginOwner,
											AccessDeniedOwner.accessDeniedOwner)


	setUp(
        vetsScn.inject(rampUsers(3000) during (100 seconds)),
		ownersScn.inject(rampUsers(3000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
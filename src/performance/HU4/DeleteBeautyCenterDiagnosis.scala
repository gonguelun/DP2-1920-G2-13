package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteBeautyCenterDiagnosis extends Simulation {

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

	object Login {
    val login = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(15)
    .exec(
      http("Logged")
        .post("/login")
        .headers(headers_3)
        .formParam("username", "admin1")
        .formParam("password", "4dm1n")        
        .formParam("_csrf", "${stoken}")
    ).pause(27)
  }

  object ShowBeauticianForm {
	  val showBeauticianForm = exec(http("ShowBeauticianForm")
			.get("/beauticians/principal/f")
			.headers(headers_0))
		.pause(40)
  }

  object DeleteBeautician {
	  val deleteBeautician = exec(http("DeleteBeautician")
			.get("/beauticians/1/beauty-centers/1/delete")
			.headers(headers_0))
		.pause(23)
  }

  object DeleteBeauticianError {
	  val deleteBeauticianError = exec(http("DeleteBeauticianError")
			.get("/beauticians/2/beauty-centers/2/delete")
			.headers(headers_0))
		.pause(15)
  }

	val beauticiansSuccessScn = scenario("BeauticiansSuccess").exec(Home.home,
													  Login.login,
													  ShowBeauticianForm.showBeauticianForm,
													  DeleteBeautician.deleteBeautician)
	val beauticiansErrorScn = scenario("BeauticiansError").exec(Home.home,
													  Login.login,
													  DeleteBeauticianError.deleteBeauticianError)

	setUp(
		beauticiansSuccessScn.inject(rampUsers(2500) during (100 seconds)),
		beauticiansErrorScn.inject(rampUsers(2500) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
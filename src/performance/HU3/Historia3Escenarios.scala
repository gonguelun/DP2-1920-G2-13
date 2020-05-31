package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia3EscenariosDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
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
		"Accept" -> "*/*",
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-CryptoAPI/10.0")

	val headers_5 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")
	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(9)
	}
	object Login{
		val login = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(3).exec(http("Logged")
			.post("/login")
			.headers(headers_5)
			.formParam("username", "f")
			.formParam("password", "f")
			.formParam("_csrf", "${stoken}") )
		.pause(14)
	}
	object Beautician{
		val beautician = exec(http("Beautician")
			.get("/beauticians/principal/f")
			.headers(headers_0))
		.pause(12)
	}
	object ModifyBeautyCenterSuccess{
		val modifyBeautyCenterSuccess = exec(http("ModifyBeautyCenterSuccess")
			.get("/beauticians/1/beauty-centers/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(21).exec(http("ModifiedSuccess")
			.post("/beauticians/1/beauty-centers/1/edit")
			.headers(headers_5)
			.formParam("name", "name2")
			.formParam("description", "hey")
			.formParam("petType", "cat")
			.formParam("_csrf", "${stoken}") )
		.pause(12)
	}
	object ModifyBeautyCenterFail{
		val modifyBeautyCenterFail = exec(http("ModifyBeautyCenterFail")
			.get("/beauticians/1/beauty-centers/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(14).exec(http("ModifiedFail")
			.post("/beauticians/1/beauty-centers/1/edit")
			.headers(headers_5)
			.formParam("name", "")
			.formParam("description", "hey")
			.formParam("petType", "cat")
			.formParam("_csrf", "${stoken}") )
			.pause(10)
	}


	val successScn = scenario("Success").exec(Home.home,
									  Login.login,
									  Beautician.beautician,
									  ModifyBeautyCenterSuccess.modifyBeautyCenterSuccess)
	val failScn = scenario("Fail").exec(Home.home,
									  Login.login,
									  Beautician.beautician,
									  ModifyBeautyCenterFail.modifyBeautyCenterFail)


	setUp(
		successScn.inject(rampUsers(4000) during (100 seconds)),
		failScn.inject(rampUsers(4000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
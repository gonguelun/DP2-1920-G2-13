package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Historia1EscenariosDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive")
	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(12)
	}
	object BeauticianRegisterSuccess{
		val beauticianRegisterSuccess = exec(http("BeauticianRegisterSuccess")
			.get("/users/new-beautician")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(33).exec(http("RegisterSuccess")
			.post("/users/new-beautician")
			.headers(headers_2)
			.formParam("firstName", "Beautician")
			.formParam("lastName", "Beautician")
			.formParam("user.username", "beautician")
			.formParam("user.password", "beautician")
			.formParam("Specializations", "cat")
			.formParam("Specializations", "dog")
			.formParam("_Specializations", "1")
			.formParam("_csrf", "${stoken}") )
		.pause(38)
	}
	object BeauticianRegisterFail{
		val beauticianRegisterFail = exec(http("BeauticianRegisterFail")
			.get("/users/new-beautician")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(33).exec(http("RegisterFail")
			.post("/users/new-beautician")
			.headers(headers_2)
			.formParam("firstName", "")
			.formParam("lastName", "Beautician")
			.formParam("user.username", "beautician")
			.formParam("user.password", "beautician")
			.formParam("Specializations", "cat")
			.formParam("Specializations", "dog")
			.formParam("_Specializations", "1")
			.formParam("_csrf", "${stoken}") )
		.pause(14)
	}
	val successScn = scenario("Success").exec(Home.home,
									  BeauticianRegisterSuccess.beauticianRegisterSuccess)
	val failScn = scenario("Fail").exec(Home.home,
									  BeauticianRegisterFail.beauticianRegisterFail)
	

	setUp(
		successScn.inject(rampUsers(5500) during (100 seconds)),
		failScn.inject(rampUsers(5500) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}
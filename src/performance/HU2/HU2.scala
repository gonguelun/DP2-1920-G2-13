package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU2 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.ico""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:68.0) Gecko/20100101 Firefox/68.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map("Accept" -> "image/webp,*/*")


	object Home {
	  val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(10)
	}
	
	object Login {
	  val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(12)
	.exec(http("BeauticianLogged")
			.post("/login")
			.headers(headers_0)
			.formParam("username", "f")
			.formParam("password", "f")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
	}
	
	object BeauticianInfo {
	  val beauticianInfo = exec(http("BeauticianInfo")
			.get("/beauticians/principal/f")
			.headers(headers_0))
		.pause(11)
	}

	object BeautyCenterForm {
	  val beautyCenterForm = exec(http("BeautyCenterForm")
			.get("/beauticians/1/beauty-centers/new")
			.headers(headers_0)
			 .check(css("input[name=_csrf]", "value").saveAs("stoken"))
			 ).pause(23)
.exec(http("BeautyCenterCreation")
			.post("/beauticians/1/beauty-centers/new")
			.headers(headers_0)
			.formParam("name", "PerformanceTest")
			.formParam("description", "Descripcion")
			.formParam("petType", "lizard")
			.formParam("_csrf", "${stoken}")
			).pause(15)
	}
	
	object CantCreate {
	  val cantCreate = exec(http("BeautyCenterForm")
			.get("/beauticians/1/beauty-centers/new")
			.headers(headers_0)
			 .check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(23)
.exec(http("CantCreate")
			.post("/beauticians/1/beauty-centers/new")
			.headers(headers_0)
			.formParam("name", "CasoError")
			.formParam("description", "ajajajaj")
.formParam("_csrf", "${stoken}"))		
.pause(17)
	}
	

	val scnCorrect = scenario("Correct").exec(Home.home, Login.login,
	    BeauticianInfo.beauticianInfo, BeautyCenterForm.beautyCenterForm)
		
	val scnCantCreate = scenario("CantCreate").exec(Home.home, Login.login, 
	    BeauticianInfo.beauticianInfo, CantCreate.cantCreate)
		
	setUp(scnCorrect.inject(rampUsers(1500) during (100 seconds)),
	      scnCantCreate.inject(rampUsers(1500) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )
}
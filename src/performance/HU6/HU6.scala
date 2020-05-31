package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU6 extends Simulation {

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
		            .pause(7)
  	}

	object Login {
	  val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
	.exec(http("Logged")
			.post("/login")
			.formParam("username", "f")
			.formParam("password", "f")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}
	
	object ShowBeautician {
	  val showBeautician = exec(http("ShowBeautician")
			.get("/beauticians/principal/f"))
		.pause(20)
	}
	
	object ProductList {
	  val productList = exec(http("ProductList")
			.get("/1/products"))
		.pause(13)
	}
	
	object ModifyProduct {
	  val modifyProduct = exec(http("ModifyProduct")
			.get("/%7BbeautyCenterId%7D/products/1/edit")
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(22)
.exec(http("ModifiedProduct")
			.post("/%7BbeautyCenterId%7D/products/1/edit")
			.formParam("name", "prueba1changed")
			.formParam("description", "prueba2")
			.formParam("type", "cat")
			.formParam("avaliable", "true")
			.formParam("_csrf", "${stoken}"))
		.pause(26)
	}
	
	object ModifiedError {
	   val modifiedError = exec(http("ModifyProduct")
			.get("/%7BbeautyCenterId%7D/products/1/edit")
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(22)
		.exec(http("ModifiedError")
			.post("/%7BbeautyCenterId%7D/products/1/edit")
			.formParam("name", "")
			.formParam("description", "")
			.formParam("type", "cat")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}

	val scnCorrectModified = scenario("CorrectModified").exec(Home.home, Login.login,
	    ShowBeautician.showBeautician, ProductList.productList, ModifyProduct.modifyProduct)
		
	val scnCantModify = scenario("CantModify").exec(Home.home, Login.login,
	    ShowBeautician.showBeautician, ProductList.productList, ModifiedError.modifiedError)
		
	setUp(scnCorrectModified.inject(rampUsers(2000) during (100 seconds)),
	      scnCantModify.inject(rampUsers(2000) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )
}
package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU5 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.jpeg""", """.*.css""", """.*.ico""", """.*.js""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_1 = Map("Origin" -> "http://www.dp2.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(40)
	}

	object Login {
		val login = exec(
			http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(40)
		.exec(http("LoggedBeautician")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "f")
			.formParam("password", "f")
			.formParam("_csrf", "${stoken}"))
		.pause(40)
	}

	object ShowBeautician {
		val showBeautician = exec(http("ShowBeautician")
			.get("/beauticians/principal/f"))
		.pause(40)
	}

	object CreateProductForm {
		val createProductForm = 		exec(http("CreateProductForm")
			.get("/beauticians/1/products/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(40)
		.exec(http("CreateProductConfirm")
			.post("/beauticians/1/products/new")
			.headers(headers_1)
			.formParam("name", "asdf")
			.formParam("description", "asdf")
			.formParam("type", "cat")
			.formParam("avaliable", "true")
			.formParam("_csrf", "${stoken}"))
		.pause(40)
	}

	object ListProducts {
		val listProducts = exec(http("ProductList")
			.get("/1/products"))
		.pause(40)
	}


	val createProductScenario = scenario("CreateProduct").exec(Home.home,
														Login.login, 
														ShowBeautician.showBeautician, 
														CreateProductForm.createProductForm)
		
	val listProductsScenario = scenario("ListProducts").exec(Home.home,
														Login.login, 
														ShowBeautician.showBeautician, 
														ListProducts.listProducts)
	


	setUp(createProductScenario.inject(rampUsers(5000) during (100 seconds)),
	      listProductsScenario.inject(rampUsers(5000) during (100 seconds))).protocols(httpProtocol)
	      
	      . assertions( global.responseTime.max.lt(5000),
	            global.responseTime.mean.lt(1000),
	            global.successfulRequests.percent.gt(95)
	            )
}
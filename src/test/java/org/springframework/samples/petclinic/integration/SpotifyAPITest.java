
package org.springframework.samples.petclinic.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.java.Log;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log
public class SpotifyAPITest {

	@LocalServerPort
	private int				port;

	public static String	accessToken	= "";


	@BeforeAll
	public static void authenticationSpotify() {
		String authToken = EncodeToken.getAuthToken("a50470cef9114d7caf7fb9c074bb12a8", "152f88761b544da380b3983636e72448");
		SpotifyAPITest.generateAccessToken(authToken);
	}

	private static void generateAccessToken(final String authToken) {
		RestAssured.baseURI = "https://accounts.spotify.com/api";

		Response response = RestAssured.given().header("Authorization", "Basic " + authToken).contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").log().all().when().post("token");

		SpotifyAPITest.accessToken = response.jsonPath().get("access_token");
	}

	@Test
	public void loadSearchPage() {
		RestAssured.get("http://localhost:" + this.port + "/api/spotify/query").then().statusCode(200);
	}

	@Test
	public void shouldShowAlbums() {

		RestAssured.when().get("http://localhost:" + this.port + "/api/spotify/search/farruko").then().statusCode(200);

	}

	@Test
	public void testSpotify() {
		String query = "farruko";
		RestAssured.given().auth().oauth2(SpotifyAPITest.accessToken).and().request().contentType(ContentType.JSON).log().all().response().log().all().when().get("https://api.spotify.com/v1/search?q=" + query + "&type=album&limit=5").then().statusCode(200)
			.assertThat().body("albums.items", Matchers.notNullValue());
	}

}

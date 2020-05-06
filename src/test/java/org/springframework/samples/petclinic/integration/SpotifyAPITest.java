
package org.springframework.samples.petclinic.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.restassured.RestAssured;
import lombok.extern.java.Log;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log
public class SpotifyAPITest {

	private static final String	SPOTIFY_OBTAIN_CODE		= "https://accounts.spotify.com/authorize";
	private static final String	SPOTIFY_CLIENT_ID		= "58c54265caac4e80aa3bfe97fa094be6";
	private static final String	SPOTIFY_MY_TRACKS_SCOPE	= "user-library-read";
	private static final String	REDIRECT_URI			= "http://localhost:8080/callback/";

	private static final String	SPOTIFY_BASE64CODE		= "NThjNTQyNjVjYWFjNGU4MGFhM2JmZTk3ZmEwOTRiZTY6ODA2OTM4ODI5NzU5NDY4NDk2MjE5ZmQ3MjQwN2RlY2I";
	private static final String	URL_SPOTIFY_TOKEN		= "https://accounts.spotify.com/api/token";
	private static final String	URL_SPOTIFY_MY_TRACKS	= "https://api.spotify.com/v1/me/tracks";

	@LocalServerPort
	private int					port;

	public static String		accessToken				= "";

	private final static String	AUTHPAGE				= "https://accounts.spotify.com/authorize?response_type=code&client_id=58c54265caac4e80aa3bfe97fa094be6&scope=user-library-read&redirect_uri=http://localhost:8080/callback/";
	private static WebClient	webClient;
	private static HtmlPage		page					= null;
	private static String		code;


	@BeforeAll
	public static void setUp() {
		WebSite.logIn("apitest123spotify@gmail.com", "apitest123");
		SpotifyAPITest.code = WebSite.getCode();
	}

	@AfterAll
	public static void tearDown() {
	}

	//    @Test
	//    public void testWithWrongCode() {
	//
	//    }

	//
	//	@BeforeAll
	//	public static void authenticationSpotify() {
	//		String authToken = EncodeToken.getAuthToken("a50470cef9114d7caf7fb9c074bb12a8", "152f88761b544da380b3983636e72448");
	//		SpotifyAPITest.generateAccessToken(authToken);
	//	}
	//
	//	private static void generateAccessToken(final String authToken) {
	//		RestAssured.baseURI = "https://accounts.spotify.com/api";
	//
	//		Response response = RestAssured.given().header("Authorization", "Basic " + authToken).contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").log().all().when().post("token");
	//
	//		SpotifyAPITest.accessToken = response.jsonPath().get("access_token");
	//	}
	//
	//		@BeforeEach
	//		public void antesTestes() {
	//			RestAssured.baseURI = "https://api.spotify.com";
	//			RestAssured.basePath = "/v1/";
	//		}
	//
	//	@Test
	//	public void testDefaultEvent() {
	//		RestAssured.when().get("http://localhost:" + this.port + "/api/spotify/access").then().assertThat().statusCode(200);
	//
	//	}
	//
	//	@Test
	//	public void testDefaultEvent2() {
	//		String body = RestAssured.given().queryParam("format", "json").when().get("http://localhost:" + this.port + "/api/spotify/access").then().log().all().and().assertThat().statusCode(200).and().extract().body().asString();
	//		System.out.println(body);
	//	}

	//	@Test
	//	public void testDefaultEvent3() {
	//		String body = RestAssured.given().given().auth().basic("apitest123spotify@gmail.com", "apitest123").queryParam("format", "json").and().when()
	//			.get(SpotifyAPITest.SPOTIFY_OBTAIN_CODE + "?response_type=code" + "&client_id=" + SpotifyAPITest.SPOTIFY_CLIENT_ID + "&scope=" + SpotifyAPITest.SPOTIFY_MY_TRACKS_SCOPE + "&redirect_uri=" + SpotifyAPITest.REDIRECT_URI).then().log().all().and()
	//			.assertThat().statusCode(200).and().extract().body().asString();
	//		System.out.println(body);
	//	}

	@Test
	public void testDefaultEvent3() {

		RestAssured.get("http://localhost:" + this.port + "/callback/?code=" + SpotifyAPITest.code).then().log().body().statusCode(200).body("items.name", Matchers.hasItem("Yo Perreo Sola"));

	}

	//	private String getCode() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	//		SpotifyAPITest.page = null;
	//		SpotifyAPITest.webClient = null;
	//		SpotifyAPITest.webClient = new WebClient(BrowserVersion.CHROME);
	//		SpotifyAPITest.webClient.getOptions().setRedirectEnabled(true);
	//		SpotifyAPITest.webClient.getOptions().setCssEnabled(true);
	//		SpotifyAPITest.webClient.setCssErrorHandler(new SilentCssErrorHandler());
	//		SpotifyAPITest.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	//		SpotifyAPITest.webClient.getOptions().setThrowExceptionOnScriptError(false);
	//		SpotifyAPITest.webClient.getOptions().setAppletEnabled(true);
	//		SpotifyAPITest.webClient.getOptions().setJavaScriptEnabled(true);
	//		SpotifyAPITest.webClient.getOptions().setTimeout(5000);
	//		new WebConnectionWrapper(SpotifyAPITest.webClient) {
	//
	//			@Override
	//			public WebResponse getResponse(final WebRequest request) throws IOException {
	//				WebResponse response = super.getResponse(request);
	//				try {
	//					String s = response.getResponseHeaderValue("Location");
	//					if (s.contains("code=")) {
	//						SpotifyAPITest.webClient.getOptions().setTimeout(100);
	//						SpotifyAPITest.code = s.substring(s.indexOf("=") + 1, s.indexOf("&"));
	//					} else {
	//						SpotifyAPITest.webClient.getOptions().setTimeout(5000);
	//					}
	//				} catch (Exception e) {
	//
	//				}
	//				return response;
	//			}
	//		};
	//		SpotifyAPITest.page = (HtmlPage) SpotifyAPITest.webClient.getPage(SpotifyAPITest.AUTHPAGE);
	//		SpotifyAPITest.webClient.waitForBackgroundJavaScript(50000);
	//		HtmlAnchor a = SpotifyAPITest.page.getAnchorByText("Log in to Spotify");
	//		SpotifyAPITest.page = a.click();
	//		SpotifyAPITest.webClient.waitForBackgroundJavaScript(50000);
	//		HtmlInput username = SpotifyAPITest.page.getHtmlElementById("login-username");
	//		username.setValueAttribute("apitest123spotify@gmail.com");
	//		HtmlInput password = SpotifyAPITest.page.getHtmlElementById("login-password");
	//		password.setValueAttribute("apitest123");
	//		SpotifyAPITest.webClient.getOptions().setRedirectEnabled(true);
	//		HtmlButton login = (HtmlButton) SpotifyAPITest.page.getFirstByXPath("//button[text()='Log In']");
	//		SpotifyAPITest.page = login.click();
	//		SpotifyAPITest.webClient.waitForBackgroundJavaScript(50000);
	//		SpotifyAPITest.webClient.getOptions().setRedirectEnabled(true);
	//		HtmlButton b = SpotifyAPITest.page.getFirstByXPath("//button[text()='Okay']");
	//		SpotifyAPITest.page = b.click();
	//		SpotifyAPITest.webClient.waitForBackgroundJavaScript(50000);
	//		SpotifyAPITest.page = (HtmlPage) SpotifyAPITest.webClient.getPage(SpotifyAPITest.AUTHPAGE);
	//		SpotifyAPITest.webClient.waitForBackgroundJavaScript(50000);
	//		System.out.println("code");
	//		return SpotifyAPITest.code;
	//
	//	}

	//	@Test
	//	public void getCode() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	//
	//		WebClient webClient = new WebClient(BrowserVersion.CHROME);
	//		webClient.getOptions().setJavaScriptEnabled(false);
	//		webClient.waitForBackgroundJavaScriptStartingBefore(5000);
	//
	//		try {
	//			final HtmlPage page = webClient
	//				// https://accounts.spotify.com/authorize?response_type=code&client_id=58c54265caac4e80aa3bfe97fa094be6&scope=user-library-read&redirect_uri=http://localhost:8080/callback/
	//				.getPage(SpotifyAPITest.SPOTIFY_OBTAIN_CODE + "?response_type=code" + "&client_id=" + SpotifyAPITest.SPOTIFY_CLIENT_ID + "&scope=" + SpotifyAPITest.SPOTIFY_MY_TRACKS_SCOPE + "&redirect_uri=" + SpotifyAPITest.REDIRECT_URI);
	//			//.getPage(
	//			//	"https://accounts.spotify.com/es-ES/login?continue=https:%2F%2Faccounts.spotify.com%2Fauthorize%3Fscope%3Duser-library-read%26response_type%3Dcode%26redirect_uri%3Dhttp%253A%252F%252Flocalhost%253A8080%252Fcallback%252F%26client_id%3D58c54265caac4e80aa3bfe97fa094be6");
	//			HtmlInput username = page.getHtmlElementById("login-username");
	//			username.setValueAttribute("apitest123spotify@gmail.com");
	//			HtmlInput password = page.getHtmlElementById("login-password");
	//			password.setValueAttribute("apitest123");
	//			webClient.getOptions().setRedirectEnabled(true);
	//			HtmlButton login = (HtmlButton) page.getFirstByXPath("//button[text()='Log In']");
	//			page = login.click();
	//			webClient.waitForBackgroundJavaScript(50000);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		} catch (FailingHttpStatusCodeException expectedFailCode) {
	//			String oauth_code = StringUtils.substringBetween(expectedFailCode.getMessage(), "code=", "#_");
	//			System.out.println(oauth_code);
	//		} finally {
	//			webClient.close();
	//		}
	//
	//	}

	//
	//
	//	public void shouldReturnPlaylistsOfUser() {
	//
	//		Response responsePlaylistsOfUser = RestAssured.given().auth().oauth2(SpotifyAPITest.accessToken).accept(ContentType.JSON).when().get("https://api.spotify.com/v1/me/tracks").then().log().body().statusCode(200)
	//			.body("items.name", Matchers.hasItem("Yo Perreo Sola")).extract().response();
	//
	//		List<HashMap> tracks = responsePlaylistsOfUser.jsonPath().getList("items");
	//
	//		this.printTracks(tracks);
	//	}
	//
	//	private void printTracks(final List<HashMap> tracks) {
	//		List<String> tracksMine = new ArrayList<>();
	//		for (HashMap track : tracks) {
	//			String trackName = track.get("name").toString();
	//
	//			tracksMine.add(trackName);
	//
	//		}
	//		tracksMine.forEach(System.out::println);
	//	}
	//
	//	@Test
	//	public void shouldGetCode() {
	//		String res = RestAssured.given().auth().form("apitest123spotify@gmail.com", "apitest123", new FormAuthConfig()).when()
	//			.get(SpotifyAPITest.SPOTIFY_OBTAIN_CODE + "?response_type=code" + "&client_id=" + SpotifyAPITest.SPOTIFY_CLIENT_ID + "&scope=" + SpotifyAPITest.SPOTIFY_MY_TRACKS_SCOPE + "&redirect_uri=" + SpotifyAPITest.REDIRECT_URI).jsonPath().get("code");
	//		System.out.println(res);
	//	}

}

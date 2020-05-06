
package org.springframework.samples.petclinic.integration;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

public class WebSite {

	private final static String	AUTHPAGE	= "https://accounts.spotify.com/authorize?response_type=code&client_id=58c54265caac4e80aa3bfe97fa094be6&scope=user-library-read&redirect_uri=http://localhost:8080/callback/";
	private static WebClient	webClient;
	private static HtmlPage		page		= null;
	private static String		code;


	/**
	 * Main method for logging into spotify
	 *
	 * @param un
	 * @param pw
	 * @see checkState()
	 * @see setUp()
	 * @return error message String, null if successful
	 */
	public static String logIn(final String un, final String pw) {
		WebSite.code = "";
		try {
			WebSite.setUp();
			WebSite.openPage();
			int i = 0;
			while (i < 14) {
				int state = WebSite.checkState();
				if (state == 1) {
					WebSite.connectToSpotify();
					i += 1;
				} else if (state == 2) {
					i += 1;
					WebSite.logInToSpotify(un, pw);
				} else if (state == 3) {
					i += 0;
					WebSite.authorize();
				} else if (state == 4) {
					WebSite.openPage();
					i += 1;
				} else if (state == 6) {
					return "Incorrect username or password";
				} else if (state == 7) {
					WebSite.openPage();
					i += 1;
				} else if (state == 5) {
					break;
				}
			}
			if (WebSite.code.equals("")) {
				return "Unable to connect to Spotify. Try again later.";
			}
			return "";
		} catch (Exception e) {
			if (WebSite.code.equals("")) {
				return "Something went wrong. Try again later.";
			} else {
				WebSite.webClient.close();
				return "";
			}
		}
	}

	/**
	 * Checks the current state of the logging in -process and returns the
	 * corresponding integer
	 */
	private static int checkState() {
		try {
			if (!WebSite.code.equals("")) {
				return 5;
			}
			String url = WebSite.page.getUrl().toString();
			if (url.contains("callback?code=")) {
				WebSite.code = url.substring(url.indexOf("?") + 1, url.indexOf("&"));
				return 5;
			}
			HtmlSpan s = WebSite.page.getFirstByXPath("//span[text()='Incorrect username or password.']");
			if (s != null) {
				return 6;
			}
			s = WebSite.page.getFirstByXPath("//span[text()='Your request failed. Please try again.']");
			if (s != null) {
				return 7;
			}
			HtmlAnchor a = (HtmlAnchor) WebSite.page.getFirstByXPath("//a[text()='Log in to Spotify']");
			if (a != null) {
				return 1;
			}
			HtmlButton login = (HtmlButton) WebSite.page.getFirstByXPath("//button[text()='Log In']");
			if (login != null) {
				return 2;
			}
			login = WebSite.page.getFirstByXPath("//button[text()='Okay']");
			if (login != null) {
				return 3;
			}
			a = (HtmlAnchor) WebSite.page.getFirstByXPath("//a[text()='Account Settings']");
			if (a != null) {
				return 4;
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	/**
	 * The first step in the login-process, opens the spotify website.
	 */
	private static void openPage() throws Exception {
		WebSite.page = (HtmlPage) WebSite.webClient.getPage(WebSite.AUTHPAGE);
		WebSite.webClient.waitForBackgroundJavaScript(50000);
	}

	/**
	 * The second step in the login-process, clicks the log in button
	 */
	private static void connectToSpotify() throws Exception {
		HtmlAnchor a = WebSite.page.getAnchorByText("Log in to Spotify");
		WebSite.page = a.click();
		WebSite.webClient.waitForBackgroundJavaScript(50000);
	}

	/**
	 * The third step in the login-process, inputs the user data and logs in
	 */
	private static void logInToSpotify(final String un, final String pw) throws Exception {
		HtmlInput username = WebSite.page.getHtmlElementById("login-username");
		username.setValueAttribute(un);
		HtmlInput password = WebSite.page.getHtmlElementById("login-password");
		password.setValueAttribute(pw);
		WebSite.webClient.getOptions().setRedirectEnabled(true);
		HtmlButton login = (HtmlButton) WebSite.page.getFirstByXPath("//button[text()='Log In']");
		WebSite.page = login.click();
		WebSite.webClient.waitForBackgroundJavaScript(50000);
	}

	/**
	 * Clicks the authorize button
	 */
	private static void authorize() throws Exception {
		WebSite.webClient.getOptions().setRedirectEnabled(true);
		HtmlButton b = WebSite.page.getFirstByXPath("//button[text()='Okay']");
		WebSite.page = b.click();
		WebSite.webClient.waitForBackgroundJavaScript(50000);
	}

	/**
	 * Sets up the needed settings for the webclient
	 */
	private static void setUp() {
		WebSite.page = null;
		WebSite.webClient = null;
		WebSite.webClient = new WebClient(BrowserVersion.CHROME);
		WebSite.webClient.getOptions().setRedirectEnabled(true);
		WebSite.webClient.getOptions().setCssEnabled(true);
		WebSite.webClient.setCssErrorHandler(new SilentCssErrorHandler());
		WebSite.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		WebSite.webClient.getOptions().setThrowExceptionOnScriptError(false);
		WebSite.webClient.getOptions().setAppletEnabled(true);
		WebSite.webClient.getOptions().setJavaScriptEnabled(true);
		WebSite.webClient.getOptions().setTimeout(5000);

		new WebConnectionWrapper(WebSite.webClient) {

			@Override
			public WebResponse getResponse(final WebRequest request) throws IOException {
				WebResponse response = super.getResponse(request);
				try {
					String s = response.getResponseHeaderValue("Location");
					if (s.contains("code=")) {
						WebSite.webClient.getOptions().setTimeout(100);
						WebSite.code = s.substring(s.indexOf("=") + 1, s.indexOf("&"));
					} else {
						WebSite.webClient.getOptions().setTimeout(5000);
					}
				} catch (Exception e) {

				}
				return response;
			}
		};
	}

	/**
	 * Returns the current login code
	 *
	 * @return
	 */
	public static String getCode() {
		return WebSite.code;
	}

}

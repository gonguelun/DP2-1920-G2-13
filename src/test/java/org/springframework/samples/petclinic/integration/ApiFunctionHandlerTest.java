
package org.springframework.samples.petclinic.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Owner
 */
public class ApiFunctionHandlerTest {

	static String code;


	public ApiFunctionHandlerTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		WebSite.logIn("shufflertest", "1234");
		ApiFunctionHandlerTest.code = WebSite.getCode();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testWithWrongCode() {

		Assert.assertTrue(!ApiFunctionHandlerTest.code.equals("ajaja"));
	}

}


package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ListPickUpRequestsSuccessUITest {

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = "C:\\Users\\Gonzalo\\Desktop\\UNIVERSIDAD\\TERCERO\\DP2";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testListPickUpRequestsSuccess() throws Exception {
		this.driver.get("http://localhost:8080/");
		this.driver.findElement(By.linkText("OWNER REGISTER")).click();
		this.driver.findElement(By.id("firstName")).click();
		this.driver.findElement(By.id("firstName")).clear();
		this.driver.findElement(By.id("firstName")).sendKeys("b");
		this.driver.findElement(By.id("lastName")).clear();
		this.driver.findElement(By.id("lastName")).sendKeys("b");
		this.driver.findElement(By.id("address")).clear();
		this.driver.findElement(By.id("address")).sendKeys("b");
		this.driver.findElement(By.id("city")).clear();
		this.driver.findElement(By.id("city")).sendKeys("b");
		this.driver.findElement(By.id("telephone")).clear();
		this.driver.findElement(By.id("telephone")).sendKeys("123456789");
		this.driver.findElement(By.id("user.username")).clear();
		this.driver.findElement(By.id("user.username")).sendKeys("b");
		this.driver.findElement(By.id("user.password")).clear();
		this.driver.findElement(By.id("user.password")).sendKeys("b");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("b");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("b");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		this.driver.findElement(By.linkText("VIEW MY PICK UP REQUESTS")).click();
		this.driver.findElement(By.linkText("New request")).click();
		this.driver.findElement(By.id("description")).click();
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("yo1");
		new Select(this.driver.findElement(By.id("petType"))).selectByVisibleText("cat");
		this.driver.findElement(By.xpath("//option[@value='cat']")).click();
		this.driver.findElement(By.id("physicalStatus")).click();
		this.driver.findElement(By.id("physicalStatus")).clear();
		this.driver.findElement(By.id("physicalStatus")).sendKeys("yo1");
		this.driver.findElement(By.id("address")).click();
		this.driver.findElement(By.id("address")).clear();
		this.driver.findElement(By.id("address")).sendKeys("yo1");
		this.driver.findElement(By.id("physicalStatus")).click();
		this.driver.findElement(By.id("physicalStatus")).clear();
		this.driver.findElement(By.id("physicalStatus")).sendKeys("yo2");
		this.driver.findElement(By.id("address")).click();
		this.driver.findElement(By.id("address")).clear();
		this.driver.findElement(By.id("address")).sendKeys("yo3");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("B")).click();
		this.driver.findElement(By.linkText("Logout")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("VET REGISTER")).click();
		this.driver.findElement(By.id("firstName")).click();
		this.driver.findElement(By.id("firstName")).click();
		this.driver.findElement(By.id("firstName")).clear();
		this.driver.findElement(By.id("firstName")).sendKeys("h");
		this.driver.findElement(By.id("lastName")).clear();
		this.driver.findElement(By.id("lastName")).sendKeys("h");
		// ERROR: Caught exception [ERROR: Unsupported command [addSelection | id=specialties | label=radiology]]
		this.driver.findElement(By.xpath("//option[@value='radiology']")).click();
		this.driver.findElement(By.id("user.username")).click();
		this.driver.findElement(By.id("user.username")).clear();
		this.driver.findElement(By.id("user.username")).sendKeys("h");
		this.driver.findElement(By.id("user.password")).clear();
		this.driver.findElement(By.id("user.password")).sendKeys("h");
		this.driver.findElement(By.id("add-vet-form")).submit();
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("h");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("h");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		this.driver.findElement(By.linkText("PICK UP REQUESTS")).click();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}

}

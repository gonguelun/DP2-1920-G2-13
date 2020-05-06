
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChooseProductsInBeautyDateSuccessUITest {

	@LocalServerPort
	private int				port;

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testChooseProductsInBeautyDateSuccess() throws Exception {
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
		this.driver.findElement(By.id("add-owner-form")).submit();
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("f");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("f");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		this.driver.findElement(By.linkText("BEAUTICIAN")).click();
		this.driver.findElement(By.linkText("Create Product")).click();
		this.driver.findElement(By.id("name")).click();
		this.driver.findElement(By.id("name")).clear();
		this.driver.findElement(By.id("name")).sendKeys("yo");
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("yo");
		new Select(this.driver.findElement(By.id("type"))).selectByVisibleText("cat");
		this.driver.findElement(By.xpath("//option[@value='cat']")).click();
		this.driver.findElement(By.name("avaliable")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("F")).click();
		this.driver.findElement(By.linkText("Logout")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("b");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("b");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		this.driver.findElement(By.linkText("Find owners")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("b b")).click();
		this.driver.findElement(By.linkText("Add New Pet")).click();
		this.driver.findElement(By.id("name")).click();
		this.driver.findElement(By.id("name")).clear();
		this.driver.findElement(By.id("name")).sendKeys("yo2");
		this.driver.findElement(By.id("birthDate")).clear();
		this.driver.findElement(By.id("birthDate")).sendKeys("2020/02/10");
		new Select(this.driver.findElement(By.id("type"))).selectByVisibleText("cat");
		this.driver.findElement(By.xpath("//option[@value='cat']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.id("typePet")).click();
		new Select(this.driver.findElement(By.id("typePet"))).selectByVisibleText("cat");
		this.driver.findElement(By.xpath("//option[@value='1']")).click();
		this.driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
		this.driver.findElement(By.linkText("Book a date")).click();
		this.driver.findElement(By.id("description")).click();
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("yo3");
		new Select(this.driver.findElement(By.id("startDate"))).selectByVisibleText("2020/06/02 16:00");
		this.driver.findElement(By.xpath("//option[@value='2020/06/02 16:00']")).click();
		new Select(this.driver.findElement(By.id("pet"))).selectByVisibleText("yo2 - 14");
		this.driver.findElement(By.xpath("//option[@value='yo2 - 14']")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [addSelection | id=products | label=yo - 1]]
		this.driver.findElement(By.xpath("//option[@value='yo - 1']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
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

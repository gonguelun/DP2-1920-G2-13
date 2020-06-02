
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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
public class RegisterPickUpRequestNoDescriptionNoPhysicalStatusSuccessUITest {

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
	public void testRegisterPickUpRequestNoDescriptionNoPhysicalStatusSuccessUITest() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("0wn3r");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("OWNER1", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span")).click();
		this.driver.findElement(By.linkText("New request")).click();
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("");
		new Select(this.driver.findElement(By.id("petType"))).selectByVisibleText("dog");
		this.driver.findElement(By.xpath("//option[@value='dog']")).click();
		this.driver.findElement(By.id("physicalStatus")).click();
		this.driver.findElement(By.id("physicalStatus")).clear();
		this.driver.findElement(By.id("physicalStatus")).sendKeys("");
		this.driver.findElement(By.id("address")).click();
		this.driver.findElement(By.id("address")).clear();
		this.driver.findElement(By.id("address")).sendKeys("Calle Arjona Numero 23");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("VIEW MY PICK UP REQUESTS")).click();
		Assert.assertEquals("", this.driver.findElement(By.xpath("//tr[@id='']/td")).getText());
		Assert.assertEquals("dog", this.driver.findElement(By.xpath("//tr[@id='']/td[2]")).getText());
		Assert.assertEquals("", this.driver.findElement(By.xpath("//tr[@id='']/td[3]")).getText());
		Assert.assertEquals("false", this.driver.findElement(By.xpath("//tr[@id='']/td[4]")).getText());
		Assert.assertEquals("Calle Arjona Numero 23", this.driver.findElement(By.xpath("//tr[@id='']/td[5]")).getText());
		
		Assert.assertEquals("Delete PickUp Request", driver.findElement(By.xpath("(//a[contains(text(),'Delete PickUp Request')])[3]")).getText());
		
		driver.findElement(By.xpath("(//a[contains(text(),'Delete PickUp Request')])[3]")).click();
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

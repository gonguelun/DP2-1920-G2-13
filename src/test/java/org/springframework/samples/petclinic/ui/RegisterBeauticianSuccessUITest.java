package org.springframework.samples.petclinic.ui;


import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterBeauticianSuccessUITest {
	@LocalServerPort
	private int port;
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeEach
  public void setUp() throws Exception {
	String pathtoChromeDriver="C:\\Users\\carsa\\OneDrive\\Escritorio";
	System.setProperty("webdriver.gecko.driver", pathtoChromeDriver+"\\geckodriver.exe");
    driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testPruebaHU1() throws Exception {
	    driver.get("http://localhost:"+port);
	    driver.findElement(By.linkText("BEAUTICIAN REGISTER")).click();
	    driver.findElement(By.id("firstName")).clear();
	    driver.findElement(By.id("firstName")).sendKeys("f");
	    driver.findElement(By.id("lastName")).clear();
	    driver.findElement(By.id("lastName")).sendKeys("f");
	    driver.findElement(By.id("user.username")).clear();
	    driver.findElement(By.id("user.username")).sendKeys("f");
	    driver.findElement(By.id("user.password")).clear();
	    driver.findElement(By.id("user.password")).sendKeys("f");
	    // ERROR: Caught exception [ERROR: Unsupported command [addSelection | id=Specializations | label=cat]]
	    driver.findElement(By.xpath("//option[@value='cat']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("is already in use", driver.findElement(By.xpath("//form[@id='add-beautician-form']/div/div[3]/div/span[2]")).getText());
  }

  @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}


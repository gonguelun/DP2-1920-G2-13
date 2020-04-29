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
public class RegisterPickUpRequestErrorUITest {
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
  public void testRegisterPickUpRequestSuccessUITest() throws Exception {
	    driver.get("http://localhost:"+port);
	    driver.findElement(By.linkText("LOGIN")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("owner1");
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("0wn3r");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("OWNER1", driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	    driver.findElement(By.linkText("VIEW MY PICK UP REQUESTS")).click();
	    driver.findElement(By.linkText("New request")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Peticion de recogida");
	    driver.findElement(By.id("physicalStatus")).click();
	    driver.findElement(By.id("physicalStatus")).clear();
	    driver.findElement(By.id("physicalStatus")).sendKeys("Está herido en una pata");
	    driver.findElement(By.id("address")).click();
	    driver.findElement(By.id("address")).clear();
	    driver.findElement(By.id("address")).sendKeys("Calle Arjona Número 23");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("It's mandatory", driver.findElement(By.xpath("//form[@id='add-pickUpRequest-form']/div/div[2]/div/div/span[2]")).getText());
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

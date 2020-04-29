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
public class ModifyBeautyCenterSuccessUITest {
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
  public void testModifyBeautyCenterSuccessUITest() throws Exception {
	    driver.get("http://localhost:"+port);
	    driver.findElement(By.linkText("LOGIN")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("f");
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("f");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
	    driver.findElement(By.linkText("Modify name1 Beauty Center")).click();
	    driver.findElement(By.id("name")).clear();
	    driver.findElement(By.id("name")).sendKeys("name3");
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("hey2");
	    new Select(driver.findElement(By.id("petType"))).selectByVisibleText("bird");
	    driver.findElement(By.xpath("//option[@value='bird']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("name3", driver.findElement(By.xpath("//table[@id='beautyTable']/tbody/tr/td")).getText());
	    assertEquals("hey2", driver.findElement(By.xpath("//table[@id='beautyTable']/tbody/tr/td[2]")).getText());
	    assertEquals("bird", driver.findElement(By.xpath("//table[@id='beautyTable']/tbody/tr/td[3]")).getText());
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

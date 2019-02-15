package com.aqacourses.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String URL = "https://s1.demo.opensourcecms.com/s/44";
    private final String USERNAME_INPUT_FIELD_XPATH = "//input[@name='txtUsername']";
    private final String PASSWORD_INPUT_FIELD_XPATH = "//input[@id='txtPassword']";
    private final String LOGIN_BUTTON_XPATH = "//input[@id='btnLogin']";
    private final String MESSAGE_INVALID_CREDENTIALS = "Invalid credentials";
    private final String MESSAGE_EMPTY_USERNAME = "Username cannot be empty";
    private final String MESSAGE_EMPTY_PASSWORD = "Password cannot be empty";
    private final String REMOVE_FRAME = "//div[@class='preview__header']//a//span[.='Remove Frame']/..";

    /**
     * Set up method to initialize driver and WebDriverWait
     */
    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 10);
        wait.ignoring(TimeoutException.class)
                .withMessage("Where the element?")
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1));
    }

    /**
     * Login form validation
     */
    @Test
    public void testLoginTest() {
        driver.get(URL);

        wait.until(ExpectedConditions.visibilityOf(driver.switchTo().frame("preview-frame")
                .findElement(By.xpath(USERNAME_INPUT_FIELD_XPATH)))).click();
        driver.findElement(By.xpath(USERNAME_INPUT_FIELD_XPATH)).sendKeys("TestUsername");
        driver.findElement(By.xpath(PASSWORD_INPUT_FIELD_XPATH)).sendKeys("TestPassword");
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        //WebElement login = driver.findElement(By.xpath(LOGIN_BUTTON_XPATH));
        //login.click();
        Assert.assertEquals("Message is not the same as expected", MESSAGE_INVALID_CREDENTIALS,
                driver.findElement(By.xpath("//span[contains(text(),'Invalid credentials')]")).getText());

        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        //login.click(); - doesn't work. Why?
        Assert.assertEquals("Message is not the same as expected", MESSAGE_EMPTY_USERNAME,
                driver.findElement(By.xpath("//span[contains(text(),'Username cannot be empty')]")).getText());

        driver.findElement(By.xpath(USERNAME_INPUT_FIELD_XPATH)).sendKeys("TestUsername");
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        Assert.assertEquals("Message is not the same as expected", MESSAGE_EMPTY_PASSWORD,
                driver.findElement(By.xpath("//span[contains(text(),'Password cannot be empty')]")).getText());

        driver.switchTo().defaultContent();
        driver.findElement(By.xpath(REMOVE_FRAME)).click();
        Assert.assertEquals(0, driver.findElements(By.xpath("//iframe[@name='preview-frame']")).size());
    }

    /**
     * Quit WebDriver
     */
    @After
    public void tearDown() {
        driver.quit();
    }

}

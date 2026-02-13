package com.mondial.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.mondial.utils.ConfigReader;
import com.mondial.utils.DriverManager;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigReader config;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.config = new ConfigReader();
        int explicitWait = Integer.parseInt(config.getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        PageFactory.initElements(driver, this);
    }

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.config = new ConfigReader();
        int explicitWait = Integer.parseInt(config.getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        PageFactory.initElements(driver, this);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void clickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("Regular click failed, using JavaScript click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', behavior: 'instant'});", element);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    protected void waitForPageLoad() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete")
            );
        } catch (Exception e) {
            System.out.println("Page load wait timed out");
        }
    }
}
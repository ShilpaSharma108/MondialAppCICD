package com.mondial.tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Zendesk Test Class
 * Tests Help icon navigation to Zendesk and logged-in user display
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ZendeskTest extends BaseTest {

    private HomePage homePage;
    private String parentWindow;
    private String userName;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Stores parent window handle for window switching
     */
    @BeforeClass
    public void zendeskSetup() {
        System.out.println("=== Starting Zendesk Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        userName = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with user: " + userName);
        loginPage.login(userName, password);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be on home page after login");

        parentWindow = driver.getWindowHandle();
        System.out.println("=== Zendesk Test Setup Complete ===\n");
    }

    /**
     * Test 1: Verify user can navigate to Zendesk using Help (?) icon
     * Clicks help icon, switches to new window, verifies Zendesk URL
     */
    @Test(priority = 1, description = "User is able to navigate to Zendesk using Help(?) Icon")
    public void testHelpIconNavigatesToZendesk() {
        System.out.println("\n[TEST 1] Testing Help Icon Navigation to Zendesk...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        homePage.clickHelpIcon();

        // Wait for a new window to open
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                d -> d.getWindowHandles().size() > 1);

        // Switch to the new Zendesk window
        boolean switchedToNewWindow = false;
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(parentWindow)) {
                driver.switchTo().window(windowHandle);
                switchedToNewWindow = true;
                break;
            }
        }

        Assert.assertTrue(switchedToNewWindow,
                         "A new window should open after clicking Help icon");

        String currentUrl = driver.getCurrentUrl();
        System.out.println("[TEST 1] New window URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("zendesk"),
                         "Should navigate to Zendesk support page");

        System.out.println("[TEST 1] Zendesk URL verified: " + currentUrl);

        // Close Zendesk window and switch back to parent
        driver.close();
        driver.switchTo().window(parentWindow);

        // Wait for parent page to be fully ready after window switch
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                d -> ((org.openqa.selenium.JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));

        // Re-initialize page elements after window switch to avoid stale references
        homePage = new HomePage(driver);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Home page should be displayed after switching back from Zendesk");

        System.out.println("[TEST 1] Switched back to parent window");
    }

    /**
     * Test 2: Verify logged-in username is displayed correctly
     */
    @Test(priority = 2, description = "Verify logged in username is displaying correctly", dependsOnMethods = {"testHelpIconNavigatesToZendesk"})
    public void testLoggedInUserNameDisplayed() {
        System.out.println("\n[TEST 2] Testing Logged In User Name Display...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Home page should be displayed after switching back from Zendesk");

        String displayedUserName = homePage.getLoggedInUserName();
        Assert.assertTrue(displayedUserName.contains(userName),
                         "Logged in username should contain: " + userName);

        System.out.println("[TEST 2] Logged in user verified: " + displayedUserName);
    }
}

package com.mondial.tests;

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

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

        // Switch to the new Zendesk window
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(parentWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        homePage.waitForSubmitRequest();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("https://mondialsoftware.zendesk.com"),
                         "Should navigate to Zendesk support page");

        System.out.println("[TEST 1] Zendesk URL verified: " + currentUrl);

        // Close Zendesk window and switch back to parent
        driver.close();
        driver.switchTo().window(parentWindow);

        System.out.println("[TEST 1] Switched back to parent window");
    }

    /**
     * Test 2: Verify logged-in username is displayed correctly
     */
    @Test(priority = 2, description = "Verify logged in username is displaying correctly", dependsOnMethods = {"testHelpIconNavigatesToZendesk"})
    public void testLoggedInUserNameDisplayed() {
        System.out.println("\n[TEST 2] Testing Logged In User Name Display...");

        String displayedUserName = homePage.getLoggedInUserName();
        Assert.assertTrue(displayedUserName.contains(userName),
                         "Logged in username should contain: " + userName);

        System.out.println("[TEST 2] Logged in user verified: " + displayedUserName);
    }
}

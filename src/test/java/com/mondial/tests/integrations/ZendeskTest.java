package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;

import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Zendesk Test Class
 * Tests Help icon navigation to Zendesk
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ZendeskTest extends BaseTest {

    private HomePage homePage;
    private String parentWindow;

    @BeforeClass
    public void zendeskSetup() {
        System.out.println("=== Starting Zendesk Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        String userName = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with user: " + userName);
        loginPage.login(userName, password);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be on home page after login");

        parentWindow = driver.getWindowHandle();
        System.out.println("=== Zendesk Test Setup Complete ===\n");
    }

    /**
     * Verify user can navigate to Zendesk using Help (?) icon
     * Clicks help icon, switches to new window, verifies Zendesk URL
     */
    @Test(description = "User is able to navigate to Zendesk using Help(?) Icon")
    public void testHelpIconNavigatesToZendesk() {
        System.out.println("\n[TEST] Testing Help Icon Navigation to Zendesk...");

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

        // Wait for the Zendesk page URL to load in the new window
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                d -> d.getCurrentUrl().contains("zendesk") || d.getCurrentUrl().contains("support"));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("[TEST] New window URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("zendesk"),
                         "Should navigate to Zendesk support page");

        System.out.println("[TEST] Zendesk navigation verified: " + currentUrl);
    }
}

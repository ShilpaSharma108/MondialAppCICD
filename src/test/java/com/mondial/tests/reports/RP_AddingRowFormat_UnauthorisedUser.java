package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.RowFormat;

/**
 * Unauthorised Row Format Access Test Class
 * Verifies that a consumer (non-admin) user cannot add a Row Format
 */
public class RP_AddingRowFormat_UnauthorisedUser extends BaseTest {

    private HomePage homePage;
    private RowFormat rf;

    @BeforeClass
    public void rowFormatUnauthorisedSetup() {
        System.out.println("=== Starting Row Format Unauthorised User Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new RowFormat(driver);

        String consumerUsername = config.getProperty("Consumer");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with consumer user: " + consumerUsername);
        loginPage.login(consumerUsername, password);
        rf.waitForPageLoad();

        System.out.println("=== Row Format Unauthorised User Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Unauthorised User Cannot Add Row Format")
    public void testUnauthorisedAddRowFormat() {
        System.out.println("\n[TEST 1] Verifying unauthorised add access for Row Format...");

        rf.navigateToRowFormat();

        Assert.assertTrue(rf.isAddRowFormatBtnDisplayed(),
                "Add Row Format button should be displayed on the listing page");

        rf.clickAddRowFormat();

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Unauthorised user should be redirected to home page after clicking Add");

        System.out.println("[TEST 1] Unauthorised user correctly redirected to home page");
    }
}

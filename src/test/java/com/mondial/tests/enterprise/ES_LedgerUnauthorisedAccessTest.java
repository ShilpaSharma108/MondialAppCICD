package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LedgerPage;
import com.mondial.pages.LoginPage;

/**
 * Ledger Unauthorised Access Test Class
 * Verifies that a consumer (non-admin) user cannot create, edit, or delete ledgers
 *
**/
public class ES_LedgerUnauthorisedAccessTest extends BaseTest {

    private HomePage homePage;
    private LedgerPage ledgerPage;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with consumer (non-admin) credentials
     */
    @BeforeClass
    public void ledgerUnauthorisedSetup() {
        System.out.println("=== Starting Ledger Unauthorised Access Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        ledgerPage = new LedgerPage(driver);

        String consumerUsername = config.getProperty("Consumer");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with consumer user: " + consumerUsername);
        loginPage.login(consumerUsername, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=== Ledger Unauthorised Access Test Setup Complete ===\n");
    }

    /**
     * Test 1: Verify unauthorised user cannot create a ledger
     * Clicks Add Ledger and expects redirect back to home page
     */
    @Test(priority = 1, description = "Unauthorised Access - Create")
    public void testUnauthorizedCreateLedger() {
        System.out.println("\n[TEST 1] Verifying unauthorised create access...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        ledgerPage.navigateToLedgerPage();

        Assert.assertTrue(ledgerPage.isAddLedgerBtnDisplayed(),
                         "Add Ledger button should be displayed");

        ledgerPage.clickAddLedger();

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be redirected to home page after unauthorised create attempt");

        System.out.println("[TEST 1] Unauthorised user is not able to create new ledger");
    }

    /**
     * Test 2: Verify unauthorised user cannot edit a ledger
     * Clicks Edit on first record and expects redirect back to home page
     */
    @Test(priority = 2, dependsOnMethods = {"testUnauthorizedCreateLedger"}, description = "Unauthorised Access - Edit")
    public void testUnauthorizedEditLedger() {
        System.out.println("\n[TEST 2] Verifying unauthorised edit access...");

        ledgerPage.navigateToLedgerPage();

        String heading = ledgerPage.getPageHeading();
        Assert.assertTrue(heading.contains("Ledgers"),
                         "Heading should contain 'Ledgers'");

        ledgerPage.clickEditFirstRecord();

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be redirected to home page after unauthorised edit attempt");

        System.out.println("[TEST 2] Unauthorised user is not able to edit ledger");
    }

    /**
     * Test 3: Verify unauthorised user cannot delete a ledger
     * Clicks Delete on first record and expects redirect to home page
     */
    @Test(priority = 3, dependsOnMethods = {"testUnauthorizedEditLedger"}, description = "Unauthorised Access - Delete")
    public void testUnauthorizedDeleteLedger() {
        System.out.println("\n[TEST 3] Verifying unauthorised delete access...");

        ledgerPage.navigateToLedgerPage();

        String heading = ledgerPage.getPageHeading();
        Assert.assertTrue(heading.contains("Ledgers"),
                         "Heading should contain 'Ledgers'");

        ledgerPage.clickDeleteFirstRecord();

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be redirected to home page after unauthorised delete attempt");

        System.out.println("[TEST 3] Unauthorised user is not able to delete ledger");
    }
}

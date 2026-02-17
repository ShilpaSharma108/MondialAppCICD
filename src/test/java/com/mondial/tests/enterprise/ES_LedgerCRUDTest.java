package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LedgerPage;
import com.mondial.pages.LoginPage;

/**
 * Ledger CRUD Test Class
 * Tests Create, Read, Update, and Delete operations for Ledgers
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ES_LedgerCRUDTest extends BaseTest {

    private HomePage homePage;
    private LedgerPage ledgerPage;
    private String ledgerName;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates unique name for ledger tests
     */
    @BeforeClass
    public void ledgerSetup() {
        System.out.println("=== Starting Ledger CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        ledgerPage = new LedgerPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with user: " + username);
        loginPage.login(username, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ledgerName = "Ledger_" + System.currentTimeMillis();
        System.out.println("Generated Ledger name: " + ledgerName);
        System.out.println("=== Ledger CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Ledger page
     * Verifies the page heading contains "Ledgers"
     */
    @Test(priority = 1, description = "Verify Navigation to Ledger Page")
    public void testNavigateToLedger() {
        System.out.println("\n[TEST 1] Navigating to Ledger page...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        ledgerPage.navigateToLedgerPage();

        Assert.assertTrue(ledgerPage.isAddLedgerBtnDisplayed(),
                         "Add Ledger button should be displayed");

        String heading = ledgerPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains("Ledgers"),
                         "Heading should contain 'Ledgers'");

        System.out.println("[TEST 1] Successfully navigated to Ledger page");
    }

    /**
     * Test 2: Create a new Ledger
     * Verifies cancel flow, creates a ledger, and verifies it appears in the table
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToLedger"}, description = "Verify Creation of a new Ledger")
    public void testCreateLedger() {
        System.out.println("\n[TEST 2] Creating Ledger: " + ledgerName);

        ledgerPage.verifyCancelCreation();

        Assert.assertTrue(ledgerPage.getPageHeading().contains("Ledgers"),
                         "Should return to Ledgers listing page after cancel");

        ledgerPage.createLedger(ledgerName);

        String successMsg = ledgerPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Ledger was successfully created."),
                         "Success message should confirm ledger creation");

        Assert.assertTrue(ledgerPage.verifyRecordPresent(ledgerName),
                         "Newly created ledger should appear in the table");

        System.out.println("[TEST 2] Ledger created successfully: " + ledgerName);
    }

    /**
     * Test 3: Edit the previously created Ledger
     * Navigates to edit, cancels, then edits again and renames
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateLedger"}, description = "Verify Edit Ledger functionality")
    public void testEditLedger() {
        System.out.println("\n[TEST 3] Editing Ledger: " + ledgerName);

        ledgerPage.navigateToEdit(ledgerName);

        Assert.assertTrue(ledgerPage.isSubmitBtnDisplayed(),
                         "Submit button should be displayed on edit page");

        ledgerPage.clickCancelAndWaitForListing();

        ledgerPage.navigateToEdit(ledgerName);

        ledgerName = ledgerPage.editLedger(ledgerName);

        Assert.assertTrue(ledgerPage.verifyRecordPresent(ledgerName),
                         "Updated ledger name should appear in the table");

        System.out.println("[TEST 3] Ledger updated to: " + ledgerName);
    }

    /**
     * Test 4: Delete the Ledger
     * Deletes the ledger and verifies it no longer appears in the table
     */
    @Test(priority = 4, dependsOnMethods = {"testEditLedger"}, description = "Verify user is able to Delete Unused Ledger")
    public void testDeleteLedger() {
        System.out.println("\n[TEST 4] Deleting Ledger: " + ledgerName);

        ledgerPage.clickDelete(ledgerName);

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        String successMsg = ledgerPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Ledger was successfully deleted"),
                         "Success message should confirm ledger deletion");

        ledgerPage.waitForSuccessMessageToDisappear();

        Assert.assertFalse(ledgerPage.verifyRecordPresent(ledgerName),
                          "Deleted ledger should no longer appear in the table");

        System.out.println("[TEST 4] Ledger deleted successfully: " + ledgerName);
    }
}

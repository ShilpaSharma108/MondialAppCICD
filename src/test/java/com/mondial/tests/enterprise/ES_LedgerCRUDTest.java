package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import java.util.List;
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
        Assert.assertTrue(heading.contains("Ledger"),
                         "Heading should contain 'Ledger'");

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
     * Test 3: Attempt to create a Ledger with a duplicate name
     * Uses the same name as the ledger created in Test 2
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateLedger"}, description = "Verify user is not able to create two Ledgers with the same name")
    public void testCreateDuplicateLedger() {
        System.out.println("\n[TEST 3] Attempting to create duplicate Ledger: " + ledgerName);

        ledgerPage.createLedger(ledgerName);

        Assert.assertTrue(ledgerPage.isWarningMessageDisplayed(),
                         "Warning/error message should be displayed when creating duplicate ledger");

        ledgerPage.clickCancelAndWaitForListing();

        System.out.println("[TEST 3] Duplicate ledger creation correctly prevented");
    }

    /**
     * Test 4: Edit transaction categories on the Ledger
     * Selects additional (unchecked) transaction category checkboxes, saves,
     * then re-opens edit to verify all newly selected categories are persisted
     */
    @Test(priority = 4, description = "Verify editing transaction categories saves successfully")
    public void testEditLedgerTransactionCategories() {
        System.out.println("\n[TEST 4] Editing transaction categories for Ledger: " + ledgerName);

        // Defensive setup: navigate to listing and create the ledger if it doesn't exist
        // (no-op when running as part of the full chain; enables standalone execution)
        ledgerPage.navigateToLedgerPage();
        if (!ledgerPage.verifyRecordPresent(ledgerName)) {
            System.out.println("[TEST 4] Ledger not found, creating: " + ledgerName);
            ledgerPage.createLedger(ledgerName);
            ledgerPage.verifyRecordPresent(ledgerName); // waits for listing to reload
        }

        ledgerPage.navigateToEdit(ledgerName);

        Assert.assertTrue(ledgerPage.isSubmitBtnDisplayed(),
                         "Submit button should be displayed on edit page");

        List<String> additionalCategories = ledgerPage.selectAdditionalTxnTypes();

        Assert.assertFalse(additionalCategories.isEmpty(),
                          "At least one additional transaction category should be available to select");

        System.out.println("[TEST 4] Newly selected transaction categories: " + additionalCategories);

        ledgerPage.submitAndWaitForListing();

        // Re-open edit to verify the newly selected categories were saved
        ledgerPage.navigateToEdit(ledgerName);

        List<String> checkedCategories = ledgerPage.getCheckedTxnTypeNames();

        Assert.assertTrue(checkedCategories.containsAll(additionalCategories),
                         "All newly selected transaction categories should be saved and checked");

        ledgerPage.clickCancelAndWaitForListing();

        System.out.println("[TEST 4] Transaction categories updated and verified successfully");
    }

    /**
     * Test 5: Edit the previously created Ledger
     * Navigates to edit, cancels, then edits again and renames
     */
    @Test(priority = 5, dependsOnMethods = {"testEditLedgerTransactionCategories"}, description = "Verify Edit Ledger functionality")
    public void testEditLedger() {
        System.out.println("\n[TEST 5] Editing Ledger: " + ledgerName);

        ledgerPage.navigateToEdit(ledgerName);

        Assert.assertTrue(ledgerPage.isSubmitBtnDisplayed(),
                         "Submit button should be displayed on edit page");

        ledgerPage.clickCancelAndWaitForListing();

        ledgerPage.navigateToEdit(ledgerName);

        ledgerName = ledgerPage.editLedger(ledgerName);

        Assert.assertTrue(ledgerPage.verifyRecordPresent(ledgerName),
                         "Updated ledger name should appear in the table");

        System.out.println("[TEST 5] Ledger updated to: " + ledgerName);
    }

    /**
     * Test 6: Delete the Ledger
     * Deletes the ledger and verifies it no longer appears in the table
     */
    @Test(priority = 6, dependsOnMethods = {"testEditLedger"}, description = "Verify user is able to Delete Unused Ledger")
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

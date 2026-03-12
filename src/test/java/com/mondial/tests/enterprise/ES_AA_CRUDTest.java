package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.AlternateAccountPage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Alternate Account CRUD Test Class
 * Tests Create, Read, Update, and Delete operations for Alternate Account Sets
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ES_AA_CRUDTest extends BaseTest {

    private HomePage homePage;
    private AlternateAccountPage alternateAccountPage;
    private String alternateAccountName;
    private String symbol;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates unique names for alternate account set tests
     */
    @BeforeClass
    public void alternateAccountSetup() {
        System.out.println("=== Starting Alternate Account CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        alternateAccountPage = new AlternateAccountPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with user: " + username);
        loginPage.login(username, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        alternateAccountName = "AltAcc_" + System.currentTimeMillis();
        symbol = "SYM_" + System.currentTimeMillis();
        System.out.println("Generated Alternate Account Set name: " + alternateAccountName);
        System.out.println("=== Alternate Account CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Alternate Account Sets page
     * Verifies the page heading contains "Alternate Account Sets"
     */
    @Test(priority = 1, description = "Verify Navigation to Alternate Account Sets Page")
    public void testNavigateToAlternateAccount() {
        System.out.println("\n[TEST 1] Navigating to Alternate Account Sets page...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        alternateAccountPage.navigateToAlternateAccountPage();

        Assert.assertTrue(alternateAccountPage.isAddAlternateAccountBtnDisplayed(),
                         "Add Alternate Account Set button should be displayed");

        String heading = alternateAccountPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains("Alternate Account Sets"),
                         "Heading should contain 'Alternate Account Sets'");

        System.out.println("[TEST 1] Successfully navigated to Alternate Account Sets page");
    }

    /**
     * Test 2: Create a new Alternate Account Set
     * Verifies cancel flow, creates a set, and verifies it appears in the table
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToAlternateAccount"}, description = "Verify Creation of a new Alternate Account Set")
    public void testCreateAlternateAccount() {
        System.out.println("\n[TEST 2] Creating Alternate Account Set: " + alternateAccountName);

        alternateAccountPage.verifyCancelCreation();

        Assert.assertTrue(alternateAccountPage.getPageHeading().contains("Alternate Account Sets"),
                         "Should return to Alternate Account Sets listing page after cancel");

        alternateAccountPage.createAlternateAccount(alternateAccountName, symbol);

        String successMsg = alternateAccountPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Alternate Account Set successfully created."),
                         "Success message should confirm alternate account set creation");

        Assert.assertTrue(alternateAccountPage.verifyRecordPresent(alternateAccountName),
                         "Newly created alternate account set should appear in the table");

        System.out.println("[TEST 2] Alternate Account Set created successfully: " + alternateAccountName);
    }

    /**
     * Test 3: Edit the previously created Alternate Account Set
     * Navigates to edit, cancels, then edits again and renames
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateAlternateAccount"}, description = "Verify Edit Alternate Account Set Functionality")
    public void testEditAlternateAccount() {
        System.out.println("\n[TEST 3] Editing Alternate Account Set: " + alternateAccountName);

        alternateAccountPage.navigateToEdit(alternateAccountName);

        Assert.assertTrue(alternateAccountPage.isSubmitBtnDisplayed(),
                         "Submit button should be displayed on edit page");

        alternateAccountPage.clickCancelAndWaitForListing();

        alternateAccountPage.navigateToEdit(alternateAccountName);

        alternateAccountName = alternateAccountPage.editAlternateAccount(alternateAccountName);

        Assert.assertTrue(alternateAccountPage.verifyRecordPresent(alternateAccountName),
                         "Updated alternate account set name should appear in the table");

        System.out.println("[TEST 3] Alternate Account Set updated to: " + alternateAccountName);
    }

    /**
     * Test 4: Delete the Alternate Account Set
     * Deletes the set and verifies it no longer appears in the table
     */
    @Test(priority = 4, dependsOnMethods = {"testEditAlternateAccount"}, description = "Verify user is able to Delete Alternate Account Set")
    public void testDeleteAlternateAccount() {
        System.out.println("\n[TEST 4] Deleting Alternate Account Set: " + alternateAccountName);

        alternateAccountPage.clickDelete(alternateAccountName);

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        String successMsg = alternateAccountPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Alternate Account Set successfully deleted."),
                         "Success message should confirm alternate account set deletion");

        alternateAccountPage.waitForSuccessMessageToDisappear();

        Assert.assertFalse(alternateAccountPage.verifyRecordPresent(alternateAccountName),
                          "Deleted alternate account set should no longer appear in the table");

        System.out.println("[TEST 4] Alternate Account Set deleted successfully: " + alternateAccountName);
    }
}

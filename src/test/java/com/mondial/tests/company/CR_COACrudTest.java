package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ChartOfAccountsPage;

/**
 * Chart of Accounts CRUD Test Class
 * Tests Create, Read, Update, and Delete operations for GL Accounts
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_COACrudTest extends BaseTest {

    private HomePage homePage;
    private ChartOfAccountsPage coaPage;
    private String companyName;
    private String acNumber;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates unique account number for GL Account tests
     */
    @BeforeClass
    public void coaSetup() {
        System.out.println("=== Starting Chart of Accounts CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        coaPage = new ChartOfAccountsPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        companyName = "AutomationTest222";

        System.out.println("Logging in with user: " + username);
        loginPage.login(username, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int naturalAC = (int) (Math.random() * 100000) + 100000;
        acNumber = String.valueOf(naturalAC);
        System.out.println("Generated Account Number: " + acNumber);
        System.out.println("Test Company: " + companyName);
        System.out.println("=== Chart of Accounts CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Chart of Accounts Page
     * Verifies the page heading contains "Chart of Accounts"
     */
    @Test(priority = 1, description = "Verify Navigation to Chart of Accounts Page")
    public void testNavigateToChartOfAccounts() {
        System.out.println("\n[TEST 1] Navigating to Chart of Accounts page...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        coaPage.navigateToChartOfAccounts(companyName);

        Assert.assertTrue(coaPage.isAddGLAccountBtnDisplayed(),
                         "Add GL Account button should be displayed");

        String heading = coaPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains("Chart of Accounts"),
                         "Heading should contain 'Chart of Accounts'");

        System.out.println("[TEST 1] Successfully navigated to Chart of Accounts page");
    }

    /**
     * Test 2: Create a new GL Account
     * Verifies cancel flow, creates an account, and verifies it appears in the table
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToChartOfAccounts"}, description = "Verify Creation of a new GL Account")
    public void testCreateGLAccount() {
        System.out.println("\n[TEST 2] Creating GL Account: " + acNumber);

        coaPage.verifyCancelCreation();

        Assert.assertTrue(coaPage.getPageHeading().contains("Chart of Accounts"),
                         "Should return to Chart of Accounts page after cancel");

        coaPage.clickAddGLAccountBtn();
        coaPage.createNewGLAccount(acNumber);
        waitForPageLoad();

        Assert.assertTrue(coaPage.isAddGLAccountBtnDisplayed(),
                         "Add GL Account button should be displayed after creation");

        Assert.assertTrue(coaPage.verifyGLAccount(acNumber),
                         "Newly created GL Account should appear in the table");

        System.out.println("[TEST 2] GL Account created successfully: " + acNumber);
    }

    /**
     * Test 3: Attempt to create a duplicate GL Account
     * Uses the same account number as the one just created and verifies failure message
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateGLAccount"}, description = "Verify duplicate GL Account creation fails")
    public void testCreateDuplicateGLAccount() {
        System.out.println("\n[TEST 3] Creating duplicate GL Account: " + acNumber);

        coaPage.clickAddGLAccountBtn();
        coaPage.incorrectGLAccount(acNumber);

        Assert.assertTrue(coaPage.isFailureMessageDisplayed(),
                         "Failure message should be displayed for duplicate account number");

        coaPage.clickCancelAndWaitForListing();

        System.out.println("[TEST 3] Duplicate GL Account creation correctly rejected");
    }

    /**
     * Test 4: Attempt to create an incomplete GL Account
     * Submits form without type/subtype/currency and verifies failure message
     */
    @Test(priority = 4, dependsOnMethods = {"testCreateGLAccount"}, description = "Verify incomplete GL Account creation fails")
    public void testCreateIncompleteGLAccount() {
        System.out.println("\n[TEST 4] Creating incomplete GL Account...");

        coaPage.clickAddGLAccountBtn();
        coaPage.incompleteGLAccount("999999");

        Assert.assertTrue(coaPage.isFailureMessageDisplayed(),
                         "Failure message should be displayed for incomplete account");

        coaPage.clickCancelAndWaitForListing();

        System.out.println("[TEST 4] Incomplete GL Account creation correctly rejected");
    }

    /**
     * Test 5: Edit the previously created GL Account
     * Navigates to edit, cancels, then edits again and renames
     */
    @Test(priority = 5, dependsOnMethods = {"testCreateGLAccount"}, description = "Verify Edit GL Account functionality")
    public void testEditGLAccount() {
        System.out.println("\n[TEST 5] Editing GL Account: " + acNumber);

        // Refresh page to ensure clean state after validation tests
        driver.navigate().refresh();
        waitForPageLoad();

        coaPage.navigateToEdit(acNumber);

        Assert.assertTrue(coaPage.isUpdateBtnDisplayed(),
                         "Update button should be displayed on edit page");

        coaPage.clickCancelAndWaitForListing();

        coaPage.navigateToEdit(acNumber);

        Assert.assertTrue(coaPage.isUpdateBtnDisplayed(),
                         "Update button should be displayed on edit page");

        acNumber = coaPage.editGlAccount(acNumber);
        waitForPageLoad();

        Assert.assertTrue(coaPage.isAddGLAccountBtnDisplayed(),
                         "Add GL Account button should be displayed after edit");

        Assert.assertTrue(coaPage.verifyGLAccount(acNumber),
                         "Updated GL Account should appear in the table");

        System.out.println("[TEST 5] GL Account updated to: " + acNumber);
    }

    /**
     * Test 6: Delete the GL Account
     * Deletes the account and verifies it no longer appears in the table
     */
    @Test(priority = 6, dependsOnMethods = {"testEditGLAccount"}, description = "Verify user is able to Delete GL Account")
    public void testDeleteGLAccount() {
        System.out.println("\n[TEST 6] Deleting GL Account: " + acNumber);

        coaPage.clickDelete(acNumber);

        // Wait for success message and dismiss it
        coaPage.waitForSuccessMessageToDisappear();

        // Refresh page so the AG Grid reloads without the deleted record
        driver.navigate().refresh();
        waitForPageLoad();

        Assert.assertFalse(coaPage.verifyGLAccount(acNumber),
                          "Deleted GL Account should no longer appear in the table");

        System.out.println("[TEST 6] GL Account deleted successfully: " + acNumber);
    }

    /**
     * Helper method to wait for page load
     */
    private void waitForPageLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

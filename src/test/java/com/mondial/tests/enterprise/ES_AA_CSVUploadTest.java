package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;
import com.mondial.utils.DriverManager;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.AlternateAccountPage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Alternate Account Set - CSV Upload Test Class
 * Verifies that accounts can be added to an Alternate Account Set via CSV file upload
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Download directory configured via DriverManager.getDownloadDir()
 */
public class ES_AA_CSVUploadTest extends BaseTest {

    private final String TEMPLATE_CSV_PATH =
            DriverManager.getDownloadDir() + java.io.File.separator + "alternate_accounts_csv_upload_template.csv";

    private HomePage homePage;
    private AlternateAccountPage alternateAccountPage;
    private String alternateAccountName;
    private String symbol;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates unique names for the alternate account set
     * - Cleans up any stale template CSV from a previous run
     */
    @BeforeClass
    public void csvUploadSetup() {
        System.out.println("=== Starting Alternate Account CSV Upload Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        alternateAccountPage = new AlternateAccountPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");

        // Remove stale template file before test to ensure a fresh download
        new java.io.File(TEMPLATE_CSV_PATH).delete();

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
        System.out.println("=== Alternate Account CSV Upload Test Setup Complete ===\n");
    }

    /**
     * Test 1: Create an Alternate Account Set, upload accounts via CSV, verify, then delete
     * - Creates a new alternate account set
     * - Opens the set and uploads a CSV file
     * - Verifies accounts are populated after upload
     * - Navigates back to listing and deletes the set
     */
    @Test(priority = 1, description = "Alternate Account Set - CSV Upload")
    public void testCSVUpload_AlternateAccount() {
        System.out.println("\n[TEST 1] Verifying CSV upload for Alternate Account Set: " + alternateAccountName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        alternateAccountPage.navigateToAlternateAccountPage();

        alternateAccountPage.createAlternateAccount(alternateAccountName, symbol);

        String successMsg = alternateAccountPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Alternate Account Set successfully created."),
                         "Success message should confirm alternate account set creation");

        alternateAccountPage.clickAccountLink(alternateAccountName);

        Assert.assertTrue(alternateAccountPage.isAccountSetDetailDisplayed(),
                         "Account set detail page should be displayed after clicking the account link");

        alternateAccountPage.downloadTemplateCSV();
        System.out.println("[TEST 1] Template CSV downloaded");

        Assert.assertTrue(alternateAccountPage.isFileDownloaded(TEMPLATE_CSV_PATH),
                         "Template CSV file should be downloaded at: " + TEMPLATE_CSV_PATH);

        alternateAccountPage.uploadCSVFile(TEMPLATE_CSV_PATH);
        System.out.println("[TEST 1] Uploaded file: alternate_accounts_csv_upload_template.csv");

        Assert.assertTrue(alternateAccountPage.isAccountsListPopulated(),
                         "Accounts list should be populated after CSV upload");

        System.out.println("[TEST 1] CSV upload successful, navigating back to listing...");

        alternateAccountPage.navigateBackToListing();

        Assert.assertTrue(alternateAccountPage.isAddAlternateAccountBtnDisplayed(),
                         "Should be back on Alternate Account Sets listing page");

        alternateAccountPage.clickDelete(alternateAccountName);

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        String deleteMsg = alternateAccountPage.getSuccessMessage();
        Assert.assertTrue(deleteMsg.contains("Alternate Account Set successfully deleted."),
                         "Success message should confirm alternate account set deletion");

        alternateAccountPage.waitForSuccessMessageToDisappear();

        Assert.assertFalse(alternateAccountPage.verifyRecordPresent(alternateAccountName),
                          "Deleted alternate account set should no longer appear in the table");

        System.out.println("[TEST 1] Alternate Account Set CSV upload test completed successfully");
    }
}

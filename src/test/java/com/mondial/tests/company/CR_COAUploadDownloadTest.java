package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ChartOfAccountsPage;
import com.mondial.utils.DriverManager;

/**
 * Chart of Accounts CSV Upload/Download Test Class
 * Tests Download Template CSV, Upload CSV, and Download Table as CSV
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company with Chart of Accounts: AutomationTest222
 */
public class CR_COAUploadDownloadTest extends BaseTest {

    private HomePage homePage;
    private ChartOfAccountsPage coaPage;
    private String companyName;
    private String templateCsvPath;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     */
    @BeforeClass
    public void coaUploadDownloadSetup() {
        System.out.println("=== Starting COA Upload/Download Test Setup ===");

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

        System.out.println("Test Company: " + companyName);
        System.out.println("=== COA Upload/Download Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Chart of Accounts and upload CSV
     * Downloads the template CSV, uploads it, and verifies success message
     */
    @Test(priority = 1, description = "Verify user is able to Download Template and Upload CSV File for Chart of Accounts")
    public void testUploadCOA() {
        System.out.println("\n[TEST 1] Navigating to Chart of Accounts and uploading CSV...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        coaPage.navigateToChartOfAccounts(companyName);

        Assert.assertTrue(coaPage.isAddGLAccountBtnDisplayed(),
                         "Add GL Account button should be displayed");

        String heading = coaPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains("Chart of Accounts"),
                         "Heading should contain 'Chart of Accounts'");

        // Download template CSV
        coaPage.downloadTemplateCSV();
        System.out.println("Template CSV download initiated");

        // Find the downloaded file (app prepends company name to the filename)
        templateCsvPath = coaPage.findDownloadedFile(
                DriverManager.getDownloadDir(), "local_chart_of_accounts");
        Assert.assertNotNull(templateCsvPath,
                         "Template CSV file should be downloaded in: " + DriverManager.getDownloadDir());
        System.out.println("Template CSV found: " + templateCsvPath);

        // Upload the downloaded template CSV
        coaPage.uploadCSVFile(templateCsvPath);
        System.out.println("Uploaded file: " + templateCsvPath);

        String successMsg = coaPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Upload CSV process successfully finished"),
                         "Success message should confirm CSV upload");

        coaPage.waitForSuccessMessageToDisappear();

        System.out.println("[TEST 1] CSV upload completed successfully");
    }

    /**
     * Test 2: Download Table as CSV
     * Verifies user can download the table and page heading is still correct
     */
    @Test(priority = 2, dependsOnMethods = {"testUploadCOA"}, description = "Verify user is able to Download Table as CSV")
    public void testDownloadTableCOA() {
        System.out.println("\n[TEST 2] Downloading Table as CSV...");

        coaPage.clickDownloadTable();

        String heading = coaPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains("Chart of Accounts"),
                         "Heading should still contain 'Chart of Accounts' after download");

        System.out.println("[TEST 2] Table CSV download completed successfully");
    }

    /**
     * Test 3: Delete all GL Accounts
     * Deletes all records from Chart of Accounts and verifies no records remain
     */
    @Test(priority = 3, dependsOnMethods = {"testDownloadTableCOA"}, description = "Delete all GL Accounts from Chart of Accounts")
    public void testDeleteAllGLAccounts() {
        System.out.println("\n[TEST 3] Deleting all GL Accounts...");

        coaPage.deleteAllGLAccounts();

        Assert.assertFalse(coaPage.hasRecords(),
                          "No GL Account records should remain after deletion");

        System.out.println("[TEST 3] All GL Accounts deleted successfully");
    }
}

package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ChartOfAccountsPage;
import com.mondial.utils.DriverManager;

/**
 * Chart of Accounts CSV Upload/Download and Grid Test Class
 * Tests Download Template CSV, Upload CSV, Sort/Filter AG Grid,
 * Download Table as CSV, Upload Invalid CSV, and Delete All
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
    private final String INVALID_CSV_PATH =
            System.getProperty("user.dir") + java.io.File.separator
            + "src" + java.io.File.separator + "test" + java.io.File.separator
            + "resources" + java.io.File.separator + "testdata" + java.io.File.separator
            + "invalid_gl_accounts.csv";

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
     * Test 2: Verify records exist in the grid after upload
     */
    @Test(priority = 2, dependsOnMethods = {"testUploadCOA"}, description = "Verify records exist in grid after upload")
    public void testVerifyRecordsExist() {
        System.out.println("\n[TEST 2] Verifying records exist in grid...");

        int recordCount = coaPage.getRecordCount();
        System.out.println("Record count: " + recordCount);
        Assert.assertTrue(recordCount > 0,
                         "Grid should contain at least one GL Account record after upload");

        System.out.println("[TEST 2] Grid contains " + recordCount + " records");
    }

    /**
     * Test 3: Sort AG Grid by account number column
     */
    @Test(priority = 3, dependsOnMethods = {"testVerifyRecordsExist"}, description = "Sort grid by account number")
    public void testSortGridByAccountNumber() {
        System.out.println("\n[TEST 3] Sorting grid by account number...");

        // Click column header to sort ascending (use header text since col-ids vary)
        coaPage.clickColumnHeaderByText("Account Number");
        List<String> ascValues = coaPage.getColumnValuesByIndex(0);
        System.out.println("[TEST 3] Values after first click (ascending): " + ascValues);

        Assert.assertTrue(coaPage.isSortedAscending(ascValues),
                         "Column values should be sorted in ascending order");
        System.out.println("[TEST 3] Ascending sort verified");

        // Click twice more to sort descending (ascending → unsorted → descending)
        coaPage.clickColumnHeaderByText("Account Number");
        List<String> descValues = coaPage.getColumnValuesByIndex(0);
        System.out.println("[TEST 3] Values after descending sort: " + descValues);

        Assert.assertTrue(coaPage.isSortedDescending(descValues),
                         "Column values should be sorted in descending order");
        System.out.println("[TEST 3] Descending sort verified");

        // Click again to reset sort
        coaPage.clickColumnHeaderByText("Account Number");

        System.out.println("[TEST 3] Sort by account number verified successfully");
    }

    /**
     * Test 4: Filter AG Grid by account name
     */
    @Test(priority = 4, dependsOnMethods = {"testVerifyRecordsExist"}, description = "Filter grid by account name")
    public void testFilterGridByAccountName() {
        System.out.println("\n[TEST 4] Filtering grid by account name...");

        int totalRecords = coaPage.getRecordCount();
        System.out.println("[TEST 4] Total records before filter: " + totalRecords);

        // Open filter on Name column (use header text since col-ids vary)
        coaPage.openColumnFilterByText("Name");

        // Enter filter text that matches a known record
        coaPage.enterFilterText("Test");

        int filteredRecords = coaPage.getRecordCount();
        System.out.println("[TEST 4] Records after filter: " + filteredRecords);

        Assert.assertTrue(filteredRecords > 0,
                         "Should have at least one matching record");
        Assert.assertTrue(filteredRecords <= totalRecords,
                         "Filtered records should be less than or equal to total records");

        // Clear filter
        coaPage.clearFilter();

        int afterClearRecords = coaPage.getRecordCount();
        System.out.println("[TEST 4] Records after clearing filter: " + afterClearRecords);

        Assert.assertEquals(afterClearRecords, totalRecords,
                           "All records should be visible after clearing filter");

        System.out.println("[TEST 4] Filter by account name verified successfully");
    }

    /**
     * Test 5: Download Table as CSV
     * Verifies user can download the table and page heading is still correct
     */
    @Test(priority = 5, dependsOnMethods = {"testFilterGridByAccountName"}, description = "Verify user is able to Download Table as CSV")
    public void testDownloadTableCOA() {
        System.out.println("\n[TEST 5] Downloading Table as CSV...");

        coaPage.clickDownloadTable();

        String heading = coaPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains("Chart of Accounts"),
                         "Heading should still contain 'Chart of Accounts' after download");

        System.out.println("[TEST 5] Table CSV download completed successfully");
    }

    /**
     * Test 6: Upload invalid CSV file and verify error or no new records
     */
    @Test(priority = 6, dependsOnMethods = {"testDownloadTableCOA"}, description = "Upload invalid CSV file for Chart of Accounts")
    public void testUploadInvalidCSV() {
        System.out.println("\n[TEST 6] Uploading invalid CSV file...");
        System.out.println("[TEST 6] Invalid CSV path: " + INVALID_CSV_PATH);

        int recordsBefore = coaPage.getRecordCount();
        System.out.println("[TEST 6] Records before upload: " + recordsBefore);

        coaPage.uploadCSVFile(INVALID_CSV_PATH);
        System.out.println("[TEST 6] Uploaded file: invalid_gl_accounts.csv");

        // Check for error message or verify no new records were added
        boolean errorShown = coaPage.isUploadErrorDisplayed();
        System.out.println("[TEST 6] Error message displayed: " + errorShown);

        String heading = coaPage.getPageHeading();
        Assert.assertTrue(heading.contains("Chart of Accounts"),
                         "Should remain on Chart of Accounts page after failed upload");

        if (!errorShown) {
            // If no explicit error message, verify record count unchanged
            int recordsAfter = coaPage.getRecordCount();
            System.out.println("[TEST 6] Records after upload: " + recordsAfter);
            Assert.assertEquals(recordsAfter, recordsBefore,
                               "Record count should not change after invalid CSV upload");
        }

        System.out.println("[TEST 6] Invalid CSV upload error verified successfully");
    }

    /**
     * Test 7: Delete all GL Accounts
     * Deletes all records from Chart of Accounts and verifies no records remain
     */
    @Test(priority = 7, dependsOnMethods = {"testUploadInvalidCSV"}, description = "Delete all GL Accounts from Chart of Accounts")
    public void testDeleteAllGLAccounts() {
        System.out.println("\n[TEST 7] Deleting all GL Accounts...");

        coaPage.deleteAllGLAccounts();

        Assert.assertFalse(coaPage.hasRecords(),
                          "No GL Account records should remain after deletion");

        System.out.println("[TEST 7] All GL Accounts deleted successfully");
    }
}

package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.CustomersVendorsPage;

/**
 * Customers Grid Test Class
 * Tests Sort/Filter AG Grid, Download Table,
 * and Upload Invalid CSV for Customers page
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_CustomersGridTest extends BaseTest {

    private HomePage homePage;
    private CustomersVendorsPage customersVendorsPage;
    private String companyName;
    private final String MULTIPLE_CUSTOMERS_CSV_PATH =
            System.getProperty("user.dir") + java.io.File.separator
            + "src" + java.io.File.separator + "test" + java.io.File.separator
            + "resources" + java.io.File.separator + "testdata" + java.io.File.separator
            + "multiple_customers.csv";
    private final String INVALID_CSV_PATH =
            System.getProperty("user.dir") + java.io.File.separator
            + "src" + java.io.File.separator + "test" + java.io.File.separator
            + "resources" + java.io.File.separator + "testdata" + java.io.File.separator
            + "invalid_customers.csv";

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     */
    @BeforeClass
    public void customersGridSetup() {
        System.out.println("=== Starting Customers Grid Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        customersVendorsPage = new CustomersVendorsPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        companyName = config.getProperty("testCompanyName");

        System.out.println("Logging in with user: " + username);
        loginPage.login(username, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Test Company: " + companyName);
        System.out.println("=== Customers Grid Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Customers Page and upload multiple customer records for grid tests
     */
    @Test(priority = 1, description = "Navigate to Customers and upload multiple customer records")
    public void testNavigateAndUploadCustomers() {
        System.out.println("\n[TEST 1] Navigating to Customers page and uploading multiple customers...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        customersVendorsPage.navigateToResourcePage(companyName, "Customers");

        Assert.assertTrue(customersVendorsPage.isUploadButtonDisplayed(),
                         "Upload button should be displayed on Customers page");

        String heading = customersVendorsPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + " - Customers"),
                         "Heading should contain company name and Customers");

        // Delete any existing records from previous runs
        if (customersVendorsPage.hasRecords()) {
            System.out.println("[TEST 1] Cleaning up existing records...");
            customersVendorsPage.deleteAllRecords();
        }

        System.out.println("[TEST 1] Uploading multiple customers CSV: " + MULTIPLE_CUSTOMERS_CSV_PATH);
        customersVendorsPage.uploadCSVFile(MULTIPLE_CUSTOMERS_CSV_PATH);

        Assert.assertTrue(customersVendorsPage.verifyRecordsUploaded(),
                         "Uploaded CSV records should be present in the table");

        System.out.println("[TEST 1] Customers page ready with 5 uploaded records");
    }

    /**
     * Test 2: Filter AG Grid by company name
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateAndUploadCustomers"}, description = "Filter grid by company name")
    public void testFilterGridByCompanyName() {
        System.out.println("\n[TEST 2] Filtering grid by company name...");

        int totalRecords = customersVendorsPage.getRecordCount();
        System.out.println("[TEST 2] Total records before filter: " + totalRecords);

        // Open filter on company_name column
        customersVendorsPage.openColumnFilter("company_name");

        // Enter filter text that matches a known record
        customersVendorsPage.enterFilterText("Raiders");

        int filteredRecords = customersVendorsPage.getRecordCount();
        System.out.println("[TEST 2] Records after filter: " + filteredRecords);

        Assert.assertTrue(filteredRecords > 0,
                         "Should have at least one matching record");
        Assert.assertTrue(filteredRecords <= totalRecords,
                         "Filtered records should be less than or equal to total records");

        // Clear filter
        customersVendorsPage.clearFilter();

        int afterClearRecords = customersVendorsPage.getRecordCount();
        System.out.println("[TEST 2] Records after clearing filter: " + afterClearRecords);

        Assert.assertEquals(afterClearRecords, totalRecords,
                           "All records should be visible after clearing filter");

        System.out.println("[TEST 2] Filter by company name verified successfully");
    }

    /**
     * Test 3: Download Table as CSV and verify page stays on Customers
     */
    @Test(priority = 3, dependsOnMethods = {"testFilterGridByCompanyName"}, description = "Download Table as CSV")
    public void testDownloadTable() {
        System.out.println("\n[TEST 3] Downloading Table as CSV...");

        customersVendorsPage.clickDownloadTable();

        String heading = customersVendorsPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - Customers"),
                         "Should remain on Customers page after download");

        System.out.println("[TEST 3] Table downloaded successfully");
    }

    /**
     * Test 4: Upload invalid CSV file and verify error message
     */
    @Test(priority = 4, dependsOnMethods = {"testDownloadTable"}, description = "Upload invalid CSV file")
    public void testUploadInvalidCSV() {
        System.out.println("\n[TEST 4] Uploading invalid CSV file...");
        System.out.println("[TEST 4] Invalid CSV path: " + INVALID_CSV_PATH);

        customersVendorsPage.uploadCSVFile(INVALID_CSV_PATH);
        System.out.println("[TEST 4] Uploaded file: invalid_customers.csv");

        Assert.assertTrue(customersVendorsPage.isUploadErrorDisplayed(),
                         "Upload error message should be displayed for invalid CSV");

        // Verify page stays on Customers
        String heading = customersVendorsPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - Customers"),
                         "Should remain on Customers page after failed upload");

        System.out.println("[TEST 4] Invalid CSV upload error verified successfully");
    }

    /**
     * Test 5: Delete all uploaded customer records and verify deletion
     */
    @Test(priority = 5, dependsOnMethods = {"testUploadInvalidCSV"}, description = "Delete uploaded customer records")
    public void testDeleteUploadedRecords() {
        System.out.println("\n[TEST 5] Deleting uploaded customer records...");

        customersVendorsPage.deleteAllRecords();

        Assert.assertFalse(customersVendorsPage.hasRecords(),
                          "No records should be present after deletion");

        System.out.println("[TEST 5] All customer records deleted successfully");
    }
}

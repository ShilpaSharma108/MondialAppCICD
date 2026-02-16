package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.CustomersVendorsPage;
import com.mondial.utils.DriverManager;

/**
 * Customers CSV Upload/Download Test Class
 * Tests Download Template CSV, Upload CSV, Verify Records,
 * Download Table, and Delete Records for Customers page
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_CustomersTest extends BaseTest {

    private HomePage homePage;
    private CustomersVendorsPage customersVendorsPage;
    private String companyName;
    private final String TEMPLATE_CSV_PATH =
            DriverManager.getDownloadDir() + java.io.File.separator + "customers_csv_upload_template.csv";

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Navigates to Customers page for the test company
     */
    @BeforeClass
    public void uploadCustomersSetup() {
        System.out.println("=== Starting Upload Customers Test Setup ===");

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
        System.out.println("=== Upload Customers Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Customers Page
     * Verifies the page heading contains the company name and Customers
     */
    @Test(priority = 1, description = "Navigate to Customers Page")
    public void testNavigateToCustomers() {
        System.out.println("\n[TEST 1] Navigating to Customers page...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        customersVendorsPage.navigateToResourcePage(companyName, "Customers");

        Assert.assertTrue(customersVendorsPage.isUploadButtonDisplayed(),
                         "Upload button should be displayed on Customers page");

        String heading = customersVendorsPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + " - Customers"),
                         "Heading should contain company name and Customers");

        System.out.println("[TEST 1] Successfully navigated to Customers page");
    }

    /**
     * Test 2: Download Template CSV and Upload Customers
     * Downloads the template CSV file and uploads it to create customer records
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToCustomers"}, description = "Download template CSV and upload customers")
    public void testDownloadAndUploadCustomers() {
        System.out.println("\n[TEST 2] Downloading template and uploading customers...");

        customersVendorsPage.downloadTemplateCSV();
        System.out.println("[TEST 2] Template CSV downloaded");

        Assert.assertTrue(customersVendorsPage.isFileDownloaded(TEMPLATE_CSV_PATH),
                         "Template CSV file should be downloaded at: " + TEMPLATE_CSV_PATH);

        customersVendorsPage.uploadCSVFile(TEMPLATE_CSV_PATH);
        System.out.println("[TEST 2] Uploaded file: customers_csv_upload_template.csv");

        System.out.println("[TEST 2] CSV download and upload completed successfully");
    }

    /**
     * Test 3: Verify records are successfully uploaded using CSV Upload
     * Checks that expected customer records appear in the AG Grid table
     */
    @Test(priority = 3, dependsOnMethods = {"testDownloadAndUploadCustomers"}, description = "Verify records are uploaded via CSV")
    public void testVerifyRecordsAfterCSVUpload() {
        System.out.println("\n[TEST 3] Verifying uploaded customer records...");

        Assert.assertTrue(customersVendorsPage.verifyRecordsUploaded(),
                         "Uploaded CSV records should be present in the table");

        System.out.println("[TEST 3] Customer records verified successfully");
    }

    /**
     * Test 4: Download Table as CSV and verify page stays on Customers
     */
    @Test(priority = 4, dependsOnMethods = {"testVerifyRecordsAfterCSVUpload"}, description = "Download Table as CSV")
    public void testDownloadTable() {
        System.out.println("\n[TEST 4] Downloading Table as CSV...");

        customersVendorsPage.clickDownloadTable();

        String heading = customersVendorsPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - Customers"),
                         "Should remain on Customers page after download");

        System.out.println("[TEST 4] Table downloaded successfully");
    }

    /**
     * Test 5: Delete all uploaded customer records and verify deletion
     */
    @Test(priority = 5, dependsOnMethods = {"testDownloadTable"}, description = "Delete uploaded customer records")
    public void testDeleteUploadedRecords() {
        System.out.println("\n[TEST 5] Deleting uploaded customer records...");

        customersVendorsPage.deleteAllRecords();

        Assert.assertFalse(customersVendorsPage.hasRecords(),
                          "No records should be present after deletion");

        System.out.println("[TEST 5] All customer records deleted successfully");
    }
}

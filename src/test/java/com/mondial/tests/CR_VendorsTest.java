package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.CustomersVendorsPage;
import com.mondial.utils.DriverManager;

/**
 * Vendors CSV Upload/Download Test Class
 * Tests Download Template CSV, Upload CSV, Verify Records,
 * and Delete Records for Vendors page
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_VendorsTest extends BaseTest {

    private HomePage homePage;
    private CustomersVendorsPage customersVendorsPage;
    private String companyName;
    private final String TEMPLATE_CSV_PATH =
            DriverManager.getDownloadDir() + java.io.File.separator + "vendors_csv_upload_template.csv";

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     */
    @BeforeClass
    public void uploadVendorsSetup() {
        System.out.println("=== Starting Vendors Test Setup ===");

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
        System.out.println("=== Vendors Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Vendors Page
     * Verifies the page heading contains the company name and Vendors
     */
    @Test(priority = 1, description = "Navigate to Vendors Page")
    public void testNavigateToVendors() {
        System.out.println("\n[TEST 1] Navigating to Vendors page...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        customersVendorsPage.navigateToResourcePage(companyName, "Vendors");

        Assert.assertTrue(customersVendorsPage.isUploadButtonDisplayed(),
                         "Upload button should be displayed on Vendors page");

        String heading = customersVendorsPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + " - Vendors"),
                         "Heading should contain company name and Vendors");

        System.out.println("[TEST 1] Successfully navigated to Vendors page");
    }

    /**
     * Test 2: Download Template CSV and Upload Vendors
     * Downloads the template CSV file and uploads it to create vendor records
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToVendors"}, description = "Download template CSV and upload vendors")
    public void testDownloadAndUploadVendors() {
        System.out.println("\n[TEST 2] Downloading template and uploading vendors...");

        customersVendorsPage.downloadTemplateCSV();
        System.out.println("[TEST 2] Template CSV downloaded");

        Assert.assertTrue(customersVendorsPage.isFileDownloaded(TEMPLATE_CSV_PATH),
                         "Template CSV file should be downloaded at: " + TEMPLATE_CSV_PATH);

        customersVendorsPage.uploadCSVFile(TEMPLATE_CSV_PATH);
        System.out.println("[TEST 2] Uploaded file: vendors_csv_upload_template.csv");

        System.out.println("[TEST 2] CSV download and upload completed successfully");
    }

    /**
     * Test 3: Verify records are successfully uploaded using CSV Upload
     * Checks that expected vendor records appear in the AG Grid table
     */
    @Test(priority = 3, dependsOnMethods = {"testDownloadAndUploadVendors"}, description = "Verify records are uploaded via CSV")
    public void testVerifyRecordsAfterCSVUpload() {
        System.out.println("\n[TEST 3] Verifying uploaded vendor records...");

        Assert.assertTrue(customersVendorsPage.verifyRecordsUploaded(),
                         "Uploaded CSV records should be present in the table");

        System.out.println("[TEST 3] Vendor records verified successfully");
    }

    /**
     * Test 4: Delete all uploaded vendor records and verify deletion
     */
    @Test(priority = 4, dependsOnMethods = {"testVerifyRecordsAfterCSVUpload"}, description = "Delete uploaded vendor records")
    public void testDeleteUploadedRecords() {
        System.out.println("\n[TEST 4] Deleting uploaded vendor records...");

        customersVendorsPage.deleteAllRecords();

        Assert.assertFalse(customersVendorsPage.hasRecords(),
                          "No records should be present after deletion");

        System.out.println("[TEST 4] All vendor records deleted successfully");
    }
}

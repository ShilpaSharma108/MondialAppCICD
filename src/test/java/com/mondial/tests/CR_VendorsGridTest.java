package com.mondial.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.CustomersVendorsPage;

/**
 * Vendors Grid Test Class
 * Tests Edit Record, Sort/Filter AG Grid, Download Table,
 * and Upload Invalid CSV for Vendors page
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_VendorsGridTest extends BaseTest {

    private HomePage homePage;
    private CustomersVendorsPage customersVendorsPage;
    private String companyName;
    private final String MULTIPLE_VENDORS_CSV_PATH =
            System.getProperty("user.dir") + java.io.File.separator
            + "src" + java.io.File.separator + "test" + java.io.File.separator
            + "resources" + java.io.File.separator + "testdata" + java.io.File.separator
            + "multiple_vendors.csv";
    private final String INVALID_CSV_PATH =
            System.getProperty("user.dir") + java.io.File.separator
            + "src" + java.io.File.separator + "test" + java.io.File.separator
            + "resources" + java.io.File.separator + "testdata" + java.io.File.separator
            + "invalid_vendors.csv";

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     */
    @BeforeClass
    public void vendorsGridSetup() {
        System.out.println("=== Starting Vendors Grid Test Setup ===");

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
        System.out.println("=== Vendors Grid Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Vendors Page and upload multiple vendor records for grid tests
     * Uses the static multiple_vendors.csv file with 5 vendor records
     */
    @Test(priority = 1, description = "Navigate to Vendors and upload multiple vendor records")
    public void testNavigateAndUploadVendors() {
        System.out.println("\n[TEST 1] Navigating to Vendors page and uploading multiple vendors...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        customersVendorsPage.navigateToResourcePage(companyName, "Vendors");

        Assert.assertTrue(customersVendorsPage.isUploadButtonDisplayed(),
                         "Upload button should be displayed on Vendors page");

        String heading = customersVendorsPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + " - Vendors"),
                         "Heading should contain company name and Vendors");

        System.out.println("[TEST 1] Uploading multiple vendors CSV: " + MULTIPLE_VENDORS_CSV_PATH);
        customersVendorsPage.uploadCSVFile(MULTIPLE_VENDORS_CSV_PATH);

        Assert.assertTrue(customersVendorsPage.verifyRecordsUploaded(),
                         "Uploaded CSV records should be present in the table");

        System.out.println("[TEST 1] Vendors page ready with 5 uploaded records");
    }

    /**
     * Test 2: Edit a vendor record
     * Clicks edit on the first row, modifies company name, saves, and verifies
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateAndUploadVendors"}, description = "Edit vendor record")
    public void testEditVendorRecord() {
        System.out.println("\n[TEST 2] Editing vendor record...");

        // Get the original company name from first row
        List<String> originalValues = customersVendorsPage.getColumnValues("company_name");
        Assert.assertTrue(originalValues.size() > 0, "Should have at least one record to edit");
        String originalName = originalValues.get(0);
        System.out.println("[TEST 2] Original company name: " + originalName);

        // Click edit on first row
        customersVendorsPage.clickEditOnRow(0);

        // Modify company name
        String editedName = originalName + " Edited";
        customersVendorsPage.editVendorCompanyName(editedName);
        System.out.println("[TEST 2] Edited company name to: " + editedName);

        // Save the edit
        customersVendorsPage.clickSaveEdit();
        customersVendorsPage.waitForUpdateConfirmationToDisappear();

        // Verify updated name appears in grid
        Assert.assertTrue(customersVendorsPage.verifyVendorInGrid(editedName),
                         "Updated vendor name should appear in the grid");

        System.out.println("[TEST 2] Vendor record edited successfully");
    }

    /**
     * Test 3: Sort AG Grid by company name column
     * Clicks column header to sort ascending, verifies order, then sorts descending
     */
    @Test(priority = 3, dependsOnMethods = {"testEditVendorRecord"}, description = "Sort grid by company name")
    public void testSortGridByCompanyName() {
        System.out.println("\n[TEST 3] Sorting grid by company name...");

        // Click column header to sort ascending
        customersVendorsPage.clickColumnHeader("company_name");
        List<String> ascValues = customersVendorsPage.getColumnValues("company_name");
        System.out.println("[TEST 3] Values after first click (ascending): " + ascValues);

        Assert.assertTrue(customersVendorsPage.isSortedAscending(ascValues),
                         "Column values should be sorted in ascending order");
        System.out.println("[TEST 3] Ascending sort verified");

        // Click again to sort descending
        customersVendorsPage.clickColumnHeader("company_name");
        List<String> descValues = customersVendorsPage.getColumnValues("company_name");
        System.out.println("[TEST 3] Values after second click (descending): " + descValues);

        Assert.assertTrue(customersVendorsPage.isSortedDescending(descValues),
                         "Column values should be sorted in descending order");
        System.out.println("[TEST 3] Descending sort verified");

        // Click again to reset sort
        customersVendorsPage.clickColumnHeader("company_name");

        System.out.println("[TEST 3] Sort by company name verified successfully");
    }

    /**
     * Test 4: Filter AG Grid by company name
     * Opens filter, enters text, verifies filtered results, clears filter
     */
    @Test(priority = 4, dependsOnMethods = {"testSortGridByCompanyName"}, description = "Filter grid by company name")
    public void testFilterGridByCompanyName() {
        System.out.println("\n[TEST 4] Filtering grid by company name...");

        int totalRecords = customersVendorsPage.getRecordCount();
        System.out.println("[TEST 4] Total records before filter: " + totalRecords);

        // Open filter on company_name column
        customersVendorsPage.openColumnFilter("company_name");

        // Enter filter text that matches a known record
        customersVendorsPage.enterFilterText("Raiders");

        int filteredRecords = customersVendorsPage.getRecordCount();
        System.out.println("[TEST 4] Records after filter: " + filteredRecords);

        Assert.assertTrue(filteredRecords > 0,
                         "Should have at least one matching record");
        Assert.assertTrue(filteredRecords <= totalRecords,
                         "Filtered records should be less than or equal to total records");

        // Clear filter
        customersVendorsPage.clearFilter();

        int afterClearRecords = customersVendorsPage.getRecordCount();
        System.out.println("[TEST 4] Records after clearing filter: " + afterClearRecords);

        Assert.assertEquals(afterClearRecords, totalRecords,
                           "All records should be visible after clearing filter");

        System.out.println("[TEST 4] Filter by company name verified successfully");
    }

    /**
     * Test 5: Download Table as CSV and verify page stays on Vendors
     */
    @Test(priority = 5, dependsOnMethods = {"testFilterGridByCompanyName"}, description = "Download Table as CSV")
    public void testDownloadTable() {
        System.out.println("\n[TEST 5] Downloading Table as CSV...");

        customersVendorsPage.clickDownloadTable();

        String heading = customersVendorsPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - Vendors"),
                         "Should remain on Vendors page after download");

        System.out.println("[TEST 5] Table downloaded successfully");
    }

    /**
     * Test 6: Upload invalid CSV file and verify error message
     */
    @Test(priority = 6, dependsOnMethods = {"testDownloadTable"}, description = "Upload invalid CSV file")
    public void testUploadInvalidCSV() {
        System.out.println("\n[TEST 6] Uploading invalid CSV file...");
        System.out.println("[TEST 6] Invalid CSV path: " + INVALID_CSV_PATH);

        customersVendorsPage.uploadCSVFile(INVALID_CSV_PATH);
        System.out.println("[TEST 6] Uploaded file: invalid_vendors.csv");

        Assert.assertTrue(customersVendorsPage.isUploadErrorDisplayed(),
                         "Upload error message should be displayed for invalid CSV");

        // Verify page stays on Vendors
        String heading = customersVendorsPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - Vendors"),
                         "Should remain on Vendors page after failed upload");

        System.out.println("[TEST 6] Invalid CSV upload error verified successfully");
    }

    /**
     * Test 7: Delete all uploaded vendor records and verify deletion
     */
    @Test(priority = 7, dependsOnMethods = {"testUploadInvalidCSV"}, description = "Delete uploaded vendor records")
    public void testDeleteUploadedRecords() {
        System.out.println("\n[TEST 7] Deleting uploaded vendor records...");

        customersVendorsPage.deleteAllRecords();

        Assert.assertFalse(customersVendorsPage.hasRecords(),
                          "No records should be present after deletion");

        System.out.println("[TEST 7] All vendor records deleted successfully");
    }
}

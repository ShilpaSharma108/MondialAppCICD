package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportingSegmentPage;

/**
 * Segment Options CRUD Test Class
 * Tests Create, Edit, and Delete operations for GL Account Segment Options
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 * - Company must have available segment slots (Account Segments > 1)
 */
public class CR_SegmentOptionsAllTest extends BaseTest {

    private HomePage homePage;
    private ReportingSegmentPage reportingSegmentPage;
    private String companyName;
    private String reportingSegment;
    private String reportingSegmentOption = "Test Options";
    private int numOfSegments;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates random name for segment tests
     */
    @BeforeClass
    public void segmentOptionsSetup() {
        System.out.println("=== Starting Segment Options CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        reportingSegmentPage = new ReportingSegmentPage(driver);

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

        reportingSegment = "SegOpt_" + System.currentTimeMillis();
        System.out.println("Generated Segment name: " + reportingSegment);
        System.out.println("Test Company: " + companyName);
        System.out.println("=== Segment Options CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Create Reporting Segments and Segment Options
     * Gets available segment count, creates segments, then creates an option
     */
    @Test(priority = 1, description = "Create Reporting Segment Options")
    public void testCreateReportingSegmentOptions() {
        System.out.println("\n[TEST 1] Creating Reporting Segment Options...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        numOfSegments = reportingSegmentPage.getAccountSegmentCount(companyName) - 1;
        System.out.println("Number of additional segments to create: " + numOfSegments);

        reportingSegmentPage.navigateToReportingSegment(companyName);

        String heading = reportingSegmentPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + " - GL Account Segments"),
                         "Heading should contain company name and GL Account Segments");

        // Delete existing segments if any before proceeding
        System.out.println("[TEST 1] Checking for existing segments to clean up...");
        reportingSegmentPage.deleteAllSegments();

        reportingSegmentPage.createReportingSegmentOptions(reportingSegment, reportingSegmentOption, numOfSegments);

        Assert.assertTrue(reportingSegmentPage.verifySegmentExists(reportingSegmentOption),
                         "Segment option should appear in the table");

        System.out.println("[TEST 1] Segment option created successfully: " + reportingSegmentOption);
    }

    /**
     * Test 2: Edit Reporting Segment Option
     * First edits and cancels, then edits and saves
     */
    @Test(priority = 2, dependsOnMethods = {"testCreateReportingSegmentOptions"}, description = "Edit Reporting Segment Options")
    public void testEditReportingSegmentOption() {
        System.out.println("\n[TEST 2] Editing Segment Option: " + reportingSegmentOption);

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentOptionsBtnDisplayed(),
                         "Add GL Segment Options button should be displayed");

        // First attempt - edit and cancel
        reportingSegmentPage.navigateToEdit(reportingSegmentOption);

        Assert.assertTrue(reportingSegmentPage.isCreateButtonDisplayed(),
                         "Create/Submit button should be displayed on edit page");

        reportingSegmentPage.clickCancel();

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentOptionsBtnDisplayed(),
                         "Should return to options listing after cancel");
        System.out.println("[TEST 2] Edit cancel verified successfully");

        // Second attempt - edit and save
        reportingSegmentPage.navigateToEdit(reportingSegmentOption);

        Assert.assertTrue(reportingSegmentPage.isCreateButtonDisplayed(),
                         "Create/Submit button should be displayed on edit page");

        reportingSegmentOption = reportingSegmentPage.editSegmentOptions(reportingSegmentOption);
        reportingSegmentPage.clickCreateButton();

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentOptionsBtnDisplayed(),
                         "Should return to options listing after update");
        Assert.assertTrue(reportingSegmentPage.verifySegmentExists(reportingSegmentOption),
                         "Updated option name should appear in the table");

        System.out.println("[TEST 2] Segment option updated to: " + reportingSegmentOption);
    }

    /**
     * Test 3: Upload Segment Options via CSV
     * Downloads template CSV, uploads it, and waits for completion
     */
    @Test(priority = 3, dependsOnMethods = {"testEditReportingSegmentOption"}, description = "Upload Reporting Segment Options using CSV")
    public void testUploadCSV() {
        System.out.println("\n[TEST 3] Uploading Segment Options via CSV...");

        reportingSegmentPage.uploadCSV();

        System.out.println("[TEST 3] CSV upload completed successfully");
    }

    /**
     * Test 4: Verify uploaded CSV records and download table
     * Verifies records from CSV appear in table, then downloads the table
     */
    @Test(priority = 4, dependsOnMethods = {"testUploadCSV"}, description = "Verify CSV upload and download table")
    public void testVerifyUploadAndDownloadTable() {
        System.out.println("\n[TEST 4] Verifying uploaded records and downloading table...");

        Assert.assertTrue(reportingSegmentPage.verifyRecordsUploaded(),
                         "Uploaded CSV records should be present in the table");
        System.out.println("[TEST 4] Uploaded records verified successfully");

        reportingSegmentPage.clickDownloadTable();

        String heading = reportingSegmentPage.getPageHeading();
        System.out.println("Page heading after download: " + heading);
        Assert.assertTrue(heading.contains(companyName),
                         "Should remain on the segment options page after download");

        System.out.println("[TEST 4] Table download completed successfully");
    }

    /**
     * Test 5: Delete Reporting Segment Option and cleanup created segments
     * Deletes the option, navigates home, then deletes all created segments
     */
    @Test(priority = 5, dependsOnMethods = {"testVerifyUploadAndDownloadTable"}, description = "Delete Reporting Segment Options")
    public void testDeleteReportingSegmentOption() {
        System.out.println("\n[TEST 5] Deleting Segment Option: " + reportingSegmentOption);

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentOptionsBtnDisplayed(),
                         "Add GL Segment Options button should be displayed");

        reportingSegmentPage.clickDelete(reportingSegmentOption);

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        reportingSegmentPage.waitForConfirmationMessageToDisappear();

        Assert.assertFalse(reportingSegmentPage.verifySegmentExists(reportingSegmentOption),
                          "Deleted option should no longer appear in the table");

        System.out.println("[TEST 5] Segment option deleted successfully");

        // Cleanup: navigate home and delete all created segments
        System.out.println("[TEST 5] Cleaning up created segments...");
        homePage.clickHome();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be back on home page");

        reportingSegmentPage.deleteReportingSegment(reportingSegment, companyName);

        Assert.assertFalse(reportingSegmentPage.verifySegmentExists(reportingSegment),
                          "Created segments should be cleaned up");

        System.out.println("[TEST 5] Cleanup completed successfully");
    }
}

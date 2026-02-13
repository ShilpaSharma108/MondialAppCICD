package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportingSegmentPage;

/**
 * Reporting Segment (GL Account Segment) CRUD Test Class
 * Tests Create, Read, Update, and Delete operations for Reporting Segments
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_SegmentCRUDTest extends BaseTest {

    private HomePage homePage;
    private ReportingSegmentPage reportingSegmentPage;
    private String companyName;
    private String reportingSegment;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates random name for reporting segment tests
     */
    @BeforeClass
    public void reportingSegmentSetup() {
        System.out.println("=== Starting Reporting Segment CRUD Test Setup ===");

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

        reportingSegment = "RptSeg_" + System.currentTimeMillis();
        System.out.println("Generated Reporting Segment name: " + reportingSegment);
        System.out.println("Test Company: " + companyName);
        System.out.println("=== Reporting Segment CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Reporting Segments page for the test company
     * Verifies the page heading contains the company name
     */
    @Test(priority = 1, description = "Navigate to Reporting Segment Page")
    public void testNavigateToReportingSegments() {
        System.out.println("\n[TEST 1] Navigating to Reporting Segments page...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        reportingSegmentPage.navigateToReportingSegment(companyName);

        String heading = reportingSegmentPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + " - GL Account Segments"),
                         "Heading should contain company name and GL Account Segments");

        // Add button may not be present if segment limit is reached
        if (reportingSegmentPage.isAddGLSegmentBtnDisplayed()) {
            System.out.println("[TEST 1] Add GL Account Segment button is available");
        } else {
            System.out.println("[TEST 1] Segment limit reached - Add button not available");
        }

        System.out.println("[TEST 1] Successfully navigated to Reporting Segments page");
    }

    /**
     * Test 2: Create a new Reporting Segment
     * First cancels to verify cancel works, then creates and verifies
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToReportingSegments"}, description = "Create Reporting Segment")
    public void testCreateReportingSegment() {
        System.out.println("\n[TEST 2] Creating Reporting Segment: " + reportingSegment);

        // First attempt - cancel
        reportingSegmentPage.createNewSegment(reportingSegment);
        reportingSegmentPage.clickCancel();

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentBtnDisplayed(),
                         "Should return to listing page after cancel");
        System.out.println("[TEST 2] Cancel verified successfully");

        // Second attempt - create
        reportingSegmentPage.createNewSegment(reportingSegment);
        reportingSegmentPage.clickCreateButton();

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentBtnDisplayed(),
                         "Should return to listing page after create");
        Assert.assertTrue(reportingSegmentPage.verifySegmentExists(reportingSegment),
                         "Newly created segment should appear in the table");

        System.out.println("[TEST 2] Reporting Segment created successfully: " + reportingSegment);
    }

    /**
     * Test 3: Edit the previously created Reporting Segment
     * First edits and cancels, then edits and saves
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateReportingSegment"}, description = "Edit Reporting Segment")
    public void testEditReportingSegment() {
        System.out.println("\n[TEST 3] Editing Reporting Segment: " + reportingSegment);

        // First attempt - edit and cancel
        reportingSegmentPage.navigateToEdit(reportingSegment);

        Assert.assertTrue(reportingSegmentPage.isCreateButtonDisplayed(),
                         "Create/Submit button should be displayed on edit page");

        reportingSegmentPage.editSegment(reportingSegment);
        reportingSegmentPage.clickCancel();

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentBtnDisplayed(),
                         "Should return to listing page after cancel");
        System.out.println("[TEST 3] Edit cancel verified successfully");

        // Second attempt - edit and save
        reportingSegmentPage.navigateToEdit(reportingSegment);

        Assert.assertTrue(reportingSegmentPage.isCreateButtonDisplayed(),
                         "Create/Submit button should be displayed on edit page");

        reportingSegment = reportingSegmentPage.editSegment(reportingSegment);
        reportingSegmentPage.clickCreateButton();

        Assert.assertTrue(reportingSegmentPage.isAddGLSegmentBtnDisplayed(),
                         "Should return to listing page after update");
        Assert.assertTrue(reportingSegmentPage.verifySegmentExists(reportingSegment),
                         "Updated segment name should appear in the table");

        System.out.println("[TEST 3] Reporting Segment updated to: " + reportingSegment);
    }

    /**
     * Test 4: Download Table as CSV from the segment listing page
     * Clicks Download Table as CSV and verifies the file is downloaded
     */
    @Test(priority = 4, dependsOnMethods = {"testEditReportingSegment"}, description = "Download Table as CSV")
    public void testDownloadTableCSV() {
        System.out.println("\n[TEST 4] Downloading Table as CSV...");

        reportingSegmentPage.clickDownloadTable();

        String downloadedFilePath = "C:\\Users\\Shilpa\\Downloads\\gl_account_segments_automation_test_dnd.csv";
        Assert.assertTrue(reportingSegmentPage.isTemplateCSVDownloaded(downloadedFilePath),
                         "Table CSV file should be downloaded at: " + downloadedFilePath);

        String heading = reportingSegmentPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - GL Account Segments"),
                         "Should remain on the segments listing page after download");

        System.out.println("[TEST 4] Table CSV downloaded successfully at: " + downloadedFilePath);
    }

    /**
     * Test 5: Delete the Reporting Segment
     * Navigates back home, then to reporting segments to delete
     */
    @Test(priority = 5, dependsOnMethods = {"testDownloadTableCSV"}, description = "Delete Reporting Segment")
    public void testDeleteReportingSegment() {
        System.out.println("\n[TEST 5] Deleting Reporting Segment: " + reportingSegment);

        // Navigate back to reporting segments page
        homePage.clickHome();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be back on home page");

        reportingSegmentPage.navigateToReportingSegment(companyName);

        reportingSegmentPage.clickDelete(reportingSegment);

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        reportingSegmentPage.waitForConfirmationMessageToDisappear();

        Assert.assertFalse(reportingSegmentPage.verifySegmentExists(reportingSegment),
                          "Deleted segment should no longer appear in the table");

        System.out.println("[TEST 5] Reporting Segment deleted successfully: " + reportingSegment);
    }
}

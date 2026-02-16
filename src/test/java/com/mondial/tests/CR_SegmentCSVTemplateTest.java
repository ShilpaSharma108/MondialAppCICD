package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportingSegmentPage;
import com.mondial.utils.DriverManager;

/**
 * GL Account Segment CSV Template Download and Upload Test Class
 * Tests Download Template CSV File and Upload CSV File functionality
 * on the GL Account Segments page
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_SegmentCSVTemplateTest extends BaseTest {

    private HomePage homePage;
    private ReportingSegmentPage reportingSegmentPage;
    private String companyName;
    private final String TEMPLATE_CSV_PATH =
            DriverManager.getDownloadDir() + java.io.File.separator + "gl_account_segments_csv_upload_template.csv";

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Navigates to GL Account Segments page
     */
    @BeforeClass
    public void csvTemplateSetup() {
        System.out.println("=== Starting Segment CSV Template Test Setup ===");

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

        System.out.println("Test Company: " + companyName);

        // Navigate to GL Account Segments page
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");
        reportingSegmentPage.navigateToReportingSegment(companyName);

        String heading = reportingSegmentPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - GL Account Segments"),
                         "Heading should contain company name and GL Account Segments");

        // Delete existing segments if any before proceeding
        System.out.println("Checking for existing segments to clean up...");
        reportingSegmentPage.deleteAllSegments();

        System.out.println("Navigated to GL Account Segments page");
        System.out.println("=== Segment CSV Template Test Setup Complete ===\n");
    }

    /**
     * Test 1: Download Template CSV File
     * Clicks the Download Template CSV link and verifies the file is downloaded
     */
    @Test(priority = 1, description = "Download Template CSV File from GL Account Segments page")
    public void testDownloadTemplateCSV() {
        System.out.println("\n[TEST 1] Downloading Template CSV File...");

        reportingSegmentPage.downloadTemplateCSV();

        Assert.assertTrue(reportingSegmentPage.isTemplateCSVDownloaded(TEMPLATE_CSV_PATH),
                         "Template CSV file should be downloaded at: " + TEMPLATE_CSV_PATH);

        System.out.println("[TEST 1] Template CSV file downloaded successfully at: " + TEMPLATE_CSV_PATH);
    }

    /**
     * Test 2: Upload CSV File and verify segments are created
     * Uploads the downloaded template CSV file and verifies segments appear in table
     */
    @Test(priority = 2, dependsOnMethods = {"testDownloadTemplateCSV"}, description = "Upload CSV File and verify segments are created")
    public void testUploadCSVFile() {
        System.out.println("\n[TEST 2] Uploading CSV File: " + TEMPLATE_CSV_PATH);

        reportingSegmentPage.uploadCSVFile(TEMPLATE_CSV_PATH);
        System.out.println("[TEST 2] Uploaded file: gl_account_segments_csv_upload_template.csv");

        Assert.assertTrue(reportingSegmentPage.verifySegmentExists("Cost Center"),
                         "Segments should be created after uploading the CSV file");

        System.out.println("[TEST 2] CSV file uploaded and segments verified successfully");
    }

    /**
     * Test 3: Delete the segments created via CSV upload
     * Deletes all segments on the page and verifies they are removed
     */
    @Test(priority = 3, dependsOnMethods = {"testUploadCSVFile"}, description = "Delete segments created via CSV upload")
    public void testDeleteUploadedSegments() {
        System.out.println("\n[TEST 3] Deleting segments created via CSV upload...");

        reportingSegmentPage.deleteAllSegments();

        Assert.assertFalse(reportingSegmentPage.verifySegmentExists("Cost Center"),
                          "Uploaded segments should no longer be present after deletion");

        System.out.println("[TEST 3] All uploaded segments deleted successfully");
    }
}

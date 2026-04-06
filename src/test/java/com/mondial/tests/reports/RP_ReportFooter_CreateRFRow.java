package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportFooter;

/**
 * Report Footer - Create RF Row Test Class
 * Tests creating and deleting Report Footer Rows within a Report Footer
 */
public class RP_ReportFooter_CreateRFRow extends BaseTest {

    private HomePage homePage;
    private ReportFooter rf;
    private String recordName;

    @BeforeClass
    public void reportFooterSetup() {
        System.out.println("=== Starting Report Footer Create RF Row Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new ReportFooter(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "RptFtr_" + System.currentTimeMillis();
        System.out.println("Generated Report Footer name: " + recordName);
        System.out.println("=== Report Footer Create RF Row Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Adding new Report Footer Row")
    public void testCreateReportFooterRow() {
        System.out.println("\n[TEST 1] Creating Report Footer and inserting a row: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rf.navigateToReportFooter();

        Assert.assertTrue(rf.isAddReportFooterBtnDisplayed(),
                "Add Report Footer button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        rf.addNewReportFooter(companyName, recordName);
        rf.createRFRow();

        System.out.println("[TEST 1] Report Footer Row created successfully");
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateReportFooterRow"}, description = "Delete newly added Report Footer Row")
    public void testDeleteReportFooterRow() {
        System.out.println("\n[TEST 2] Deleting Report Footer Row...");

        Assert.assertTrue(rf.newRowPresent("1"),
                "Newly added report footer row with ordinal '1' should be visible in the grid");

        rf.deleteRFRow("1");
        rf.clickCancelAndWaitForListing();
        rf.deleteReportFooter(recordName);

        Assert.assertFalse(rf.verifyRecordPresent(recordName),
                "Report footer should be deleted during cleanup");

        System.out.println("[TEST 2] Report Footer Row and Report Footer deleted successfully");
    }
}

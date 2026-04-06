package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportFooter;

/**
 * Report Footer CRUD Test Class
 * Tests Create, Read, Update and Delete operations for Report Footers
 */
public class RP_ReportFooter_CRUD extends BaseTest {

    private HomePage homePage;
    private ReportFooter rf;
    private String recordName;

    @BeforeClass
    public void reportFooterSetup() {
        System.out.println("=== Starting Report Footer CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new ReportFooter(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "RptFtr_" + System.currentTimeMillis();
        System.out.println("Generated Report Footer name: " + recordName);
        System.out.println("=== Report Footer CRUD Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Navigation to Report Footers Page and Create Report Footer")
    public void testCreateReportFooter() {
        System.out.println("\n[TEST 1] Creating Report Footer: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rf.navigateToReportFooter();

        Assert.assertTrue(rf.isAddReportFooterBtnDisplayed(),
                "Add Report Footer button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        rf.addNewReportFooter(companyName, recordName);
        rf.cancelAndWaitForListing();

        Assert.assertTrue(rf.verifyRecordPresent(recordName),
                "Newly created report footer should appear in the listing");

        System.out.println("[TEST 1] Report Footer created successfully: " + recordName);
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateReportFooter"}, description = "Verify Edit Report Footer")
    public void testEditReportFooter() {
        System.out.println("\n[TEST 2] Editing Report Footer: " + recordName);

        recordName = rf.editReportFooter(recordName);

        Assert.assertTrue(rf.verifyRecordPresent(recordName),
                "Updated report footer name should appear in the listing");

        System.out.println("[TEST 2] Report Footer updated to: " + recordName);
    }

    @Test(priority = 3, dependsOnMethods = {"testEditReportFooter"}, description = "Verify Delete Report Footer")
    public void testDeleteReportFooter() {
        System.out.println("\n[TEST 3] Deleting Report Footer: " + recordName);

        rf.deleteReportFooter(recordName);

        Assert.assertFalse(rf.verifyRecordPresent(recordName),
                "Deleted report footer should not appear in the listing");

        System.out.println("[TEST 3] Report Footer deleted successfully");
    }
}

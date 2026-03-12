package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportHeader;

/**
 * Report Header - Create RH Row Test Class
 * Tests creating and deleting Report Header Rows within a Report Header
 */
public class ReportHeader_CreateRHRow extends BaseTest {

    private HomePage homePage;
    private ReportHeader rh;
    private String recordName;

    @BeforeClass
    public void reportHeaderSetup() {
        System.out.println("=== Starting Report Header Create RH Row Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rh = new ReportHeader(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "RptHdr_" + System.currentTimeMillis();
        System.out.println("Generated Report Header name: " + recordName);
        System.out.println("=== Report Header Create RH Row Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Adding new Report Header Row")
    public void testCreateReportHeaderRow() {
        System.out.println("\n[TEST 1] Creating Report Header and inserting a row: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rh.navigateToReportHeader();

        Assert.assertTrue(rh.isAddReportHeaderBtnDisplayed(),
                "Add Report Header button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        rh.addNewReportHeader(companyName, recordName);
        rh.createRHRow();

        System.out.println("[TEST 1] Report Header Row created successfully");
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateReportHeaderRow"}, description = "Delete newly added Report Header Row")
    public void testDeleteReportHeaderRow() {
        System.out.println("\n[TEST 2] Deleting Report Header Row...");

        Assert.assertTrue(rh.newRowPresent("1"),
                "Newly added report header row with ordinal '1' should be visible in the grid");

        rh.deleteRHRow("1");
        rh.clickCancelAndWaitForListing();
        rh.deleteReportHeader(recordName);

        Assert.assertFalse(rh.verifyRecordPresent(recordName),
                "Report header should be deleted during cleanup");

        System.out.println("[TEST 2] Report Header Row and Report Header deleted successfully");
    }
}

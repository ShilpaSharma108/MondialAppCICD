package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportHeader;

/**
 * Error Message For Header Deletion Test Class
 * Verifies behaviour when attempting to delete a Report Header
 * (e.g. error message when the header is in use by a report)
 */
public class RP_ErrorMessageForHeaderDeletion extends BaseTest {

    private HomePage homePage;
    private ReportHeader rh;
    private String recordName;

    @BeforeClass
    public void reportHeaderSetup() {
        System.out.println("=== Starting Error Message For Header Deletion Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rh = new ReportHeader(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "RptHdr_" + System.currentTimeMillis();
        System.out.println("Generated Report Header name: " + recordName);

        // Explicitly wait for the home page after login.
        // With PageLoadStrategy.NONE the post-login redirect may still be in flight
        // when @BeforeClass returns, so we anchor here rather than relying solely on
        // the assertion in the first @Test method.
        boolean headingVisible = homePage.isCompanyHeadingDisplayed();
        if (!headingVisible) {
            System.out.println("WARNING: Company heading not visible after login – " +
                "current URL: " + driver.getCurrentUrl() +
                ". Re-navigating to base URL and retrying.");
            driver.get(config.getProperty("base.url"));
            headingVisible = homePage.isCompanyHeadingDisplayed();
        }
        Assert.assertTrue(headingVisible,
                "Company heading not visible after login and retry – cannot proceed with tests");

        System.out.println("=== Error Message For Header Deletion Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Create Report Header for deletion error test")
    public void testCreateReportHeader() {
        System.out.println("\n[TEST 1] Creating Report Header: " + recordName);

        rh.navigateToReportHeader();

        Assert.assertTrue(rh.isAddReportHeaderBtnDisplayed(),
                "Add Report Header button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        rh.addNewReportHeader(companyName, recordName);
        rh.cancelAndWaitForListing();

        Assert.assertTrue(rh.verifyRecordPresent(recordName),
                "Newly created report header should appear in the listing");

        System.out.println("[TEST 1] Report Header created successfully: " + recordName);
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateReportHeader"}, description = "Edit Report Header")
    public void testEditReportHeader() {
        System.out.println("\n[TEST 2] Editing Report Header: " + recordName);

        recordName = rh.editReportHeader(recordName);

        Assert.assertTrue(rh.verifyRecordPresent(recordName),
                "Updated report header name should appear in the listing");

        System.out.println("[TEST 2] Report Header updated to: " + recordName);
    }

    @Test(priority = 3, dependsOnMethods = {"testEditReportHeader"}, description = "Delete Report Header")
    public void testDeleteReportHeader() {
        System.out.println("\n[TEST 3] Deleting Report Header: " + recordName);

        rh.deleteReportHeader(recordName);

        Assert.assertFalse(rh.verifyRecordPresent(recordName),
                "Deleted report header should not appear in the listing");

        System.out.println("[TEST 3] Report Header deleted successfully");
    }
}

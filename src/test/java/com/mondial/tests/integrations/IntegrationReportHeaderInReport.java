package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportHeader;
import com.mondial.pages.ReportsWriter;

/**
 * Integration Test - Verify that a newly added Report Header
 * appears in the Report Writer > Add Report > Report Header dropdown.
 */
public class IntegrationReportHeaderInReport extends BaseTest {

    private HomePage homePage;
    private ReportHeader rh;
    private ReportsWriter rw;
    private String companyName;
    private String recordName;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Integration Report Header In Report Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rh = new ReportHeader(driver);
        rw = new ReportsWriter(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        companyName = config.getProperty("testCompanyName");
        recordName = "RptHdr_" + System.currentTimeMillis();

        System.out.println("Company: " + companyName);
        System.out.println("Record name: " + recordName);
        System.out.println("=== Integration Report Header In Report Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Newly Added Report Header is present in Report Writer - Report Header Dropdown")
    public void verifyNewReportHeaderInReport() {
        System.out.println("\n[TEST 1] Verifying newly added report header appears in Report Writer dropdown...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rh.navigateToReportHeader();
        Assert.assertTrue(rh.isAddReportHeaderBtnDisplayed(),
                "Add Report Header button should be displayed");

        rh.addNewReportHeader(companyName, recordName);
        rh.cancelAndWaitForListing();

        homePage.clickHome();
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after clicking home");

        rw.navigateToReportWriter();
        Assert.assertTrue(rw.isAddReportBtnDisplayed(),
                "Add Report button should be displayed on Reports page");

        rw.openAddReportForm();

        boolean isPresent = rw.isValueInDropdown(recordName, rw.getReportHeaderDD());
        Assert.assertTrue(isPresent,
                "Newly added report header '" + recordName + "' should be present in the dropdown");

        boolean isAbsent = rw.isValueInDropdown("TestScenario", rw.getReportHeaderDD());
        Assert.assertFalse(isAbsent,
                "'TestScenario' should NOT be present in the dropdown");

        rh.navigateToReportHeader();
        rh.deleteReportHeader(recordName);

        System.out.println("[TEST 1] Report Header dropdown integration verification complete");
    }
}

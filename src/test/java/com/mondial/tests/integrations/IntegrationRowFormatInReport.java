package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsWriter;
import com.mondial.pages.RowFormat;

/**
 * Integration Test - Verify that a newly added Row Format
 * appears in the Report Writer > Add Report > Row Format dropdown.
 */
public class IntegrationRowFormatInReport extends BaseTest {

    private HomePage homePage;
    private RowFormat rf;
    private ReportsWriter rw;
    private String companyName;
    private String recordName;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Integration Row Format In Report Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new RowFormat(driver);
        rw = new ReportsWriter(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        companyName = config.getProperty("testCompanyName");
        recordName = "RowFmt_" + System.currentTimeMillis();

        System.out.println("Company: " + companyName);
        System.out.println("Record name: " + recordName);
        System.out.println("=== Integration Row Format In Report Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Newly Added Row Format is present in Report Writer - Row Format Dropdown")
    public void verifyNewRowFormatInReport() {
        System.out.println("\n[TEST 1] Verifying newly added row format appears in Report Writer dropdown...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rf.navigateToRowFormat();
        Assert.assertTrue(rf.isAddRowFormatBtnDisplayed(),
                "Add Row Format button should be displayed");

        rf.addNewRowFormat(companyName, recordName);
        rf.cancelAndWaitForListing();

        rw.navigateToReportWriter();
        Assert.assertTrue(rw.isAddReportBtnDisplayed(),
                "Add Report button should be displayed on Reports page");

        rw.openAddReportForm();

        boolean isPresent = rw.isValueInDropdown(recordName, rw.getRowFormatDD());
        Assert.assertTrue(isPresent,
                "Newly added row format '" + recordName + "' should be present in the dropdown");

        boolean isAbsent = rw.isValueInDropdown("TestScenario", rw.getRowFormatDD());
        Assert.assertFalse(isAbsent,
                "'TestScenario' should NOT be present in the dropdown");

        rf.navigateToRowFormat();
        rf.deleteRowFormat(recordName);

        System.out.println("[TEST 1] Row Format dropdown integration verification complete");
    }
}

package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportFooter;
import com.mondial.pages.ReportsWriter;

/**
 * Integration Test - Verify that a newly added Report Footer
 * appears in the Report Writer > Add Report > Report Footer dropdown.
 */
public class IntegrationReportFooterInReport extends BaseTest {

    private HomePage homePage;
    private ReportFooter rf;
    private ReportsWriter rw;
    private String companyName;
    private String recordName;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Integration Report Footer In Report Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new ReportFooter(driver);
        rw = new ReportsWriter(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        companyName = config.getProperty("testCompanyName");
        recordName = "RptFtr_" + System.currentTimeMillis();

        System.out.println("Company: " + companyName);
        System.out.println("Record name: " + recordName);
        System.out.println("=== Integration Report Footer In Report Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Newly Added Report Footer is present in Report Writer - Report Footer Dropdown")
    public void verifyNewReportFooterInReport() {
        System.out.println("\n[TEST 1] Verifying newly added report footer appears in Report Writer dropdown...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rf.navigateToReportFooter();
        Assert.assertTrue(rf.isAddReportFooterBtnDisplayed(),
                "Add Report Footer button should be displayed");

        rf.addNewReportFooter(companyName, recordName);
        rf.cancelAndWaitForListing();

        homePage.clickHome();
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after clicking home");

        rw.navigateToReportWriter();
        Assert.assertTrue(rw.isAddReportBtnDisplayed(),
                "Add Report button should be displayed on Reports page");

        rw.openAddReportForm();

        boolean isPresent = rw.isValueInDropdown(recordName, rw.getReportFooterDD());
        Assert.assertTrue(isPresent,
                "Newly added report footer '" + recordName + "' should be present in the dropdown");

        boolean isAbsent = rw.isValueInDropdown("TestScenario", rw.getReportFooterDD());
        Assert.assertFalse(isAbsent,
                "'TestScenario' should NOT be present in the dropdown");

        rf.navigateToReportFooter();
        rf.deleteReportFooter(recordName);

        System.out.println("[TEST 1] Report Footer dropdown integration verification complete");
    }
}

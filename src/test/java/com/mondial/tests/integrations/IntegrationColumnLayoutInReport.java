package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.ColumnLayout;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsWriter;

/**
 * Integration Test - Verify that a newly added Column Layout
 * appears in the Report Writer > Add Report > Column Layout dropdown.
 */
public class IntegrationColumnLayoutInReport extends BaseTest {

    private HomePage homePage;
    private ColumnLayout cl;
    private ReportsWriter rw;
    private String companyName;
    private String recordName;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Integration Column Layout In Report Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        cl = new ColumnLayout(driver);
        rw = new ReportsWriter(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        companyName = config.getProperty("testCompanyName");
        recordName = "ColLayout_" + System.currentTimeMillis();

        System.out.println("Company: " + companyName);
        System.out.println("Record name: " + recordName);
        System.out.println("=== Integration Column Layout In Report Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Newly Added Column Layout is present in Report Writer - Column Layout Dropdown")
    public void verifyNewColumnLayoutInReport() {
        System.out.println("\n[TEST 1] Verifying newly added column layout appears in Report Writer dropdown...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        cl.navigateToColumnLayout();
        Assert.assertTrue(cl.isAddColumnLayoutBtnDisplayed(),
                "Add Column Layout button should be displayed");

        cl.addNewColumnLayout(companyName, recordName);
        cl.cancelAndWaitForListing();

        rw.navigateToReportWriter();
        Assert.assertTrue(rw.isAddReportBtnDisplayed(),
                "Add Report button should be displayed on Reports page");

        rw.openAddReportForm();

        boolean isPresent = rw.isValueInDropdown(recordName, rw.getColumnLayoutDD());
        Assert.assertTrue(isPresent,
                "Newly added column layout '" + recordName + "' should be present in the dropdown");

        boolean isAbsent = rw.isValueInDropdown("TestScenario", rw.getColumnLayoutDD());
        Assert.assertFalse(isAbsent,
                "'TestScenario' should NOT be present in the dropdown");

        cl.navigateToColumnLayout();
        cl.deleteColumnLayout(recordName);

        System.out.println("[TEST 1] Column Layout dropdown integration verification complete");
    }
}

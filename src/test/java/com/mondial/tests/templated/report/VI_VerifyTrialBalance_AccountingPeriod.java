package com.mondial.tests.templated.report;

import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class VI_VerifyTrialBalance_AccountingPeriod extends BaseTest {

    private HomePage homePage;
    private ReportsPage rp;
    private static final String REPORT_TYPE = "Trial Balance";

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Trial Balance Accounting Period Test Setup ===");
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp = new ReportsPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        System.out.println("=== Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Trial Balance generates successfully using Accounting Period dropdown")
    public void verifyTrialBalanceWithAccountingPeriod() throws InterruptedException {

    	rp.navigateToTemplatedReport();
        rp.waitForVisible(rp.templatedReportHeading);
        String accountingPeriod = rp.verifyAccountPeriodField(config.getProperty("companyReport"), REPORT_TYPE);
        rp.waitForVisible(rp.firstRecord);
        System.out.println("Trial Balance generated successfully for Accounting Period: " + accountingPeriod);
    }
}

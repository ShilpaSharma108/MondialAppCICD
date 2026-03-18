package com.mondial.tests.tempated.report;

import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class V_VerifyTrialBalance_AllTxnTypes extends BaseTest {

    private HomePage homePage;
    private ReportsPage rp;
    private static final String REPORT_TYPE = "Trial Balance";

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Trial Balance All Transaction Types Test Setup ===");
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp = new ReportsPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        System.out.println("=== Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Trial Balance report generates successfully for each Transaction Type")
    public void verifyReportForAllTxnTypes() throws InterruptedException {
        ArrayList<String> txnTypes = rp.getTxnTypes();
        String templatedReportUrl = null;
        for (String txnType : txnTypes) {
            System.out.println("\n--- Testing Transaction Type: " + txnType + " ---");
            if (templatedReportUrl == null) {
                rp.navigateToTemplatedReport();
                rp.waitForVisible(rp.templatedReportHeading);
                templatedReportUrl = driver.getCurrentUrl();
            } else {
                driver.navigate().to(templatedReportUrl);
                rp.waitForVisible(rp.templatedReportHeading);
            }
            rp.selectRequiredValues(config.getProperty("companyReport"), REPORT_TYPE);
            rp.deSelectTxnType();
            rp.selectTxnType(txnType.trim());
            rp.generateReport(REPORT_TYPE);
            rp.waitForVisible(rp.firstRecord);
            System.out.println("Trial Balance generated successfully for Transaction Type: " + txnType);
        }
    }
}

package com.mondial.tests.templated.report;

import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class X_VerifyAllReportTypes extends BaseTest {

    private HomePage homePage;
    private ReportsPage rp;

    private static final List<String> REPORT_TYPES = Arrays.asList(
            "Transaction History",
            "Trial Balance",
            "Current Period Trial Balance",
            "Detailed Trial Balance"
    );

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting All Report Types Test Setup ===");
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp = new ReportsPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        System.out.println("=== Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify all Templated Report types generate successfully")
    public void verifyAllReportTypesGenerate() throws InterruptedException {
        String templatedReportUrl = null;
        for (String reportType : REPORT_TYPES) {
            System.out.println("\n--- Testing Report Type: " + reportType + " ---");
            if (templatedReportUrl == null) {
                rp.navigateToTemplatedReport();
                rp.waitForVisible(rp.templatedReportHeading);
                templatedReportUrl = driver.getCurrentUrl();
            } else {
                driver.navigate().to(templatedReportUrl);
                rp.waitForVisible(rp.templatedReportHeading);
            }
            if (reportType.equals("Transaction History")) {
                rp.generateTxnHistory(config.getProperty("companyReport"), reportType);
            } else {
                rp.selectRequiredValues(config.getProperty("companyReport"), reportType);
                rp.checkTxnType("source");
                rp.generateReport(reportType);
                rp.waitForVisible(rp.firstRecord);
            }
            System.out.println("Report generated successfully for type: " + reportType);
        }
    }
}

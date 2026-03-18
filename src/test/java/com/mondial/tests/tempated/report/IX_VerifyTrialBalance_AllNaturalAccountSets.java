package com.mondial.tests.tempated.report;

import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class IX_VerifyTrialBalance_AllNaturalAccountSets extends BaseTest {

    private HomePage homePage;
    private ReportsPage rp;
    private static final String REPORT_TYPE = "Trial Balance";

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Trial Balance All Natural Account Sets Test Setup ===");
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp = new ReportsPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        System.out.println("=== Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Trial Balance report generates successfully for each available Natural Account Set")
    public void verifyReportForAllNaturalAccountSets() throws InterruptedException {
        rp.navigateToTemplatedReport();
        rp.waitForVisible(rp.templatedReportHeading);
        rp.selectRequiredValues(config.getProperty("companyReport"), REPORT_TYPE);
        List<String> nasList = rp.getNaturalAccountSets();
        System.out.println("Available Natural Account Sets: " + nasList);
        String templatedReportUrl = driver.getCurrentUrl();

        for (String nas : nasList) {
            System.out.println("\n--- Testing Natural Account Set: " + nas + " ---");
            driver.navigate().to(templatedReportUrl);
            rp.waitForVisible(rp.templatedReportHeading);
            rp.selectRequiredValues(config.getProperty("companyReport"), REPORT_TYPE);
            rp.verifyBalanceGeneration_NAS(nas, REPORT_TYPE);
            rp.waitForVisible(rp.firstRecord);
            System.out.println("Trial Balance generated successfully for Natural Account Set: " + nas);
        }
    }
}

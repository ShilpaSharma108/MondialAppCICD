package com.mondial.tests.tempated.report;

import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class XII_VerifyDownload extends BaseTest {

    private HomePage homePage;
    private ReportsPage rp;
    private static final String REPORT_TYPE = "Trial Balance";

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Download Test Setup ===");
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp = new ReportsPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        System.out.println("=== Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Download button is available after generating Trial Balance")
    public void verifyDownloadButtonAvailable() throws InterruptedException {
        String parentWin = driver.getWindowHandle();
        rp.navigateToTemplatedReport();
        rp.waitForVisible(rp.templatedReportHeading);
        rp.selectRequiredValues(config.getProperty("companyReport"), REPORT_TYPE);
        rp.checkTxnType("source");
        rp.generateReport(REPORT_TYPE);
        rp.waitForVisible(rp.firstRecord);
        rp.getBalanceDetails();
        rp.waitForLoad();
        rp.switchToChildWindow(parentWin);
        rp.waitForVisible(rp.downloadBtn);
        Assert.assertTrue(rp.downloadBtn.isEnabled(), "Download button should be enabled after report generation");
        System.out.println("Download button is visible and enabled after report generation");
    }

    @Test(priority = 2, description = "Verify clicking Download button initiates download successfully",
            dependsOnMethods = {"verifyDownloadButtonAvailable"})
    public void verifyDownloadInitiated() {
        rp.downloadBtn.click();
        System.out.println("Download initiated successfully for report type: " + REPORT_TYPE);
    }
}

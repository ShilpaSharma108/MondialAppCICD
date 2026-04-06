package com.mondial.tests.templated.report;

import com.mondial.tests.BaseTest;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class II_VerifyBalanceDetails_TrialBalance extends BaseTest {

    private HomePage homePage;
    private ReportsPage rp;
    private String parentWin;
    private String reportType;
    public static final String RESET = "\u001B[0m";
    public static final String PrintColour = "\u001B[35m";

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Trial Balance Test Setup ===");
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp = new ReportsPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        parentWin = driver.getWindowHandle();
        System.out.println("=== Trial Balance Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Trial Balance: Verify user is able to generate balance")
    public void generateTrialBalance() throws InterruptedException {
        reportType = "Trial Balance";
        rp.navigateToTemplatedReport();
        rp.waitForVisible(rp.templatedReportHeading);
        rp.selectRequiredValues(config.getProperty("companyReport"), reportType);
        rp.checkTxnType("source");
        rp.generateReport(reportType);
    }

    @Test(priority = 2, description = "Trial Balance: Verify Balance Details Page",
            dependsOnMethods = {"generateTrialBalance"})
    public void verifyTrialBalance() throws InterruptedException {
        String closingBalance = rp.getBalanceDetails().replaceAll(",", "").strip();
        System.out.println("\n\033[0;1m" + PrintColour + "Balance - Trial Balance Table: " + closingBalance + RESET);
        rp.waitForLoad();
        rp.switchToChildWindow(parentWin);
        System.out.println("\033[3m" + "User navigated to a new window:  " + driver.getTitle() + "\033[0m" + RESET);
        rp.waitForVisible(rp.downloadBtn);
        String balanceDetails = rp.getBalanceAmount().replaceAll(",", "").strip();
        System.out.println("\n\033[0;1m" + PrintColour + "Balance Details Page: " + balanceDetails + RESET + "\n");
        Assert.assertTrue(rp.showACTxn.getAttribute("innerText").contains("Balance Details:"));
        Assert.assertEquals(balanceDetails, closingBalance);
    }
}

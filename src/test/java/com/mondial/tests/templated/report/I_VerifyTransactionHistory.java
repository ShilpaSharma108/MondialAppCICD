package com.mondial.tests.templated.report;

import com.mondial.tests.BaseTest;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class I_VerifyTransactionHistory extends BaseTest {

    private HomePage homePage;
    private ReportsPage rp;
    private String parentWin;
    private String reportType;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Transaction History Test Setup ===");
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp = new ReportsPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        parentWin = driver.getWindowHandle();
        System.out.println("=== Transaction History Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Transaction History: Verify user is able to generate Transaction History")
    public void generateTransactionHistory() throws InterruptedException {
        reportType = "Transaction History";
        rp.navigateToTemplatedReport();
        rp.waitForVisible(rp.templatedReportHeading);
        rp.generateTxnHistory(config.getProperty("companyReport"), reportType);
        rp.downloadBtn.click();
    }

    @Test(priority = 2, description = "Transaction History: Verify Showing Accounting Transaction Page details",
            dependsOnMethods = {"generateTransactionHistory"})
    public void verifyTransactionIdDetails() throws InterruptedException {
        ArrayList<String> recordDetails = rp.selectRecord();
        rp.waitForLoad();
        rp.switchToChildWindow(parentWin);
        rp.waitForLoad();
        System.out.println("\nTransaction Id: " + rp.txnID_ShowingACTxn.getAttribute("innerText"));
        Assert.assertTrue(rp.txnID_ShowingACTxn.getAttribute("innerText").contains(recordDetails.get(0)));
        Assert.assertEquals(recordDetails.get(1), rp.externalRxId_ShowingACTxn.getAttribute("innerText"));
        Assert.assertEquals(recordDetails.get(2), rp.type_ShowingACTxn.getAttribute("innerText"));
        Assert.assertEquals(recordDetails.get(3), rp.postedDate_ShowingACTxn.getAttribute("innerText"));
        Assert.assertEquals(recordDetails.get(4), rp.txnDate_ShowingACTxn.getAttribute("innerText"));
        Assert.assertEquals(recordDetails.get(5), rp.description_ShowingACTxn.getAttribute("innerText"));
    }

    @Test(priority = 3, description = "Verify Debit and Credit Values Matches",
            dependsOnMethods = {"verifyTransactionIdDetails"})
    public void verifyDebitCreditValues() {
        rp.verifyAccountingTxnAmt();
        driver.close();
        driver.switchTo().window(parentWin);
        System.out.println("\nUser is back to page - " + driver.getTitle() + "\n");
    }

    @Test(priority = 4, description = "Verify Reverse Transaction Model appears successfully",
            dependsOnMethods = {"verifyDebitCreditValues"})
    public void verifyReversetxnModel() {
        rp.reverseLink.click();
        rp.waitForVisible(rp.postReverseBtn);
        Assert.assertTrue(rp.reverseTransacrionModel.getAttribute("innerText").contains("REVERSE TRANSACTION"));
        rp.closeModel.click();
    }
}

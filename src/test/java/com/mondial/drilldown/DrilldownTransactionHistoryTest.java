package com.mondial.drilldown;

import com.mondial.tests.BaseTest;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class DrilldownTransactionHistoryTest extends BaseTest {

    private static final String DRILLDOWN_URL     = "https://41c52bea-cbe7-43f3-add9-808e84f18d67.dev.mondialsoftware.com";
    private static final String DRILLDOWN_USERNAME = "mondial.support.dev.newserver@mondialsoftware.com";
    private static final String DRILLDOWN_PASSWORD = "5e-fdxj%4*YY";

    private HomePage homePage;
    private ReportsPage rp;
    private String parentWin;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Drilldown Transaction History Test Setup ===");
        driver.get(DRILLDOWN_URL);
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp       = new ReportsPage(driver);
        loginPage.login(DRILLDOWN_USERNAME, DRILLDOWN_PASSWORD);
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        parentWin = driver.getWindowHandle();
        System.out.println("=== Drilldown Transaction History Test Setup Complete ===\n");
    }

    @Test(priority = 1,
          description = "Drilldown | Transaction History: Generate report and verify report heading")
    public void generateTransactionHistoryReport() throws InterruptedException {
        rp.navigateToTemplatedReport();
        rp.waitForVisible(rp.templatedReportHeading);
        rp.generateTxnHistory("Shaneel Perfume Shop 13", "Transaction History");
    }

    @Test(priority = 2,
          description = "Drilldown | Transaction History: Click a transaction row and verify detail window fields",
          dependsOnMethods = {"generateTransactionHistoryReport"})
    public void verifyTransactionDetailOnDrilldown() throws InterruptedException {
        ArrayList<String> recordDetails = rp.selectRecord();
        rp.waitForLoad();
        rp.switchToChildWindow(parentWin);
        rp.waitForLoad();
        System.out.println("Transaction Id: " + rp.txnID_ShowingACTxn.getAttribute("innerText"));
        Assert.assertTrue(rp.txnID_ShowingACTxn.getAttribute("innerText").contains(recordDetails.get(0)),
                "Transaction ID on detail page should match selected record");
        Assert.assertEquals(rp.externalRxId_ShowingACTxn.getAttribute("innerText"), recordDetails.get(1),
                "External Ref ID should match");
        Assert.assertEquals(rp.type_ShowingACTxn.getAttribute("innerText"), recordDetails.get(2),
                "Transaction type should match");
        Assert.assertEquals(rp.postedDate_ShowingACTxn.getAttribute("innerText"), recordDetails.get(3),
                "Posted date should match");
        Assert.assertEquals(rp.txnDate_ShowingACTxn.getAttribute("innerText"), recordDetails.get(4),
                "Transaction date should match");
        Assert.assertEquals(rp.description_ShowingACTxn.getAttribute("innerText"), recordDetails.get(5),
                "Description should match");
    }

    @Test(priority = 3,
          description = "Drilldown | Transaction History: Verify debit and credit totals are equal",
          dependsOnMethods = {"verifyTransactionDetailOnDrilldown"})
    public void verifyDebitCreditTotalsMatch() {
        rp.verifyAccountingTxnAmt();
        driver.close();
        driver.switchTo().window(parentWin);
        System.out.println("Returned to parent window: " + driver.getTitle());
    }
}

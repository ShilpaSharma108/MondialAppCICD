package com.mondial.drilldown;

import com.mondial.tests.BaseTest;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class DrilldownTrialBalanceTest extends BaseTest {

    private static final String DRILLDOWN_URL     = "https://41c52bea-cbe7-43f3-add9-808e84f18d67.dev.mondialsoftware.com";
    private static final String DRILLDOWN_USERNAME = "mondial.support.dev.newserver@mondialsoftware.com";
    private static final String DRILLDOWN_PASSWORD = "5e-fdxj%4*YY";

    private HomePage homePage;
    private ReportsPage rp;
    private String parentWin;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Drilldown Trial Balance Test Setup ===");
        driver.get(DRILLDOWN_URL);
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rp       = new ReportsPage(driver);
        loginPage.login(DRILLDOWN_USERNAME, DRILLDOWN_PASSWORD);
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), "Should be on home page after login");
        parentWin = driver.getWindowHandle();
        System.out.println("=== Drilldown Trial Balance Test Setup Complete ===\n");
    }

    @Test(priority = 1,
          description = "Drilldown | Trial Balance: Generate Trial Balance report")
    public void generateTrialBalanceReport() throws InterruptedException {
        rp.navigateToTemplatedReport();
        rp.waitForVisible(rp.templatedReportHeading);
        rp.selectCompany("Shaneel Perfume Shop 13");
        rp.waitForLoad();
        rp.selectReport("Trial Balance");
        rp.waitForLoad();
        rp.selectCurrency("USD");
        rp.waitForLoad();
        rp.selectNaturalAccountSets("Local");
        rp.waitForLoad();
        rp.selectLedger("Default");
        rp.waitForLoad();
        rp.selectOutputType("Screen");
        rp.waitForLoad();
        rp.selectDatesJS("01/01/2025", "01/31/2025");
        rp.checkTxnType("source");
        rp.generateReport("Trial Balance");
    }

    @Test(priority = 2,
          description = "Drilldown | Trial Balance: Drilldown into 5 non-zero closing balance records and verify Balance Details",
          dependsOnMethods = {"generateTrialBalanceReport"})
    public void verifyNonZeroBalanceDrilldownFor5Records() throws InterruptedException {
        rp.waitForVisible(rp.firstRecord);
        int available = rp.countNonZeroBalanceCells();
        Assert.assertTrue(available > 0, "No non-zero balance rows found in Trial Balance");
        int toProcess = Math.min(5, available);
        System.out.println("Non-zero balance cells found: " + available + " — processing " + toProcess);

        for (int i = 0; i < toProcess; i++) {
            String closingBalance = rp.clickNthNonZeroBalanceCell(i).replaceAll(",", "").strip();
            System.out.println("Cell [" + i + "] - Closing Balance: " + closingBalance);
            rp.waitForLoad();
            rp.switchToChildWindow(parentWin);
            rp.waitForVisible(rp.downloadBtn);
            Assert.assertTrue(rp.showACTxn.getAttribute("innerText").contains("Balance Details:"),
                    "Balance Details heading should be present for cell index " + i);
            System.out.println("Cell [" + i + "] - Verifying Balance Details calculations...");
            rp.verifyBalanceDetailsCalculation(closingBalance);
            System.out.println("Cell [" + i + "] - Balance Details calculation verified successfully");
            driver.close();
            driver.switchTo().window(parentWin);
            System.out.println("Returned to parent after cell " + i);
        }
    }
}

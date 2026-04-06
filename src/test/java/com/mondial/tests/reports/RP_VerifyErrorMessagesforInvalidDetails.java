package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsWriter;

/**
 * Verify Error Messages for Invalid Details Test Class
 * Verifies that a browser alert popup is displayed when the Generate button
 * is clicked with one or more mandatory fields left blank in Report Writer.
 */
public class RP_VerifyErrorMessagesforInvalidDetails extends BaseTest {

    private HomePage homePage;
    private ReportsWriter rw;
    private String companyName;

    @BeforeClass
    public void reportGenerationSetup() {
        System.out.println("=== Starting Verify Error Messages Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rw = new ReportsWriter(driver);
        companyName = config.getProperty("companyReport");

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        System.out.println("=== Verify Error Messages Test Setup Complete ===\n");
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private void assertBrowserAlert(String fieldDescription) {
        Assert.assertTrue(rw.isBrowserAlertPresent(),
                "Browser alert should appear when '" + fieldDescription + "' is blank");
        String alertText = rw.getBrowserAlertTextAndDismiss();
        System.out.println("    Alert message: " + alertText);
        Assert.assertFalse(alertText.isEmpty(),
                "Alert message should not be empty when '" + fieldDescription + "' is blank");
    }

    // -----------------------------------------------------------------------
    // Test 1 – All fields blank
    // -----------------------------------------------------------------------

    @Test(priority = 1, description = "Error alert when all mandatory fields are blank")
    public void testAllFieldsBlank() {
        System.out.println("\n[TEST 1] All fields blank...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rw.navigateToReportWriter();
        rw.clickGenerateWithoutFillingFields();

        assertBrowserAlert("all fields");
        System.out.println("[TEST 1] PASSED");
    }

    // -----------------------------------------------------------------------
    // Test 2 – Report field blank (all others filled)
    // -----------------------------------------------------------------------

    @Test(priority = 2, description = "Error alert when Report field is blank")
    public void testReportFieldBlank() {
        System.out.println("\n[TEST 2] Report field blank, all others filled...");

        rw.refreshAndWaitForForm();

        // Fill Company only — Report is blank so dependent dropdowns won't fully load.
        // The app fires "Please choose a report first" before checking anything else.
        rw.fillCompanyOnly(companyName);
        rw.clickGenerateWithoutFillingFields();

        assertBrowserAlert("Report");
        System.out.println("[TEST 2 ] PASSED");
    }

    // -----------------------------------------------------------------------
    // Test 3 – Company field blank (all others filled)
    // -----------------------------------------------------------------------

    @Test(priority = 3, description = "Error alert when Company field is blank")
    public void testCompanyFieldBlank() {
        System.out.println("\n[TEST 3] Company field blank - dependent dropdowns unavailable without Company...");
        rw.refreshAndWaitForForm();
        rw.fillReport("Balance Sheet");
        rw.clickGenerateWithoutFillingFields();
        assertBrowserAlert("Company");
        System.out.println("[TEST 3] PASSED");
    }

    // -----------------------------------------------------------------------
    // Test 4 – Reporting Set field blank (all others filled, Ledger auto-selected)
    // -----------------------------------------------------------------------

    @Test(priority = 4, description = "Error alert when Reporting Set field is blank")
    public void testReportingSetFieldBlank() {
        System.out.println("\n[TEST 4] Reporting Set field blank, all others filled...");

        rw.refreshAndWaitForForm();

        // Fill everything except Reporting Set
        rw.fillReport("Balance Sheet");
        rw.fillCompanyAndWait(companyName);
        rw.fillCurrency("USD");
        rw.fillStartDate("04/01/2018");
        rw.fillEndDate("03/31/2019");

        rw.clickGenerateWithoutFillingFields();

        assertBrowserAlert("Reporting Set");
        System.out.println("[TEST 4] PASSED");
    }

    // -----------------------------------------------------------------------
    // Test 6 – Currency field blank (all others filled)
    // -----------------------------------------------------------------------

    @Test(priority = 5, description = "Error alert when Currency field is blank")
    public void testCurrencyFieldBlank() {
        System.out.println("\n[TEST 5] Currency field blank, all others filled...");

        rw.refreshAndWaitForForm();

        // Fill everything except Currency (leave at index 0 / blank)
        rw.fillReport("Balance Sheet");
        rw.fillCompanyAndWait(companyName);
        rw.fillReportingSet("OECC");
        rw.fillStartDate("04/02/2018");
        rw.fillEndDate("03/31/2019");

        rw.clickGenerateWithoutFillingFields();

        assertBrowserAlert("Currency");
        System.out.println("[TEST 5] PASSED");
    }

}

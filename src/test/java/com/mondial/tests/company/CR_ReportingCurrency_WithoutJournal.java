package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportingCurrency;

/**
 * Reporting Currency CRUD Test (Without Journal)
 * Tests create and delete of a Reporting Currency on a company that has no journal records.
 * Verifies that deletion succeeds when no journals reference the currency.
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName) — must have no journal records
 */
public class CR_ReportingCurrency_WithoutJournal extends BaseTest {

    private HomePage homePage;
    private ReportingCurrency reportingCurrency;
    private String companyName;
    private String rcName;

    @BeforeClass
    public void reportingCurrencySetup() {
        System.out.println("=== Starting Reporting Currency (Without Journal) Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        reportingCurrency = new ReportingCurrency(driver);

        companyName = config.getProperty("testCompanyName");
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        System.out.println("Test company: " + companyName);
        System.out.println("=== Reporting Currency (Without Journal) Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Reporting Currencies page for the test company
     */
    @Test(priority = 1, description = "Navigate to Reporting Currencies Page")
    public void testNavigateToReportingCurrency() {
        System.out.println("\n[TEST 1] Navigating to Reporting Currencies page...");

        reportingCurrency.navigateToReportingCurrency(companyName);

        Assert.assertTrue(reportingCurrency.isAddBtnDisplayed(),
                "Add Reporting Currency button should be displayed");

        String heading = reportingCurrency.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + ": Reporting Currencies"),
                "Heading should contain company name and 'Reporting Currencies'");

        System.out.println("[TEST 1] Successfully navigated to Reporting Currencies page");
    }

    /**
     * Test 2: Verify Cancel works, then create a new Reporting Currency
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToReportingCurrency"}, description = "Create Reporting Currency")
    public void testCreateReportingCurrency() {
        System.out.println("\n[TEST 2] Creating Reporting Currency...");

        reportingCurrency.verifyCancel();
        Assert.assertTrue(reportingCurrency.isAddBtnDisplayed(),
                "Should return to listing after Cancel");

        rcName = reportingCurrency.createReportingCurrency();

        Assert.assertTrue(reportingCurrency.getSuccessMessageText()
                .contains("Reporting currency was successfully created."),
                "Success message should confirm currency creation");

        reportingCurrency.waitForAlertToDismiss();

        Assert.assertTrue(reportingCurrency.isAddBtnDisplayed(),
                "Should be on Reporting Currencies listing page");
        Assert.assertTrue(reportingCurrency.verifyCurrencyPresent(rcName),
                "Newly created currency should appear in the table");

        System.out.println("[TEST 2] Reporting Currency created successfully: " + rcName);
    }

    /**
     * Test 3: Delete the Reporting Currency — should succeed since no journals reference it
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateReportingCurrency"}, description = "Delete Reporting Currency (no journal)")
    public void testDeleteReportingCurrency() {
        System.out.println("\n[TEST 3] Deleting Reporting Currency: " + rcName);

        reportingCurrency.clickDelete(rcName);

        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        reportingCurrency.waitForAlertToDismiss();

        Assert.assertFalse(reportingCurrency.verifyCurrencyPresent(rcName),
                "Deleted currency should no longer appear in the table");

        System.out.println("[TEST 3] Reporting Currency deleted successfully: " + rcName);
    }
}

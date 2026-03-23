package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportingCurrency;

/**
 * Reporting Currency Test (With Journal)
 * Verifies that a Reporting Currency cannot be deleted when journal records reference it.
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - A company with existing journals in config.properties (companyReport)
 * - That company must have "CAD" as an existing reporting currency with journal records
 */
public class CR_ReportingCurrency_WithJournal extends BaseTest {

    private HomePage homePage;
    private ReportingCurrency reportingCurrency;
    private String companyName;
    private final String rcName = "CAD";

    @BeforeClass
    public void reportingCurrencyWithJournalSetup() {
        System.out.println("=== Starting Reporting Currency (With Journal) Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        reportingCurrency = new ReportingCurrency(driver);

        companyName = config.getProperty("companyReport");
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        System.out.println("Test company (with journals): " + companyName);
        System.out.println("Currency under test: " + rcName);
        System.out.println("=== Reporting Currency (With Journal) Test Setup Complete ===\n");
    }

    /**
     * Test 1: Attempt to delete a currency that has journal records — deletion should be blocked
     */
    @Test(priority = 1, description = "Verify Currency not deleted when Journal is present")
    public void testCurrencyNotDeletedWithJournal() {
        System.out.println("\n[TEST 1] Navigating to Reporting Currencies for: " + companyName);

        reportingCurrency.navigateToReportingCurrency(companyName);

        Assert.assertTrue(reportingCurrency.isAddBtnDisplayed(),
                "Add Reporting Currency button should be displayed");
        Assert.assertTrue(reportingCurrency.getPageHeading().contains(companyName + ": Reporting Currencies"),
                "Heading should contain company name and 'Reporting Currencies'");

        System.out.println("[TEST 1] Attempting to delete currency: " + rcName);
        reportingCurrency.clickDelete(rcName);

        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        Assert.assertTrue(reportingCurrency.getWarningMessageText()
                .contains("Reporting currency could not be deleted: Reporting journal exist."),
                "Warning message should indicate currency cannot be deleted due to existing journals");

        reportingCurrency.waitForWarningToDismiss();

        Assert.assertTrue(reportingCurrency.verifyCurrencyPresent(rcName),
                "Currency should still be present after failed deletion");

        System.out.println("[TEST 1] Confirmed: currency '" + rcName + "' cannot be deleted while journals exist");
    }
}

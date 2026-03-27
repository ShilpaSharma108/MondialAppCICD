package com.mondial.tests.company;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mondial.pages.AccountingPeriodPage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * CR_AccountingPeriod Test Class
 * Creates a brand-new company, sets its accounting period start date to
 * 01/01/2018, verifies the record appears, then deletes the company on teardown.
 */
public class CR_AccountingPeriod extends BaseTest {

    private static final String ACCOUNTING_DATE = "01/01/2018";
    private static final String CURRENCY         = "USD";
    private static final String NUM_SEGMENTS     = "1";
    private static final String REPORTING_SYMBOL = "xyz";

    private HomePage homePage;
    private AccountingPeriodPage accountingPeriodPage;
    private String companyName;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting CR_AccountingPeriod Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        accountingPeriodPage = new AccountingPeriodPage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        // Anchor the post-login redirect before any navigation
        if (!homePage.isCompanyHeadingDisplayed()) {
            System.out.println("WARNING: Company heading not visible after login – " +
                    "current URL: " + driver.getCurrentUrl() +
                    ". Re-navigating to base URL and retrying.");
            driver.get(config.getProperty("base.url"));
            homePage.isCompanyHeadingDisplayed();
        }

        // Generate a unique company name for this test run
        companyName = "AccPeriodCo_" + System.currentTimeMillis();

        System.out.println("Creating test company: " + companyName);
        homePage.createCompany(companyName, CURRENCY, NUM_SEGMENTS, REPORTING_SYMBOL);
        System.out.println("Test company created: " + companyName);

        System.out.println("=== CR_AccountingPeriod Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Navigate to Accounting Period page for the new company")
    public void testNavigateToAccountingPeriod() {
        System.out.println("\n[TEST 1] Navigating to Accounting Period page for: " + companyName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after company creation");

        accountingPeriodPage.navigateToAccountingPeriod(companyName);

        Assert.assertTrue(accountingPeriodPage.isAccountingPeriodFormDisplayed(),
                "Accounting Period form (submit button) should be displayed");

        Assert.assertTrue(accountingPeriodPage.getPageHeading().contains(companyName),
                "Page heading should contain the company name");

        System.out.println("[TEST 1] Navigation to Accounting Period page successful");
    }

    @Test(priority = 2, dependsOnMethods = {"testNavigateToAccountingPeriod"},
            description = "Set accounting period start date to 01/01/2018 and verify listing")
    public void testSetAccountingPeriod() {
        System.out.println("\n[TEST 2] Setting Accounting Period date to: " + ACCOUNTING_DATE);

        accountingPeriodPage.setAccountingPeriod(ACCOUNTING_DATE);

        Assert.assertTrue(accountingPeriodPage.verifyRecordPresentAP(ACCOUNTING_DATE),
                "Accounting period '" + ACCOUNTING_DATE + "' should appear in the listing");

        System.out.println("[TEST 2] Accounting Period set successfully: " + ACCOUNTING_DATE);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        System.out.println("\n=== Cleaning up: deleting test company: " + companyName + " ===");
        try {
            // Navigate back to home (companies listing) before deleting
            driver.get(config.getProperty("base.url"));
            homePage.isCompanyHeadingDisplayed();
            homePage.deleteCompany(companyName);
            System.out.println("Test company deleted: " + companyName);
        } catch (Exception e) {
            System.out.println("WARNING: Could not delete test company '" + companyName
                    + "': " + e.getMessage());
        }
        System.out.println("=== Cleanup Complete ===\n");
    }
}

package com.mondial.tests.company;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mondial.pages.AccountingPeriodPage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.UserRolesPage;

/**
 * CR_AccountingPeriod Test Class
 * 1) Creates a brand-new company
 * 2) Assigns Group Application Admin role to the current user for that company
 * 3) Sets the accounting period start date to 01/01/2018 and verifies it
 * Deletes the company on teardown.
 */
public class CR_AccountingPeriod extends BaseTest {

    private static final String ACCOUNTING_DATE = "01/01/2018";
    private static final String CURRENCY        = "USD";
    private static final String NUM_SEGMENTS    = "1";

    private HomePage             homePage;
    private UserRolesPage        userRolesPage;
    private AccountingPeriodPage accountingPeriodPage;
    private String               companyName;
    private String               reportingSymbol;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting CR_AccountingPeriod Test Setup ===");

        LoginPage loginPage     = new LoginPage(driver);
        homePage                = new HomePage(driver);
        userRolesPage           = new UserRolesPage(driver);
        accountingPeriodPage    = new AccountingPeriodPage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        if (!homePage.isCompanyHeadingDisplayed()) {
            System.out.println("WARNING: Company heading not visible after login – re-navigating.");
            driver.get(config.getProperty("base.url"));
            homePage.isCompanyHeadingDisplayed();
        }

        long ts = System.currentTimeMillis();
        companyName     = "AccPeriodCo_" + ts;
        reportingSymbol = "rs" + (ts % 100000);
        System.out.println("Creating test company: " + companyName + " (symbol: " + reportingSymbol + ")");
        homePage.createCompany(companyName, CURRENCY, NUM_SEGMENTS, reportingSymbol);
        System.out.println("Test company created: " + companyName);

        System.out.println("=== CR_AccountingPeriod Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Assign Group Application Admin role to the current user for the new company")
    public void testAssignGroupApplicationAdminRole() {
        System.out.println("\n[TEST 1] Navigating to User Roles for: " + companyName);

        driver.get(config.getProperty("base.url"));
        homePage.isCompanyHeadingDisplayed(); // wait for home page to fully load
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible before navigating to User Roles");

        String adminUser = config.getProperty("validUsername");
        userRolesPage.navigateToUserRoles(adminUser);

        Assert.assertTrue(userRolesPage.isPageDisplayed(),
                "User Roles page should be displayed");

        userRolesPage.assignRole(companyName, "Group Application Admin");

        Assert.assertEquals(userRolesPage.getAssignedRole(companyName), "Group Application Admin",
                "Role should be set to Group Application Admin for company: " + companyName);

        System.out.println("[TEST 1] Group Application Admin role assigned to: " + adminUser);
    }

    @Test(priority = 2, dependsOnMethods = {"testAssignGroupApplicationAdminRole"},
            description = "Navigate to Accounting Period page for the new company")
    public void testNavigateToAccountingPeriod() {
        System.out.println("\n[TEST 2] Navigating to Accounting Period page for: " + companyName);

        driver.get(config.getProperty("base.url"));
        homePage.isCompanyHeadingDisplayed(); // wait for home page to fully load
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible before navigating to Accounting Period");

        accountingPeriodPage.navigateToAccountingPeriod(companyName);

        Assert.assertTrue(accountingPeriodPage.isAccountingPeriodFormDisplayed(),
                "Accounting Period form should be displayed");

        Assert.assertTrue(accountingPeriodPage.getPageHeading().contains(companyName),
                "Page heading should contain the company name");

        System.out.println("[TEST 2] Navigation to Accounting Period page successful");
    }

    @Test(priority = 3, dependsOnMethods = {"testNavigateToAccountingPeriod"},
            description = "Set accounting period start date to 01/01/2018 and verify listing")
    public void testSetAccountingPeriod() {
        System.out.println("\n[TEST 3] Setting Accounting Period date to: " + ACCOUNTING_DATE);

        accountingPeriodPage.setAccountingPeriod(ACCOUNTING_DATE);

        Assert.assertTrue(accountingPeriodPage.verifyRecordPresentAP(ACCOUNTING_DATE),
                "Accounting period '" + ACCOUNTING_DATE + "' should appear in the listing");

        System.out.println("[TEST 3] Accounting Period set successfully: " + ACCOUNTING_DATE);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        System.out.println("\n=== Cleaning up: deleting test company: " + companyName + " ===");
        try {
            driver.get(config.getProperty("base.url"));
            homePage.isCompanyHeadingDisplayed(); // wait for home page to fully load
            homePage.deleteCompany(companyName);
            System.out.println("Test company deleted: " + companyName);
        } catch (Exception e) {
            System.out.println("WARNING: Could not delete test company '" + companyName
                    + "': " + e.getMessage());
        }
        System.out.println("=== Cleanup Complete ===\n");
    }
}

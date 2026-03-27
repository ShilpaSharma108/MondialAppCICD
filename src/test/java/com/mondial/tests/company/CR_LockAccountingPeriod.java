package com.mondial.tests.company;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mondial.pages.AccountingPeriodPage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Lock / Unlock Accounting Period Test Class
 * Verifies that an accounting period can be locked (Close Fiscal Year)
 * and subsequently unlocked (Open Fiscal Year)
 */
public class CR_LockAccountingPeriod extends BaseTest {

    private HomePage homePage;
    private AccountingPeriodPage accountingPeriodPage;
    private String companyName;

    @BeforeClass
    public void initialise() {
        System.out.println("=== Starting Lock/Unlock Accounting Period Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        accountingPeriodPage = new AccountingPeriodPage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        companyName = config.getProperty("testCompanyName");

        // Anchor the post-login redirect before any navigation
        if (!homePage.isCompanyHeadingDisplayed()) {
            System.out.println("WARNING: Company heading not visible after login – " +
                    "current URL: " + driver.getCurrentUrl() +
                    ". Re-navigating to base URL and retrying.");
            driver.get(config.getProperty("base.url"));
            homePage.isCompanyHeadingDisplayed();
        }

        System.out.println("Company: " + companyName);
        System.out.println("=== Lock/Unlock Accounting Period Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Navigate to Accounting Period page and verify heading")
    public void testNavigateToAccountingPeriod() {
        System.out.println("\n[TEST 1] Navigating to Accounting Period page for: " + companyName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        accountingPeriodPage.navigateToAccountingPeriod(companyName);

        Assert.assertTrue(accountingPeriodPage.isAccountingPeriodFormDisplayed(),
                "Accounting Period form should be displayed");

        Assert.assertTrue(accountingPeriodPage.getPageHeading().contains(companyName),
                "Page heading should contain the company name");

        System.out.println("[TEST 1] Navigation to Accounting Period page successful");
    }

    @Test(priority = 2, dependsOnMethods = {"testNavigateToAccountingPeriod"},
            description = "Lock the first accounting period via Close Fiscal Year")
    public void testLockAccountingPeriod() {
        System.out.println("\n[TEST 2] Locking Accounting Period...");

        accountingPeriodPage.lockAccountingPeriod();

        Assert.assertTrue(accountingPeriodPage.isLocked(),
                "Accounting period should be locked (Open Fiscal Year button should be visible)");

        System.out.println("[TEST 2] Accounting Period locked successfully");
    }

    @Test(priority = 3, dependsOnMethods = {"testLockAccountingPeriod"},
            description = "Unlock the first accounting period via Open Fiscal Year")
    public void testUnlockAccountingPeriod() {
        System.out.println("\n[TEST 3] Unlocking Accounting Period...");

        accountingPeriodPage.unlockAccountingPeriod();

        Assert.assertTrue(accountingPeriodPage.isUnlocked(),
                "Accounting period should be unlocked (Close Fiscal Year button should be visible)");

        System.out.println("[TEST 3] Accounting Period unlocked successfully");
    }
}

package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.CustomersVendorsPage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * CR_CustomersSortTest
 * Verifies ascending and descending sort on the Customer Name column
 * of the Customers page.
 *
 * Navigation: Home Page → Customers link for testCompanyName
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 * - At least two customer records must exist for testCompanyName
 */
public class CR_CustomersSortTest extends BaseTest {

    private HomePage homePage;
    private CustomersVendorsPage customersVendorsPage;
    private String companyName;

    @BeforeClass
    public void setup() {
        System.out.println("=== Starting CR_CustomersSortTest Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        customersVendorsPage = new CustomersVendorsPage(driver);

        companyName = config.getProperty("companyReport");

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        homePage.isCompanyHeadingDisplayed();
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Home page should be displayed after login");

        System.out.println("Test company: " + companyName);
        System.out.println("=== CR_CustomersSortTest Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to the Customers page from the Home page
     * via the Customers link in the company row.
     */
    @Test(priority = 1, description = "Navigate to Customers page from Home Page")
    public void testNavigateToCustomersPage() {
        System.out.println("\n[TEST 1] Navigating to Customers page for: " + companyName);

        customersVendorsPage.navigateToResourcePage(companyName, "Customers");

        String heading = customersVendorsPage.getPageHeading();
        System.out.println("[TEST 1] Page heading: " + heading);

        Assert.assertTrue(heading.contains(companyName + " - Customers"),
                "Page heading should contain company name and 'Customers'");

        Assert.assertTrue(customersVendorsPage.hasRecords(),
                "Customers page should have at least one record to test sorting");

        System.out.println("[TEST 1] Successfully navigated to Customers page");
    }

    /**
     * Test 2: Sort customers by Customer Name (company_name) ascending.
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToCustomersPage"},
            description = "Sort customers by Customer Name ascending")
    public void testSortByCustomerNameAscending() {
        System.out.println("\n[TEST 2] Sorting customers by Customer Name (ascending)...");

        customersVendorsPage.clickColumnHeader("company_name");

        List<String> values = customersVendorsPage.getColumnValues("company_name");
        System.out.println("[TEST 2] Values after ascending sort: " + values);

        Assert.assertFalse(values.isEmpty(),
                "Customer name column should have values after sorting");

        Assert.assertTrue(customersVendorsPage.isSortedAscending(values),
                "Customer names should be sorted in ascending order");

        System.out.println("[TEST 2] Ascending sort by Customer Name verified");
    }

    /**
     * Test 3: Sort customers by Customer Name (company_name) descending.
     */
    @Test(priority = 3, dependsOnMethods = {"testSortByCustomerNameAscending"},
            description = "Sort customers by Customer Name descending")
    public void testSortByCustomerNameDescending() {
        System.out.println("\n[TEST 3] Sorting customers by Customer Name (descending)...");

        customersVendorsPage.clickColumnHeader("company_name");

        List<String> values = customersVendorsPage.getColumnValues("company_name");
        System.out.println("[TEST 3] Values after descending sort: " + values);

        Assert.assertFalse(values.isEmpty(),
                "Customer name column should have values after sorting");

        Assert.assertTrue(customersVendorsPage.isSortedDescending(values),
                "Customer names should be sorted in descending order");

        System.out.println("[TEST 3] Descending sort by Customer Name verified");
    }
}

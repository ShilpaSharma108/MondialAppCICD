package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;
import com.mondial.pages.ExchangeRatePage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Exchange Rate Source - Unauthorised Access Test
 * Verifies that a Consumer (non-admin) user cannot Create, Edit or Delete
 * Exchange Rate Sources:
 * - Create: redirected to home page
 * - Edit:   redirected to home page
 * - Delete: redirected to home page
 *
 * Prerequisites:
 * - Consumer credentials in config.properties (Consumer, validPassword)
 * - At least one Exchange Rate Source must exist for the Edit and Delete tests
 */
public class ES_Exchange_VerifyUnauthorisedUserUnabletoAccess extends BaseTest {

    private HomePage homePage;
    private ExchangeRatePage exchangeRatePage;

    /**
     * Setup: login as Consumer (non-admin) user
     */
    @BeforeClass
    public void setup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        exchangeRatePage = new ExchangeRatePage(driver);

        loginPage.login(config.getProperty("Consumer"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after Consumer login");
    }

    /**
     * Test 1: Verify Consumer cannot CREATE a new Exchange Rate Source
     */
    @Test(priority = 1, description = "Verify that Unauthorised user not able to CREATE New Exchange Rate Source")
    public void unauthorisedUserCreateESR() {
        System.out.println("\n[TEST 1] Verifying Consumer cannot create Exchange Rate Source...");

        exchangeRatePage.navigateERSMenu();
        exchangeRatePage.clickCreateERSButton();

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Consumer should be redirected to home page after attempting to create ERS");

        System.out.println("[TEST 1] Confirmed: Consumer cannot create Exchange Rate Source");
    }

    /**
     * Test 2: Verify Consumer cannot EDIT an Exchange Rate Source.
     * Expected behaviour: user is redirected to home page.
     */
    @Test(priority = 2, dependsOnMethods = {"unauthorisedUserCreateESR"},
          description = "Verify that Unauthorised user not able to EDIT Exchange Rate Source")
    public void unauthorisedUserESRUpdation() {
        System.out.println("\n[TEST 2] Verifying Consumer cannot edit Exchange Rate Source...");

        exchangeRatePage.navigateERSMenu();
        exchangeRatePage.clickLastEditBtn();

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Consumer should be redirected to home page after attempting to edit ERS");

        System.out.println("[TEST 2] Confirmed: Consumer cannot edit Exchange Rate Source");
    }

    /**
     * Test 3: Verify Consumer cannot DELETE an Exchange Rate Source
     */
    @Test(priority = 3, dependsOnMethods = {"unauthorisedUserESRUpdation"},
          description = "Verify that Unauthorised user not able to DELETE Exchange Rate Source")
    public void unauthorisedUserESRDeletion() {
        System.out.println("\n[TEST 3] Verifying Consumer cannot delete Exchange Rate Source...");

        exchangeRatePage.navigateERSMenu();
        exchangeRatePage.clickLastDeleteBtn();

        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Consumer should be redirected to home page after attempting to delete ERS");

        System.out.println("[TEST 3] Confirmed: Consumer cannot delete Exchange Rate Source");
    }
}

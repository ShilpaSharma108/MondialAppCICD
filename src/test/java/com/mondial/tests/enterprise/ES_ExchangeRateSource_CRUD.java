package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;
import com.mondial.pages.ExchangeRatePage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Exchange Rate Source CRUD Test
 * Tests Create, Cancel Update, Update, and Delete operations for Exchange Rate Sources
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ES_ExchangeRateSource_CRUD extends BaseTest {

    private HomePage homePage;
    private ExchangeRatePage exchangeRatePage;
    private String ersName;

    /**
     * Setup: login and initialise page objects
     */
    @BeforeClass
    public void ersSetup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        exchangeRatePage = new ExchangeRatePage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        ersName = "ERS_" + System.currentTimeMillis();
        System.out.println("Generated ERS name: " + ersName);
    }

    /**
     * Test 1: Create a new Exchange Rate Source and verify it appears in the listing
     */
    @Test(priority = 1, description = "Verify Exchange Rate Source creation")
    public void createERS() {
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        exchangeRatePage.exchangeRateCreation(ersName);

        Assert.assertTrue(exchangeRatePage.verifyRecordPresent(ersName),
                "Newly created ERS '" + ersName + "' should appear in the listing");
    }

    /**
     * Test 2: Open edit form, enter a new name, then Cancel — record should be unchanged
     */
    @Test(priority = 2, description = "Verify Cancel Button while updating Exchange Rate Source")
    public void cancelUpdation_ERS() {
        exchangeRatePage.clickEditRecord(ersName);
        exchangeRatePage.cancelRename(ersName);

        Assert.assertTrue(exchangeRatePage.verifyRecordPresent(ersName),
                "ERS '" + ersName + "' should still be present after cancel");
    }

    /**
     * Test 3: Rename the Exchange Rate Source and verify the updated name appears
     */
    @Test(priority = 3, description = "Verify Update button to update Exchange Rate Source")
    public void updateERS() {
        exchangeRatePage.clickEditRecord(ersName);
        ersName = exchangeRatePage.renameRecord(ersName);

        Assert.assertTrue(exchangeRatePage.verifyRecordPresent(ersName),
                "Updated ERS '" + ersName + "' should appear in the listing");
    }

    /**
     * Test 4: Delete the Exchange Rate Source and verify it is removed from the listing
     */
    @Test(priority = 4, description = "Verify user is able to Delete Exchange Rate Source")
    public void deleteERS() {
        exchangeRatePage.deleteRecord(ersName);

        Assert.assertFalse(exchangeRatePage.verifyRecordPresent(ersName),
                "Deleted ERS '" + ersName + "' should no longer appear in the listing");
    }
}

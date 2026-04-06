package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.AlternateAccountPage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Alternate Account Set - Copy From Company Test Class
 * Verifies that accounts can be added to an Alternate Account Set by copying from an existing company
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - At least one company with accounts must exist in the system
 */
public class ES_AA_CopyFromCompanyTest extends BaseTest {

    private HomePage homePage;
    private AlternateAccountPage alternateAccountPage;
    private String alternateAccountName;
    private String symbol;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates unique names for the alternate account set
     */
    @BeforeClass
    public void copyFromCompanySetup() {
        System.out.println("=== Starting Alternate Account Copy From Company Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        alternateAccountPage = new AlternateAccountPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with user: " + username);
        loginPage.login(username, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        alternateAccountName = "AltAcc_" + System.currentTimeMillis();
        symbol = "SYM_" + System.currentTimeMillis();
        System.out.println("Generated Alternate Account Set name: " + alternateAccountName);
        System.out.println("=== Alternate Account Copy From Company Test Setup Complete ===\n");
    }

    /**
     * Test 1: Create an Alternate Account Set, copy accounts from a company, verify, then delete
     * - Creates a new alternate account set
     * - Opens the set and copies accounts from the first available company
     * - Verifies accounts are populated after copy
     * - Navigates back to listing and deletes the set
     */
    @Test(priority = 1, description = "Alternate Account Set - Copy from Company")
    public void testCopyFromCompany_AlternateAccount() {
        System.out.println("\n[TEST 1] Verifying Copy From Company for Alternate Account Set: " + alternateAccountName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        alternateAccountPage.navigateToAlternateAccountPage();

        alternateAccountPage.createAlternateAccount(alternateAccountName, symbol);

        String successMsg = alternateAccountPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("Alternate Account Set successfully created."),
                         "Success message should confirm alternate account set creation");

        alternateAccountPage.clickAccountLink(alternateAccountName);

        Assert.assertTrue(alternateAccountPage.isAccountSetDetailDisplayed(),
                         "Account set detail page should be displayed after clicking the account link");

        String companyName = "OEC Brasil";
        System.out.println("[TEST 1] Copying accounts from company: " + companyName);

        alternateAccountPage.addRecordsFromCompany(companyName);
        System.out.println("[TEST 1] Records copied from company: " + companyName);

        Assert.assertTrue(alternateAccountPage.isAccountsListPopulated(),
                         "Accounts list should be populated after copying from company");

        System.out.println("[TEST 1] Copy from company successful, navigating back to listing...");

        alternateAccountPage.navigateBackToListing();

        Assert.assertTrue(alternateAccountPage.isAddAlternateAccountBtnDisplayed(),
                         "Should be back on Alternate Account Sets listing page");

        alternateAccountPage.clickDelete(alternateAccountName);

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        String deleteMsg = alternateAccountPage.getSuccessMessage();
        Assert.assertTrue(deleteMsg.contains("Alternate Account Set successfully deleted."),
                         "Success message should confirm alternate account set deletion");

        alternateAccountPage.waitForSuccessMessageToDisappear();

        Assert.assertFalse(alternateAccountPage.verifyRecordPresent(alternateAccountName),
                          "Deleted alternate account set should no longer appear in the table");

        System.out.println("[TEST 1] Alternate Account Set Copy From Company test completed successfully");
    }
}

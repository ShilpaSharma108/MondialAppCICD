package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.NaturalAccountSetPage;

/**
 * Natural Account Set CRUD Test Class
 * Tests Create, Read, Update, and Delete operations for Natural Account Sets
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_NaturalAccCRUDTest extends BaseTest {

    private HomePage homePage;
    private NaturalAccountSetPage naturalAccountSetPage;
    private String companyName;
    private String naturalAccSet;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates random name for natural account set tests
     */
    @BeforeClass
    public void naturalAccSetup() {
        System.out.println("=== Starting Natural Account Set CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        naturalAccountSetPage = new NaturalAccountSetPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        companyName = config.getProperty("testCompanyName");

        System.out.println("Logging in with user: " + username);
        loginPage.login(username, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        naturalAccSet = "NASet_" + System.currentTimeMillis();
        System.out.println("Generated Natural Account Set name: " + naturalAccSet);
        System.out.println("Test Company: " + companyName);
        System.out.println("=== Natural Account Set CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Natural Account Sets page for the test company
     * Verifies the page heading contains the company name
     */
    @Test(priority = 1, description = "Navigate to Natural Account Set Page")
    public void testNavigateToNaturalAccountSets() {
        System.out.println("\n[TEST 1] Navigating to Natural Account Sets page...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");

        naturalAccountSetPage.navigateToNaturalAccountsPage(companyName);

        Assert.assertTrue(naturalAccountSetPage.isAddNaturalACSetBtnDisplayed(),
                         "Add Natural Account Set button should be displayed");

        String heading = naturalAccountSetPage.getNaturalAccountSetHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + ": Natural Account Sets"),
                         "Heading should contain company name and Natural Account Sets");

        System.out.println("[TEST 1] Successfully navigated to Natural Account Sets page");
    }

    /**
     * Test 2: Create a new Natural Account Set
     * Creates a set with a random name and verifies it appears in the table
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToNaturalAccountSets"}, description = "Create Natural Account Set")
    public void testCreateNaturalAccountSet() {
        System.out.println("\n[TEST 2] Creating Natural Account Set: " + naturalAccSet);

        naturalAccountSetPage.createNewACSet(naturalAccSet);

        Assert.assertTrue(naturalAccountSetPage.isAddNaturalACSetBtnDisplayed(),
                         "Should return to Natural Account Sets listing page");
        Assert.assertTrue(naturalAccountSetPage.verifyNewAccountSet(naturalAccSet),
                         "Newly created account set should appear in the table");

        System.out.println("[TEST 2] Natural Account Set created successfully: " + naturalAccSet);
    }

    /**
     * Test 3: Edit the previously created Natural Account Set
     * Updates the name and verifies the change is reflected
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateNaturalAccountSet"}, description = "Edit Natural Account Set")
    public void testEditNaturalAccountSet() {
        System.out.println("\n[TEST 3] Editing Natural Account Set: " + naturalAccSet);

        naturalAccountSetPage.navigateToEdit(naturalAccSet);

        Assert.assertTrue(naturalAccountSetPage.isCreateButtonDisplayed(),
                         "Create/Submit button should be displayed on edit page");

        naturalAccSet = naturalAccountSetPage.editACSet(naturalAccSet);

        Assert.assertTrue(naturalAccountSetPage.isAddNaturalACSetBtnDisplayed(),
                         "Should return to Natural Account Sets listing page");
        Assert.assertTrue(naturalAccountSetPage.verifyNewAccountSet(naturalAccSet),
                         "Updated account set name should appear in the table");

        System.out.println("[TEST 3] Natural Account Set updated to: " + naturalAccSet);
    }

    /**
     * Test 4: Delete the Natural Account Set
     * Deletes the set and verifies it no longer appears in the table
     */
    @Test(priority = 4, dependsOnMethods = {"testEditNaturalAccountSet"}, description = "Delete Natural Account Set")
    public void testDeleteNaturalAccountSet() {
        System.out.println("\n[TEST 4] Deleting Natural Account Set: " + naturalAccSet);

        naturalAccountSetPage.clickDelete(naturalAccSet);

        // Handle browser confirmation dialog
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        naturalAccountSetPage.waitForConfirmationMessageToDisappear();

        Assert.assertFalse(naturalAccountSetPage.verifyNewAccountSet(naturalAccSet),
                          "Deleted account set should no longer appear in the table");

        System.out.println("[TEST 4] Natural Account Set deleted successfully: " + naturalAccSet);
    }
}

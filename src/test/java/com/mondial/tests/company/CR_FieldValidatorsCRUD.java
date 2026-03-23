package com.mondial.tests.company;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.FieldValidatorsPage;

/**
 * Field Validators CRUD Test Class
 * Tests Create, Read, Update, and Delete operations for Field Validators
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_FieldValidatorsCRUD extends BaseTest {

    private HomePage homePage;
    private FieldValidatorsPage fieldValidatorsPage;
    private String companyName;
    private String fieldValidatorName;

    @BeforeClass
    public void fieldValidatorSetup() {
        System.out.println("=== Starting Field Validators CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        fieldValidatorsPage = new FieldValidatorsPage(driver);

        companyName = config.getProperty("testCompanyName");
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        fieldValidatorName = "FV_" + System.currentTimeMillis();
        System.out.println("Test company: " + companyName);
        System.out.println("Generated Field Validator name: " + fieldValidatorName);
        System.out.println("=== Field Validators CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Field Validators page for the test company
     */
    @Test(priority = 1, description = "Navigate to Field Validators Page")
    public void testNavigateToFieldValidators() {
        System.out.println("\n[TEST 1] Navigating to Field Validators page...");

        fieldValidatorsPage.navigateToFieldValidators(companyName);

        Assert.assertTrue(fieldValidatorsPage.isAddBtnDisplayed(),
                "Add Field Validator button should be displayed");

        String heading = fieldValidatorsPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains(companyName + ": Field Validators"),
                "Heading should contain company name and 'Field Validators'");

        System.out.println("[TEST 1] Successfully navigated to Field Validators page");
    }

    /**
     * Test 2: Verify Cancel button returns to listing, then create a new Field Validator
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToFieldValidators"}, description = "Create New Field Validator")
    public void testCreateFieldValidator() {
        System.out.println("\n[TEST 2] Creating Field Validator: " + fieldValidatorName);

        fieldValidatorsPage.verifyCancel();
        Assert.assertTrue(fieldValidatorsPage.isAddBtnDisplayed(),
                "Should return to Field Validators listing after Cancel");

        fieldValidatorsPage.addFieldValidator(fieldValidatorName);

        Assert.assertTrue(fieldValidatorsPage.isAddBtnDisplayed(),
                "Should be on Field Validators listing page after create");
        Assert.assertTrue(fieldValidatorsPage.verifyRecordPresent(fieldValidatorName),
                "Newly created field validator should appear in the table");

        System.out.println("[TEST 2] Field Validator created successfully: " + fieldValidatorName);
    }

    /**
     * Test 3: Edit the previously created Field Validator
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateFieldValidator"}, description = "Edit Field Validator")
    public void testEditFieldValidator() {
        System.out.println("\n[TEST 3] Editing Field Validator: " + fieldValidatorName);

        fieldValidatorsPage.navigateToEdit(fieldValidatorName);
        Assert.assertTrue(fieldValidatorsPage.isSubmitBtnDisplayed(),
                "Submit button should be displayed on edit page");

        // Cancel back to listing, then navigate to edit again
        fieldValidatorsPage.cancelAndWaitForListing();
        fieldValidatorsPage.navigateToEdit(fieldValidatorName);

        fieldValidatorName = fieldValidatorsPage.editFieldValidator(fieldValidatorName);

        Assert.assertTrue(fieldValidatorsPage.isAddBtnDisplayed(),
                "Should return to Field Validators listing after edit");
        Assert.assertTrue(fieldValidatorsPage.verifyRecordPresent(fieldValidatorName),
                "Updated field validator name should appear in the table");

        System.out.println("[TEST 3] Field Validator updated to: " + fieldValidatorName);
    }

    /**
     * Test 4: Delete the Field Validator
     */
    @Test(priority = 4, dependsOnMethods = {"testEditFieldValidator"}, description = "Delete Field Validator")
    public void testDeleteFieldValidator() {
        System.out.println("\n[TEST 4] Deleting Field Validator: " + fieldValidatorName);

        fieldValidatorsPage.clickDelete(fieldValidatorName);

        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        fieldValidatorsPage.waitForAlertToDismiss();

        Assert.assertFalse(fieldValidatorsPage.verifyRecordPresent(fieldValidatorName),
                "Deleted field validator should no longer appear in the table");

        System.out.println("[TEST 4] Field Validator deleted successfully: " + fieldValidatorName);
    }
}

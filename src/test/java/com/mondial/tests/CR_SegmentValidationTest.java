package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportingSegmentPage;

/**
 * GL Account Segment Validation Error Messages Test Class
 * Tests that appropriate error messages are displayed when creating
 * a new segment with blank or invalid fields
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class CR_SegmentValidationTest extends BaseTest {

    private HomePage homePage;
    private ReportingSegmentPage reportingSegmentPage;
    private String companyName;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Navigates to GL Account Segments page
     */
    @BeforeClass
    public void validationTestSetup() {
        System.out.println("=== Starting Segment Validation Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        reportingSegmentPage = new ReportingSegmentPage(driver);

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

        System.out.println("Test Company: " + companyName);

        // Navigate to GL Account Segments page
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Company heading should be displayed on home page");
        reportingSegmentPage.navigateToReportingSegment(companyName);

        String heading = reportingSegmentPage.getPageHeading();
        Assert.assertTrue(heading.contains(companyName + " - GL Account Segments"),
                         "Heading should contain company name and GL Account Segments");

        System.out.println("Navigated to GL Account Segments page");
        System.out.println("=== Segment Validation Test Setup Complete ===\n");
    }

    /**
     * Test 1: Submit form with all fields blank
     * Expects "Name can't be blank" and "Field validator can't be blank" errors
     */
    @Test(priority = 1, description = "Verify error messages when all fields are blank")
    public void testAllFieldsBlank() {
        System.out.println("\n[TEST 1] Submitting form with all fields blank...");

        reportingSegmentPage.clickAddGLSegmentBtn();
        reportingSegmentPage.clickCreateButton();

        Assert.assertTrue(reportingSegmentPage.isNameBlankErrorDisplayed(),
                         "Error message 'Name can't be blank' should be displayed");
        System.out.println("[TEST 1] Verified: Name can't be blank");

        Assert.assertTrue(reportingSegmentPage.isFieldValidatorBlankErrorDisplayed(),
                         "Error message 'Field validator can't be blank' should be displayed");
        System.out.println("[TEST 1] Verified: Field validator can't be blank");

        reportingSegmentPage.clickCancel();
        System.out.println("[TEST 1] All fields blank validation verified successfully");
    }

    /**
     * Test 2: Submit form with name blank but field validator selected
     * Expects "Name can't be blank" error only
     */
    @Test(priority = 2, dependsOnMethods = {"testAllFieldsBlank"}, description = "Verify error message when name is blank")
    public void testNameFieldBlank() {
        System.out.println("\n[TEST 2] Submitting form with name blank...");

        reportingSegmentPage.clickAddGLSegmentBtn();
        reportingSegmentPage.selectFieldValidator("Anything Goes");
        reportingSegmentPage.clickCreateButton();

        Assert.assertTrue(reportingSegmentPage.isNameBlankErrorDisplayed(),
                         "Error message 'Name can't be blank' should be displayed");
        System.out.println("[TEST 2] Verified: Name can't be blank");

        reportingSegmentPage.clickCancel();
        System.out.println("[TEST 2] Name blank validation verified successfully");
    }

    /**
     * Test 3: Submit form with field validator blank but name filled
     * Expects "Field validator can't be blank" error only
     */
    @Test(priority = 3, dependsOnMethods = {"testNameFieldBlank"}, description = "Verify error message when field validator is blank")
    public void testFieldValidatorBlank() {
        System.out.println("\n[TEST 3] Submitting form with field validator blank...");

        reportingSegmentPage.clickAddGLSegmentBtn();
        reportingSegmentPage.enterSegmentName("TestSegment");
        reportingSegmentPage.clickCreateButton();

        Assert.assertTrue(reportingSegmentPage.isFieldValidatorBlankErrorDisplayed(),
                         "Error message 'Field validator can't be blank' should be displayed");
        System.out.println("[TEST 3] Verified: Field validator can't be blank");

        reportingSegmentPage.clickCancel();
        System.out.println("[TEST 3] Field validator blank validation verified successfully");
    }

    /**
     * Test 4: Submit form with ordinal less than or equal to 1
     * Expects "Ordinal must be greater than 1" error
     */
    @Test(priority = 4, dependsOnMethods = {"testFieldValidatorBlank"}, description = "Verify error message when ordinal is less than or equal to 1")
    public void testOrdinalLessThanOrEqualToOne() {
        System.out.println("\n[TEST 4] Submitting form with ordinal set to 1...");

        reportingSegmentPage.clickAddGLSegmentBtn();
        reportingSegmentPage.enterSegmentName("TestSegment");
        reportingSegmentPage.selectFieldValidator("Anything Goes");
        reportingSegmentPage.enterOrdinal("1");
        reportingSegmentPage.clickCreateButton();

        Assert.assertTrue(reportingSegmentPage.isOrdinalErrorDisplayed(),
                         "Error message 'Ordinal must be greater than 1' should be displayed");
        System.out.println("[TEST 4] Verified: Ordinal must be greater than 1");

        reportingSegmentPage.clickCancel();
        System.out.println("[TEST 4] Ordinal validation verified successfully");
    }
}

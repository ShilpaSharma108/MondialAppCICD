package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.FormatCodes;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Format Code Checkbox Selection Test Class
 * Verifies that all checkboxes on the Format Code edit page can be selected
 */
public class FormatCode_CheckboxSelection extends BaseTest {

    private HomePage homePage;
    private FormatCodes fmt;
    private String recordName;

    @BeforeClass
    public void formatCodeSetup() {
        System.out.println("=== Starting Format Code Checkbox Selection Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        fmt = new FormatCodes(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "FmtCode_" + System.currentTimeMillis();
        System.out.println("Generated Format Code name: " + recordName);
        System.out.println("=== Format Code Checkbox Selection Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verifying Checkbox Selection on Format Code Page")
    public void testCheckboxSelection() {
        System.out.println("\n[TEST 1] Verifying checkbox selection for Format Code: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        fmt.navigateToFormatCode();

        Assert.assertTrue(fmt.isAddFormatCodeBtnDisplayed(),
                "Add Format Code button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        fmt.addNewFormatCode(companyName, recordName);

        Assert.assertTrue(fmt.verifyRecordPresent(recordName),
                "Newly created format code should appear in the listing");

        fmt.checkboxSelection(recordName);

        fmt.deleteFormatCode(recordName);

        Assert.assertFalse(fmt.verifyRecordPresent(recordName),
                "Deleted format code should not appear in the listing");

        System.out.println("[TEST 1] Checkbox selection verified and format code deleted successfully");
    }
}

package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.FormatCodes;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Format Code CRUD Test Class
 * Tests Create, Read, Update and Delete operations for Format Codes
 */
public class FormatCode_CRUD extends BaseTest {

    private HomePage homePage;
    private FormatCodes fmt;
    private String recordName;

    @BeforeClass
    public void formatCodeSetup() {
        System.out.println("=== Starting Format Code CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        fmt = new FormatCodes(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "FmtCode_" + System.currentTimeMillis();
        System.out.println("Generated Format Code name: " + recordName);
        System.out.println("=== Format Code CRUD Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Navigation to Format Codes Page and Create Format Code")
    public void testCreateFormatCode() {
        System.out.println("\n[TEST 1] Creating Format Code: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        fmt.navigateToFormatCode();

        Assert.assertTrue(fmt.isAddFormatCodeBtnDisplayed(),
                "Add Format Code button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        fmt.addNewFormatCode(companyName, recordName);

        Assert.assertTrue(fmt.verifyRecordPresent(recordName),
                "Newly created format code should appear in the listing");

        System.out.println("[TEST 1] Format Code created successfully: " + recordName);
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateFormatCode"}, description = "Verify Edit Format Code")
    public void testEditFormatCode() {
        System.out.println("\n[TEST 2] Editing Format Code: " + recordName);

        recordName = fmt.editFormatCode(recordName);

        Assert.assertTrue(fmt.verifyRecordPresent(recordName),
                "Updated format code name should appear in the listing");

        System.out.println("[TEST 2] Format Code updated to: " + recordName);
    }

    @Test(priority = 3, dependsOnMethods = {"testEditFormatCode"}, description = "Verify Delete Format Code")
    public void testDeleteFormatCode() {
        System.out.println("\n[TEST 3] Deleting Format Code: " + recordName);

        fmt.deleteFormatCode(recordName);

        Assert.assertFalse(fmt.verifyRecordPresent(recordName),
                "Deleted format code should not appear in the listing");

        System.out.println("[TEST 3] Format Code deleted successfully");
    }
}

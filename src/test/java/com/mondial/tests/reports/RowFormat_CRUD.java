package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.RowFormat;

/**
 * Row Format CRUD Test Class
 * Tests Create, Read, Update and Delete operations for Row Formats
 */
public class RowFormat_CRUD extends BaseTest {

    private HomePage homePage;
    private RowFormat rf;
    private String recordName;

    @BeforeClass
    public void rowFormatSetup() {
        System.out.println("=== Starting Row Format CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new RowFormat(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "RowFmt_" + System.currentTimeMillis();
        System.out.println("Generated Row Format name: " + recordName);
        System.out.println("=== Row Format CRUD Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Navigation to Row Formats Page and Create Row Format")
    public void testCreateRowFormat() {
        System.out.println("\n[TEST 1] Creating Row Format: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rf.navigateToRowFormat();

        Assert.assertTrue(rf.isAddRowFormatBtnDisplayed(),
                "Add Row Format button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        rf.addNewRowFormat(companyName, recordName);
        rf.cancelAndWaitForListing();

        Assert.assertTrue(rf.verifyRecordPresent(recordName),
                "Newly created row format should appear in the listing");

        System.out.println("[TEST 1] Row Format created successfully: " + recordName);
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateRowFormat"}, description = "Verify Edit Row Format")
    public void testEditRowFormat() {
        System.out.println("\n[TEST 2] Editing Row Format: " + recordName);

        recordName = rf.editRowFormat(recordName);

        Assert.assertTrue(rf.verifyRecordPresent(recordName),
                "Updated row format name should appear in the listing");

        System.out.println("[TEST 2] Row Format updated to: " + recordName);
    }

    @Test(priority = 3, dependsOnMethods = {"testEditRowFormat"}, description = "Verify Delete Row Format")
    public void testDeleteRowFormat() {
        System.out.println("\n[TEST 3] Deleting Row Format: " + recordName);

        rf.deleteRowFormat(recordName);

        Assert.assertFalse(rf.verifyRecordPresent(recordName),
                "Deleted row format should not appear in the listing");

        System.out.println("[TEST 3] Row Format deleted successfully");
    }
}

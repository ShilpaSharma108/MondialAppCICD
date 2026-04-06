package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.ColumnLayout;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Column Layout CRUD Test Class
 * Tests Create, Read, Update and Delete operations for Column Layouts
 */
public class RP_ColumnLayout_CRUD extends BaseTest {

    private HomePage homePage;
    private ColumnLayout cl;
    private String recordName;

    @BeforeClass
    public void columnLayoutSetup() {
        System.out.println("=== Starting Column Layout CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        cl = new ColumnLayout(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "ColLayout_" + System.currentTimeMillis();
        System.out.println("Generated Column Layout name: " + recordName);
        System.out.println("=== Column Layout CRUD Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Navigation to Column Layouts Page and Create Column Layout")
    public void testCreateColumnLayout() {
        System.out.println("\n[TEST 1] Creating Column Layout: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        cl.navigateToColumnLayout();

        Assert.assertTrue(cl.isAddColumnLayoutBtnDisplayed(),
                "Add Column Layout button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        cl.addNewColumnLayout(companyName, recordName);
        cl.cancelAndWaitForListing();

        Assert.assertTrue(cl.verifyRecordPresent(recordName),
                "Newly created column layout should appear in the listing");

        System.out.println("[TEST 1] Column Layout created successfully: " + recordName);
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateColumnLayout"}, description = "Verify Edit Column Layout")
    public void testEditColumnLayout() {
        System.out.println("\n[TEST 2] Editing Column Layout: " + recordName);

        recordName = cl.editColumnLayout(recordName);

        Assert.assertTrue(cl.verifyRecordPresent(recordName),
                "Updated column layout name should appear in the listing");

        System.out.println("[TEST 2] Column Layout updated to: " + recordName);
    }

    @Test(priority = 3, dependsOnMethods = {"testEditColumnLayout"}, description = "Verify Delete Column Layout")
    public void testDeleteColumnLayout() {
        System.out.println("\n[TEST 3] Deleting Column Layout: " + recordName);

        cl.deleteColumnLayout(recordName);

        Assert.assertFalse(cl.verifyRecordPresent(recordName),
                "Deleted column layout should not appear in the listing");

        System.out.println("[TEST 3] Column Layout deleted successfully");
    }
}

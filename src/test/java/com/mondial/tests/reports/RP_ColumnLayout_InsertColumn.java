package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.ColumnLayout;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Column Layout Insert Column Test Class
 * Tests inserting columns (left and right) within a Column Layout and deleting them
 */
public class RP_ColumnLayout_InsertColumn extends BaseTest {

    private HomePage homePage;
    private ColumnLayout cl;
    private String recordName;

    @BeforeClass
    public void columnLayoutSetup() {
        System.out.println("=== Starting Column Layout Insert Column Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        cl = new ColumnLayout(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        // If the browser is on a non-home page after login (e.g. previous test left
        // it elsewhere), navigate explicitly so the company heading is visible.
        if (!homePage.isCompanyHeadingDisplayed()) {
            System.out.println("WARNING: Company heading not visible after login – re-navigating.");
            driver.get(config.getProperty("base.url"));
            homePage.isCompanyHeadingDisplayed();
        }

        recordName = "ColLayout_" + System.currentTimeMillis();
        System.out.println("Generated Column Layout name: " + recordName);
        System.out.println("=== Column Layout Insert Column Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Inserting a new column to the Left")
    public void testInsertColumnLeft() {
        System.out.println("\n[TEST 1] Creating Column Layout and inserting column to left: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        cl.navigateToColumnLayout();

        Assert.assertTrue(cl.isAddColumnLayoutBtnDisplayed(),
                "Add Column Layout button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        cl.addNewColumnLayout(companyName, recordName);
        cl.insertColumnLeft("Left Column");

        System.out.println("[TEST 1] Column inserted to the left successfully");
    }

    @Test(priority = 2, dependsOnMethods = {"testInsertColumnLeft"}, description = "Inserting a new column to the Right")
    public void testInsertColumnRight() {
        System.out.println("\n[TEST 2] Inserting column to right...");

        cl.insertColumnRight("Right Column");

        System.out.println("[TEST 2] Column inserted to the right successfully");
    }

    @Test(priority = 3, dependsOnMethods = {"testInsertColumnRight"}, description = "Delete inserted columns")
    public void testDeleteInsertedColumns() {
        System.out.println("\n[TEST 3] Deleting inserted columns...");

        cl.deleteInsertedColumn("Right Column");

        System.out.println("[TEST 3] Inserted columns deleted successfully");
    }
}

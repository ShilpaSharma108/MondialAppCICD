package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.RowFormat;

/**
 * Row Format - Insert Row CRUD Test Class
 * Tests creating and deleting Report Rows within a Row Format
 */
public class RP_RowFormat_InsertRow_CRUD extends BaseTest {

    private HomePage homePage;
    private RowFormat rf;
    private String recordName;
    private String rowCode;

    @BeforeClass
    public void rowFormatSetup() {
        System.out.println("=== Starting Row Format Insert Row CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new RowFormat(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "RowFmt_" + System.currentTimeMillis();
        rowCode = "RC" + (System.currentTimeMillis() % 10000);
        System.out.println("Generated Row Format name: " + recordName);
        System.out.println("Generated Row Code: " + rowCode);
        System.out.println("=== Row Format Insert Row CRUD Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Row Format - Create Report Row")
    public void testCreateReportRow() {
        System.out.println("\n[TEST 1] Creating Row Format and inserting a row: " + recordName);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rf.navigateToRowFormat();

        Assert.assertTrue(rf.isAddRowFormatBtnDisplayed(),
                "Add Row Format button should be displayed");

        String companyName = config.getProperty("testCompanyName");
        rf.addNewRowFormat(companyName, recordName);
        rf.cancelAndWaitForListing();
        rf.clickEdit(recordName);
        rf.createReportRow(rowCode);

        Assert.assertTrue(rf.newRowPresent(rowCode),
                "Newly created report row should be visible in the grid");

        System.out.println("[TEST 1] Report row created with code: " + rowCode);
    }

    @Test(priority = 2, dependsOnMethods = {"testCreateReportRow"}, description = "Row Format - Delete newly added Report Row")
    public void testDeleteReportRow() throws InterruptedException {
        System.out.println("\n[TEST 2] Deleting report row: " + rowCode);

        rf.deleteRow(rowCode);

        Assert.assertFalse(rf.newRowPresent(rowCode),
                "Deleted report row should no longer be visible");

        rf.clickCancelAndWaitForListing();
        rf.deleteRowFormat(recordName);

        Assert.assertFalse(rf.verifyRecordPresent(recordName),
                "Row format should be deleted during cleanup");

        System.out.println("[TEST 2] Report row and Row Format deleted successfully");
    }
}

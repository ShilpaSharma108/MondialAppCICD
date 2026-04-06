package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.RowFormat;

/**
 * Copy Row Format Test Class
 * Verifies that a Row Format can be copied and both original and copy can be deleted
 */
public class RP_CopyRowFormat extends BaseTest {

    private HomePage homePage;
    private RowFormat rf;
    private String recordName;
    private String copiedName;

    @BeforeClass
    public void rowFormatSetup() {
        System.out.println("=== Starting Copy Row Format Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rf = new RowFormat(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "RowFmt_" + System.currentTimeMillis();
        System.out.println("Generated Row Format name: " + recordName);
        System.out.println("=== Copy Row Format Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Copy Row Format - Create, Copy, and Delete both")
    public void testCopyRowFormat() {
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
                "Original row format should appear in the listing");

        copiedName = rf.copyRowFormat(recordName);

        Assert.assertTrue(rf.verifyRecordPresent(copiedName),
                "Copied row format should appear in the listing");

        System.out.println("[TEST 1] Row Format copied: " + copiedName);

        rf.deleteRowFormat(recordName);
        rf.deleteRowFormat(copiedName);

        Assert.assertFalse(rf.verifyRecordPresent(recordName),
                "Original row format should be deleted");
        Assert.assertFalse(rf.verifyRecordPresent(copiedName),
                "Copied row format should be deleted");

        System.out.println("[TEST 1] Both row formats deleted successfully");
    }
}

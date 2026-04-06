package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.ColumnLayout;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Copy Column Layout Test Class
 * Verifies that a Column Layout can be copied and both original and copy can be deleted
 */
public class RP_CopyColumnLayout extends BaseTest {

    private HomePage homePage;
    private ColumnLayout cl;
    private String recordName;
    private String copiedName;

    @BeforeClass
    public void columnLayoutSetup() {
        System.out.println("=== Starting Copy Column Layout Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        cl = new ColumnLayout(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        recordName = "ColLayout_" + System.currentTimeMillis();
        System.out.println("Generated Column Layout name: " + recordName);
        System.out.println("=== Copy Column Layout Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Copy Column Layout - Create, Copy, and Delete both")
    public void testCopyColumnLayout() {
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
                "Original column layout should appear in the listing");

        copiedName = cl.copyColumnLayout(recordName);

        Assert.assertTrue(cl.verifyRecordPresent(copiedName),
                "Copied column layout should appear in the listing");

        System.out.println("[TEST 1] Column Layout copied: " + copiedName);

        cl.deleteColumnLayout(recordName);
        cl.deleteColumnLayout(copiedName);

        Assert.assertFalse(cl.verifyRecordPresent(recordName),
                "Original column layout should be deleted");
        Assert.assertFalse(cl.verifyRecordPresent(copiedName),
                "Copied column layout should be deleted");

        System.out.println("[TEST 1] Both column layouts deleted successfully");
    }
}

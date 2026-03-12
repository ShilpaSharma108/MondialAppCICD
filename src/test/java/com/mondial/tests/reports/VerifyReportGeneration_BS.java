package com.mondial.tests.reports;

import com.mondial.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Verify Report Generation - Balance Sheet Test Class
 * Verifies that a Balance Sheet report can be generated successfully via Report Writer
 */
public class VerifyReportGeneration_BS extends BaseTest {

    private HomePage homePage;
    private ReportsWriter rw;
    private String reportType;
    private String companyName;

    @BeforeClass
    public void reportGenerationSetup() {
        System.out.println("=== Starting Report Generation (Balance Sheet) Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        rw = new ReportsWriter(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        loginPage.login(username, password);

        reportType = "Balance Sheet";
        companyName = config.getProperty("companyReport");

        System.out.println("Report type: " + reportType);
        System.out.println("Company: " + companyName);
        System.out.println("=== Report Generation (Balance Sheet) Test Setup Complete ===\n");
    }

    @Test(priority = 1, description = "Verify Report Writer - Balance Sheet Report Generation")
    public void testReportGeneration_BS() {
        System.out.println("\n[TEST 1] Generating Balance Sheet report...");

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be visible after login");

        rw.navigateToReportWriter();

        Assert.assertTrue(rw.isAddReportBtnDisplayed(),
                "Add Report button should be displayed on Reports page");

        String parentWindow = driver.getWindowHandle();
        rw.generateReport(reportType, companyName);
        rw.switchToGeneratedReportTab(parentWindow);

        String headerContent = rw.getReportHeaderContent();

        Assert.assertFalse(headerContent.trim().isEmpty(),
                "Report header content should not be empty after generation");

        Assert.assertTrue(headerContent.contains(reportType),
                "Report header should contain the report type: " + reportType);

        Assert.assertTrue(headerContent.contains(companyName),
                "Report header should contain the company name: " + companyName);

        Assert.assertTrue(rw.isReportDataPresent(),
                "Generated report should contain at least one data row");

        String pageTitle = rw.getReportPageTitle();
        Assert.assertFalse(pageTitle.trim().isEmpty(),
                "Report page tab should have a non-empty title");

        Assert.assertTrue(rw.isReportLogoDisplayed(),
                "Report left header (logo area) should be visible");

        String rightHeader = rw.getReportRightHeaderContent();
        String expectedDate = new SimpleDateFormat("d MMMM, yyyy").format(new Date());
        String selectedCurrency = rw.getLastSelectedCurrency();
        String selectedLedger = rw.getLastSelectedLedger();

        Assert.assertTrue(rightHeader.contains(expectedDate),
                "Report right header should contain generated date: " + expectedDate);

        Assert.assertTrue(rightHeader.contains(selectedCurrency),
                "Report right header should contain selected currency: " + selectedCurrency);

        Assert.assertTrue(rightHeader.contains(selectedLedger),
                "Report right header should contain selected ledger: " + selectedLedger);

        System.out.println("[TEST 1] Balance Sheet report generated and verified successfully");
        System.out.println("  Header   : " + headerContent);
        System.out.println("  Right    : " + rightHeader);
        System.out.println("  Date     : " + expectedDate);
        System.out.println("  Currency : " + selectedCurrency);
        System.out.println("  Ledger   : " + selectedLedger);
        System.out.println("  Title    : " + pageTitle);
    }
}

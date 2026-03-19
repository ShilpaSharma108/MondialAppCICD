package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mondial.pages.HomePage;
import com.mondial.pages.LedgerPage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsWriter;

/**
 * Integration Test: Ledger - Report Writer
 * Verifies that ledgers on the Ledger page match ledgers on the Report Writer page
 */
public class IntegrationLedger_ReportWriterTest extends BaseTest {

	private HomePage homePage;
	private LedgerPage ledgerPage;
	private ReportsWriter reportsWriter;
	private List<String> ledgerPageList;
	private List<String> reportWriterList;

	@BeforeClass
	public void integrationSetup() {
		System.out.println("=== Starting Ledger-ReportWriter Integration Test Setup ===");

		LoginPage loginPage = new LoginPage(driver);
		homePage = new HomePage(driver);
		ledgerPage = new LedgerPage(driver);
		reportsWriter = new ReportsWriter(driver);

		String username = config.getProperty("validUsername");
		String password = config.getProperty("validPassword");

		System.out.println("Logging in with user: " + username);
		loginPage.login(username, password);

		// Wait for the post-login redirect to fully complete before any navigation.
		// Thread.sleep is unreliable with PageLoadStrategy.NONE; use the same
		// 60-second company-heading anchor used by all other test classes.
		homePage.isCompanyHeadingDisplayed();

		System.out.println("=== Ledger-ReportWriter Integration Test Setup Complete ===\n");
	}

	@Test(priority = 1, description = "Verify all Ledgers on Ledger Page are available on Report Writer Page")
	public void testLedgerIntegrationWithReportWriter() {
		System.out.println("\n[TEST 1] Verifying ledger integration with Report Writer...");

		ledgerPage.navigateToLedgerPage();
		Assert.assertTrue(ledgerPage.isAddLedgerBtnDisplayed(),
				"Add Ledger button should be displayed");

		ledgerPageList = reportsWriter.getLedgerTableValues();
		System.out.println("Ledger Page values: " + ledgerPageList);

		reportsWriter.navigateToReportWriter();
		String heading = reportsWriter.getReportHeading();
		Assert.assertTrue(heading.contains("Reports"),
				"Report heading should be displayed");

		reportWriterList = reportsWriter.getLedgerDropdownValues();
		System.out.println("Report Writer values: " + reportWriterList);

		Assert.assertEquals(ledgerPageList, reportWriterList,
				"Ledger values on Ledger Page should match Report Writer dropdown");

		System.out.println("[TEST 1] Ledger integration with Report Writer verified successfully");
	}
}

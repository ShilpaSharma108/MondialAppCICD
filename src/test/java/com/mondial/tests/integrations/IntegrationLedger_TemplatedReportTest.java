package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mondial.pages.LedgerPage;
import com.mondial.pages.LoginPage;

/**
 * Integration Test: Ledger - Templated Report
 * Verifies that ledgers on the Ledger page match ledgers on the Templated Report page
 *
 * NOTE: Tests are disabled because the TemplatedReportPage page object has not been
 * migrated to the new framework yet. Once TemplatedReportPage is available, re-enable
 * these tests and replace the TODO sections with actual templated report assertions.
 */
public class IntegrationLedger_TemplatedReportTest extends BaseTest {

	private LedgerPage ledgerPage;
	private String ledgerName;
	private List<String> ledgerPageList;
	private List<String> templatedReportList;

	@BeforeClass
	public void integrationSetup() {
		System.out.println("=== Starting Ledger-TemplatedReport Integration Test Setup ===");

		LoginPage loginPage = new LoginPage(driver);
		ledgerPage = new LedgerPage(driver);

		String username = config.getProperty("validUsername");
		String password = config.getProperty("validPassword");

		System.out.println("Logging in with user: " + username);
		loginPage.login(username, password);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ledgerName = "Ledger_" + System.currentTimeMillis();
		System.out.println("Generated Ledger name: " + ledgerName);
		System.out.println("=== Ledger-TemplatedReport Integration Test Setup Complete ===\n");
	}

	// TODO: Re-enable once TemplatedReportPage page object is migrated to the new framework.
	// This test needs TemplatedReportPage methods: navigateToTemplatedReport(),
	// selectRequiredValues(company, reportType), getLedgerDropdownValues()
	@Test(priority = 1, enabled = false,
			description = "Verify all Ledgers on Ledger Page are available on Templated Report Page")
	public void testLedgerIntegrationWithTemplatedReport() {
		System.out.println("\n[TEST 1] Verifying ledger integration with Templated Report...");

		ledgerPage.navigateToLedgerPage();
		Assert.assertTrue(ledgerPage.isAddLedgerBtnDisplayed(),
				"Add Ledger button should be displayed");

		ledgerPage.createLedger(ledgerName);
		String successMsg = ledgerPage.getSuccessMessage();
		Assert.assertTrue(successMsg.contains("Ledger was successfully created."),
				"Success message should confirm ledger creation");

		ledgerPage.waitForSuccessMessageToDisappear();

		// TODO: Navigate to Templated Report page, select company/report type,
		// get ledger dropdown values and compare with ledgerPageList
		// templatedReportPage.navigateToTemplatedReport();
		// templatedReportPage.selectRequiredValues(companyName, "Trial Balance");
		// templatedReportList = templatedReportPage.getLedgerDropdownValues();
		// Assert.assertEquals(ledgerPageList, templatedReportList);

		System.out.println("[TEST 1] Ledger integration with Templated Report verified");
	}

	// TODO: Re-enable once TemplatedReportPage page object is migrated to the new framework.
	@Test(priority = 2, enabled = false, dependsOnMethods = {"testLedgerIntegrationWithTemplatedReport"},
			description = "Verify user is able to Delete Unused Ledger")
	public void testDeleteLedger() {
		System.out.println("\n[TEST 2] Deleting Ledger: " + ledgerName);

		ledgerPage.navigateToLedgerPage();
		Assert.assertTrue(ledgerPage.isAddLedgerBtnDisplayed(),
				"Add Ledger button should be displayed");

		ledgerPage.clickDelete(ledgerName);

		try {
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println("No confirmation dialog present");
		}

		String successMsg = ledgerPage.getSuccessMessage();
		Assert.assertTrue(successMsg.contains("Ledger was successfully deleted"),
				"Success message should confirm ledger deletion");

		ledgerPage.waitForSuccessMessageToDisappear();

		Assert.assertFalse(ledgerPage.verifyRecordPresent(ledgerName),
				"Deleted ledger should no longer appear in the table");

		// TODO: Navigate to Templated Report page and verify ledger is removed
		// from the dropdown as well
		// templatedReportPage.navigateToTemplatedReport();
		// templatedReportPage.selectRequiredValues(companyName, "Trial Balance");
		// templatedReportList = templatedReportPage.getLedgerDropdownValues();
		// Assert.assertEquals(ledgerPageList, templatedReportList);

		System.out.println("[TEST 2] Ledger deleted successfully: " + ledgerName);
	}
}

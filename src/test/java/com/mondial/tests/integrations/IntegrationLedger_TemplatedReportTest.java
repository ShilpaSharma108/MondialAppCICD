package com.mondial.tests.integrations;

import com.mondial.tests.BaseTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mondial.pages.LedgerPage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.ReportsWriter;

/**
 * Integration Test: Ledger - Report Writer (Create/Delete)
 * Verifies that creating a new ledger makes it appear in the Report Writer
 * ledger dropdown, and deleting it removes it from the dropdown.
 */
public class IntegrationLedger_TemplatedReportTest extends BaseTest {

	private LedgerPage ledgerPage;
	private ReportsWriter reportsWriter;
	private String ledgerName;
	private List<String> ledgerPageList;
	private List<String> reportWriterList;

	@BeforeClass
	public void integrationSetup() {
		System.out.println("=== Starting Ledger Create/Delete Integration Test Setup ===");

		LoginPage loginPage = new LoginPage(driver);
		ledgerPage = new LedgerPage(driver);
		reportsWriter = new ReportsWriter(driver);

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
		System.out.println("=== Ledger Create/Delete Integration Test Setup Complete ===\n");
	}

	@Test(priority = 1,
			description = "Verify newly created Ledger appears in Report Writer ledger dropdown")
	public void testCreatedLedgerAppearsInReportWriter() {
		System.out.println("\n[TEST 1] Creating ledger and verifying it appears in Report Writer...");

		// Create a new ledger
		ledgerPage.navigateToLedgerPage();
		Assert.assertTrue(ledgerPage.isAddLedgerBtnDisplayed(),
				"Add Ledger button should be displayed");

		ledgerPage.createLedger(ledgerName);
		String successMsg = ledgerPage.getSuccessMessage();
		Assert.assertTrue(successMsg.contains("Ledger was successfully created."),
				"Success message should confirm ledger creation");

		ledgerPage.waitForSuccessMessageToDisappear();

		// Get ledger values from the Ledger table
		ledgerPageList = reportsWriter.getLedgerTableValues();
		System.out.println("Ledger Page values: " + ledgerPageList);

		// Navigate to Report Writer and compare ledger dropdown
		reportsWriter.navigateToReportWriter();
		String heading = reportsWriter.getReportHeading();
		Assert.assertTrue(heading.contains("Reports"),
				"Report heading should be displayed");

		reportWriterList = reportsWriter.getLedgerDropdownValues();
		System.out.println("Report Writer values: " + reportWriterList);

		Assert.assertEquals(ledgerPageList, reportWriterList,
				"Ledger values should match between Ledger Page and Report Writer dropdown");

		System.out.println("[TEST 1] Created ledger verified in Report Writer successfully");
	}

	@Test(priority = 2, dependsOnMethods = {"testCreatedLedgerAppearsInReportWriter"},
			description = "Verify deleted Ledger is removed from Report Writer ledger dropdown")
	public void testDeletedLedgerRemovedFromReportWriter() {
		System.out.println("\n[TEST 2] Deleting Ledger: " + ledgerName);

		// Delete the ledger
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

		// Get updated ledger values from the Ledger table
		ledgerPageList = reportsWriter.getLedgerTableValues();
		System.out.println("Ledger Page values after delete: " + ledgerPageList);

		// Navigate to Report Writer and compare ledger dropdown
		reportsWriter.navigateToReportWriter();
		reportWriterList = reportsWriter.getLedgerDropdownValues();
		System.out.println("Report Writer values after delete: " + reportWriterList);

		Assert.assertEquals(ledgerPageList, reportWriterList,
				"Ledger values should match after deletion");

		System.out.println("[TEST 2] Deleted ledger removed from Report Writer successfully");
	}
}

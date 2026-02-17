package com.mondial.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Reports Writer Page Object
 * Represents the Report Writer page and report generation operations
 */
public class ReportsWriter extends BasePage {

	@FindBy(xpath = "//span[contains(text(), 'Report')]")
	private WebElement reportMainMenu;

	@FindBy(xpath = "//li//span[contains(text(), 'Report Writer')]")
	private WebElement reportWriterMenu;

	@FindBy(xpath = "//li//a[contains(text(), 'Reports')][@class='nav-link']")
	private WebElement reportsMenu;

	@FindBy(xpath = "//li//a[contains(text(), 'Generated Report Files')]")
	private WebElement generatedReportFiles;

	@FindBy(xpath = "//h4[contains(text(), 'Reports')]")
	private WebElement reportHeading;

	@FindBy(xpath = "//h4[contains(text(), 'Generated Report')]")
	private WebElement generatedReportHeading;

	@FindBy(xpath = "//select[@id='crx_report_company_id']")
	private WebElement companyDD;

	@FindBy(xpath = "//select[@id='crx_report_company_id']//option")
	private List<WebElement> companyDDOptions;

	@FindBy(xpath = "//div[@id='crx_report_id_chosen']//a")
	private WebElement reportDD;

	@FindBy(xpath = "//div[@id='crx_report_id_chosen']//input")
	private WebElement reportTB;

	@FindBy(xpath = "//select[@id='crx_report_ledger_id']")
	private WebElement ledgerDD;

	@FindBy(xpath = "//select[@id='crx_report_reporting_set_id']")
	private WebElement reportingSetDD;

	@FindBy(xpath = "//select[@id='crx_report_reporting_currency_code']")
	private WebElement reportingCurrencyDD;

	@FindBy(xpath = "//select[@id='crx_report_budget_version_id']")
	private WebElement reportingBudgetDD;

	@FindBy(xpath = "//div[@id ='crx_report_accounting_period_id_chosen']//a")
	private WebElement accountingPeriodDD;

	@FindBy(xpath = "//input[@id='datetimepicker1']")
	private WebElement startDate;

	@FindBy(xpath = "//input[@id='datetimepicker2']")
	private WebElement endDate;

	@FindBy(xpath = "//select[@id='crx_report_ledger_id']//option")
	private List<WebElement> ledgerOptions;

	@FindBy(xpath = "//table//tbody//td[1]")
	private List<WebElement> ledgersTable;

	@FindBy(xpath = "//div[@id='report_fields_report_format_chosen']//a")
	private WebElement outputTypeDD;

	@FindBy(xpath = "//input[@id='generateReportButton2']")
	private WebElement generateReportButton;

	@FindBy(xpath = "//div[@class='header-center']")
	private WebElement reportHeader;

	@FindBy(xpath = "//div[@class='container']//a[contains(text(),'Add Report')]")
	private WebElement addReport;

	@FindBy(xpath = "//input[@id='crx_report_reference_identifier']")
	private WebElement newReportRef;

	@FindBy(xpath = "//input[@id='crx_report_name']")
	private WebElement newReportName;

	@FindBy(xpath = "//input[@id='crx_report_description']")
	private WebElement newReportDesc;

	@FindBy(xpath = "//select[@id='crx_report_row_format_id']")
	private WebElement newReportRowFormatDD;

	@FindBy(xpath = "//select[@id='crx_report_column_layout_id']")
	private WebElement newReportColumnLayoutDD;

	@FindBy(xpath = "//select[@id='crx_report_report_header_id']")
	private WebElement newReportHeaderDD;

	@FindBy(xpath = "//select[@id='crx_report_report_footer_id']")
	private WebElement newReportFooterDD;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement createBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	// Constructor
	public ReportsWriter(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Reports > Report Writer menu
	 */
	public void navigateToReportWriter() {
		clickElement(reportMainMenu);
		wait.until(ExpectedConditions.visibilityOf(reportWriterMenu));
		clickElement(reportWriterMenu);
		wait.until(ExpectedConditions.visibilityOf(reportsMenu));
		clickElement(reportsMenu);
		wait.until(ExpectedConditions.visibilityOf(reportHeading));
	}

	/**
	 * Click the Add Report button
	 */
	public void clickAddReport() {
		clickElement(addReport);
	}

	/**
	 * Click the Generated Report Files menu link
	 */
	public void clickGeneratedReportFiles() {
		clickElement(generatedReportFiles);
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Generate a report with the given parameters
	 * @param reportType - Report type to select from dropdown
	 * @param companyName - Company name to select
	 */
	public void generateReport(String reportType, String companyName) {
		clickElement(reportDD);
		reportTB.sendKeys(reportType);
		reportTB.sendKeys(Keys.ENTER);
		new Select(companyDD).selectByVisibleText(companyName);
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(reportingSetDD));
		new Select(ledgerDD).selectByIndex(2);
		new Select(reportingSetDD).selectByIndex(2);
		new Select(reportingCurrencyDD).selectByIndex(2);
		new Select(reportingBudgetDD).selectByIndex(1);
		startDate.sendKeys("01/01/2020");
		endDate.sendKeys("01/31/2020");
		clickElement(generateReportButton);
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Get ledger dropdown values from the templated report page (sorted)
	 * @return Sorted list of ledger names
	 */
	public List<String> getLedgerDropdownValues() {
		List<String> dropdownValues = new ArrayList<>();
		for (WebElement option : ledgerOptions) {
			dropdownValues.add(option.getAttribute("innerText"));
		}
		Collections.sort(dropdownValues);
		return dropdownValues;
	}

	/**
	 * Get ledger table values from the Ledgers page (sorted)
	 * @return Sorted list of ledger names from the table
	 */
	public List<String> getLedgerTableValues() {
		List<String> tableValues = new ArrayList<>();
		for (WebElement option : ledgersTable) {
			tableValues.add(option.getAttribute("innerText"));
		}
		Collections.sort(tableValues);
		return tableValues;
	}

	/**
	 * Check if a value is present in a Select dropdown
	 * @param name - Value to search for
	 * @param ddElement - The Select WebElement to check
	 * @return true if value is present, false otherwise
	 */
	public boolean isValueInDropdown(String name, WebElement ddElement) {
		Select dropdown = new Select(ddElement);
		for (WebElement option : dropdown.getOptions()) {
			if (option.getText().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the report heading text
	 * @return Report heading text
	 */
	public String getReportHeading() {
		wait.until(ExpectedConditions.visibilityOf(reportHeading));
		return reportHeading.getAttribute("innerText");
	}

	/**
	 * Get the generated report heading text
	 * @return Generated report heading text
	 */
	public String getGeneratedReportHeading() {
		wait.until(ExpectedConditions.visibilityOf(generatedReportHeading));
		return generatedReportHeading.getAttribute("innerText");
	}

	/**
	 * Wait for the generated report page to load in a new window
	 * @param parentWindow - Handle of the parent window
	 */
	public void waitForGeneratedReport(String parentWindow) {
		switchToNewWindow(parentWindow);
		wait.until(ExpectedConditions.visibilityOf(generatedReportHeading));
	}

	/**
	 * Get the Row Format dropdown element (for external validation)
	 * @return Row Format dropdown WebElement
	 */
	public WebElement getRowFormatDD() {
		return newReportRowFormatDD;
	}

	/**
	 * Get the Column Layout dropdown element (for external validation)
	 * @return Column Layout dropdown WebElement
	 */
	public WebElement getColumnLayoutDD() {
		return newReportColumnLayoutDD;
	}

	/**
	 * Get the Report Header dropdown element (for external validation)
	 * @return Report Header dropdown WebElement
	 */
	public WebElement getReportHeaderDD() {
		return newReportHeaderDD;
	}

	/**
	 * Get the Report Footer dropdown element (for external validation)
	 * @return Report Footer dropdown WebElement
	 */
	public WebElement getReportFooterDD() {
		return newReportFooterDD;
	}

	// ============================================
	// HELPER METHODS
	// ============================================

	/**
	 * Switch to a newly opened window (not the parent)
	 * @param parentWindow - Handle of the parent window
	 */
	private void switchToNewWindow(String parentWindow) {
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			if (!handle.equals(parentWindow)) {
				driver.switchTo().window(handle);
				return;
			}
		}
	}
}

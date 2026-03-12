package com.mondial.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	@FindBy(xpath = "//*[@id='generateReportButton2']")
	private WebElement generateReportButton;

	@FindBy(xpath = "//div[@class='header-center']")
	private WebElement reportHeader;

	@FindBy(xpath = "//div[@class='header-left']")
	private WebElement reportHeaderLeft;

	@FindBy(xpath = "//div[@class='header-right']")
	private WebElement reportHeaderRight;

	@FindBy(xpath = "//table//tbody//tr")
	private List<WebElement> reportDataRows;

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

	private String lastSelectedCurrency;
	private String lastSelectedLedger;

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
		wait.until(ExpectedConditions.elementToBeClickable(companyDD));
	}

	/**
	 * Click the Add Report button
	 */
	public void clickAddReport() {
		clickElement(addReport);
	}

	/**
	 * Click Add Report and wait for the new report form to be ready
	 */
	public void openAddReportForm() {
		clickElement(addReport);
		wait.until(ExpectedConditions.visibilityOf(cancelBtn));
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
		Select ledgerSelect = new Select(ledgerDD);
		ledgerSelect.selectByVisibleText("Default");
		lastSelectedLedger = ledgerSelect.getFirstSelectedOption().getText();
		new Select(reportingSetDD).selectByVisibleText("OECC");
		Select currencySelect = new Select(reportingCurrencyDD);
		currencySelect.selectByIndex(1);
		lastSelectedCurrency = currencySelect.getFirstSelectedOption().getText();
		//new Select(reportingBudgetDD).selectByIndex(1);
		startDate.sendKeys("04/01/2018");
		endDate.sendKeys("03/31/2019");
		clickElement(generateReportButton);
	}

	/**
	 * Refresh the page and wait for the generate form to be ready.
	 * Use this between tests instead of re-navigating through the menu.
	 */
	public void refreshAndWaitForForm() {
		driver.navigate().refresh();
		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
			.until(ExpectedConditions.elementToBeClickable(companyDD));
	}

	/**
	 * Fill the Report (Chosen dropdown) field only.
	 * Waits for the dropdown to close, confirming the selection was registered.
	 */
	public void fillReport(String reportType) {
		wait.until(ExpectedConditions.elementToBeClickable(reportDD));
		clickElement(reportDD);
		wait.until(ExpectedConditions.visibilityOf(reportTB));
		reportTB.sendKeys(reportType);
		reportTB.sendKeys(Keys.ENTER);
		// Wait for Chosen dropdown to close — confirmed by absence of 'chosen-with-drop' class
		wait.until(ExpectedConditions.not(
			ExpectedConditions.attributeContains(
				org.openqa.selenium.By.id("crx_report_id_chosen"), "class", "chosen-with-drop")));
	}

	/**
	 * Select the Company dropdown without waiting for dependent fields.
	 * Use this when dependent dropdowns (Reporting Set, Currency) are not needed,
	 * e.g. when testing that Report is the first mandatory field.
	 */
	public void fillCompanyOnly(String companyName) {
		wait.until(ExpectedConditions.elementToBeClickable(companyDD));
		new Select(companyDD).selectByVisibleText(companyName);
		waitForPageLoad();
	}

	/**
	 * Fill the Company dropdown and wait for dependent fields to load.
	 */
	public void fillCompanyAndWait(String companyName) {
		wait.until(ExpectedConditions.elementToBeClickable(companyDD));
		new Select(companyDD).selectByVisibleText(companyName);
		waitForPageLoad();
		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
			.until(ExpectedConditions.elementToBeClickable(reportingSetDD));
	}

	/**
	 * Fill the Ledger dropdown.
	 */
	public void fillLedger(String ledgerName) {
		wait.until(ExpectedConditions.elementToBeClickable(ledgerDD));
		Select ledgerSelect = new Select(ledgerDD);
		ledgerSelect.selectByVisibleText(ledgerName);
		lastSelectedLedger = ledgerSelect.getFirstSelectedOption().getText();
	}

	/**
	 * Fill the Reporting Set dropdown.
	 */
	public void fillReportingSet(String reportingSet) {
		wait.until(ExpectedConditions.elementToBeClickable(reportingSetDD));
		new Select(reportingSetDD).selectByVisibleText(reportingSet);
	}

	/**
	 * Fill the Reporting Currency dropdown by visible text.
	 */
	public void fillCurrency(String currency) {
		wait.until(ExpectedConditions.elementToBeClickable(reportingCurrencyDD));
		Select currencySelect = new Select(reportingCurrencyDD);
		currencySelect.selectByVisibleText(currency);
		lastSelectedCurrency = currencySelect.getFirstSelectedOption().getText();
	}

	/**
	 * Fill the Start Date field.
	 */
	public void fillStartDate(String date) {
		wait.until(ExpectedConditions.elementToBeClickable(startDate));
		startDate.clear();
		startDate.sendKeys(date);
	}

	/**
	 * Fill the End Date field.
	 */
	public void fillEndDate(String date) {
		wait.until(ExpectedConditions.elementToBeClickable(endDate));
		endDate.clear();
		endDate.sendKeys(date);
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
	 * Switch to the new tab that opens directly after clicking Generate Report.
	 * Waits up to 60 seconds for the new tab to appear, then switches to it.
	 * @param parentWindow - Handle of the parent window captured before generateReport()
	 */
	public void switchToGeneratedReportTab(String parentWindow) {
		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(60))
			.until(ExpectedConditions.numberOfWindowsToBe(2));
		switchToNewWindow(parentWindow);
		waitForPageLoad();
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

	/**
	 * Check if the Add Report button is displayed (Reports listing page ready)
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddReportBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addReport));
			return addReport.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Click the Generate Report button without filling any fields.
	 * Used to trigger the browser popup validation error for mandatory fields.
	 */
	public void clickGenerateWithoutFillingFields() {
		clickElement(generateReportButton);
	}

	/**
	 * Check whether a native browser alert popup is present after clicking Generate.
	 * @return true if a browser alert is present, false otherwise
	 */
	public boolean isBrowserAlertPresent() {
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the text from the native browser alert popup, then dismiss it.
	 * @return alert text, or empty string if no alert is present
	 */
	public String getBrowserAlertTextAndDismiss() {
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			org.openqa.selenium.Alert alert = driver.switchTo().alert();
			String text = alert.getText();
			alert.accept();
			return text;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * View the first generated report after clicking Generate.
	 * Waits for the Generated Report Files heading, then clicks the first
	 * report link and switches to the new window it opens.
	 */
	public void viewGeneratedReport() {
		// After generateReport(), the Reports menu may be collapsed. Expand it if needed.
		waitForPageLoad();
		if (!generatedReportFiles.isDisplayed()) {
			clickElement(reportMainMenu);
			wait.until(ExpectedConditions.visibilityOf(reportWriterMenu));
			clickElement(reportWriterMenu);
		}
		wait.until(ExpectedConditions.elementToBeClickable(generatedReportFiles));
		clickElement(generatedReportFiles);
		waitForPageLoad();
		String parentWindow = driver.getWindowHandle();
		// Use a longer timeout to allow async report generation to complete
		org.openqa.selenium.WebElement firstLink = new org.openqa.selenium.support.ui.WebDriverWait(
				driver, java.time.Duration.ofSeconds(60)).until(
			ExpectedConditions.elementToBeClickable(
				org.openqa.selenium.By.xpath(
					"//table//tbody//tr[1]//td[1]//a")));
		firstLink.click();
		switchToNewWindow(parentWindow);
		waitForPageLoad();
	}

	/**
	 * Get the report header centre text (contains report type and company name).
	 * @return Report header text
	 */
	public String getReportHeaderContent() {
		wait.until(ExpectedConditions.visibilityOf(reportHeader));
		return reportHeader.getAttribute("innerText");
	}

	/**
	 * Check whether the report left header (logo area) is visible on the page.
	 * @return true if visible, false otherwise
	 */
	public boolean isReportLogoDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(reportHeaderLeft));
			return reportHeaderLeft.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the report header right section text (typically contains page info or currency).
	 * @return Report header right text, or empty string if not present
	 */
	public String getReportRightHeaderContent() {
		try {
			wait.until(ExpectedConditions.visibilityOf(reportHeaderRight));
			return reportHeaderRight.getAttribute("innerText");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Check whether the generated report has rendered at least one data row in its table.
	 * @return true if one or more table rows are present, false otherwise
	 */
	public boolean isReportDataPresent() {
		try {
			new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
				.until(ExpectedConditions.visibilityOfAllElements(reportDataRows));
			return !reportDataRows.isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the current browser page title of the report tab.
	 * @return Page title string
	 */
	public String getReportPageTitle() {
		return driver.getTitle();
	}

	/**
	 * Get the currency that was selected when generateReport() was last called.
	 * @return Currency text as displayed in the dropdown
	 */
	public String getLastSelectedCurrency() {
		return lastSelectedCurrency;
	}

	/**
	 * Get the ledger that was selected when generateReport() was last called.
	 * @return Ledger text as displayed in the dropdown
	 */
	public String getLastSelectedLedger() {
		return lastSelectedLedger;
	}

	// ============================================
	// HELPER METHODS
	// ============================================

	/**
	 * Switch to a newly opened window (not the parent)
	 * @param parentWindow - Handle of the parent window
	 */
	private void switchToNewWindow(String parentWindow) {
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(parentWindow)) {
				driver.switchTo().window(handle);
				return;
			}
		}
		throw new RuntimeException("New window did not open within the expected time.");
	}
}

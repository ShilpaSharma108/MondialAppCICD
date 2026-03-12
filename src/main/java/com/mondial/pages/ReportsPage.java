package com.mondial.pages;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ReportsPage extends BasePage {

	private JavascriptExecutor jse;
	private WebDriverWait longWait;
	private static final Random RANDOM = new Random();
	public static final String RESET = "\u001B[0m";
	public static final String PrintColour = "\u001B[35m";

	public ReportsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		jse = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(100));
		longWait = new WebDriverWait(driver, Duration.ofMinutes(5));
	}

	@FindBy(xpath = "//span[contains(text(), 'Report')]")
	public WebElement reportMenu;

	@FindBy(xpath = "//li//a[contains(text(), 'Templated')]")
	public WebElement templateReport;

	@FindBy(xpath = "//li//span[contains(text(), 'Report Writer')]")
	public WebElement reportWriter;

	@FindBy(xpath = "//li//a[contains(text(), 'Generated Report Files')]")
	public WebElement generatedReportFiles;

	@FindBy(xpath = "//h4[contains(text(), 'Templated Report')]")
	public WebElement templatedReportHeading;

	@FindBy(xpath = "//select[@id='report_company_id']")
	public WebElement companyDD;

	@FindBy(xpath = "//select[@id='report_company_id']//option")
	public List<WebElement> companyDDOptions;

	@FindBy(xpath = "//div[@id='report-options']//a")
	public WebElement reportDD;

	@FindBy(xpath = "//div//a[contains(*, 'Transaction History')]")
	public WebElement transactionHistory;

	@FindBy(xpath = "//select[@id='report_fields_currency_code']")
	public WebElement currencyDropDown;

	@FindBy(xpath = "//div[@id ='report_fields_accounting_period_id_chosen']//a")
	public WebElement accountingPeriodDD;

	@FindBy(xpath = "//div[@id ='report_fields_accounting_period_id_chosen']//li")
	public List<WebElement> accountingPeriodList;

	@FindBy(xpath = "//div[@id='report_fields_currency_code_chosen']//a")
	public WebElement reportingCurrencyDD;

	@FindBy(xpath = "//div[@id='report_fields_currency_code_chosen']//input")
	public WebElement inputCurrency;

	@FindBy(xpath = "//div[@id='report_fields_natural_account_set_id_chosen']//a")
	public WebElement naturalAccountSetDD;

	@FindBy(xpath = "//div[@id='report_fields_natural_account_set_id_chosen']//input")
	public WebElement inputNaturalACSet;

	@FindBy(xpath = "//select[@id='report_fields_ledger_id']//option")
	public List<WebElement> ledgerValues;

	@FindBy(xpath = "//table//tbody//td[1]")
	public List<WebElement> ledgersTable;

	@FindBy(xpath = "//div[@id='report_fields_ledger_id_chosen']//a")
	public WebElement ledgerDD;

	@FindBy(xpath = "//div[@id='report_fields_ledger_id_chosen']//input")
	public WebElement inputLedger;

	@FindBy(xpath = "//div[@id='report_fields_report_format_chosen']//a")
	public WebElement outputTypeDD;

	@FindBy(xpath = "//input[@id='generateReport']")
	public WebElement generateButton;

	@FindBy(xpath = "//div[@id='report_company_id_chosen']//input[@class='chosen-search-input']")
	public WebElement inputCName;

	@FindBy(xpath = "//div[@id='report-options']//input[@class='chosen-search-input']")
	public WebElement inputRName;

	@FindBy(xpath = "//div[@id ='report_fields_accounting_period_id_chosen']//input[@class='chosen-search-input']")
	public WebElement inputAPName;

	@FindBy(xpath = "//div[@id='report_fields_report_format_chosen']//input[@class='chosen-search-input']")
	public WebElement inputOutputType;

	@FindBy(xpath = "//div//h3")
	public WebElement heading;

	@FindBy(xpath = "//div//h4")
	public WebElement showACTxn;

	@FindBy(xpath = "//input[@id='downloadButton']")
	public WebElement downloadBtn;

	@FindBy(xpath = "//input[@name='report_fields[accounting_period_start_date]']")
	public WebElement startDate;

	@FindBy(xpath = "//input[@name='report_fields[accounting_period_end_date]']")
	public WebElement endDate;

	@FindBy(xpath = "//div[@ref='centerContainer'][@role='presentation']//*[@ref='eContainer']//div[@role='row']")
	public List<WebElement> txnTable;

	@FindBy(xpath = "//div[@ref='eContainer']//div[@row-index='0']")
	public WebElement firstRecord;

	@FindBy(xpath = "//span[@ref='eText'][contains(text(),'Closing')] | //span[@ref='eText'][contains(text(),'Balance')]")
	public WebElement sortTableCB;

	@FindBy(xpath = "//div[@row-id='0']//div[@col-id='balance']")
	public WebElement balance;

	@FindBy(xpath = "//div[contains(@col-id,'balance')]")
	public List<WebElement> balanceTable;

	@FindBy(xpath = "//table[@class='table table-bordered table-sm']//tbody//td[1]")
	public WebElement txnId;

	@FindBy(xpath = "//input[contains(@name,'regular_journal')][@type='checkbox']")
	public WebElement regularJournalEntryCB;

	@FindBy(xpath = "//input[contains(@name,'statutory')][@type='checkbox']")
	public WebElement statutoryAdjustment;

	@FindBy(xpath = "//input[contains(@name,'revaluation')][@type='checkbox']")
	public WebElement revaluationAdjustment;

	@FindBy(xpath = "//input[contains(@name,'elimination')][@type='checkbox']")
	public WebElement eleminationEntries;

	@FindBy(xpath = "//input[contains(@name,'translation')][@type='checkbox']")
	public WebElement translationAdjustments;

	@FindBy(xpath = "//table//tbody//td[1]")
	public WebElement txnID_ShowingACTxn;

	@FindBy(xpath = "//table//tbody//td[2]")
	public WebElement externalRxId_ShowingACTxn;

	@FindBy(xpath = "//table//tbody//td[3]")
	public WebElement type_ShowingACTxn;

	@FindBy(xpath = "//table//tbody//td[4]")
	public WebElement postedDate_ShowingACTxn;

	@FindBy(xpath = "//table//tbody//td[5]")
	public WebElement txnDate_ShowingACTxn;

	@FindBy(xpath = "//table//tbody//td[6]")
	public WebElement description_ShowingACTxn;

	@FindBy(xpath = "//div[@col-id='reversed'][@role='gridcell']")
	public WebElement reverseLink;

	@FindBy(xpath = "//button[@id='postReversal']")
	public WebElement postReverseBtn;

	@FindBy(xpath = "//h5[@id='modalTitle']")
	public WebElement reverseTransacrionModel;

	@FindBy(xpath = "//i[@class='ti-close']")
	public WebElement closeModel;

	@FindBy(xpath = "//div[@class='col-sm-2']")
	public WebElement txnTypeCB;

	// Methods
	public void navigateToTemplatedReport() {
		reportMenu.click();
		templateReport.click();
	}

	public void generateTxnHistory(String companyName, String reportType) throws InterruptedException {
		selectCompany(companyName);
		waitForPageLoad();
		selectReport(reportType);
		waitForPageLoad();
		selectOutputType("Screen");
		waitForPageLoad();
		selectDatesJS("11/11/2020", "12/12/2020");
		generateButton.click();
		wait.until(ExpectedConditions.visibilityOf(heading));
		Assert.assertTrue(heading.getAttribute("innerText").contains(reportType));
		longWait.until(ExpectedConditions.visibilityOf(firstRecord));
	}

	@SuppressWarnings("null")
	public ArrayList<String> selectRecord() throws InterruptedException {
		waitForPageLoad();
		ArrayList<String> ar = new ArrayList<String>();
		List<WebElement> tablebody = driver.findElements(By.xpath("//div[@col-id='id']//parent::div[@role='row']"));
		int tableSize = tablebody.size();
		int selectIndex = RANDOM.nextInt(tableSize);
		if (selectIndex == 0)
			selectIndex = 1;
		String txnID = tablebody.get(selectIndex).getAttribute("innerText").substring(14, 18);
		System.out.println("\n\033[0;1m" + PrintColour + "Table Size: " + txnTable.size() + RESET);
		System.out.println("\n\033[0;1m" + PrintColour + "Table Row to view the balance: " + selectIndex + RESET);
		WebElement linkToClick = driver.findElement(By.xpath("//div[contains(.,'" + txnID + "')][@role='gridcell']"));
		String externalTxnID = driver
				.findElement(By.xpath("//div[contains(.,'" + txnID + "')][@role='gridcell']//following-sibling::div[3]"))
				.getAttribute("innerText");
		String type = driver
				.findElement(By.xpath("//div[contains(.,'" + txnID + "')][@role='gridcell']//following-sibling::div[6]"))
				.getAttribute("innerText");
		String postedDate = driver
				.findElement(By.xpath("//div[contains(.,'" + txnID + "')][@role='gridcell']//following-sibling::div[4]"))
				.getAttribute("innerText");
		String transactionDate = driver
				.findElement(By.xpath("//div[contains(.,'" + txnID + "')][@role='gridcell']//following-sibling::div[5]"))
				.getAttribute("innerText");
		String description = driver
				.findElement(By.xpath("//div[contains(.,'" + txnID + "')][@role='gridcell']//following-sibling::div[7]"))
				.getAttribute("innerText");
		ar.add(txnID);
		ar.add(externalTxnID);
		ar.add(type);
		ar.add(postedDate);
		ar.add(transactionDate);
		ar.add(description);
		linkToClick.click();
		return ar;
	}

	public void verifyAccountingTxnAmt() {
		List<WebElement> list1 = driver
				.findElements(By.xpath("//thead//tr[contains(text(), DR)][2]//ancestor::table//tbody//td[3]"));
		List<WebElement> list2 = driver
				.findElements(By.xpath("//thead//tr[contains(text(), DR)][2]//ancestor::table//tbody//td[4]"));
		Double[] debit = new Double[list1.size()];
		for (int i = 0; i < list1.size(); i++) {
			if (!list1.get(i).getAttribute("innerText").isBlank()) {
				debit[i] = Double.parseDouble(list1.get(i).getText().replaceAll(",", ""));
			}
		}
		double debitTotal = debitSum(debit);
		Double[] credit = new Double[list2.size()];
		for (int i = 0; i < list2.size(); i++) {
			if (!list2.get(i).getAttribute("innerText").isBlank()) {
				credit[i] = Double.parseDouble(list2.get(i).getText().replaceAll(",", ""));
			}
		}
		double creditTotal = creditSum(credit);
		Assert.assertEquals(creditTotal, debitTotal);
	}

	public static double debitSum(Double[] debit) {
		double result = 0;
		for (int i = 0; i < debit.length; i++) {
			if (debit[i] != null)
				result = result + debit[i];
		}
		System.out.println("\nTotal of Debit: " + result);
		return result;
	}

	public static double creditSum(Double[] credit) {
		double result = 0;
		for (int i = 0; i < credit.length; i++) {
			if (credit[i] != null)
				result = result + credit[i];
		}
		System.out.println("\nTotal of Credit: " + result);
		return result;
	}

	public String verifyAccountPeriodField(String companyName, String reportType) throws InterruptedException {
		selectCompany(companyName);
		waitForPageLoad();
		selectReport(reportType);
		waitForPageLoad();
		selectOutputType("Screen");
		waitForPageLoad();
		accountingPeriodDD.click();
		accountingPeriodList.get(1).click();
		String date = accountingPeriodList.get(1).getAttribute("innerText");
		waitForPageLoad();
		generateButton.click();
		wait.until(ExpectedConditions.visibilityOf(heading));
		Assert.assertTrue(heading.getAttribute("innerText").contains(reportType));
		longWait.until(ExpectedConditions.visibilityOf(firstRecord));
		return date;
	}

	public void selectRequiredValues(String companyName, String reportType) throws InterruptedException {
		selectCompany(companyName);
		waitForPageLoad();
		selectReport(reportType);
		waitForPageLoad();
		selectCurrency("USD");
		waitForPageLoad();
		selectNaturalAccountSets("Local");
		waitForPageLoad();
		selectLedger("Default");
		waitForPageLoad();
		waitForPageLoad();
		selectOutputType("Screen");
		selectDatesJS("11/11/2024", "12/12/2024");
	}

	public void selectDates() {
		wait.until(ExpectedConditions.elementToBeClickable(startDate));
		startDate.click();
		startDate.clear();
		wait.until(ExpectedConditions.elementToBeClickable(startDate));
		startDate.sendKeys(currentDateMinus15());
		wait.until(ExpectedConditions.elementToBeClickable(endDate));
		endDate.click();
		endDate.clear();
		wait.until(ExpectedConditions.elementToBeClickable(endDate));
		endDate.sendKeys(currentDatePlus15());
		waitForPageLoad();
	}

	public void selectDatesJS(String startD, String endD) {
		wait.until(ExpectedConditions.elementToBeClickable(startDate));
		startDate.click();
		startDate.clear();
		jse.executeScript("arguments[0].value = arguments[1];", startDate, startD);
		wait.until(ExpectedConditions.elementToBeClickable(endDate));
		endDate.click();
		endDate.clear();
		jse.executeScript("arguments[0].value = arguments[1];", endDate, endD);
		waitForPageLoad();
	}

	public void generateReport(String reportType) {
		jse.executeScript("arguments[0].click();", generateButton);
		wait.until((ExpectedCondition<Boolean>) wd ->
				((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
		Assert.assertTrue(heading.getAttribute("innerText").contains(reportType));
		if (reportType.equals("Detailed Trial Balance"))
			longWait.until(ExpectedConditions.visibilityOf(firstRecord));
	}

	public void selectCompany(String cName) {
		WebElement dropdownLink = driver.findElement(By.xpath("//a[@class='chosen-single']"));
		Actions action = new Actions(driver);
		action.moveToElement(dropdownLink).perform();
		inputCName.sendKeys(cName);
		inputCName.sendKeys(Keys.ENTER);
	}

	public void selectReport(String rName) throws InterruptedException {
		reportDD.click();
		inputRName.sendKeys(rName);
		inputRName.sendKeys(Keys.ENTER);
	}

	public void selectAccoutingPeriod(String ap) {
		accountingPeriodDD.click();
		inputAPName.sendKeys(ap);
		inputAPName.sendKeys(Keys.ENTER);
	}

	public void selectCurrency(String currency) {
		boolean isPresent = driver.findElements(By.xpath("//div[@id='report_fields_currency_code_chosen']//a"))
				.size() > 0;
		if (isPresent) {
			reportingCurrencyDD.click();
			inputCurrency.sendKeys(currency);
			inputCurrency.sendKeys(Keys.ENTER);
		}
	}

	public void selectNaturalAccountSets(String accountSet) {
		boolean isPresent = driver.findElements(By.xpath("//div[@id='report_fields_natural_account_set_id_chosen']//a"))
				.size() > 0;
		if (isPresent) {
			naturalAccountSetDD.click();
			inputNaturalACSet.sendKeys(accountSet);
			inputNaturalACSet.sendKeys(Keys.ENTER);
		}
	}

	public void selectLedger(String ledger) {
		boolean isPresent = driver.findElements(By.xpath("//div[@id='report_fields_ledger_id_chosen']//a")).size() > 0;
		if (isPresent) {
			ledgerDD.click();
			inputLedger.sendKeys(ledger);
			inputLedger.sendKeys(Keys.ENTER);
		}
	}

	public void selectOutputType(String type) {
		outputTypeDD.click();
		inputOutputType.sendKeys(type);
		inputOutputType.sendKeys(Keys.ENTER);
	}

	public boolean verifyRecordPresent(String txnId) {
		boolean isPresent = false;
		for (int i = 0; i < txnTable.size(); i++) {
			if (txnTable.get(i).getAttribute("innerText").contains(txnId)) {
				System.out.println("Record Present !!");
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	public boolean verifyDateInResult(String date) {
		boolean isPresent = false;
		for (int i = 0; i < txnTable.size(); i++) {
			String temp = txnTable.get(i).getAttribute("innerText");
			System.out.println(temp);
			if (txnTable.get(i).getAttribute("innerText").contains(date)) {
				System.out.println("Correct Record Present!!");
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	public String viewBalanceDetails() throws InterruptedException {
		waitForPageLoad();
		sortTableCB.click();
		sortTableCB.click();
		System.out.println(txnTable.size());
		Thread.sleep(1000);
		WebElement path = driver.findElement(By.xpath(
				"//div[@row-index='0']//div[@*= 'closing_balance'] | //div[@row-index='0']//div[@*= 'balance'] | //div[@row-index='0']//div[contains(@col-id, 'adjusted_trial_balance')]"));
		String balance = path.getAttribute("innerText");
		path.click();
		System.out.println("Balance Details: " + balance);
		return balance;
	}

	public String getBalanceDetails() throws InterruptedException {
		waitForPageLoad();
		List<WebElement> tablebody = driver
				.findElements(By.xpath("//div[@ref='eBodyViewport']//div[@col-id='balance']|//div[@ref='eBodyViewport']//div[@col-id='closing_balance']|//div[@ref='eBodyViewport']//div[@col-id='adjusted_trial_balance']"));
		int tableSize = tablebody.size() - 1;
		int selectIndex = RANDOM.nextInt(tableSize);
		System.out.println("\n\033[0;1m" + PrintColour + "Table Size: " + txnTable.size() + RESET);
		System.out.println("\n\033[0;1m" + PrintColour + "Table Row to view the balance: " + selectIndex + RESET);
		WebElement path = driver.findElement(
				By.xpath("//div[@row-index='" + selectIndex + "']//div[@*= 'closing_balance'] | //div[@row-index='"
						+ selectIndex + "']//div[@*= 'balance'] | //div[@row-index='" + selectIndex
						+ "']//div[contains(@col-id, 'adjusted_trial_balance')]"));
		String balance = path.getAttribute("innerText");
		path.click();
		return balance;
	}

	public String getBalanceAmount() {
		longWait.until(ExpectedConditions.visibilityOf(firstRecord));
		int getIndex = balanceTable.size() - 1;
		return balanceTable.get(getIndex).getAttribute("innerText");
	}

	public void checkTxnType(String txnType) {
		Actions action = new Actions(driver);
		List<WebElement> chkBx = driver
				.findElements(By.xpath("//input[@type='checkbox'][contains(@name,'accounting')]"));
		for (int i = 0; i <= chkBx.size() - 1; i++) {
			if (chkBx.get(i).isSelected()) {
				action.moveToElement(chkBx.get(i)).click().perform();
			}
		}
		waitForPageLoad();
		for (int i = 0; i <= chkBx.size() - 1; i++) {
			if (chkBx.get(i).getAttribute("name").contains(txnType))
				action.moveToElement(chkBx.get(i)).click().perform();
		}
	}

	public void selectTxnType(String txnType) {
		Actions action = new Actions(driver);
		WebElement label;
		if (txnType.equals("Source System"))
			label = driver.findElement(
					By.xpath("//div[@class='col-sm-2']//label[.//span[contains(text(),'" + txnType + "')]]"));
		else
			label = driver.findElement(
					By.xpath("//div[@class='col-sm-2']//label[contains(text(),'" + txnType + "')]"));

		// Resolve the actual checkbox input — either nested inside label or linked via 'for'
		WebElement checkbox;
		List<WebElement> inner = label.findElements(By.xpath(".//input[@type='checkbox']"));
		if (!inner.isEmpty()) {
			checkbox = inner.get(0);
		} else {
			checkbox = driver.findElement(By.id(label.getAttribute("for")));
		}

		if (!checkbox.isSelected())
			action.moveToElement(checkbox).click().perform();
		waitForPageLoad();
	}

	public void deSelectTxnType() {
		Actions action = new Actions(driver);
		List<WebElement> chkBx = driver
				.findElements(By.xpath("//input[@type='checkbox'][contains(@name,'accounting')]"));
		for (int i = 0; i <= chkBx.size() - 1; i++) {
			if (chkBx.get(i).isSelected()) {
				action.moveToElement(chkBx.get(i)).click().perform();
			}
		}
	}

	public String getTxnTypeCode() {
		String urlPath = driver.getCurrentUrl();
		System.out.println(urlPath);
		String[] path = urlPath.split("type_codes");
		System.out.println("Updated URL path" + path[1]);
		return path[1];
	}

	public void verifyBalanceGeneration_Currencies(String currencyName, String reportType) throws InterruptedException {
		waitForPageLoad();
		selectCurrency(currencyName);
		generateReport(reportType);
		System.out.println("Balance generated successfully for " + currencyName);
	}

	public void verifyBalanceGeneration_NAS(String naturalAccount, String reportType) throws InterruptedException {
		waitForPageLoad();
		selectNaturalAccountSets(naturalAccount);
		generateReport(reportType);
		System.out.println("Balance generated successfully for " + naturalAccount);
	}

	public void verifyBalanceGeneration_Ledger(String ledgerName, String reportType) throws InterruptedException {
		waitForPageLoad();
		selectLedger(ledgerName);
		waitForPageLoad();
		generateReport(reportType);
		System.out.println("Balance generated successfully for " + ledgerName);
	}

	public ArrayList<String> getTxnTypes() {
		ArrayList<String> ar = new ArrayList<String>();
		ar.add("Source System");
		ar.add("Regular Journal Entries");
		ar.add("Statutory Adjustments");
		ar.add("Revaluation Adjustments");
		ar.add("Elimination Entries");
		ar.add("Translation Adjustments");
		return ar;
	}

	public void verifyLink(String txnCB) throws InterruptedException {
		String linkPath = getTxnTypeCode();
		if (txnCB.equals("Source System"))
			Assert.assertTrue(linkPath.contains("=JE%20SI%20PI%20PD%20PR%20RV%20AI%20ST%20TB%20&"));
		else if (txnCB.equals("Regular Journal Entries"))
			Assert.assertTrue(linkPath.contains("%20JEH%20JER%20&"));
		else if (txnCB.equals("Statutory Adjustments"))
			Assert.assertTrue(linkPath.contains("%20STA%20&"));
		else if (txnCB.equals("Revaluation Adjustments"))
			Assert.assertTrue(linkPath.contains("%20RVA%20&"));
		else if (txnCB.equals("Elimination Entries"))
			Assert.assertTrue(linkPath.contains("%20ELE%20&"));
		else if (txnCB.equals("Translation Adjustments"))
			Assert.assertTrue(linkPath.contains("%20CTA%20&"));
		else
			System.out.println("Error encountered!!!");
	}

	public List<String> ledgerIntegration() {
		List<String> dropdownValues = new ArrayList<>();
		for (WebElement option : ledgerValues) {
			dropdownValues.add(option.getAttribute("innerText"));
		}
		Collections.sort(dropdownValues);
		System.out.println(dropdownValues);
		return dropdownValues;
	}

	public List<String> ledgerTableValues() {
		List<String> tableValues = new ArrayList<>();
		for (WebElement option : ledgersTable) {
			tableValues.add(option.getAttribute("innerText"));
		}
		Collections.sort(tableValues);
		System.out.println(tableValues);
		return tableValues;
	}

	// ============================================
	// PUBLIC UTILITY METHODS (for test classes)
	// ============================================

	public void waitForLoad() {
		waitForPageLoad();
	}

	public void waitForVisible(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void switchToChildWindow(String parentWindow) {
		wait.until(d -> d.getWindowHandles().size() > 1);
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(parentWindow)) {
				driver.switchTo().window(handle);
				return;
			}
		}
		throw new RuntimeException("Child window did not open within the expected time.");
	}

	// ============================================
	// PRIVATE DATE HELPER METHODS
	// ============================================

	private String currentDateMinus15() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -15);
		return new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime());
	}

	private String currentDatePlus15() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 15);
		return new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime());
	}
}

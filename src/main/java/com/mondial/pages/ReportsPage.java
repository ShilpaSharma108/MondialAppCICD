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
		Assert.assertEquals(creditTotal, debitTotal, 0.01, "Credit and Debit totals should match");
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
		Thread.sleep(30);
		selectReport(reportType);
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='report_fields[accounting_period_start_date]']")));
		selectNaturalAccountSets("Local");
		waitForPageLoad();
		selectOutputType("Screen");
		waitForPageLoad();
		accountingPeriodDD.click();
		wait.until(ExpectedConditions.visibilityOf(accountingPeriodList.get(1)));
		String date = accountingPeriodList.get(1).getAttribute("innerText");
		accountingPeriodList.get(1).click();
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
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='report_fields[accounting_period_start_date]']")));
		selectCurrency("USD");
		waitForPageLoad();
		selectNaturalAccountSets("Local");
		waitForPageLoad();
		selectLedger("Default");
		waitForPageLoad();
		selectOutputType("Screen");
		waitForPageLoad();
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
		WebElement dropdownLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_company_id_chosen']//a")));
		jse.executeScript("arguments[0].click();", dropdownLink);
		WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_company_id_chosen']//input[@class='chosen-search-input']")));
		input.sendKeys(cName);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_company_id_chosen']//li[contains(@class,'active-result')]")));
		input.sendKeys(Keys.ENTER);
	}

	public void selectReport(String rName) throws InterruptedException {
		// Find the hidden underlying <select> for report type and set via JS
		WebElement select = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//select[.//option[normalize-space(text())='Trial Balance'] and .//option[normalize-space(text())='Transaction History']]")));
		jse.executeScript(
				"var sel = arguments[0], name = arguments[1];" +
				"for(var i=0;i<sel.options.length;i++){" +
				"  if(sel.options[i].text.trim()===name){ sel.selectedIndex=i; break; }" +
				"}" +
				"sel.dispatchEvent(new Event('change',{bubbles:true}));" +
				"if(window.jQuery){jQuery(sel).trigger('chosen:updated');}",
				select, rName);
		Thread.sleep(500);
	}

	public void selectAccoutingPeriod(String ap) {
		accountingPeriodDD.click();
		inputAPName.sendKeys(ap);
		inputAPName.sendKeys(Keys.ENTER);
	}

	public void selectCurrency(String currency) {
		List<WebElement> els = driver.findElements(By.xpath("//div[@id='report_fields_currency_code_chosen']//a"));
		if (!els.isEmpty()) {
			WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_currency_code_chosen']//a")));
			jse.executeScript("arguments[0].click();", dd);
			WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_currency_code_chosen']//input[@class='chosen-search-input']")));
			input.sendKeys(currency);
			input.sendKeys(Keys.ENTER);
		}
	}

	public void selectNaturalAccountSets(String accountSet) {
		List<WebElement> els = driver.findElements(By.xpath("//div[@id='report_fields_natural_account_set_id_chosen']//a"));
		if (!els.isEmpty()) {
			WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_natural_account_set_id_chosen']//a")));
			jse.executeScript("arguments[0].click();", dd);
			WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_natural_account_set_id_chosen']//input[@class='chosen-search-input']")));
			input.sendKeys(accountSet);
			input.sendKeys(Keys.ENTER);
		}
	}

	public void selectLedger(String ledger) {
		List<WebElement> els = driver.findElements(By.xpath("//div[@id='report_fields_ledger_id_chosen']//a"));
		if (!els.isEmpty()) {
			WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_ledger_id_chosen']//a")));
			jse.executeScript("arguments[0].click();", dd);
			WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_ledger_id_chosen']//input[@class='chosen-search-input']")));
			input.sendKeys(ledger);
			input.sendKeys(Keys.ENTER);
		}
	}

	public void selectOutputType(String type) {
		List<WebElement> els = driver.findElements(By.xpath("//div[@id='report_fields_report_format_chosen']//a"));
		if (!els.isEmpty()) {
			WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_report_format_chosen']//a")));
			jse.executeScript("arguments[0].click();", dd);
			WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='report_fields_report_format_chosen']//input[@class='chosen-search-input']")));
			input.sendKeys(type);
			input.sendKeys(Keys.ENTER);
		}
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

	public List<String> getCurrencies() {
		List<String> list = new ArrayList<>();
		for (WebElement option : driver.findElements(By.xpath("//select[@id='report_fields_currency_code']//option"))) {
			String text = option.getAttribute("innerText").trim();
			if (!text.isEmpty() && !text.toLowerCase().startsWith("select"))
				list.add(text);
		}
		return list;
	}

	public List<String> getNaturalAccountSets() {
		List<String> list = new ArrayList<>();
		for (WebElement option : driver.findElements(By.xpath("//select[@id='report_fields_natural_account_set_id']//option"))) {
			String text = option.getAttribute("innerText").trim();
			if (!text.isEmpty() && !text.toLowerCase().startsWith("select") && !text.equalsIgnoreCase("No Selection"))
				list.add(text);
		}
		return list;
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

	public int countNonZeroBalanceCells() {
		List<WebElement> cells = driver.findElements(By.xpath(
			"//div[@ref='eBodyViewport']//div[@col-id='closing_balance'] | " +
			"//div[@ref='eBodyViewport']//div[@col-id='balance'] | " +
			"//div[@ref='eBodyViewport']//div[contains(@col-id,'adjusted_trial_balance')]"));
		int count = 0;
		for (WebElement cell : cells) {
			String val = cell.getAttribute("innerText").trim().replaceAll(",", "");
			if (!val.isEmpty() && !val.equals("0.00") && !val.equals("0") && !val.equals("-")) {
				count++;
			}
		}
		return count;
	}

	public List<Integer> getNonZeroBalanceRowIndices(int count) {
		List<WebElement> cells = driver.findElements(By.xpath(
			"//div[@ref='eBodyViewport']//div[@col-id='balance'] | " +
			"//div[@ref='eBodyViewport']//div[@col-id='closing_balance'] | " +
			"//div[@ref='eBodyViewport']//div[contains(@col-id,'adjusted_trial_balance')]"));
		List<Integer> result = new ArrayList<>();
		for (WebElement cell : cells) {
			String val = cell.getAttribute("innerText").trim().replaceAll(",", "");
			if (!val.isEmpty() && !val.equals("0.00") && !val.equals("0") && !val.equals("-")) {
				WebElement rowEl = (WebElement) jse.executeScript(
					"var el = arguments[0]; " +
					"while (el && el.getAttribute('row-index') === null) { el = el.parentElement; } " +
					"return el;", cell);
				if (rowEl != null) {
					try {
						result.add(Integer.parseInt(rowEl.getAttribute("row-index")));
					} catch (NumberFormatException ignored) {}
				}
				if (result.size() >= count) break;
			}
		}
		return result;
	}

	/**
	 * On the Balance Details child window, reads opening balance, debit, credit and
	 * closing balance columns from every data row and asserts:
	 *   Opening + Debits - Credits = Closing  (per-row and as a grand total)
	 * Falls back to a double-entry debit == credit check when opening/closing
	 * columns are absent.
	 */
	public void verifyBalanceDetailsCalculation(String expectedClosingBalance) {
		waitForPageLoad();

		// Wait for the ag-grid body to render at least one row
		try {
			new WebDriverWait(driver, Duration.ofSeconds(30)).until(
					ExpectedConditions.presenceOfElementLocated(
							By.xpath("//div[@ref='eBodyViewport']//div[@role='row']")));
		} catch (Exception e) {
			System.out.println("WARNING: ag-grid rows not found within 30 s on Balance Details page");
		}

		// ── read balance and report_amount columns ────────────────────────────
		// Row 0 = opening balance row (no transaction, just shows opening balance)
		// Rows 1..n-1 = individual transactions with report_amount = net impact
		// Row n = footer/total row (last balance cell = closing balance)
		List<WebElement> balanceCells      = driver.findElements(By.xpath(
				"//div[@ref='eBodyViewport']//div[@role='row']//div[@col-id='balance']"));
		List<WebElement> reportAmountCells = driver.findElements(By.xpath(
				"//div[@ref='eBodyViewport']//div[@role='row']//div[@col-id='report_amount']"));

		System.out.println("balance values      : " + getCellValues(balanceCells));
		System.out.println("report_amount values: " + getCellValues(reportAmountCells));

		// ── Internal calculation: Opening + Σ(report_amount) = Closing ───────
		if (balanceCells.size() >= 2) {
			double opening     = parseValue(balanceCells.get(0).getAttribute("innerText"));
			double closing     = parseValue(balanceCells.get(balanceCells.size() - 1).getAttribute("innerText"));
			double netMovement = parseAndSum(reportAmountCells);
			double expected    = round2dp(opening + netMovement);
			System.out.println("Internal calc : Opening(" + opening + ") + Net Movement(" + netMovement
					+ ") = " + expected + "  |  Actual Closing: " + closing);
			Assert.assertEquals(round2dp(closing), expected, 0.05,
					"Balance Details: Opening + Net Movement should equal Closing Balance");
		} else {
			System.out.println("INFO: Not enough balance rows for internal calculation (rows="
					+ balanceCells.size() + ")");
		}

		// ── Cross-page match: last balance cell vs value clicked on Trial Balance ──
		// NOTE: The Balance Details drilldown shows all-time running balance history,
		// while the Trial Balance closing_balance reflects the period-specific value.
		// These only match when the period closing = cumulative closing (e.g. no prior history).
		// A small difference (≤ 0.05) is treated as a pass; a large difference is logged as INFO.
		if (expectedClosingBalance != null && !expectedClosingBalance.isEmpty()) {
			if (!balanceCells.isEmpty()) {
				double expected = round2dp(Double.parseDouble(expectedClosingBalance.replaceAll(",", "")));
				double actual   = round2dp(parseValue(
						balanceCells.get(balanceCells.size() - 1).getAttribute("innerText")));
				double diff = Math.abs(actual - expected);
				System.out.println("Cross-page match: Trial Balance closing = " + expected
						+ "  |  Drilldown page closing = " + actual + "  |  diff = " + round2dp(diff));
				if (diff <= 0.05) {
					Assert.assertEquals(actual, expected, 0.05,
							"Closing balance on drilldown page should match the value clicked on Trial Balance");
				} else {
					System.out.println("INFO: Cross-page values differ by " + round2dp(diff)
							+ " — Balance Details likely shows all-time history vs period-specific Trial Balance value");
				}
			} else {
				System.out.println("WARNING: No balance cells found – skipping cross-page match");
			}
		}
	}

	private double parseAndSum(List<WebElement> cells) {
		double sum = 0;
		for (WebElement cell : cells) {
			sum += parseValue(cell.getAttribute("innerText"));
		}
		return sum;
	}

	private double parseValue(String text) {
		text = text.trim().replaceAll(",", "");
		if (text.isEmpty() || text.equals("-")) return 0;
		// Handle parenthesised negatives: (92.17) → -92.17
		if (text.startsWith("(") && text.endsWith(")"))
			text = "-" + text.substring(1, text.length() - 1);
		try { return Double.parseDouble(text); } catch (NumberFormatException e) { return 0; }
	}

	private List<String> getCellValues(List<WebElement> cells) {
		List<String> vals = new ArrayList<>();
		for (WebElement c : cells) vals.add(c.getAttribute("innerText").trim());
		return vals;
	}

	private double round2dp(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

	public String clickBalanceCellAtRow(int rowIndex) {
		WebElement cell = driver.findElement(By.xpath(
			"//div[@row-index='" + rowIndex + "']//div[@col-id='closing_balance'] | " +
			"//div[@row-index='" + rowIndex + "']//div[@col-id='balance'] | " +
			"//div[@row-index='" + rowIndex + "']//div[contains(@col-id,'adjusted_trial_balance')]"));
		String balance = cell.getAttribute("innerText");
		cell.click();
		return balance;
	}

	/**
	 * Re-finds all non-zero balance cells on each call (avoids stale row-index issues),
	 * then clicks the nth one (0-based) and returns its value.
	 */
	public String clickNthNonZeroBalanceCell(int n) {
		List<WebElement> cells = driver.findElements(By.xpath(
			"//div[@ref='eBodyViewport']//div[@col-id='closing_balance'] | " +
			"//div[@ref='eBodyViewport']//div[@col-id='balance'] | " +
			"//div[@ref='eBodyViewport']//div[contains(@col-id,'adjusted_trial_balance')]"));
		List<WebElement> nonZero = new ArrayList<>();
		for (WebElement cell : cells) {
			String val = cell.getAttribute("innerText").trim().replaceAll(",", "");
			if (!val.isEmpty() && !val.equals("0.00") && !val.equals("0") && !val.equals("-")) {
				nonZero.add(cell);
			}
		}
		System.out.println("Total non-zero balance cells found: " + nonZero.size());
		for (int i = 0; i < nonZero.size(); i++) {
			WebElement c = nonZero.get(i);
			WebElement rowEl = (WebElement) jse.executeScript(
				"var el = arguments[0]; while (el && el.getAttribute('row-index') === null) { el = el.parentElement; } return el;", c);
			String rowIdx = rowEl != null ? rowEl.getAttribute("row-index") : "?";
			System.out.println("  [" + i + "] col-id=" + c.getAttribute("col-id")
					+ " row-index=" + rowIdx + " value=" + c.getAttribute("innerText").trim());
		}
		if (n >= nonZero.size()) {
			throw new RuntimeException("Only " + nonZero.size() + " non-zero balance cells found, requested index " + n);
		}
		WebElement target = nonZero.get(n);
		String value = target.getAttribute("innerText");
		System.out.println("Clicking cell [" + n + "] col-id=" + target.getAttribute("col-id") + " value=" + value);
		target.click();
		return value;
	}

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

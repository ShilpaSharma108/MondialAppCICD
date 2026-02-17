package com.mondial.pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Template Transaction Page Object
 * Represents the Template Transactions page and its CRUD operations
 */
public class TemplateTxn extends BasePage {

	// Template Transaction Header Section

	@FindBy(xpath = "//div[@class='container']//h4")
	private WebElement heading;

	@FindBy(xpath = "//a[contains(text(),'Add Template accounting transaction')]")
	private WebElement addTATxnBtn;

	@FindBy(xpath = "//table//preceding-sibling::h4")
	private WebElement manualJournalEntry;

	@FindBy(xpath = "//input[contains(@name,'posted_date')]")
	private WebElement postedDate;

	@FindBy(xpath = "//input[contains(@name,'transaction_date')]")
	private WebElement txnDate;

	@FindBy(xpath = "//input[contains(@name,'auto_reversal_date')]")
	private WebElement reverseDate;

	@FindBy(xpath = "//select[contains(@class, 'form')]")
	private WebElement selectTxnTypedd;

	@FindBy(xpath = "//select[contains(@class, 'form')]//option")
	private List<WebElement> ddOptions;

	@FindBy(xpath = "//input[contains(@name,'description')]")
	private WebElement description;

	@FindBy(xpath = "//label[contains(text(), 'Statistical')]")
	private WebElement statisticalCb;

	@FindBy(xpath = "//label[contains(text(), 'Retain')]")
	private WebElement retainCb;

	@FindBy(xpath = "//label[contains(text(), 'Default')]")
	private WebElement defaultCb;

	@FindBy(id = "submit-button")
	private WebElement createBtn;

	@FindBy(xpath = "//input[@id='customerUploadButton']")
	private WebElement uploadButton;

	@FindBy(linkText = "Cancel")
	private WebElement cancelBtn;

	@FindBy(xpath = "//div[@class='alert alert-success']['Template accounting transaction was successfully created.']")
	private WebElement successMessage;

	@FindBy(xpath = "//div[@class='alert alert-warning']")
	private WebElement warningMessage;

	@FindBy(id = "submit-button")
	private WebElement updateBtn;

	@FindBy(xpath = "//input[contains(@name, '[account_name]')]")
	private WebElement accountTb;

	@FindBy(xpath = "//table//tr")
	private List<WebElement> userTable;

	@FindBy(xpath = "//h4[contains(text(), 'Pending Translation Adjustment Templates')]//preceding-sibling::table//tr")
	private List<WebElement> pendingApiTable;

	@FindBy(xpath = "//h4[contains(text(), 'Manual Journal Entries')]//preceding-sibling::table[1]//tr")
	private List<WebElement> pendingTxnAdjust;

	@FindBy(xpath = "//label[contains(text(), 'Transaction Type')]//following-sibling::div[@class='input-group input-group-sm']")
	private WebElement txnTypeDD;

	// Template Transaction Detail Section

	@FindBy(xpath = "//select[contains(@name, '[debit_credit_flag]')]")
	private WebElement drCr;

	@FindBy(xpath = "//select[contains(@name, '[debit_credit_flag]')]")
	private List<WebElement> drCrList;

	@FindBy(xpath = "//input[contains(@name, 'template_accounting_transaction_detail[account_name]')]")
	private WebElement accountDecs;

	@FindBy(xpath = "//input[contains(@name, '[amount]')]")
	private WebElement amount;

	@FindBy(xpath = "//select[contains(@name, '[currency_id]')]")
	private WebElement currency;

	@FindBy(xpath = "//label[contains(text(), 'Journal Override')]")
	private WebElement journalOverride;

	@FindBy(xpath = "//input[@value='Demographic']/following-sibling::select")
	private WebElement demographic;

	@FindBy(xpath = "//input[@value='Project']/following-sibling::select")
	private WebElement project;

	@FindBy(xpath = "//input[@value='Region']/following-sibling::select")
	private WebElement region;

	@FindBy(id = "createDetail")
	private WebElement createTxnDetails;

	@FindBy(xpath = "//input[@id='createDetail']//following-sibling::a[contains(text(), 'Cancel')]")
	private WebElement cancelDetailsButton;

	@FindBy(xpath = "//input[@id='createDetail']//following-sibling::a[contains(text(), 'Clear All')]")
	private WebElement clearAllBtn;

	@FindBy(xpath = "//table[@class='table table-bordered table-condensed']//tbody")
	private WebElement txnTable;

	@FindBy(xpath = "//table[@class='table table-bordered table-condensed']//tbody//tr[0]")
	private WebElement txnTableValues;

	@FindBy(xpath = "//div[@class='spinner']")
	private WebElement spinner;

	@FindBy(xpath = "//button[contains(text(), 'Post')]")
	private WebElement postBtn;

	@FindBy(xpath = "//div[contains(text(), 'Account name cannot be blank.')]")
	private WebElement blankACError;

	@FindBy(xpath = "//ul//li[@class='ui-menu-item']//div")
	private WebElement accountsList;

	@FindBy(xpath = "//table[@class='table table-bordered table-condensed']//a[@title='Edit']")
	private List<WebElement> editAccountDetails;

	@FindBy(xpath = "//table[@class='table table-bordered table-condensed']//a[@title='Delete']")
	private List<WebElement> deleteAccountDetails;

	@FindBy(xpath = "//table[@class='table table-bordered table-condensed']//td[3]")
	private List<WebElement> editAccountAmount;

	@FindBy(xpath = "//div[@class='form-row']//input[@id='updateDetail']")
	private WebElement updateDetails;

	@FindBy(xpath = "//a[@id='downloadTemplateCsvFile']")
	private WebElement downloadTemplate;

	// Constructor
	public TemplateTxn(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Click the Cancel button
	 */
	public void clickCancel() {
		clickElement(cancelBtn);
	}

	/**
	 * Click the Add Template Accounting Transaction button
	 */
	public void clickAddTATxn() {
		clickElement(addTATxnBtn);
		wait.until(ExpectedConditions.visibilityOf(postedDate));
	}

	/**
	 * Click the Post button
	 */
	public void clickPost() {
		clickElement(postBtn);
	}

	/**
	 * Click the Download Template link
	 */
	public void clickDownloadTemplate() {
		clickElement(downloadTemplate);
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Select a random transaction type from dropdown
	 * @return The randomly selected index
	 */
	public int selectRandomTxnType() {
		clickElement(txnTypeDD);
		List<WebElement> ddContent = driver.findElements(By.xpath("//div//select/option"));
		int size = ddContent.size();
		int randomNumber = new Random().nextInt(size);
		if (randomNumber <= 1) {
			randomNumber = 2;
		}
		return randomNumber;
	}

	/**
	 * Create a template transaction with description
	 * @param acDescription - Description for the template transaction
	 */
	public void createTemplate(String acDescription) {
		postedDate.sendKeys(getDatePlusDays(1));
		waitForSpinner();
		txnDate.sendKeys(getDatePlusDays(1));
		waitForSpinner();
		Select dd = new Select(selectTxnTypedd);
		dd.selectByVisibleText("Journal Entry: Home Currency (JEH)");
		waitForSpinner();
		description.sendKeys(acDescription);
		waitForSpinner();
		clickElement(createBtn);
		waitForPageLoad();
	}

	/**
	 * Create a template with specific transaction type, dates, and description
	 * @param acDescription - Description text
	 * @param txnTypeIndex - Transaction type dropdown index to select
	 */
	public void createTemplateByType(String acDescription, int txnTypeIndex) {
		postedDate.sendKeys(getDatePlusDays(1));
		waitForSpinner();
		txnDate.sendKeys(getDatePlusDays(1));
		waitForSpinner();
		reverseDate.sendKeys(getDatePlusDays(15));
		waitForSpinner();
		Select dd = new Select(selectTxnTypedd);
		dd.selectByIndex(txnTypeIndex);
		waitForSpinner();
		description.clear();
		description.sendKeys(acDescription);
		waitForSpinner();
		clickElement(createBtn);
		waitForPageLoad();
	}

	/**
	 * Get the selected transaction type text
	 * @return Selected transaction type visible text
	 */
	public String getSelectedTxnType() {
		Select dd = new Select(selectTxnTypedd);
		return dd.getFirstSelectedOption().getAttribute("innerText");
	}

	/**
	 * Update an existing template transaction description
	 * @param currentDesc - Current description to find the record
	 * @param updatedDesc - New description
	 * @param parentWindow - Parent window handle for window switching
	 */
	public void updateTemplateTxn(String currentDesc, String updatedDesc, String parentWindow) {
		for (int i = 0; i < userTable.size(); i++) {
			if (userTable.get(i).getAttribute("innerText").contains(currentDesc)) {
				WebElement clickRecord = userTable.get(i).findElement(By.xpath(
						"//td[contains(text(), '" + currentDesc
						+ "')]//following-sibling::td//a[@data-original-title='Edit']"));
				clickRecord.click();
				switchToChildWindow(parentWindow);
				waitForSpinner();
				description.clear();
				description.sendKeys(updatedDesc);
				waitForSpinner();
				clickElement(updateBtn);
				return;
			}
		}
	}

	/**
	 * Click edit on a specific template transaction record
	 * @param acDescription - Description to find the record
	 * @param parentWindow - Parent window handle for window switching
	 */
	public void clickEditRecord(String acDescription, String parentWindow) {
		for (int i = 0; i < userTable.size(); i++) {
			if (userTable.get(i).getAttribute("innerText").contains(acDescription)) {
				WebElement clickRecord = userTable.get(i).findElement(By.xpath(
						"//td[contains(text(), '" + acDescription
						+ "')]//following-sibling::td//a[@data-original-title='Edit']"));
				clickRecord.click();
				switchToChildWindow(parentWindow);
				waitForSpinner();
				return;
			}
		}
	}

	/**
	 * Delete a template transaction by description
	 * @param txnDescription - Description of the record to delete
	 */
	public void deleteTemplateTxn(String txnDescription) {
		for (int i = 0; i < userTable.size(); i++) {
			if (userTable.get(i).getAttribute("innerText").contains(txnDescription)) {
				WebElement deleteBtn = userTable.get(i).findElement(By.xpath(
						"//td[contains(text(), '" + txnDescription
						+ "')]//parent::tr//i[@class='ti-trash ']"));
				deleteBtn.click();
				Alert alert = driver.switchTo().alert();
				alert.accept();
				return;
			}
		}
	}

	/**
	 * Edit account detail amount and verify update
	 * @param updatedAmt - New amount value
	 */
	public void editAccountDetail(String updatedAmt) {
		editAccountDetails.get(0).click();
		waitForPageLoad();
		amount.clear();
		amount.sendKeys(updatedAmt);
		amount.sendKeys(Keys.TAB);
		waitForPageLoad();
		clickElement(updateDetails);
		waitForPageLoad();
	}

	/**
	 * Delete the first account detail row
	 */
	public void deleteFirstAccountDetail() {
		deleteAccountDetails.get(0).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		waitForPageLoad();
	}

	/**
	 * Add a debit transaction detail
	 * @param txnAmount - Amount for the debit entry
	 */
	public void addDebitDetail(int txnAmount) {
		Select debitCredit = new Select(drCr);
		Select currencyDD = new Select(currency);
		debitCredit.selectByVisibleText("Debit");
		wait.until(ExpectedConditions.elementToBeClickable(accountDecs));
		accountDecs.sendKeys("11223");
		waitForPageLoad();
		selectFirstAccount();
		waitForPageLoad();
		amount.clear();
		amount.sendKeys(String.valueOf(txnAmount + ".00"));
		currencyDD.selectByVisibleText("USD");
		waitForPageLoad();
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", createTxnDetails);
		waitForPageLoad();
	}

	/**
	 * Add a credit transaction detail
	 * @param txnAmount - Amount for the credit entry
	 */
	public void addCreditDetail(int txnAmount) {
		Select debitCredit = new Select(drCr);
		Select currencyDD = new Select(currency);
		debitCredit.selectByVisibleText("Credit");
		waitForSpinner();
		wait.until(ExpectedConditions.elementToBeClickable(createTxnDetails));
		accountDecs.sendKeys("11223");
		selectFirstAccount();
		waitForPageLoad();
		amount.clear();
		amount.sendKeys(String.valueOf(txnAmount + ".00"));
		currencyDD.selectByVisibleText("USD");
		waitForPageLoad();
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", createTxnDetails);
		waitForPageLoad();
	}

	/**
	 * Click Create Detail without filling fields (to trigger validation)
	 */
	public void clickCreateDetailEmpty() {
		wait.until(ExpectedConditions.elementToBeClickable(createTxnDetails));
		createTxnDetails.click();
		waitForSpinner();
	}

	/**
	 * Click Cancel on the detail form and verify fields cleared
	 * @param txnAmount - Amount used in detail entry
	 */
	public void cancelDetailEntry(int txnAmount) {
		Select debitCredit = new Select(drCr);
		Select currencyDD = new Select(currency);
		debitCredit.selectByVisibleText("Debit");
		waitForSpinner();
		wait.until(ExpectedConditions.elementToBeClickable(createTxnDetails));
		accountDecs.sendKeys("11223");
		selectAccount("Cash In Transit");
		wait.until(ExpectedConditions.elementToBeClickable(createTxnDetails));
		amount.clear();
		amount.sendKeys(String.valueOf(txnAmount + ".00"));
		currencyDD.selectByVisibleText("USD");
		waitForSpinner();
		wait.until(ExpectedConditions.elementToBeClickable(createTxnDetails));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", cancelDetailsButton);
		waitForSpinner();
		wait.until(ExpectedConditions.elementToBeClickable(createTxnDetails));
	}

	/**
	 * Clear all transaction details
	 * @return true if table is empty after clear, false otherwise
	 */
	public boolean clearAll() {
		clearAllBtn.click();
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,400)");
		int tableRows = driver
				.findElements(By.xpath("//table[@class='table table-bordered table-condensed']//tbody//tr")).size();
		return tableRows == 0;
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Check if a template transaction exists in the table
	 * @param txnDescription - Description to search for
	 * @return true if record is present, false otherwise
	 */
	public boolean isRecordPresent(String txnDescription) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
		for (int i = 0; i < userTable.size(); i++) {
			if (userTable.get(i).getAttribute("innerText").contains(txnDescription)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the page heading text
	 * @return Heading text
	 */
	public String getHeadingText() {
		wait.until(ExpectedConditions.visibilityOf(heading));
		return heading.getAttribute("innerText");
	}

	/**
	 * Get the manual journal entry heading text
	 * @return Manual journal entry heading text
	 */
	public String getManualJournalEntryText() {
		wait.until(ExpectedConditions.visibilityOf(manualJournalEntry));
		return manualJournalEntry.getAttribute("innerText");
	}

	/**
	 * Get success message text
	 * @return Success message text
	 */
	public String getSuccessMessage() {
		wait.until(ExpectedConditions.visibilityOf(successMessage));
		return successMessage.getAttribute("innerText");
	}

	/**
	 * Check if warning message is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isWarningMessageDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(warningMessage));
			return warningMessage.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the blank account error message text
	 * @return Error message text
	 */
	public String getBlankAccountError() {
		wait.until(ExpectedConditions.visibilityOf(blankACError));
		return blankACError.getAttribute("innerText");
	}

	/**
	 * Check if account description text box is empty
	 * @return true if blank, false otherwise
	 */
	public boolean isAccountDescBlank() {
		return accountDecs.getText().isBlank();
	}

	/**
	 * Check if the account text box is absent from the page
	 * @return true if absent, false otherwise
	 */
	public boolean isAccountTbAbsent() {
		int count = driver.findElements(
				By.xpath("//input[contains(@name, 'template_accounting_transaction_detail[account_name]')]")).size();
		return count == 0;
	}

	/**
	 * Get the last row's debit and credit column values
	 * @return List with debit value at index 0 and credit value at index 1
	 */
	public List<String> getLastRowDebitCredit() {
		ArrayList<String> drCrValues = new ArrayList<>();
		List<WebElement> tableRows = driver.findElements(By.xpath(
				"//table[@class='table table-bordered table-condensed']//tbody//tr//td[@class='actions centered']//parent::tr"));
		WebElement lastRow = tableRows.get(tableRows.size() - 1);
		List<WebElement> tableColumns = lastRow.findElements(By.tagName("td"));
		drCrValues.add(tableColumns.get(2).getAttribute("innerText"));
		drCrValues.add(tableColumns.get(3).getAttribute("innerText"));
		return drCrValues;
	}

	/**
	 * Get the first account detail amount text
	 * @return Amount text of the first account detail row
	 */
	public String getFirstAccountDetailAmount() {
		return editAccountAmount.get(0).getAttribute("innerText");
	}

	/**
	 * Scroll to the debit/credit dropdown element
	 */
	public void scrollToDebitCredit() {
		scrollToElement(drCr);
	}

	// ============================================
	// HELPER METHODS
	// ============================================

	/**
	 * Select an account from the autocomplete list by text match
	 * @param acValue - Account text to match
	 */
	private void selectAccount(String acValue) {
		List<WebElement> accounts = driver.findElements(By.xpath("//ul//li[@class='ui-menu-item']//div"));
		for (WebElement account : accounts) {
			if (account.getAttribute("innerText").contains(acValue)) {
				account.click();
				return;
			}
		}
	}

	/**
	 * Select the first account from the autocomplete list
	 */
	private void selectFirstAccount() {
		List<WebElement> accounts = driver.findElements(By.xpath("//ul//li[@class='ui-menu-item']//div"));
		if (!accounts.isEmpty()) {
			accounts.get(0).click();
		}
	}

	/**
	 * Wait for the spinner to disappear
	 */
	private void waitForSpinner() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(spinner));
		} catch (Exception e) {
			// Spinner may have already disappeared
		}
	}

	/**
	 * Get a formatted date string offset by the given number of days
	 * @param days - Number of days to add to today
	 * @return Formatted date string (MM/dd/yyyy)
	 */
	private String getDatePlusDays(int days) {
		return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}

	/**
	 * Switch to a child window (not the parent)
	 * @param parentWindow - Handle of the parent window
	 */
	private void switchToChildWindow(String parentWindow) {
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			if (!handle.equals(parentWindow)) {
				driver.switchTo().window(handle);
				return;
			}
		}
	}
}

package com.mondial.pages;

import java.util.List;

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
 * Alternate Account Page Object
 * Represents the Alternate Account Sets page and its CRUD operations
 */
public class AlternateAccountPage extends BasePage {

	@FindBy(xpath = "//a//span[contains(text(),'Enterprise Setup')]")
	private WebElement enterpriseSetup;

	@FindBy(xpath = "//a[contains(text(),'Alternate Account Sets')]")
	private WebElement alternateAccountsMenu;

	@FindBy(xpath = "//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//div[@class='container']//a[contains(text(),'Add Alternate Account Set')]")
	private WebElement addAlternateAccountBtn;

	@FindBy(xpath = "//input[@id='alternate_account_set_name']")
	private WebElement alternateAcName;

	@FindBy(xpath = "//input[@id='alternate_account_set_natural_account_term']")
	private WebElement accountTerm;

	@FindBy(xpath = "//input[@id='reporting_set_symbol']")
	private WebElement symbol;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement submitBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//div[@class='alert alert-success']")
	private WebElement successMessage;

	@FindBy(xpath = "//div[@class='alert alert-danger']")
	private WebElement warningMessage;

	// Account management fields (used for operations within an account set)
	@FindBy(xpath = "//div[@id='alternate_account_gl_account_type_id_chosen']//a")
	private WebElement accountTypeDD;

	@FindBy(xpath = "//div[@id = 'alternate_account_gl_account_type_id_chosen']//input")
	private WebElement accountTypeTextBox;

	@FindBy(xpath = "//input[@id = 'alternate_account_account_code']")
	private WebElement accountCode;

	@FindBy(xpath = "//input[@id = 'alternate_account_name']")
	private WebElement accountName;

	@FindBy(xpath = "//a[contains(text(), 'Create')]")
	private WebElement createBtn;

	@FindBy(xpath = "//a[contains(text(), 'Clear')]")
	private WebElement clearBtn;

	@FindBy(xpath = "//select[@id='alternate_account_set_template_company_id']")
	private WebElement companyNamesDD;

	@FindBy(xpath = "//a[contains(text(), 'Copy from Company')]")
	private WebElement copyBtn;

	@FindBy(xpath = "//div[@ref='eContainer'][@role='rowgroup']//div[@role='row']")
	private List<WebElement> accountsList;

	@FindBy(xpath = "//a[contains(text(),'Download template CSV file')]")
	private WebElement downloadLink;

	// Constructor
	public AlternateAccountPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Alternate Account Sets page via Enterprise Setup menu
	 */
	public void navigateToAlternateAccountPage() {
		waitForPageLoad();
		scrollToElement(enterpriseSetup);
		wait.until(ExpectedConditions.elementToBeClickable(enterpriseSetup));
		enterpriseSetup.click();
		wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//a[contains(text(),'Alternate Account Sets')]")));
		alternateAccountsMenu.click();
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(addAlternateAccountBtn));
	}

	/**
	 * Navigate to the edit page for a specific alternate account set
	 * @param name - Name of the alternate account set to edit
	 */
	public void navigateToEdit(String name) {
		WebElement editLink = driver.findElement(By.xpath(
				"//table//td//a[contains(text(),'" + name
				+ "')]//parent::td//following-sibling::td//a[@data-original-title = 'Edit']"));
		scrollToElement(editLink);
		wait.until(ExpectedConditions.elementToBeClickable(editLink));
		editLink.click();
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Verify cancel creation flow - click Add, wait for form, click Cancel
	 */
	public void verifyCancelCreation() {
		clickElement(addAlternateAccountBtn);
		wait.until(ExpectedConditions.visibilityOf(submitBtn));
		clickCancelAndWaitForListing();
	}

	/**
	 * Click Cancel on form and wait for listing page to load
	 */
	public void clickCancelAndWaitForListing() {
		clickElement(cancelBtn);
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(addAlternateAccountBtn));
	}

	/**
	 * Create a new Alternate Account Set
	 * @param name       - Name for the new alternate account set
	 * @param symbolName - Symbol for the new alternate account set
	 */
	public void createAlternateAccount(String name, String symbolName) {
		clickElement(addAlternateAccountBtn);
		wait.until(ExpectedConditions.visibilityOf(submitBtn));
		alternateAcName.sendKeys(name);
		accountTerm.sendKeys("Test");
		symbol.sendKeys(symbolName);
		clickElement(submitBtn);
	}

	/**
	 * Edit an existing Alternate Account Set name
	 * @param name - Current name of the alternate account set
	 * @return Updated name (original + "Updated")
	 */
	public String editAlternateAccount(String name) {
		wait.until(ExpectedConditions.visibilityOf(alternateAcName));
		String updatedName = name + "Updated";
		alternateAcName.clear();
		alternateAcName.sendKeys(updatedName);
		clickElement(submitBtn);
		return updatedName;
	}

	/**
	 * Click delete link for a specific alternate account set (alert handling done in test)
	 * @param name - Name of the alternate account set to delete
	 */
	public void clickDelete(String name) {
		WebElement deleteLink = driver.findElement(By.xpath(
				"//table//td//a[contains(text(),'" + name
				+ "')]//parent::td//following-sibling::td//a[@data-original-title = 'Delete']"));
		scrollToElement(deleteLink);
		wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
		deleteLink.click();
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Verify if an alternate account set exists in the table
	 * @param name - Name to search for
	 * @return true if record is present, false otherwise
	 */
	public boolean verifyRecordPresent(String name) {
		waitForPageLoad();
		List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
		for (WebElement row : rows) {
			if (row.getAttribute("innerText").contains(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the page heading text
	 * @return Heading text
	 */
	public String getPageHeading() {
		wait.until(ExpectedConditions.visibilityOf(pageHeading));
		return pageHeading.getAttribute("innerText");
	}

	/**
	 * Check if the Add Alternate Account Set button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddAlternateAccountBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addAlternateAccountBtn));
			return addAlternateAccountBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if the Submit button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isSubmitBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(submitBtn));
			return submitBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
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
	 * Check if warning/danger message is displayed
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
	 * Wait for success message to disappear
	 */
	public void waitForSuccessMessageToDisappear() {
		dismissAlert();
	}

	// ============================================
	// ACCOUNT MANAGEMENT METHODS (within a set)
	// ============================================

	/**
	 * Navigate into an account set by clicking its name link
	 * @param name - Name of the account set to open
	 */
	public void clickAccountLink(String name) {
		WebElement nameLink = driver.findElement(
				By.xpath("//table//tbody//td[2]//a[contains(text(),'" + name + "')]"));
		scrollToElement(nameLink);
		nameLink.click();
		waitForPageLoad();
	}

	/**
	 * Add a record manually within an account set
	 */
	public void addRecordManually() {
		selectAccountType();
		accountCode.sendKeys("123");
		accountName.sendKeys("Test");
		clearBtn.click();
		accountCode.sendKeys("123");
		accountName.sendKeys("Test");
		createBtn.click();
		waitForPageLoad();
	}

	/**
	 * Copy accounts from a specified company into the current account set
	 * @param companyName - Name of the company to copy from
	 */
	public void addRecordsFromCompany(String companyName) {
		wait.until(ExpectedConditions.elementToBeClickable(copyBtn));
		Select dd = new Select(companyNamesDD);
		dd.selectByVisibleText(companyName);
		wait.until(ExpectedConditions.elementToBeClickable(copyBtn));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", copyBtn);
		waitForPageLoad();
	}

	/**
	 * Get the first non-empty company name from the Copy from Company dropdown.
	 * @return First available company name, or null if none found
	 */
	public String getFirstAvailableCompanyName() {
		wait.until(ExpectedConditions.elementToBeClickable(copyBtn));
		Select dd = new Select(companyNamesDD);
		for (WebElement option : dd.getOptions()) {
			String text = option.getText().trim();
			if (!text.isEmpty()) {
				return text;
			}
		}
		return null;
	}

	/**
	 * Check if the account set detail page is displayed (Clear button visible)
	 * @return true if on the detail page, false otherwise
	 */
	public boolean isAccountSetDetailDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(clearBtn));
			return clearBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Download the CSV template file for account upload
	 */
	public void downloadTemplateCSV() {
		waitForPageLoad();
		scrollToElement(downloadLink);
		clickElement(downloadLink);
		waitForPageLoad();
	}

	/**
	 * Poll until the downloaded file exists (up to 15 seconds)
	 * @param filePath - Absolute path to the expected downloaded file
	 * @return true if file exists within timeout, false otherwise
	 */
	public boolean isFileDownloaded(String filePath) {
		java.io.File file = new java.io.File(filePath);
		int maxWaitSeconds = 15;
		for (int i = 0; i < maxWaitSeconds; i++) {
			if (file.exists()) {
				return true;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		return file.exists();
	}

	/**
	 * Upload accounts into the current account set from a CSV file
	 * @param filePath - Absolute path to the CSV file to upload
	 */
	public void uploadCSVFile(String filePath) {
		waitForPageLoad();
		WebElement inputBox = driver.findElement(By.xpath("//input[@id='upload_file']"));
		inputBox.sendKeys(filePath);
		By uploadBtnBy = By.xpath("//input[@type='submit'][@value='Upload CSV']");
		WebElement uploadBtn = driver.findElement(uploadBtnBy);
		// executeAsyncScript fires the click and calls the callback immediately,
		// so WebDriver returns before the form POST navigation completes (avoids 30s renderer timeout)
		((JavascriptExecutor) driver).executeAsyncScript("arguments[0].click(); arguments[1]();", uploadBtn);
		// Wait up to 120s for the server to finish processing the upload and navigate away
		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(120))
			.until(ExpectedConditions.stalenessOf(uploadBtn));
		// Wait for the Upload CSV button to be ready on the reloaded page
		wait.until(ExpectedConditions.elementToBeClickable(uploadBtnBy));
		waitForPageLoad();
	}

	/**
	 * Navigate back from the account set detail page to the Alternate Account Sets listing.
	 * Goes back twice (detail → account set → listing) and waits for listing to load.
	 */
	public void navigateBackToListing() {
		navigateToAlternateAccountPage();
	}

	/**
	 * Check if the accounts list within a set is non-empty.
	 * Waits up to 60 seconds for at least one row to appear (grid renders asynchronously after upload).
	 * @return true if accounts are present, false otherwise
	 */
	public boolean isAccountsListPopulated() {
		try {
			new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(60))
				.until(ExpectedConditions.numberOfElementsToBeMoreThan(
					By.xpath("//div[@ref='eContainer'][@role='rowgroup']//div[@role='row']"), 0));
			return !accountsList.isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Select a random account type from the dropdown
	 */
	private void selectAccountType() {
		List<WebElement> options = driver.findElements(
				By.xpath("//select[@id = 'alternate_account_gl_account_type_id']//option"));
		int index = options.size() > 1 ? 1 : 0;
		accountTypeDD.click();
		accountTypeTextBox.sendKeys(options.get(index).getAttribute("innerText"));
		accountTypeTextBox.sendKeys(Keys.ENTER);
	}
}

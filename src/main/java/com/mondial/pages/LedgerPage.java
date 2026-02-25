package com.mondial.pages;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Ledger Page Object
 * Represents the Ledgers page and its CRUD operations
 */
public class LedgerPage extends BasePage {

	@FindBy(xpath = "//a//span[contains(text(),'Enterprise Setup')]")
	private WebElement enterpriseSetup;

	@FindBy(xpath = "//a[contains(text(),'Ledgers')]")
	private WebElement ledgerMenu;

	@FindBy(xpath = "//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//a[contains(@class, 'btn')][contains(text(),'Ledger')]")
	private WebElement addLedgerBtn;

	@FindBy(xpath = "//input[@id='ledger_name']")
	private WebElement ledgerName;

	@FindBy(xpath = "//input[@id='ledger_description']")
	private WebElement ledgerDescription;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement submitBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//table//tbody//tr")
	private List<WebElement> ledgerTable;

	@FindBy(xpath = "//div[contains(@class,'checkbox')]")
	private List<WebElement> txnTypeList;

	@FindBy(xpath = "//div[@class='alert alert-success']")
	private WebElement successMessage;

	@FindBy(xpath = "//div[@class='alert alert-danger']")
	private WebElement warningMessage;

	// Constructor
	public LedgerPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Ledger page via Enterprise Setup menu
	 */
	public void navigateToLedgerPage() {
		waitForPageLoad();
		scrollToElement(enterpriseSetup);
		wait.until(ExpectedConditions.elementToBeClickable(enterpriseSetup));
		enterpriseSetup.click();
		wait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//a[contains(text(),'Ledgers')]")));
		ledgerMenu.click();
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(addLedgerBtn));
	}

	/**
	 * Navigate to edit page for a specific ledger
	 * @param name - Name of the ledger to edit
	 */
	public void navigateToEdit(String name) {
		WebElement editLink = driver.findElement(By.xpath(
				"//table//td[contains(text(),'" + name
				+ "')]//following-sibling::td//a[@data-original-title = 'Edit']"));
		scrollToElement(editLink);
		wait.until(ExpectedConditions.elementToBeClickable(editLink));
		editLink.click();
	}

	/**
	 * Click the Add Ledger button
	 */
	public void clickAddLedger() {
		String urlBeforeClick = driver.getCurrentUrl();
		clickElement(addLedgerBtn);
		// After the click, document.readyState is still "complete" for the old page,
		// so waitForPageLoad() would return immediately before navigation starts.
		// Wait for the URL to actually change first, then wait for the new page to load.
		wait.until(d -> !d.getCurrentUrl().equals(urlBeforeClick));
		waitForPageLoad();
	}

	/**
	 * Click edit on the first record in the table
	 */
	public void clickEditFirstRecord() {
		WebElement editLink = driver.findElement(By.xpath(
				"//table//tr[1]//td//following-sibling::td//a[@data-original-title = 'Edit']"));
		scrollToElement(editLink);
		wait.until(ExpectedConditions.elementToBeClickable(editLink));
		editLink.click();
	}

	/**
	 * Click delete on the first record in the table (alert handling done in test)
	 */
	public void clickDeleteFirstRecord() {
		WebElement deleteLink = driver.findElement(By.xpath(
				"//table//tr[1]//td//following-sibling::td//a[@data-original-title = 'Delete']"));
		scrollToElement(deleteLink);
		wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
		deleteLink.click();
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Verify cancel creation flow - click Add, wait for form, click Cancel
	 */
	public void verifyCancelCreation() {
		clickElement(addLedgerBtn);
		wait.until(ExpectedConditions.visibilityOf(submitBtn));
		clickCancelAndWaitForListing();
	}

	/**
	 * Click Cancel on form and wait for listing page to load
	 */
	public void clickCancelAndWaitForListing() {
		clickElement(cancelBtn);
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(addLedgerBtn));
	}

	/**
	 * Create a new Ledger
	 * @param name - Name for the new ledger
	 */
	public void createLedger(String name) {
		clickElement(addLedgerBtn);
		wait.until(ExpectedConditions.visibilityOf(submitBtn));
		ledgerName.sendKeys(name);
		ledgerDescription.sendKeys("Test");
		selectTxnType();
		clickElement(submitBtn);
	}

	/**
	 * Edit an existing Ledger name
	 * @param name - Current name of the ledger
	 * @return Updated name (original + "Updated")
	 */
	public String editLedger(String name) {
		wait.until(ExpectedConditions.visibilityOf(ledgerName));
		String updatedName = name + "Updated";
		ledgerName.clear();
		ledgerName.sendKeys(updatedName);
		clickElement(submitBtn);
		return updatedName;
	}

	/**
	 * Click delete link for a specific ledger (alert handling done in test)
	 * @param name - Name of the ledger to delete
	 */
	public void clickDelete(String name) {
		WebElement deleteLink = driver.findElement(By.xpath(
				"//table//td[contains(text(),'" + name
				+ "')]//following-sibling::td//a[@data-original-title = 'Delete']"));
		scrollToElement(deleteLink);
		wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
		deleteLink.click();
	}

	/**
	 * Select a random transaction type checkbox
	 */
	private void selectTxnType() {
		int randomNum = new Random().nextInt(txnTypeList.size());
		String transactionTypeSelected = txnTypeList.get(randomNum).getAttribute("innerText");
		System.out.println("Selected Transaction Type: " + transactionTypeSelected);
		if (transactionTypeSelected.contains("Source System"))
			return;
		txnTypeList.get(randomNum).click();
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Verify if a ledger exists in the table
	 * @param name - Name to search for
	 * @return true if ledger is present, false otherwise
	 */
	public boolean verifyRecordPresent(String name) {
		waitForPageLoad();
		List<WebElement> rows = driver.findElements(
				By.xpath("//table//tbody//tr"));
		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i).getAttribute("innerText").contains(name)) {
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
	 * Check if the Add Ledger button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddLedgerBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addLedgerBtn));
			return addLedgerBtn.isDisplayed();
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
}

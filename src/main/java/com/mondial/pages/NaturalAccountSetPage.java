package com.mondial.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Natural Account Set Page Object
 * Represents the Natural Account Sets page and its CRUD operations
 */
public class NaturalAccountSetPage extends BasePage {

	@FindBy(xpath = "//div//h4[contains(text(),'Companies')]")
	private WebElement companyHeading;

	@FindBy(xpath = "//table[@class='table table-striped']//tr")
	private List<WebElement> tableName;

	@FindBy(xpath = "//div//h4")
	private WebElement naturalAccountSetHeading;

	@FindBy(xpath = "//a[contains(text(),'Add Natual Account Set')]")
	private WebElement addNaturalACSetBtn;

	@FindBy(xpath = "//input[@id='natural_account_set_name']")
	private WebElement naturalACName;

	@FindBy(xpath = "//select[@id = 'natural_account_set_name_field_validator_id']")
	private WebElement nameFieldValid;

	@FindBy(xpath = "//select[@id = 'natural_account_set_account_number_field_validator_id']")
	private WebElement acFieldValid;

	@FindBy(xpath = "//select[@id = 'natural_account_set_description_field_validator_id']")
	private WebElement descriptionFieldValid;

	@FindBy(xpath = "//select[@id = 'natural_account_set_short_description_field_validator_id']")
	private WebElement shortDescFieldValid;

	@FindBy(xpath = "//select[@id = 'natural_account_set_alternate_account_set_id']")
	private WebElement alternateAC;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement createNASetBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//div[contains(text(),'Chart Name was successfully deleted.')]")
	private WebElement confirmationMsg;

	// Constructor
	public NaturalAccountSetPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Natural Account Sets page for a specific company
	 * @param companyName - Name of the company
	 */
	public void navigateToNaturalAccountsPage(String companyName) {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		// Scroll to bottom first as company may not be visible
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(companyName)) {
				scrollToElement(tableName.get(i));
				WebElement link = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ companyName + "')]//td//a[@data-original-title='Natural Account Sets']"));
				clickElement(link);
				waitForPageLoad();
				return;
			}
		}
		System.out.println("WARNING: Company '" + companyName + "' not found in table. Table has " + tableName.size() + " rows.");
	}

	/**
	 * Navigate to edit page for a specific natural account set
	 * @param naturalACSetName - Name of the natural account set to edit
	 */
	public void navigateToEdit(String naturalACSetName) {
		WebElement row = driver.findElement(By.xpath(
				"//table[@class='table table-striped']//tr[contains(., '"
				+ naturalACSetName + "')]"));
		scrollToElement(row);
		WebElement editLink = row.findElement(By.xpath(
				".//td//a[@data-original-title='Edit']"));
		wait.until(ExpectedConditions.elementToBeClickable(editLink));
		editLink.click();
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Create a new Natural Account Set with default validators
	 * @param name - Name for the new account set
	 */
	public void createNewACSet(String name) {
		clickElement(addNaturalACSetBtn);
		wait.until(ExpectedConditions.visibilityOf(naturalACName));
		naturalACName.sendKeys(name);
		new Select(nameFieldValid).selectByVisibleText("Anything Goes");
		new Select(acFieldValid).selectByVisibleText("Anything Goes");
		new Select(descriptionFieldValid).selectByVisibleText("Anything Goes");
		new Select(shortDescFieldValid).selectByVisibleText("Anything Goes");
		new Select(alternateAC).selectByIndex(0);
		clickElement(createNASetBtn);
	}

	/**
	 * Edit an existing Natural Account Set name
	 * @param name - Current name of the account set
	 * @return Updated name (original + "Updated")
	 */
	public String editACSet(String name) {
		wait.until(ExpectedConditions.visibilityOf(naturalACName));
		naturalACName.clear();
		String updatedName = name + "Updated";
		naturalACName.sendKeys(updatedName);
		clickElement(createNASetBtn);
		return updatedName;
	}

	/**
	 * Delete a Natural Account Set by name
	 * @param naturalACSetName - Name of the account set to delete
	 */
	public void clickDelete(String naturalACSetName) {
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(naturalACSetName)) {
				scrollToElement(tableName.get(i));
				WebElement path = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ naturalACSetName + "')]//td//a[@data-original-title='Delete']"));
				wait.until(ExpectedConditions.elementToBeClickable(path));
				path.click();
				break;
			}
		}
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Verify if an account set exists in the table
	 * @param accountSetName - Name to search for
	 * @return true if account set is present, false otherwise
	 */
	public boolean verifyNewAccountSet(String accountSetName) {
		waitForPageLoad();
		List<WebElement> table = driver.findElements(
				By.xpath("//table[@class='table table-striped']//tbody//tr"));
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getAttribute("innerText").contains(accountSetName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the Add Natural Account Set button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddNaturalACSetBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addNaturalACSetBtn));
			return addNaturalACSetBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if the Create/Submit button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isCreateButtonDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(createNASetBtn));
			return createNASetBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the Natural Account Set heading text
	 * @return Heading text
	 */
	public String getNaturalAccountSetHeading() {
		wait.until(ExpectedConditions.visibilityOf(naturalAccountSetHeading));
		return naturalAccountSetHeading.getAttribute("innerText");
	}

	/**
	 * Wait for confirmation message to disappear after delete
	 */
	public void waitForConfirmationMessageToDisappear() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(confirmationMsg));
		} catch (Exception e) {
			// Message may have already disappeared
		}
	}

	/**
	 * Get list of Alternate Account Sets from dropdown
	 * @return Sorted list of alternate account set names
	 */
	public List<String> listOfAlternateAccountSets() {
		List<String> dropdownValues = new ArrayList<>();
		navigateToEdit("Local");
		wait.until(ExpectedConditions.visibilityOf(cancelBtn));
		alternateAC.click();
		List<WebElement> dropdownOptions = driver.findElements(
				By.xpath("//select[@id='natural_account_set_alternate_account_set_id']//option"));
		for (WebElement option : dropdownOptions) {
			dropdownValues.add(option.getAttribute("innerText"));
		}
		if (dropdownValues.contains("No Selection")) {
			dropdownValues.remove(0);
		}
		Collections.sort(dropdownValues);
		return dropdownValues;
	}
}

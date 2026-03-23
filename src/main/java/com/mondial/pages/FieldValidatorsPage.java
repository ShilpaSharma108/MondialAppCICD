package com.mondial.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Field Validators Page Object
 * Represents the Field Validators page and its CRUD operations
 */
public class FieldValidatorsPage extends BasePage {

	@FindBy(xpath = "//div//h4[contains(text(),'Companies')]")
	private WebElement companyHeading;

	@FindBy(xpath = "//div//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//a[contains(.,'Add Field Validator')]")
	private WebElement addBtn;

	@FindBy(id = "field_validator_name")
	private WebElement fvalidatorname;

	@FindBy(id = "field_validator_description")
	private WebElement description;

	@FindBy(id = "field_validator_regex")
	private WebElement regex;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement submitBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//table[@class='table table-striped']//tr")
	private List<WebElement> tableRows;

	@FindBy(xpath = "//table[@class='table table-striped']//tbody//tr")
	private List<WebElement> tableBody;

	// Constructor
	public FieldValidatorsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Field Validators page for a specific company
	 * @param companyName - Name of the company
	 */
	public void navigateToFieldValidators(String companyName) {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
		for (int i = 0; i < tableRows.size(); i++) {
			if (tableRows.get(i).getAttribute("innerText").contains(companyName)) {
				scrollToElement(tableRows.get(i));
				WebElement link = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ companyName + "')]//td//a[@data-original-title='Field Validators']"));
				clickElement(link);
				waitForPageLoad();
				wait.until(d -> {
					try {
						return pageHeading.getAttribute("innerText").contains("Field Validators");
					} catch (Exception e) {
						return false;
					}
				});
				return;
			}
		}
		System.out.println("WARNING: Company '" + companyName + "' not found in table. Table has " + tableRows.size() + " rows.");
	}

	/**
	 * Navigate to edit page for a specific field validator
	 * @param name - Name of the field validator to edit
	 */
	public void navigateToEdit(String name) {
		By rowLocator = By.xpath("//table[@class='table table-striped']//tr[contains(., '" + name + "')]");
		wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
		WebElement row = driver.findElement(rowLocator);
		scrollToElement(row);
		WebElement editLink = row.findElement(By.xpath(".//td//a[@data-original-title='Edit']"));
		wait.until(ExpectedConditions.elementToBeClickable(editLink));
		editLink.click();
		waitForPageLoad();
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Click Add, then Cancel — verifies cancel returns to the listing page
	 */
	public void verifyCancel() {
		clickElement(addBtn);
		wait.until(ExpectedConditions.visibilityOf(fvalidatorname));
		clickElement(cancelBtn);
		wait.until(ExpectedConditions.visibilityOf(addBtn));
	}

	/**
	 * Click Cancel on the current form and wait to return to the listing page
	 */
	public void cancelAndWaitForListing() {
		clickElement(cancelBtn);
		wait.until(ExpectedConditions.visibilityOf(addBtn));
	}

	/**
	 * Create a new Field Validator
	 * @param name - Name (also used for description and regex)
	 */
	public void addFieldValidator(String name) {
		clickElement(addBtn);
		wait.until(ExpectedConditions.visibilityOf(fvalidatorname));
		fvalidatorname.sendKeys(name);
		description.sendKeys(name);
		regex.sendKeys(name);
		clickElement(submitBtn);
		waitForPageLoad();
		dismissAlert();
	}

	/**
	 * Edit the name of an existing field validator (already on edit page)
	 * @param name - Current name
	 * @return Updated name (original + "Updated")
	 */
	public String editFieldValidator(String name) {
		wait.until(ExpectedConditions.visibilityOf(fvalidatorname));
		fvalidatorname.clear();
		String updatedName = name + "Updated";
		fvalidatorname.sendKeys(updatedName);
		clickElement(submitBtn);
		wait.until(ExpectedConditions.visibilityOf(addBtn));
		dismissAlert();
		return updatedName;
	}

	/**
	 * Delete a field validator by name
	 * @param name - Name of the field validator to delete
	 */
	public void clickDelete(String name) {
		for (int i = tableBody.size() - 1; i >= 0; i--) {
			if (tableBody.get(i).getAttribute("innerText").contains(name)) {
				scrollToElement(tableBody.get(i));
				WebElement deleteLink = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ name + "')]//td//a[@data-original-title='Delete']"));
				wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
				deleteLink.click();
				break;
			}
		}
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Check if a field validator name is present in the table
	 * @param name - Name to search for
	 * @return true if present, false otherwise
	 */
	public boolean verifyRecordPresent(String name) {
		waitForPageLoad();
		List<WebElement> rows = driver.findElements(
				By.xpath("//table[@class='table table-striped']//tbody//tr"));
		for (WebElement row : rows) {
			if (row.getAttribute("innerText").contains(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the Add Field Validator button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addBtn));
			return addBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if the Submit button is displayed (on create/edit form)
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
	 * Get the page heading text
	 * @return Page heading inner text
	 */
	public String getPageHeading() {
		wait.until(ExpectedConditions.visibilityOf(pageHeading));
		return pageHeading.getAttribute("innerText");
	}

	/**
	 * Wait for success/confirmation alert to disappear after an operation
	 */
	public void waitForAlertToDismiss() {
		dismissAlert();
	}
}

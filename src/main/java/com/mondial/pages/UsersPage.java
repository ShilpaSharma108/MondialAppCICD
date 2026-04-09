package com.mondial.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Users Page Object
 * Represents the Enterprise Setup > Users page and its CRUD operations
 */
public class UsersPage extends BasePage {

	@FindBy(xpath = "//a//span[contains(text(),'Enterprise Setup')]")
	private WebElement enterpriseSetup;

	@FindBy(xpath = "//a[contains(text(),'Users')]")
	private WebElement usersMenu;

	@FindBy(xpath = "//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//div[@class='container']//a[contains(.,' User')]")
	private WebElement addUserBtn;

	@FindBy(xpath = "//input[@id='user_email']")
	private WebElement userMail;

	@FindBy(xpath = "//input[@id='user_password']")
	private WebElement userPW;

	@FindBy(xpath = "//input[@id='user_password_confirmation']")
	private WebElement confirmPW;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement submitBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//table//tbody//tr")
	private List<WebElement> userTable;

	@FindBy(xpath = "//table//tbody//tr//td[2]")
	private List<WebElement> emailColumn;

	@FindBy(xpath = "//div[@class='alert alert-success']")
	private WebElement successMessage;

	@FindBy(xpath = "//i[@class='fa fa-remove ']")
	private WebElement closeError;

	private static final By FAILURE_MSG_BY = By.xpath("//div[@class='alert alert-danger']");

	// Constructor
	public UsersPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Enterprise Setup > Users page via sidebar menu
	 */
	public void navigateToUsersPage() {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//a//span[contains(text(),'Enterprise Setup')]")));
		scrollToElement(enterpriseSetup);
		wait.until(ExpectedConditions.elementToBeClickable(enterpriseSetup));
		enterpriseSetup.click();
		wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//a[contains(text(),'Users')]")));
		usersMenu.click();
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(addUserBtn));
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Click Add User then Cancel — verifies cancel returns to the listing page
	 * @param userEmail - Email to partially type (not needed functionally, kept for API parity)
	 */
	public void verifyCancel(String userEmail) {
		clickElement(addUserBtn);
		wait.until(ExpectedConditions.visibilityOf(submitBtn));
		clickElement(cancelBtn);
		wait.until(ExpectedConditions.visibilityOf(addUserBtn));
	}

	/**
	 * Create a new user with a fixed test password
	 * @param userEmail - Email address for the new user
	 */
	public void createUser(String userEmail) {
		clickElement(addUserBtn);
		wait.until(ExpectedConditions.visibilityOf(submitBtn));
		userMail.sendKeys(userEmail);
		userPW.sendKeys("Testing@1234");
		confirmPW.sendKeys("Testing@1234");
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
		clickElement(submitBtn);
		waitForPageLoad();
	}

	/**
	 * Navigate to the edit page for a specific user
	 * @param recordName - Email of the user to edit
	 */
	public void editRecord(String recordName) {
		By editLocator = By.xpath("//table//td[contains(text(),'" + recordName
				+ "')]//following-sibling::td//a[@data-original-title='Edit']");
		wait.until(ExpectedConditions.presenceOfElementLocated(editLocator));
		driver.findElement(editLocator).click();
		waitForPageLoad();
	}

	/**
	 * Rename a user (already on edit page) — appends "updated" before the domain
	 * @param recordName - Current email of the user
	 * @return Updated email address
	 */
	public String renameRecord(String recordName) {
		wait.until(ExpectedConditions.visibilityOf(userMail));
		String[] temp = recordName.split("@");
		String updatedName = temp[0] + "updated" + "@mailinator.com";
		userMail.clear();
		userMail.sendKeys(updatedName);
		userPW.sendKeys("Testing@1234");
		confirmPW.sendKeys("Testing@1234");
		clickElement(submitBtn);
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(addUserBtn));
		return updatedName;
	}

	/**
	 * Click Cancel on the edit/new form and wait to return to the listing page
	 */
	public void cancelAndWaitForListing() {
		clickElement(cancelBtn);
		wait.until(ExpectedConditions.visibilityOf(addUserBtn));
	}

	/**
	 * Delete a user by email address
	 * @param recordName - Email of the user to delete
	 */
	public void deleteRecord(String recordName) {
		By deleteLocator = By.xpath("//table//td[contains(text(),'" + recordName
				+ "')]//following-sibling::td//a[@data-original-title='Delete']");
		wait.until(ExpectedConditions.presenceOfElementLocated(deleteLocator));
		driver.findElement(deleteLocator).click();
	}

	/**
	 * Assign the Accountant role to a user
	 * @param userEmail - Email of the user
	 */
	public void selectRole(String userEmail) {
		WebElement roleList = driver.findElement(By.xpath(
				"//table//tbody//tr//td[contains(text(),'" + userEmail
				+ "')]//following-sibling::td[2]//select"));
		new Select(roleList).selectByVisibleText("Accountant");
		waitForPageLoad();
	}

	/**
	 * Submit error message test with invalid credentials (on an already-open form)
	 * @param un  - Email to enter
	 * @param pw  - Password to enter
	 * @param cp  - Confirm password to enter
	 * @return    - The failure message text
	 */
	public String errorMessage_invalidCredentials(String un, String pw, String cp) {
		userMail.clear();
		userMail.sendKeys(un);
		userPW.clear();
		userPW.sendKeys(pw);
		confirmPW.clear();
		confirmPW.sendKeys(cp);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
		clickElement(submitBtn);
		// Use refreshed() to tolerate stale element references — the alert-danger div
		// can be replaced by a new DOM node between the wait and the getAttribute call
		// when the form re-renders on consecutive submissions.
		WebElement msgEl = wait.until(
				ExpectedConditions.refreshed(
						ExpectedConditions.visibilityOfElementLocated(FAILURE_MSG_BY)));
		String message = msgEl.getAttribute("innerText");
		clickElement(closeError);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(FAILURE_MSG_BY));
		return message;
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Check whether a user email is present in the table
	 * @param recordName - Email to search for
	 * @return true if present, false otherwise
	 */
	public boolean verifyRecordPresent(String recordName) {
		waitForPageLoad();
		List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
		for (WebElement row : rows) {
			if (row.getAttribute("innerText").contains(recordName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the Add User button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddUserBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addUserBtn));
			return addUserBtn.isDisplayed();
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
	 * Get the success message text
	 * @return Success message inner text
	 */
	public String getSuccessMessage() {
		wait.until(ExpectedConditions.visibilityOf(successMessage));
		return successMessage.getAttribute("innerText");
	}

	/**
	 * Wait for the success alert to disappear
	 */
	public void waitForSuccessMessageToDisappear() {
		dismissAlert();
	}

	/**
	 * Click the Add User button and wait for the create form to appear
	 */
	public void navigateToCreateForm() {
		clickElement(addUserBtn);
		wait.until(ExpectedConditions.visibilityOf(submitBtn));
	}

	/**
	 * Click the Enterprise Setup sidebar link (without waiting for submenu)
	 */
	public void clickEnterpriseSetup() {
		scrollToElement(enterpriseSetup);
		clickElement(enterpriseSetup);
	}

	/**
	 * Check that the Users submenu link is NOT present (e.g. for Consumer role)
	 * @return true if Users link is absent, false if present
	 */
	public boolean isUsersMenuNotPresent() {
		try {
			driver.findElement(By.xpath("//a[contains(text(),'Users')]"));
			return false;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return true;
		}
	}

	/**
	 * Get the failure/error message text
	 * @return Failure message inner text
	 */
	public String getFailureMessage() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(FAILURE_MSG_BY));
		return driver.findElement(FAILURE_MSG_BY).getAttribute("innerText");
	}

	/**
	 * Wait for the failure/error message to disappear
	 */
	public void waitForFailureMessageToDisappear() {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(FAILURE_MSG_BY));
		} catch (Exception e) {
			// already gone
		}
	}
}

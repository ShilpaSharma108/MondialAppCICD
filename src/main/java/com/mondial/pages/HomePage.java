package com.mondial.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class HomePage extends BasePage {
	
	@FindBy(xpath = "//div//h4[contains(text(),'Companies')]")
	private WebElement companyHeading;
	
	@FindBy(xpath = "//a[@data-original-title = 'Home']")
	private WebElement homeButton;
	
	@FindBy(xpath = "//a[contains(text(),'Add Company')]")
	private WebElement addCompanyBtn;
	
	@FindBy(xpath = "//a[@data-original-title = 'Logout']")
	private WebElement logOutBtn;
	
	@FindBy(xpath = "//li//span[contains(text(), 'Accounting Adjustments')]")
	private WebElement accountingAdjustmentMenu;
	
	@FindBy(xpath = "//li//a[contains(text(), 'Management')]")
	private WebElement managementMenu;

	@FindBy(xpath = "//div//h4[contains(text(), 'Management Adjustments')]")
	private WebElement mgmntAdjustHeading;
	
	@FindBy(xpath = "//table[@class='table table-striped']//tr")
	private List<WebElement> companiesTable;
	
	@FindBy(xpath = "//a[@data-original-title='Draft Transactions']")
	private List<WebElement> draftTxns;
	
	@FindBy(xpath = "//a[@data-original-title='New Draft Transaction']")
	private List<WebElement> newDraft;
	
	@FindBy(xpath = "//div[@class='alert alert-success']")
	private WebElement successMessage;
		
	@FindBy(xpath = "//i[@class='ti-help ']")
	private WebElement helpIcon;
	
	@FindBy(xpath = "//ul[@class='nav navbar-nav']//li")
	private WebElement loggedInUser;
	
	@FindBy(xpath = "//a[contains(.,'Submit a request')]")
	private WebElement submitRequest;
	
	// Cognito/Enterprise Setup Elements
	@FindBy(xpath = "//li//a//span[contains(text(), 'Enterprise Setup')]")
	private WebElement enterpriseSetup;
	
	@FindBy(xpath = "//li//a[contains(text(), 'Users')]")
	private WebElement users;
	
	@FindBy(xpath = "//a[contains(text(), 'Add User')]")
	private WebElement addUserButton;
	
	@FindBy(xpath = "//input[@id = 'user_email']")
	private WebElement newUserEmail;
	
	@FindBy(xpath = "//input[@id = 'user_password']")
	private WebElement newUserPW;
	
	@FindBy(xpath = "//input[@id = 'user_password_confirmation']")
	private WebElement confirmPW;
	
	@FindBy(xpath = "//input[@type = 'submit']")
	private WebElement newUserSubmit;
	
	@FindBy(xpath = "//table[@class='table table-striped']//tr")
	private List<WebElement> usersTable;
	
	@FindBy(xpath = "//li[contains(text(),'Email has already been taken')]")
	private WebElement emailAlreadyPresent;
	
	@FindBy(xpath = "//input[@value ='Accept Terms of Service']")
	private WebElement acceptTerms;
	
	// Constructor
	public HomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	// ============================================
	// NAVIGATION METHODS
	// ============================================
	
	/**
	 * Navigate to new draft template for a specific company
	 * @param companyName - Name of the company
	 */
	public void navigateToNewTemplate(String companyName) {
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		clickAccountingAdjustmentMenu();
		clickManagementMenu();
		wait.until(ExpectedConditions.visibilityOf(mgmntAdjustHeading));
		clickNewDraftForCompany(companyName);
	}
	
	/**
	 * Click new draft button for a specific company
	 * @param companyName - Name of the company
	 */
	public void clickNewDraft(String companyName) {
		clickNewDraftForCompany(companyName);
	}
	
	/**
	 * Helper method to click new draft for a company
	 * @param companyName - Name of the company
	 */
	private void clickNewDraftForCompany(String companyName) {
		waitForPageLoad();
		for (int i = 0; i < companiesTable.size(); i++) {
			if (companiesTable.get(i).getAttribute("innerText").contains(companyName)) {
				WebElement path = driver.findElement(By.xpath("//table[@class='table table-striped']//tr[contains(., '" + companyName + "')]//td//a[@data-original-title='New Draft Transaction']"));
				wait.until(ExpectedConditions.elementToBeClickable(path));
				path.click();
				break;
			}
		}
	}
	
	/**
	 * Navigate to drafts template for a specific company
	 * @param companyName - Name of the company
	 */
	public void navigateToDraftsTemplate(String companyName) {
		clickAccountingAdjustmentMenu();
		clickManagementMenu();
		waitForPageLoad();
		for (int i = 0; i < companiesTable.size(); i++) {
			if (companiesTable.get(i).getAttribute("innerText").contains(companyName)) {
				WebElement path = driver.findElement(By.xpath("//table[@class='table table-striped']//tr[contains(., '" + companyName + "')]//td//a[@data-original-title='Draft Transactions']"));
				wait.until(ExpectedConditions.elementToBeClickable(path));
				path.click();
				break;
			}
		}
	}
	
	/**
	 * Click new transaction for a specific company
	 * @param companyName - Name of the company
	 */
	public void clickNewTransaction(String companyName) {
		clickAccountingAdjustmentMenu();
		clickManagementMenu();
		waitForPageLoad();
		for (int i = 0; i < companiesTable.size(); i++) {
			if (companiesTable.get(i).getAttribute("innerText").contains(companyName)) {
				WebElement path = companiesTable.get(i).findElement(By.xpath(".//td//a[@data-original-title='New Draft Transaction']"));
				wait.until(ExpectedConditions.elementToBeClickable(path));
				path.click();
				break;
			}
		}
	}
	
	/**
	 * Click Accounting Adjustment Menu
	 */
	private void clickAccountingAdjustmentMenu() {
		wait.until(ExpectedConditions.elementToBeClickable(accountingAdjustmentMenu));
		accountingAdjustmentMenu.click();
	}
	
	/**
	 * Click Management Menu
	 */
	private void clickManagementMenu() {
		wait.until(ExpectedConditions.elementToBeClickable(managementMenu));
		managementMenu.click();
	}
	
	// ============================================
	// USER MANAGEMENT METHODS
	// ============================================
	
	/**
	 * Create a new user and assign role
	 * @param emailId - Email ID of the new user
	 * @param password - Password for the new user
	 */
	public void createUser(String emailId, String password) {
		wait.until(ExpectedConditions.elementToBeClickable(enterpriseSetup));
		enterpriseSetup.click();
		wait.until(ExpectedConditions.elementToBeClickable(users));
		users.click();
		wait.until(ExpectedConditions.elementToBeClickable(addUserButton));
		addUserButton.click();
		wait.until(ExpectedConditions.visibilityOf(newUserEmail));
		newUserEmail.clear();
		newUserEmail.sendKeys(emailId);
		newUserPW.clear();
		newUserPW.sendKeys(password);
		confirmPW.clear();
		confirmPW.sendKeys(password);
		wait.until(ExpectedConditions.elementToBeClickable(newUserSubmit));
		newUserSubmit.click();
		
		// Wait for user creation to complete
		waitForPageLoad();
		
		selectRole(emailId);
	}
	
	/**
	 * Attempt to create user with existing email (for negative testing)
	 * @param emailId - Email ID that already exists
	 * @param password - Password for the user
	 */
	public void createUserWithExistingEmail(String emailId, String password) {
		wait.until(ExpectedConditions.elementToBeClickable(enterpriseSetup));
		enterpriseSetup.click();
		wait.until(ExpectedConditions.elementToBeClickable(users));
		users.click();
		wait.until(ExpectedConditions.visibilityOf(addUserButton));
		addUserButton.click();
		wait.until(ExpectedConditions.visibilityOf(newUserEmail));
		newUserEmail.clear();
		newUserEmail.sendKeys(emailId);
		newUserPW.clear();
		newUserPW.sendKeys(password);
		confirmPW.clear();
		confirmPW.sendKeys(password);
		wait.until(ExpectedConditions.elementToBeClickable(newUserSubmit));
		newUserSubmit.click();
	}

	/**
	 * Select role for a user
	 * @param userName - Username/Email of the user
	 */
	public void selectRole(String userName) {
		waitForPageLoad();
		for (int i = usersTable.size() - 1; i > 0; i--) {
			if (usersTable.get(i).getAttribute("innerText").contains(userName.toLowerCase())) {
				WebElement roleDropdown = driver.findElement(By.xpath("//table[@class='table table-striped']//tr//td[contains(text(), '" + userName.toLowerCase() + "')]//following-sibling::td[2]//select"));
				wait.until(ExpectedConditions.elementToBeClickable(roleDropdown));
				Select selectRole = new Select(roleDropdown);
				selectRole.selectByVisibleText("Accountant");
				
				// Wait for role assignment to complete
				waitForPageLoad();
				break;
			}
		}
	}
	
	/**
	 * Delete a user from the users table
	 * Navigates to Enterprise Setup > Users, finds and deletes the user
	 * @param userName - Username/Email of the user to delete
	 */
	public void deleteUser(String userName) {
		wait.until(ExpectedConditions.elementToBeClickable(enterpriseSetup));
		enterpriseSetup.click();
		wait.until(ExpectedConditions.elementToBeClickable(users));
		users.click();
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOfAllElements(usersTable));

		for (int i = usersTable.size() - 1; i > 0; i--) {
			if (usersTable.get(i).getAttribute("innerText").contains(userName.toLowerCase())) {
				WebElement deleteBtn = driver.findElement(
					By.xpath("//table[@class='table table-striped']//tr[contains(., '" +
						userName.toLowerCase() + "')]//a[@data-method='delete']"));
				wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
				deleteBtn.click();

				// Handle browser confirmation dialog if present
				try {
					driver.switchTo().alert().accept();
				} catch (Exception e) {
					System.out.println("No confirmation dialog present");
				}

				waitForPageLoad();
				System.out.println("Deleted user: " + userName);
				break;
			}
		}
	}

	/**
	 * Check if user is present in the users table
	 * @param userName - Username/Email to check
	 * @return true if user exists, false otherwise
	 */
	public boolean isUserPresent(String userName) {
		wait.until(ExpectedConditions.visibilityOfAllElements(usersTable));
		for (int i = usersTable.size() - 1; i > 0; i--) {
			if (usersTable.get(i).getAttribute("innerText").contains(userName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	// ============================================
	// VERIFICATION METHODS
	// ============================================
	
	/**
	 * Check if company heading is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isCompanyHeadingDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(companyHeading));
			return companyHeading.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Get success message text
	 * @return Success message text or empty string if not found
	 */
	public String getSuccessMessage() {
		try {
			wait.until(ExpectedConditions.visibilityOf(successMessage));
			return successMessage.getText();
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Check if email already exists error is displayed
	 * @return true if error displayed, false otherwise
	 */
	public boolean isEmailAlreadyPresentErrorDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(emailAlreadyPresent));
			return emailAlreadyPresent.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Check if Accept Terms button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAcceptTermsDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(acceptTerms));
			return acceptTerms.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	// ============================================
	// ACTION METHODS
	// ============================================
	
	/**
	 * Click Accept Terms of Service button
	 */
	public void clickAcceptTerms() {
		wait.until(ExpectedConditions.elementToBeClickable(acceptTerms));
		acceptTerms.click();
	}
	
	/**
	 * Click Logout button
	 */
	public void clickLogout() {
		wait.until(ExpectedConditions.elementToBeClickable(logOutBtn));
		logOutBtn.click();
	}
	
	/**
	 * Click Home button
	 */
	public void clickHome() {
		wait.until(ExpectedConditions.elementToBeClickable(homeButton));
		homeButton.click();
	}
	
	/**
	 * Click Add Company button
	 */
	public void clickAddCompany() {
		wait.until(ExpectedConditions.elementToBeClickable(addCompanyBtn));
		addCompanyBtn.click();
	}
	
	// ============================================
	// COMPATIBILITY METHODS (for framework)
	// ============================================
	
	/**
	 * Get welcome message (for framework compatibility)
	 * @return Welcome message text
	 */
	public String getWelcomeMessage() {
		if (isCompanyHeadingDisplayed()) {
			return companyHeading.getText();
		}
		return "Welcome";
	}
	
	/**
	 * Check if welcome message is displayed (for framework compatibility)
	 * @return true if displayed, false otherwise
	 */
	public boolean isWelcomeMessageDisplayed() {
		return isCompanyHeadingDisplayed();
	}
	
	/**
	 * Logout and return to login page (for framework compatibility)
	 * @return LoginPage object
	 */
	public LoginPage logout() {
		clickLogout();
		return new LoginPage(driver);
	}
	
}
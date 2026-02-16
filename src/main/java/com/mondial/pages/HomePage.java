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
	
	@FindBy(xpath = "//ul[contains(@class,'navbar-nav')]//li")
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
				scrollToElement(companiesTable.get(i));
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
				scrollToElement(companiesTable.get(i));
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
				scrollToElement(companiesTable.get(i));
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
		clickElement(accountingAdjustmentMenu);
	}

	/**
	 * Click Management Menu
	 */
	private void clickManagementMenu() {
		clickElement(managementMenu);
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
		waitForPageLoad();
		clickElement(enterpriseSetup);
		clickElement(users);
		clickElement(addUserButton);
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
		waitForPageLoad();
		clickElement(enterpriseSetup);
		clickElement(users);
		clickElement(addUserButton);
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
		waitForPageLoad();
		clickElement(enterpriseSetup);
		wait.until(ExpectedConditions.visibilityOf(users));
		clickElement(users);
		waitForPageLoad();
		// Wait until we're on the Users page
		wait.until(d -> {
			try {
				return d.getCurrentUrl().contains("user");
			} catch (Exception e) {
				return false;
			}
		});

		// Scroll to bottom to ensure all tables are loaded
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		waitForPageLoad();

		// Find the row containing the user across ALL tables
		List<WebElement> matchingRows = driver.findElements(
			By.xpath("//table[@class='table table-striped']//tr[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + userName.toLowerCase() + "')]"));
		System.out.println("Found " + matchingRows.size() + " row(s) matching '" + userName + "'");

		if (matchingRows.size() > 0) {
			WebElement row = matchingRows.get(0);
			scrollToElement(row);
			WebElement deleteBtn = row.findElement(By.xpath(".//a[@data-method='delete']"));
			wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
			deleteBtn.click();

			try {
				wait.until(ExpectedConditions.alertIsPresent());
				driver.switchTo().alert().accept();
			} catch (Exception e) {
				System.out.println("No confirmation dialog present");
			}

			waitForPageLoad();
			System.out.println("Deleted user: " + userName);
		} else {
			System.out.println("WARNING: User '" + userName + "' not found in users table");
		}
	}

	/**
	 * Check if user is present in the users table
	 * @param userName - Username/Email to check
	 * @return true if user exists, false otherwise
	 */
	public boolean isUserPresent(String userName) {
		// Scroll to bottom to ensure all tables are loaded
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		waitForPageLoad();

		List<WebElement> matchingRows = driver.findElements(
			By.xpath("//table[@class='table table-striped']//tr[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + userName.toLowerCase() + "')]"));
		return matchingRows.size() > 0;
	}

	/**
	 * Navigate to Users page and delete all users whose email starts with the given prefix
	 * @param prefix - Email prefix to match (e.g., "testuser")
	 * @return Number of users deleted
	 */
	public int deleteAllUsersWithPrefix(String prefix) {
		waitForPageLoad();
		clickElement(enterpriseSetup);
		wait.until(ExpectedConditions.visibilityOf(users));
		clickElement(users);
		waitForPageLoad();
		// Wait until we're no longer on the Companies page
		wait.until(d -> {
			try {
				String url = d.getCurrentUrl();
				return url.contains("user");
			} catch (Exception e) {
				return false;
			}
		});

		System.out.println("Navigated to Users page: " + driver.getCurrentUrl());

		// Scroll to bottom to ensure all tables are loaded
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		waitForPageLoad();

		int deletedCount = 0;
		boolean found = true;

		while (found) {
			found = false;
			// Find rows containing the prefix across ALL tables on the page
			List<WebElement> matchingRows = driver.findElements(
					By.xpath("//table[@class='table table-striped']//tr[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + prefix.toLowerCase() + "')]"));
			System.out.println("Found " + matchingRows.size() + " row(s) matching '" + prefix + "'");

			if (matchingRows.size() > 0) {
				WebElement row = matchingRows.get(0);
				String rowText = row.getAttribute("innerText").trim();
				System.out.println("Deleting user: " + rowText.substring(0, Math.min(80, rowText.length())));
				scrollToElement(row);
				WebElement deleteBtn = row.findElement(
						By.xpath(".//a[@data-method='delete']"));
				wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
				deleteBtn.click();

				try {
					wait.until(ExpectedConditions.alertIsPresent());
					driver.switchTo().alert().accept();
				} catch (Exception e) {
					System.out.println("No confirmation dialog present");
				}

				waitForPageLoad();
				// Refresh to get updated table
				driver.navigate().refresh();
				waitForPageLoad();
				// Scroll to bottom again after refresh
				((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
				waitForPageLoad();

				deletedCount++;
				found = true;
			}
		}
		return deletedCount;
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
		clickElement(acceptTerms);
	}

	/**
	 * Click Logout button
	 */
	public void clickLogout() {
		clickElement(logOutBtn);
	}

	/**
	 * Click Home button
	 */
	public void clickHome() {
		clickElement(homeButton);
	}

	/**
	 * Click Add Company button
	 */
	public void clickAddCompany() {
		clickElement(addCompanyBtn);
	}
	
	// ============================================
	// ZENDESK / HELP METHODS
	// ============================================

	/**
	 * Click the Help (?) icon
	 */
	public void clickHelpIcon() {
		clickElement(helpIcon);
	}

	/**
	 * Wait for Submit Request link on Zendesk page
	 */
	public void waitForSubmitRequest() {
		wait.until(ExpectedConditions.visibilityOf(submitRequest));
	}

	/**
	 * Get the logged in user's display name
	 * @return Logged in user name text
	 */
	public String getLoggedInUserName() {
		wait.until(ExpectedConditions.visibilityOf(loggedInUser));
		return loggedInUser.getAttribute("innerText");
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
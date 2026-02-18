package com.mondial.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Chart of Accounts Page Object
 * Represents the Chart of Accounts page and its CRUD operations for GL Accounts
 */
public class ChartOfAccountsPage extends BasePage {

	@FindBy(xpath = "//div//h4[contains(text(),'Companies')]")
	private WebElement companyHeading;

	@FindBy(xpath = "//table[@class='table table-striped']//tbody//tr")
	private List<WebElement> tableName;

	@FindBy(xpath = "//div[@ref='eContainer']//div[@role='row']")
	private List<WebElement> container;

	@FindBy(xpath = "//div//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//div//h5")
	private WebElement subPageHeading;

	@FindBy(xpath = "//a[contains(text(),'Add Gl account')]")
	private WebElement addGLAccountBtn;

	@FindBy(id = "gl_account_descriptor_account_number")
	private WebElement accountNumber;

	@FindBy(id = "gl_account_descriptor_name")
	private WebElement accountName;

	@FindBy(id = "gl_account_descriptor_description")
	private WebElement description;

	@FindBy(id = "gl_account_descriptor_short_description")
	private WebElement shortDesc;

	@FindBy(xpath = "//select[@id='gl_account_gl_account_type_id']")
	private WebElement type;

	@FindBy(xpath = "//select[@id='gl_account_gl_account_subtype_id']")
	private WebElement subtype;

	@FindBy(xpath = "//select[@id='gl_account_currency_id']")
	private WebElement currency;

	@FindBy(id = "createGlAccount")
	private WebElement createBtn;

	@FindBy(id = "updateGlAccount")
	private WebElement updateBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//div[@class='alert alert-success']")
	private WebElement successMessage;

	@FindBy(xpath = "//div[@class='alert-danger']")
	private WebElement failureMessage;

	@FindBy(xpath = "//ul//li[contains(text(), 'Gl account segments Limit exceeded')]")
	private WebElement errorMsg;

	@FindBy(xpath = "//input[@type = 'submit'][@data-disable-with = 'Upload CSV']")
	private WebElement uploadCSVBtn;

	@FindBy(xpath = "//span[contains(text(),'Choose Csv')]")
	private WebElement chooseCSV;

	@FindBy(xpath = "//a[contains(text(),'Download template CSV file')]")
	private WebElement downloadLink;

	@FindBy(xpath = "//a[contains(text(),'Download Table as CSV')]")
	private WebElement downloadTable;

	// Constructor
	public ChartOfAccountsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Chart of Accounts page for a specific company
	 * @param companyName - Name of the company
	 */
	public void navigateToChartOfAccounts(String companyName) {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		waitForPageLoad();
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(companyName)) {
				scrollToElement(tableName.get(i));
				WebElement link = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ companyName + "')]//td//a[@data-original-title='Chart of Accounts']"));
				clickElement(link);
				if (!waitForTextChange(pageHeading, "Companies", 10)) {
					System.out.println("First click did not navigate, retrying...");
					link = driver.findElement(By.xpath(
							"//table[@class='table table-striped']//tr[contains(., '"
							+ companyName + "')]//td//a[@data-original-title='Chart of Accounts']"));
					scrollToElement(link);
					((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
					waitForTextChange(pageHeading, "Companies", 10);
				}
				waitForPageLoad();
				return;
			}
		}
		System.out.println("WARNING: Company '" + companyName + "' not found in table.");
	}

	/**
	 * Navigate to edit page for a specific GL Account
	 * @param name - Name/number of the GL Account to edit
	 */
	public void navigateToEdit(String name) {
		for (int i = 0; i < container.size(); i++) {
			if (container.get(i).getAttribute("innerText").contains(name)) {
				WebElement editLink = driver.findElement(By.xpath(
						"//div[@ref='eContainer']//div[@role='row']//div[contains(., '"
						+ name + "')]//following-sibling::div[contains(.,'Edit')]"));
				clickElement(editLink);
				break;
			}
		}
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Verify cancel button returns to listing page
	 */
	public void verifyCancelCreation() {
		wait.until(ExpectedConditions.elementToBeClickable(uploadCSVBtn));
		clickElement(addGLAccountBtn);
		wait.until(ExpectedConditions.visibilityOf(createBtn));
		scrollToElement(cancelBtn);
		clickElement(cancelBtn);
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
		wait.until(ExpectedConditions.visibilityOf(addGLAccountBtn));
	}

	/**
	 * Create a new GL Account with valid data
	 * @param number - Account number
	 */
	public void createNewGLAccount(String number) {
		wait.until(ExpectedConditions.visibilityOf(subPageHeading));
		accountNumber.sendKeys(number);
		accountName.sendKeys("TestName");
		description.sendKeys("TestDesc");
		shortDesc.sendKeys("TestShortDesc");
		wait.until(ExpectedConditions.elementToBeClickable(type));
		new Select(type).selectByVisibleText("Asset");
		wait.until(ExpectedConditions.elementToBeClickable(subtype));
		new Select(subtype).selectByVisibleText("Cash");
		wait.until(ExpectedConditions.elementToBeClickable(currency));
		new Select(currency).selectByVisibleText("USD");
		clickElement(createBtn);
		waitForPageLoad();
	}

	/**
	 * Attempt to create a GL Account with incorrect/duplicate number
	 * @param number - Account number
	 */
	public void incorrectGLAccount(String number) {
		wait.until(ExpectedConditions.visibilityOf(subPageHeading));
		accountNumber.clear();
		accountNumber.sendKeys(number);
		accountName.clear();
		accountName.sendKeys("TestName");
		description.clear();
		description.sendKeys("TestDesc");
		shortDesc.clear();
		shortDesc.sendKeys("TestShortDesc");
		wait.until(ExpectedConditions.elementToBeClickable(type));
		new Select(type).selectByVisibleText("Asset");
		wait.until(ExpectedConditions.elementToBeClickable(subtype));
		new Select(subtype).selectByVisibleText("Cash");
		wait.until(ExpectedConditions.elementToBeClickable(currency));
		new Select(currency).selectByVisibleText("USD");
		clickElement(createBtn);
		waitForPageLoad();
	}

	/**
	 * Attempt to create a GL Account with incomplete data (no type/subtype/currency)
	 * @param number - Account number
	 */
	public void incompleteGLAccount(String number) {
		wait.until(ExpectedConditions.visibilityOf(subPageHeading));
		accountNumber.clear();
		accountNumber.sendKeys(number);
		accountName.sendKeys("TestName");
		description.sendKeys("TestDesc");
		shortDesc.sendKeys("TestShortDesc");
		clickElement(createBtn);
		waitForPageLoad();
	}

	/**
	 * Edit a GL Account name
	 * @param name - Current name of the GL Account
	 * @return Updated name (original + "Updated")
	 */
	public String editGlAccount(String name) {
		accountName.clear();
		String updatedName = name + "Updated";
		accountName.sendKeys(updatedName);
		clickElement(updateBtn);
		waitForPageLoad();
		return updatedName;
	}

	/**
	 * Click delete button for a specific GL Account and accept confirmation dialog.
	 * Uses direct click (not clickElement) so the browser confirm() dialog is not bypassed.
	 * @param name - Name/number of the GL Account to delete
	 */
	public void clickDelete(String name) {
		for (int i = 0; i < container.size(); i++) {
			if (container.get(i).getAttribute("innerText").contains(name)) {
				WebElement deleteLink = driver.findElement(By.xpath(
						"//div[@ref='eContainer']//div[@role='row']//div[contains(., '"
						+ name + "')]//following-sibling::div[contains(.,'Delete')]"));
				scrollToElement(deleteLink);
				wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
				deleteLink.click();
				// Wait for and accept the browser confirmation dialog
				try {
					wait.until(ExpectedConditions.alertIsPresent());
					driver.switchTo().alert().accept();
				} catch (Exception e) {
					System.out.println("No confirmation dialog present");
				}
				waitForPageLoad();
				break;
			}
		}
	}

	/**
	 * Delete all GL Accounts in the AG Grid table.
	 * Iterates through rows, clicking Delete and accepting the confirm dialog for each.
	 */
	public void deleteAllGLAccounts() {
		waitForPageLoad();
		int rowCount = container.size();
		System.out.println("Records to delete: " + rowCount);
		for (int i = 0; i < rowCount; i++) {
			try {
				WebElement deleteLink = driver.findElement(By.xpath(
						"//div[@ref='eContainer']//div[@role='row'][1]//div[contains(.,'Delete')]"));
				scrollToElement(deleteLink);
				wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
				deleteLink.click();
				try {
					new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(3))
							.until(ExpectedConditions.alertIsPresent());
					driver.switchTo().alert().accept();
				} catch (Exception e) {
					System.out.println("No confirmation dialog present");
				}
				// Wait for success message and dismiss it
				waitForSuccessMessageToDisappear();
				waitForPageLoad();
				// Refresh page so the AG Grid reloads without the deleted record
				driver.navigate().refresh();
				waitForPageLoad();
			} catch (Exception e) {
				System.out.println("No more records to delete or error: " + e.getMessage());
				break;
			}
		}
		System.out.println("All GL Accounts deleted");
	}

	/**
	 * Check if any GL Account records exist in the AG Grid
	 * @return true if records are present, false otherwise
	 */
	public boolean hasRecords() {
		waitForPageLoad();
		return container.size() > 0;
	}

	/**
	 * Click the Add GL Account button
	 */
	public void clickAddGLAccountBtn() {
		clickElement(addGLAccountBtn);
		wait.until(ExpectedConditions.visibilityOf(createBtn));
	}

	/**
	 * Click the Cancel button
	 */
	public void clickCancel() {
		clickElement(cancelBtn);
		waitForPageLoad();
	}

	/**
	 * Click Cancel and wait for listing page
	 */
	public void clickCancelAndWaitForListing() {
		clickElement(cancelBtn);
		wait.until(ExpectedConditions.visibilityOf(addGLAccountBtn));
	}

	// ============================================
	// CSV UPLOAD / DOWNLOAD METHODS
	// ============================================

	/**
	 * Upload a CSV file
	 * @param filePath - Full path of the CSV file to upload
	 */
	public void uploadCSVFile(String filePath) {
		WebElement inputBox = driver.findElement(By.xpath("//input[@id='upload_file']"));
		inputBox.sendKeys(filePath);
		clickElement(uploadCSVBtn);
		waitForPageLoad();
	}

	/**
	 * Download the template CSV file
	 */
	public void downloadTemplateCSV() {
		waitForPageLoad();
		clickElement(downloadLink);
		waitForPageLoad();
	}

	/**
	 * Check if a file was downloaded to the given path
	 * @param filePath - Full path to the expected downloaded file
	 * @return true if file exists, false otherwise
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
	 * Find a downloaded file in the given directory that contains the specified keyword.
	 * The app prepends the company name to downloaded CSV filenames, so this method
	 * searches by keyword rather than requiring the exact filename.
	 * @param downloadDir - Directory to search in
	 * @param keyword - Keyword the filename must contain (e.g., "local_chart_of_accounts")
	 * @return Full path of the matching file, or null if not found
	 */
	public String findDownloadedFile(String downloadDir, String keyword) {
		java.io.File dir = new java.io.File(downloadDir);
		int maxWaitSeconds = 15;
		for (int i = 0; i < maxWaitSeconds; i++) {
			java.io.File[] matches = dir.listFiles((d, name) ->
					name.toLowerCase().contains(keyword.toLowerCase()) && name.endsWith(".csv"));
			if (matches != null && matches.length > 0) {
				System.out.println("Found downloaded file: " + matches[0].getAbsolutePath());
				return matches[0].getAbsolutePath();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		System.out.println("No file containing '" + keyword + "' found in " + downloadDir);
		return null;
	}

	/**
	 * Click the Download Table as CSV link
	 */
	public void clickDownloadTable() {
		clickElement(downloadTable);
		waitForPageLoad();
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Verify if a GL Account exists in the AG Grid container
	 * @param name - Name/number to search for
	 * @return true if GL Account is present, false otherwise
	 */
	public boolean verifyGLAccount(String name) {
		waitForPageLoad();
		for (int i = 0; i < container.size(); i++) {
			if (container.get(i).getAttribute("innerText").contains(name)) {
				System.out.println("GL Account found: " + name);
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the Add GL Account button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddGLAccountBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(addGLAccountBtn));
			return addGLAccountBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if the Update button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isUpdateBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(updateBtn));
			return updateBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
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
	 * Check if failure message is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isFailureMessageDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(failureMessage));
			return failureMessage.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if segment limit error is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isSegmentLimitErrorDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(errorMsg));
			return errorMsg.isDisplayed();
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

	/**
	 * Delete all records matching a name from the table
	 * @param name - Name to match for deletion
	 */
	public void deleteAll(String name) {
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(name)) {
				WebElement deleteLink = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ name + "')]//td//a[@data-original-title='Delete']"));
				clickElement(deleteLink);
				try {
					wait.until(ExpectedConditions.alertIsPresent());
					driver.switchTo().alert().accept();
				} catch (Exception e) {
					System.out.println("No confirmation dialog present");
				}
				waitForPageLoad();
			}
		}
	}
}

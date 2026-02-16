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
 * Reporting Segment (GL Account Segment) Page Object
 * Represents the GL Account Segments page and its CRUD operations
 */
public class ReportingSegmentPage extends BasePage {

	@FindBy(xpath = "//div//h4[contains(text(),'Companies')]")
	private WebElement companyHeading;

	@FindBy(xpath = "//table[@class='table table-striped']//tbody//tr")
	private List<WebElement> tableName;

	@FindBy(xpath = "//div//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//a[contains(text(),'Add GL Account Segment')]")
	private WebElement addGLSegmentBtn;

	@FindBy(xpath = "//a[contains(text(),'Add GL Account Segment Option')]")
	private WebElement addGLSegmentOptionsBtn;

	@FindBy(xpath = "//input[@id='gl_account_segment_ordinal']")
	private WebElement ordinal;

	@FindBy(xpath = "//input[@id='gl_account_segment_option_name']")
	private WebElement optionsName;

	@FindBy(xpath = "//input[@id='gl_account_segment_option_short_description']")
	private WebElement shortDesc;

	@FindBy(xpath = "//input[@id='gl_account_segment_option_code']")
	private WebElement optionsCode;

	@FindBy(xpath = "//input[@id='gl_account_segment_name']")
	private WebElement glName;

	@FindBy(xpath = "//select[@id='gl_account_segment_field_validator_id']")
	private WebElement fieldValidator;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement createBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//div[contains(text(),'Chart Name was successfully deleted.')]")
	private WebElement confirmationMsg;

	@FindBy(xpath = "//ul//li[contains(text(), 'Gl account segments Limit exceeded')]")
	private WebElement errorMsg;

	@FindBy(xpath = "//ul//li[contains(text(), \"Name can't be blank\")]")
	private WebElement nameBlankError;

	@FindBy(xpath = "//ul//li[contains(text(), \"Field validator can't be blank\")]")
	private WebElement fieldValidatorBlankError;

	@FindBy(xpath = "//ul//li[contains(text(), 'Ordinal must be greater than 1')]")
	private WebElement ordinalError;

	@FindBy(xpath = "//table[@class='table table-striped']//tbody//tr[2]//td//a[@data-original-title='Options']")
	private WebElement segmentOptions;

	@FindBy(xpath = "//input[@type = 'submit'][@data-disable-with = 'Upload CSV']")
	private WebElement uploadCSVBtn;

	@FindBy(xpath = "//a[contains(text(),'Download template CSV file')]")
	private WebElement downloadLink;

	@FindBy(xpath = "//a[contains(text(),'Download Table as CSV')]")
	private WebElement downloadTable;

	// Constructor
	public ReportingSegmentPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Reporting Segments page for a specific company
	 * Scrolls to bottom first as company may not be visible
	 * @param companyName - Name of the company
	 */
	public void navigateToReportingSegment(String companyName) {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		// Scroll to bottom first as company may not be visible
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		waitForPageLoad();
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(companyName)) {
				scrollToElement(tableName.get(i));
				WebElement link = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ companyName + "')]//td//a[@data-original-title='Reporting Segments']"));
				clickElement(link);
				// Wait for heading to change from "Companies" (up to 10s)
				if (!waitForTextChange(pageHeading, "Companies", 10)) {
					System.out.println("First click did not navigate, retrying...");
					link = driver.findElement(By.xpath(
							"//table[@class='table table-striped']//tr[contains(., '"
							+ companyName + "')]//td//a[@data-original-title='Reporting Segments']"));
					scrollToElement(link);
					((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
					waitForTextChange(pageHeading, "Companies", 10);
				}
				waitForPageLoad();
				return;
			}
		}
		System.out.println("WARNING: Company '" + companyName + "' not found in table. Table has " + tableName.size() + " rows.");
	}

	/**
	 * Navigate to edit page for a specific segment
	 * @param name - Name of the segment to edit
	 */
	public void navigateToEdit(String name) {
		WebElement row = driver.findElement(By.xpath(
				"//table[@class='table table-striped']//tr[contains(., '"
				+ name + "')]"));
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
	 * Open Add GL Segment form and fill in name and validator
	 * Does NOT click Create - caller controls submit vs cancel
	 * @param name - Name for the new segment
	 */
	public void createNewSegment(String name) {
		clickElement(addGLSegmentBtn);
		wait.until(ExpectedConditions.visibilityOf(glName));
		glName.sendKeys(name);
		new Select(fieldValidator).selectByVisibleText("Anything Goes");
	}

	/**
	 * Open Add GL Segment form without filling any fields
	 */
	public void clickAddGLSegmentBtn() {
		clickElement(addGLSegmentBtn);
		wait.until(ExpectedConditions.visibilityOf(glName));
	}

	/**
	 * Enter value in the Name field
	 * @param name - Name to enter
	 */
	public void enterSegmentName(String name) {
		glName.clear();
		glName.sendKeys(name);
	}

	/**
	 * Select a field validator from the dropdown
	 * @param validatorName - Visible text of the validator option
	 */
	public void selectFieldValidator(String validatorName) {
		new Select(fieldValidator).selectByVisibleText(validatorName);
	}

	/**
	 * Enter value in the Ordinal field
	 * @param value - Ordinal value to enter
	 */
	public void enterOrdinal(String value) {
		ordinal.clear();
		ordinal.sendKeys(value);
	}

	/**
	 * Check if "Name can't be blank" error is displayed
	 * @return true if error is displayed
	 */
	public boolean isNameBlankErrorDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(nameBlankError));
			return nameBlankError.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if "Field validator can't be blank" error is displayed
	 * @return true if error is displayed
	 */
	public boolean isFieldValidatorBlankErrorDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(fieldValidatorBlankError));
			return fieldValidatorBlankError.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if "Ordinal must be greater than 1" error is displayed
	 * @return true if error is displayed
	 */
	public boolean isOrdinalErrorDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(ordinalError));
			return ordinalError.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Edit an existing segment name
	 * Does NOT click Create - caller controls submit vs cancel
	 * @param name - Current name of the segment
	 * @return Updated name (original + "Updated")
	 */
	public String editSegment(String name) {
		wait.until(ExpectedConditions.visibilityOf(glName));
		glName.clear();
		String updatedName = name + "Updated";
		glName.sendKeys(updatedName);
		return updatedName;
	}

	/**
	 * Delete a segment by name
	 * @param name - Name of the segment to delete
	 */
	public void clickDelete(String name) {
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(name)) {
				scrollToElement(tableName.get(i));
				WebElement deleteLink = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ name + "')]//td//a[@data-original-title='Delete']"));
				wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
				deleteLink.click();
				break;
			}
		}
	}

	/**
	 * Click the Create/Submit button
	 */
	public void clickCreateButton() {
		clickElement(createBtn);
		waitForPageLoad();
	}

	/**
	 * Click the Cancel button
	 */
	public void clickCancel() {
		clickElement(cancelBtn);
		waitForPageLoad();
	}

	// ============================================
	// SEGMENT OPTIONS METHODS
	// ============================================

	/**
	 * Click the Options link on the 2nd table row
	 */
	public void clickSegmentOptions() {
		clickElement(segmentOptions);
	}

	/**
	 * Open Add GL Segment Option form and fill in details
	 * Does NOT click Create - caller controls submit vs cancel
	 * @param name - Option name
	 * @param code - Option code
	 */
	public void fillSegmentOptionForm(String name, String code) {
		clickElement(addGLSegmentOptionsBtn);
		wait.until(ExpectedConditions.visibilityOf(optionsName));
		optionsName.sendKeys(name);
		shortDesc.sendKeys("Test Desc");
		optionsCode.sendKeys(code);
	}

	/**
	 * Edit an existing segment option name
	 * Does NOT click Create - caller controls submit vs cancel
	 * @param name - Current name of the option
	 * @return Updated name (original + "Updated")
	 */
	public String editSegmentOptions(String name) {
		wait.until(ExpectedConditions.visibilityOf(optionsName));
		optionsName.clear();
		String updatedName = name + "Updated";
		optionsName.sendKeys(updatedName);
		return updatedName;
	}

	/**
	 * Check if the Add GL Account Segment Option button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddGLSegmentOptionsBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addGLSegmentOptionsBtn));
			return addGLSegmentOptionsBtn.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the Account Segments count for a company from the companies table
	 * @param companyName - Name of the company
	 * @return Account segment count
	 */
	public int getAccountSegmentCount(String companyName) {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		waitForPageLoad();
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(companyName)) {
				scrollToElement(tableName.get(i));
				WebElement countCell = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ companyName + "')]//td[4]"));
				return Integer.parseInt(countCell.getAttribute("innerText").trim());
			}
		}
		return 0;
	}

	/**
	 * Create multiple reporting segments to fill remaining slots, then create a segment option
	 * @param segmentBaseName - Base name for new segments
	 * @param optionName - Name for the segment option
	 * @param numOfSegments - Number of segments to create
	 */
	public void createReportingSegmentOptions(String segmentBaseName, String optionName, int numOfSegments) {
		int num = 1;
		for (int i = 0; i < numOfSegments; i++) {
			if (!isAddGLSegmentBtnDisplayed()) {
				System.out.println("Segment limit reached after creating " + (num - 1) + " segments. Stopping creation.");
				break;
			}
			String segmentName = segmentBaseName + num;
			createNewSegment(segmentName);
			clickCreateButton();
			waitForPageLoad();
			num++;
		}
		// Navigate to Options for the 2nd segment
		clickSegmentOptions();
		waitForPageLoad();
		// Test cancel on Add Option form
		fillSegmentOptionForm("temp", "0000");
		clickCancel();
		isAddGLSegmentOptionsBtnDisplayed();
		// Create the actual option
		fillSegmentOptionForm(optionName, "1234");
		clickCreateButton();
		waitForPageLoad();
	}

	/**
	 * Delete all segments on the current page
	 */
	public void deleteAllSegments() {
		waitForPageLoad();
		int rowCount = tableName.size();
		for (int i = 0; i < rowCount; i++) {
			try {
				WebElement deleteLink = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//td//a[@data-original-title='Delete']"));
				clickElement(deleteLink);
				try {
					wait.until(ExpectedConditions.alertIsPresent());
					driver.switchTo().alert().accept();
				} catch (Exception e) {
					// No alert
				}
				waitForPageLoad();
				driver.navigate().refresh();
				waitForPageLoad();
			} catch (Exception e) {
				break;
			}
		}
	}

	/**
	 * Navigate to Reporting Segments page for a company and delete all segments
	 * @param segmentName - Segment name to verify deletion
	 * @param companyName - Company to navigate to
	 */
	public void deleteReportingSegment(String segmentName, String companyName) {
		navigateToReportingSegment(companyName);
		isAddGLSegmentBtnDisplayed();
		deleteAllSegments();
		System.out.println("All segments deleted successfully");
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Verify if a segment exists in the table
	 * @param name - Name to search for
	 * @return true if segment is present, false otherwise
	 */
	public boolean verifySegmentExists(String name) {
		waitForPageLoad();
		List<WebElement> table = driver.findElements(
				By.xpath("//table[@class='table table-striped']//tbody//tr"));
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getAttribute("innerText").contains(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the Add GL Account Segment button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddGLSegmentBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addGLSegmentBtn));
			return addGLSegmentBtn.isDisplayed();
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
			wait.until(ExpectedConditions.visibilityOf(createBtn));
			return createBtn.isDisplayed();
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
	 * Check if the segment limit exceeded error is displayed
	 * @return true if error displayed, false otherwise
	 */
	public boolean isSegmentLimitErrorDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(errorMsg));
			return errorMsg.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// ============================================
	// CSV UPLOAD / DOWNLOAD METHODS
	// ============================================

	/**
	 * Download template CSV, upload it, and wait for completion
	 */
	public void uploadCSV() {
		waitForPageLoad();
		clickElement(downloadLink);
		waitForPageLoad();
		WebElement inputBox = driver.findElement(By.xpath("//input[@id='upload_file']"));
		inputBox.sendKeys(com.mondial.utils.DriverManager.getDownloadDir() + java.io.File.separator + "gl_account_segment_options_csv_upload_template.csv");
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
	 * Check if the template CSV file was downloaded to the given path
	 * @param filePath - Full path to the expected downloaded file
	 * @return true if file exists, false otherwise
	 */
	public boolean isTemplateCSVDownloaded(String filePath) {
		java.io.File file = new java.io.File(filePath);
		return file.exists();
	}

	/**
	 * Upload a CSV file using the file input and click Upload CSV button
	 * @param filePath - Full path of the CSV file to upload
	 */
	public void uploadCSVFile(String filePath) {
		waitForPageLoad();
		WebElement inputBox = driver.findElement(By.xpath("//input[@id='upload_file']"));
		inputBox.sendKeys(filePath);
		clickElement(uploadCSVBtn);
		waitForPageLoad();
	}

	/**
	 * Verify records uploaded via CSV are present in the table
	 * Checks that codes 100, 101, 102 exist in the 3rd column
	 * @return true if expected records are found
	 */
	public boolean verifyRecordsUploaded() {
		waitForPageLoad();
		java.util.List<String> expectedCodes = java.util.Arrays.asList("100", "101", "102");
		java.util.List<String> tableContent = new java.util.ArrayList<>();
		java.util.List<WebElement> codeColumn = driver.findElements(
				By.xpath("//table[@class='table table-striped']//tbody//td[3]"));
		for (WebElement element : codeColumn) {
			tableContent.add(element.getAttribute("innerText").trim());
		}
		System.out.println("Expected codes: " + expectedCodes);
		System.out.println("Table codes: " + tableContent);
		return tableContent.containsAll(expectedCodes);
	}

	/**
	 * Click the Download Table as CSV link
	 */
	public void clickDownloadTable() {
		clickElement(downloadTable);
		waitForPageLoad();
	}
}

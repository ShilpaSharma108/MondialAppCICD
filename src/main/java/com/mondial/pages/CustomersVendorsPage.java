package com.mondial.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Customers/Vendors Page Object
 * Represents the Customers and Vendors pages with CSV upload/download
 * and record management operations using AG Grid
 */
public class CustomersVendorsPage extends BasePage {

	@FindBy(xpath = "//div//h4[contains(text(),'Companies')]")
	private WebElement companyHeading;

	@FindBy(xpath = "//div[@ref='eContainer']//div[@role='row']")
	private List<WebElement> container;

	@FindBy(xpath = "//div//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//table[@class='table table-striped']//tr")
	private List<WebElement> tableName;

	@FindBy(xpath = "//a[contains(text(),'Download template CSV file')]")
	private WebElement downloadLink;

	@FindBy(xpath = "//a[contains(text(),'Download Table as CSV')]")
	private WebElement downloadTable;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//input[@id='customerUploadButton'] | //input[@id='vendorUploadButton']")
	private WebElement uploadButton;

	@FindBy(xpath = "//div[contains(text(),'was successfully deleted')]")
	private WebElement confirmationMsg;

	@FindBy(xpath = "//div[contains(text(),'Upload CSV process failed')]")
	private WebElement uploadErrorMsg;

	@FindBy(xpath = "//div[contains(text(),'was successfully updated')]")
	private WebElement updateConfirmationMsg;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement submitBtn;

	// Constructor
	public CustomersVendorsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to a resource page (Customers/Vendors) for a specific company
	 * @param companyName - Name of the company
	 * @param page - Resource page name (e.g., "Customers", "Vendors")
	 */
	public void navigateToResourcePage(String companyName, String page) {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
		for (int i = 0; i < tableName.size(); i++) {
			if (tableName.get(i).getAttribute("innerText").contains(companyName)) {
				scrollToElement(tableName.get(i));
				WebElement link = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ companyName + "')]//td//a[@data-original-title='" + page + "']"));
				clickElement(link);
				try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
				waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOf(pageHeading));
				// If still on Companies page, click again
				if (pageHeading.getAttribute("innerText").contains("Companies")) {
					System.out.println("First click did not navigate, retrying...");
					link = driver.findElement(By.xpath(
							"//table[@class='table table-striped']//tr[contains(., '"
							+ companyName + "')]//td//a[@data-original-title='" + page + "']"));
					scrollToElement(link);
					((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
					try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
					waitForPageLoad();
				}
				return;
			}
		}
		System.out.println("WARNING: Company '" + companyName + "' not found in table.");
	}

	// ============================================
	// CSV UPLOAD / DOWNLOAD METHODS
	// ============================================

	/**
	 * Download the template CSV file
	 */
	public void downloadTemplateCSV() {
		waitForPageLoad();
		clickElement(downloadLink);
		try { Thread.sleep(2000); } catch (InterruptedException e) { /* ignore */ }
	}

	/**
	 * Upload a CSV file using the file input and click Upload button
	 * @param filePath - Full path of the CSV file to upload
	 */
	public void uploadCSVFile(String filePath) {
		waitForPageLoad();
		WebElement inputBox = driver.findElement(By.xpath("//input[@id='upload_file']"));
		inputBox.sendKeys(filePath);
		clickElement(uploadButton);
		waitForPageLoad();
	}

	/**
	 * Click the Download Table as CSV link
	 */
	public void clickDownloadTable() {
		clickElement(downloadTable);
		waitForPageLoad();
	}

	/**
	 * Check if a file was downloaded to the given path
	 * @param filePath - Full path to the expected downloaded file
	 * @return true if file exists, false otherwise
	 */
	public boolean isFileDownloaded(String filePath) {
		java.io.File file = new java.io.File(filePath);
		return file.exists();
	}

	// ============================================
	// RECORD MANAGEMENT METHODS
	// ============================================

	/**
	 * Delete all records in the AG Grid table
	 */
	public void deleteAllRecords() {
		waitForPageLoad();
		for (int i = 0; i <= container.size(); i++) {
			if (container.size() > i) {
				try {
					WebElement deleteBtn = driver.findElement(
							By.xpath("//div[@row-id='0']//div[@col-id='delete']"));
					((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);
					try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
					// Handle browser confirmation dialog
					try {
						driver.switchTo().alert().accept();
					} catch (Exception e) {
						System.out.println("No confirmation dialog present");
					}
					waitForConfirmationMessageToDisappear();
				} catch (Exception e) {
					break;
				}
			}
		}
	}

	// ============================================
	// VERIFICATION METHODS
	// ============================================

	/**
	 * Verify records uploaded via CSV are present in the AG Grid table
	 * Checks that "Raiders of the Lost Ark LLC" exists in the company_name column
	 * @return true if expected records are found
	 */
	public boolean verifyRecordsUploaded() {
		waitForPageLoad();
		List<String> expectedRecords = new ArrayList<>(Arrays.asList("Raiders of the Lost Ark LLC"));
		List<String> tableContent = new ArrayList<>();
		List<WebElement> records = driver.findElements(
				By.xpath("//div[@role='gridcell'][@col-id='company_name']"));
		for (WebElement element : records) {
			tableContent.add(element.getAttribute("innerText").trim());
		}
		System.out.println("Expected records: " + expectedRecords);
		System.out.println("Table records: " + tableContent);
		return tableContent.containsAll(expectedRecords);
	}

	/**
	 * Check if any records exist in the AG Grid table
	 * @return true if records are present, false otherwise
	 */
	public boolean hasRecords() {
		waitForPageLoad();
		return container.size() > 0;
	}

	/**
	 * Check if the upload button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isUploadButtonDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(uploadButton));
			return uploadButton.isDisplayed();
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

	// ============================================
	// EDIT METHODS
	// ============================================

	/**
	 * Click the edit link/button on a specific AG Grid row
	 * @param rowIndex - Row index (0-based) in the AG Grid
	 */
	public void clickEditOnRow(int rowIndex) {
		waitForPageLoad();
		WebElement editBtn = driver.findElement(
				By.xpath("//div[@row-id='" + rowIndex + "']//div[@col-id='edit']//a"));
		scrollToElement(editBtn);
		clickElement(editBtn);
		waitForPageLoad();
	}

	/**
	 * Edit the vendor company name in the edit form
	 * @param newName - New company name to enter
	 */
	public void editVendorCompanyName(String newName) {
		waitForPageLoad();
		WebElement companyNameInput = driver.findElement(
				By.xpath("//input[@id='vendor_company_name'] | //input[@id='customer_company_name']"));
		wait.until(ExpectedConditions.visibilityOf(companyNameInput));
		companyNameInput.clear();
		companyNameInput.sendKeys(newName);
	}

	/**
	 * Click the save/submit button on the edit form
	 */
	public void clickSaveEdit() {
		clickElement(submitBtn);
		waitForPageLoad();
	}

	/**
	 * Verify if a vendor/customer with the given name exists in the AG Grid
	 * @param name - Name to search for in company_name column
	 * @return true if the name is found in the grid
	 */
	public boolean verifyVendorInGrid(String name) {
		waitForPageLoad();
		List<WebElement> cells = driver.findElements(
				By.xpath("//div[@role='gridcell'][@col-id='company_name']"));
		for (WebElement cell : cells) {
			if (cell.getAttribute("innerText").trim().equals(name)) {
				return true;
			}
		}
		return false;
	}

	// ============================================
	// SORT METHODS
	// ============================================

	/**
	 * Click an AG Grid column header to sort
	 * @param colId - The col-id attribute of the column to sort
	 */
	public void clickColumnHeader(String colId) {
		waitForPageLoad();
		WebElement header = driver.findElement(
				By.xpath("//div[@class='ag-header-row']//div[@col-id='" + colId + "']"));
		clickElement(header);
		try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
		waitForPageLoad();
	}

	/**
	 * Get all cell values for a specific column from the AG Grid
	 * @param colId - The col-id attribute of the column
	 * @return List of cell values as strings
	 */
	public List<String> getColumnValues(String colId) {
		waitForPageLoad();
		List<WebElement> cells = driver.findElements(
				By.xpath("//div[@role='gridcell'][@col-id='" + colId + "']"));
		List<String> values = new ArrayList<>();
		for (WebElement cell : cells) {
			String text = cell.getAttribute("innerText").trim();
			if (!text.isEmpty()) {
				values.add(text);
			}
		}
		return values;
	}

	/**
	 * Check if column values are sorted in ascending order
	 * @param values - List of string values to check
	 * @return true if sorted ascending (case-insensitive)
	 */
	public boolean isSortedAscending(List<String> values) {
		List<String> sorted = values.stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList());
		List<String> expected = new ArrayList<>(sorted);
		Collections.sort(expected);
		return sorted.equals(expected);
	}

	/**
	 * Check if column values are sorted in descending order
	 * @param values - List of string values to check
	 * @return true if sorted descending (case-insensitive)
	 */
	public boolean isSortedDescending(List<String> values) {
		List<String> sorted = values.stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList());
		List<String> expected = new ArrayList<>(sorted);
		Collections.sort(expected, Collections.reverseOrder());
		return sorted.equals(expected);
	}

	// ============================================
	// FILTER METHODS
	// ============================================

	/**
	 * Open the AG Grid filter menu for a column by hovering over header and clicking filter icon
	 * @param colId - The col-id attribute of the column
	 */
	public void openColumnFilter(String colId) {
		waitForPageLoad();
		WebElement header = driver.findElement(
				By.xpath("//div[@class='ag-header-row']//div[@col-id='" + colId + "']"));
		// Hover over header to reveal the filter icon
		new Actions(driver).moveToElement(header).perform();
		try { Thread.sleep(500); } catch (InterruptedException e) { /* ignore */ }
		WebElement filterIcon = header.findElement(
				By.xpath(".//span[contains(@class,'ag-header-icon') and contains(@class,'ag-header-cell-menu-button')]"));
		clickElement(filterIcon);
		try { Thread.sleep(500); } catch (InterruptedException e) { /* ignore */ }
	}

	/**
	 * Enter text into the AG Grid filter input
	 * @param text - Text to filter by
	 */
	public void enterFilterText(String text) {
		WebElement filterInput = driver.findElement(
				By.xpath("//div[contains(@class,'ag-filter')]//input[@type='text']"));
		wait.until(ExpectedConditions.visibilityOf(filterInput));
		filterInput.clear();
		filterInput.sendKeys(text);
		try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
		waitForPageLoad();
	}

	/**
	 * Clear the active AG Grid filter
	 */
	public void clearFilter() {
		try {
			WebElement filterInput = driver.findElement(
					By.xpath("//div[contains(@class,'ag-filter')]//input[@type='text']"));
			filterInput.clear();
			filterInput.sendKeys(Keys.BACK_SPACE);
			try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
			waitForPageLoad();
		} catch (Exception e) {
			// Filter may have already been cleared
			System.out.println("Filter already cleared or not present");
		}
	}

	/**
	 * Get the number of rows currently displayed in the AG Grid
	 * @return Number of visible rows
	 */
	public int getRecordCount() {
		waitForPageLoad();
		return container.size();
	}

	// ============================================
	// INVALID CSV UPLOAD METHODS
	// ============================================

	/**
	 * Check if the upload error message is displayed
	 * @return true if error message is displayed
	 */
	public boolean isUploadErrorDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(uploadErrorMsg));
			return uploadErrorMsg.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Wait for update confirmation message to disappear
	 */
	public void waitForUpdateConfirmationToDisappear() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(updateConfirmationMsg));
		} catch (Exception e) {
			// Message may have already disappeared
		}
	}
}

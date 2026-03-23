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
 * Reporting Currency Page Object
 * Represents the Reporting Currencies page and its CRUD operations
 */
public class ReportingCurrency extends BasePage {

	@FindBy(xpath = "//div//h4[contains(text(),'Companies')]")
	private WebElement companyHeading;

	@FindBy(xpath = "//div//h4")
	private WebElement pageHeading;

	@FindBy(xpath = "//a[contains(.,'Add Reporting currency')]")
	private WebElement addReportingCurrencyBtn;

	@FindBy(xpath = "//select[@id='reporting_currency_currency_id']")
	private WebElement reportingCurrenciesDD;

	@FindBy(xpath = "//select[@id='reporting_currency_exchange_rate_source_id']")
	private WebElement exchangeRateDD;

	@FindBy(xpath = "//input[@type='submit']")
	private WebElement createBtn;

	@FindBy(xpath = "//a[contains(text(),'Cancel')]")
	private WebElement cancelBtn;

	@FindBy(xpath = "//div[@class='alert alert-warning']")
	private WebElement warningMessage;

	@FindBy(xpath = "//div[@class='alert alert-success']")
	private WebElement successMessage;

	// Constructor
	public ReportingCurrency(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ============================================
	// NAVIGATION METHODS
	// ============================================

	/**
	 * Navigate to Reporting Currencies page for a specific company
	 * @param companyName - Name of the company
	 */
	public void navigateToReportingCurrency(String companyName) {
		waitForPageLoad();
		wait.until(ExpectedConditions.visibilityOf(companyHeading));
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
		List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tr"));
		for (WebElement row : rows) {
			if (row.getAttribute("innerText").contains(companyName)) {
				scrollToElement(row);
				WebElement link = driver.findElement(By.xpath(
						"//table[@class='table table-striped']//tr[contains(., '"
						+ companyName + "')]//td//a[@data-original-title='Reporting Currencies']"));
				clickElement(link);
				waitForPageLoad();
				wait.until(d -> {
					try {
						return pageHeading.getAttribute("innerText").contains("Reporting Currencies");
					} catch (Exception e) {
						return false;
					}
				});
				return;
			}
		}
		System.out.println("WARNING: Company '" + companyName + "' not found in table.");
	}

	// ============================================
	// CRUD METHODS
	// ============================================

	/**
	 * Click Add, then Cancel — verifies cancel returns to the listing page
	 */
	public void verifyCancel() {
		clickElement(addReportingCurrencyBtn);
		wait.until(ExpectedConditions.visibilityOf(reportingCurrenciesDD));
		clickElement(cancelBtn);
		wait.until(ExpectedConditions.visibilityOf(addReportingCurrencyBtn));
	}

	/**
	 * Create a new Reporting Currency by selecting a random available currency
	 * @return The name of the currency that was selected
	 */
	public String createReportingCurrency() {
		clickElement(addReportingCurrencyBtn);
		wait.until(ExpectedConditions.visibilityOf(reportingCurrenciesDD));
		Select reportingCurr = new Select(reportingCurrenciesDD);
		int index = getRandomIndex();
		reportingCurr.selectByIndex(index);
		String currencySelected = reportingCurr.getFirstSelectedOption().getAttribute("innerText");
		System.out.println("Currency selected: " + currencySelected);
		new Select(exchangeRateDD).selectByValue("2");
		clickElement(createBtn);
		waitForPageLoad();
		return currencySelected;
	}

	/**
	 * Delete a reporting currency by name
	 * @param name - Currency name to delete
	 */
	public void clickDelete(String name) {
		List<WebElement> tableBody = driver.findElements(
				By.xpath("//table[@class='table table-striped']//tbody//tr"));
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
	 * Verify if a reporting currency is present in the table
	 * @param name - Currency name to look for
	 * @return true if present, false otherwise
	 */
	public boolean verifyCurrencyPresent(String name) {
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
	 * Check if the Add Reporting Currency button is displayed
	 * @return true if displayed, false otherwise
	 */
	public boolean isAddBtnDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOf(addReportingCurrencyBtn));
			return addReportingCurrencyBtn.isDisplayed();
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
	public String getSuccessMessageText() {
		wait.until(ExpectedConditions.visibilityOf(successMessage));
		return successMessage.getAttribute("innerText");
	}

	/**
	 * Get the warning message text
	 * @return Warning message inner text
	 */
	public String getWarningMessageText() {
		wait.until(ExpectedConditions.visibilityOf(warningMessage));
		return warningMessage.getAttribute("innerText");
	}

	/**
	 * Wait for the success/confirmation alert to disappear
	 */
	public void waitForAlertToDismiss() {
		dismissAlert();
	}

	/**
	 * Wait for the warning message to disappear
	 */
	public void waitForWarningToDismiss() {
		try {
			wait.until(ExpectedConditions.invisibilityOf(warningMessage));
		} catch (Exception e) {
			// already gone
		}
	}

	// ============================================
	// HELPER METHODS
	// ============================================

	/**
	 * Get a random index (>=1) from the reporting currency dropdown options
	 * @return Random valid index (skips index 0 placeholder)
	 */
	private int getRandomIndex() {
		List<WebElement> options = driver.findElements(
				By.xpath("//select[@id='reporting_currency_currency_id']//option"));
		int listSize = options.size() - 1;
		int selectIndex = listSize > 0 ? (int) (Math.random() * listSize) + 1 : 1;
		System.out.println("Index to select: " + selectIndex);
		return selectIndex;
	}
}

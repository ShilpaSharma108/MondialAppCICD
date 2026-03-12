package com.mondial.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Row Format Page Object
 * Represents the Row Formats section within Report Writer
 */
public class RowFormat extends BasePage {

    private JavascriptExecutor jse;

    @FindBy(xpath = "//span[contains(text(), 'Report')]")
    private WebElement reportMenu;

    @FindBy(xpath = "//li//span[contains(text(), 'Report Writer')]")
    private WebElement reportWriterMenu;

    @FindBy(xpath = "//li//a[contains(text(), 'Row Formats')]")
    private WebElement rowFormatsLink;

    @FindBy(xpath = "//div//h4")
    private WebElement heading;

    @FindBy(xpath = "//div[@class='container']//a[contains(text(),'Add Row Format')]")
    private WebElement addRowFormatBtn;

    @FindBy(xpath = "//input[@id='row_format_name']")
    private WebElement rowName;

    @FindBy(xpath = "//input[@id='row_format_description']")
    private WebElement rowDescription;

    @FindBy(xpath = "//select[@id='row_format_default_company_id']")
    private WebElement companyDD;

    @FindBy(xpath = "//select[@id='row_format_default_ledger_id']")
    private WebElement ledgerDD;

    @FindBy(xpath = "//select[@id='row_format_default_reporting_set_id']")
    private WebElement reportingSetDD;

    @FindBy(xpath = "//select[@id='row_format_default_reporting_currency_code']")
    private WebElement reportingCurrencyDD;

    @FindBy(xpath = "//input[@id='datetimepicker1']")
    private WebElement startDate;

    @FindBy(xpath = "//input[@id='datetimepicker2']")
    private WebElement endDate;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement createBtn;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement cancelBtn;

    @FindBy(xpath = "//input[@id='report_row_ordinal']")
    private WebElement ordinal;

    @FindBy(xpath = "//input[@id='report_row_row_code']")
    private WebElement rowCodeInput;

    @FindBy(xpath = "//input[@id='report_row_description']")
    private WebElement description;

    @FindBy(xpath = "//input[@id='report_row_accounts']")
    private WebElement accountRange;

    @FindBy(xpath = "//input[@id='createReportRow']")
    private WebElement createReportRowBtn;

    @FindBy(xpath = "//div[@class='alert alert-success']")
    private WebElement successMessage;

    // Constructor
    public RowFormat(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        jse = (JavascriptExecutor) driver;
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Row Formats page via Report > Report Writer menu
     */
    public void navigateToRowFormat() {
        clickElement(reportMenu);
        wait.until(ExpectedConditions.visibilityOf(reportWriterMenu));
        clickElement(reportWriterMenu);
        wait.until(ExpectedConditions.visibilityOf(rowFormatsLink));
        clickElement(rowFormatsLink);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(addRowFormatBtn));
    }

    /**
     * Click the Add Row Format button (used in unauthorised access test)
     */
    public void clickAddRowFormat() {
        wait.until(ExpectedConditions.visibilityOf(addRowFormatBtn));
        clickElement(addRowFormatBtn);
    }

    // ============================================
    // CRUD METHODS
    // ============================================

    /**
     * Create a new Row Format and dismiss the success alert.
     * After this method the browser is still on the edit page.
     * Call cancelAndWaitForListing() to return to the listing.
     *
     * @param companyName - Company name to select from dropdown
     * @param name        - Name for the new row format
     */
    public void addNewRowFormat(String companyName, String name) {
        clickElement(addRowFormatBtn);
        wait.until(ExpectedConditions.visibilityOf(createBtn));
        new Select(companyDD).selectByVisibleText(companyName);
        waitForPageLoad(); // Wait for AJAX to populate ledger dropdown
        new Select(ledgerDD).selectByIndex(2);
        wait.until(ExpectedConditions.visibilityOf(reportingSetDD));
        new Select(reportingSetDD).selectByIndex(2);
        new Select(reportingCurrencyDD).selectByIndex(2);
        startDate.sendKeys("01/01/2020");
        endDate.sendKeys("01/31/2020");
        // Fill name/description LAST to prevent AJAX from clearing them
        rowName.clear();
        rowName.sendKeys(name);
        rowDescription.clear();
        rowDescription.sendKeys("TestDescription");
        jse.executeScript("arguments[0].click();", createBtn);
        dismissAlert();
    }

    /**
     * Click Cancel and wait for the Row Formats listing page to appear.
     */
    public void cancelAndWaitForListing() {
        jse.executeScript("arguments[0].click();", cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addRowFormatBtn));
    }

    /**
     * Edit an existing Row Format name
     * @param name - Current name of the row format
     * @return Updated name (original + "Updated")
     */
    public String editRowFormat(String name) {
        WebElement editLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[1]"));
        editLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        String updatedName = name + "Updated";
        rowName.sendKeys(updatedName);
        jse.executeScript("arguments[0].click();", createBtn);
        dismissAlert();
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addRowFormatBtn));
        return updatedName;
    }

    /**
     * Copy an existing Row Format
     * @param name - Name of the row format to copy
     * @return New copied name (original + "Copied")
     */
    public String copyRowFormat(String name) {
        WebElement copyLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[2]"));
        copyLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        String newName = name + "Copied";
        rowName.sendKeys(newName);
        waitForPageLoad();
        wait.until(ExpectedConditions.elementToBeClickable(createBtn));
        jse.executeScript("arguments[0].click();", createBtn);
        waitForPageLoad();
        dismissAlert();
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addRowFormatBtn));
        return newName;
    }

    /**
     * Delete an existing Row Format
     * @param name - Name of the row format to delete
     */
    public void deleteRowFormat(String name) {
        WebElement deleteLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[3]"));
        deleteLink.click();
        driver.switchTo().alert().accept();
        dismissAlert();
    }

    /**
     * Click the Edit link for a specific row format and navigate to its edit page
     * @param name - Name of the row format to edit
     * @return true if record was found and edit clicked, false otherwise
     */
    public boolean clickEdit(String name) {
        if (!verifyRecordPresent(name)) {
            return false;
        }
        WebElement editLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//a[1]"));
        editLink.click();
        return true;
    }

    /**
     * Create a Report Row on the Row Format edit page
     * @param code - Row code to enter
     */
    public void createReportRow(String code) {
        wait.until(ExpectedConditions.elementToBeClickable(ordinal));
        ordinal.sendKeys("1");
        rowCodeInput.sendKeys(code);
        description.sendKeys("RowDesc");
        accountRange.sendKeys("100:900");
        waitForPageLoad();
        wait.until(ExpectedConditions.elementToBeClickable(createReportRowBtn));
        jse.executeScript("arguments[0].click();", createReportRowBtn);
        waitForPageLoad();
        wait.until(ExpectedConditions.elementToBeClickable(createReportRowBtn));
    }

    /**
     * Delete a Report Row on the Row Format edit page.
     * Scrolls the AG Grid viewport to the far right first so the delete icon becomes visible.
     * @param code - Row code to delete
     */
    public void deleteRow(String code) throws InterruptedException {
        WebElement gridViewport = driver.findElement(
                By.xpath("//div[contains(@class,'ag-center-cols-viewport')]"));
        jse.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", gridViewport);
        WebElement path = driver.findElement(By.xpath("//div[contains(@class,'fa-trash') and contains(@class,'hyperlink')]"));
        wait.until(ExpectedConditions.elementToBeClickable(path));
        jse.executeScript("arguments[0].click();", path);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        waitForPageLoad();
        wait.until(ExpectedConditions.elementToBeClickable(createReportRowBtn));
    }

    /**
     * Click the Cancel button on the edit page and wait for the listing to appear
     */
    public void clickCancelAndWaitForListing() {
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addRowFormatBtn));
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /**
     * Verify if a row format exists in the listing table
     * @param name - Name to search for
     * @return true if present, false otherwise
     */
    public boolean verifyRecordPresent(String name) {
        waitForPageLoad();
        return driver.findElements(By.xpath(
                "//table//tbody//td[contains(text(), '" + name + "')]"))
                .size() > 0;
    }

    /**
     * Verify if a newly created Report Row is present in the AG Grid
     * @param code - Row code to search for
     * @return true if present, false otherwise
     */
    public boolean newRowPresent(String code) {
        return driver.findElements(By.xpath(
                "//div[@ref='eContainer'][@role='rowgroup']//div[@row-index]"
                + "//div[contains(text(),'" + code + "')]"))
                .size() > 0;
    }

    /**
     * Check if the Add Row Format button is displayed (listing page ready)
     * @return true if displayed, false otherwise
     */
    public boolean isAddRowFormatBtnDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addRowFormatBtn));
            return addRowFormatBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the page heading text
     * @return Heading text
     */
    public String getPageHeading() {
        wait.until(ExpectedConditions.visibilityOf(heading));
        return heading.getAttribute("innerText");
    }
}

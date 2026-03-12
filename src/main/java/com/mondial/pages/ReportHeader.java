package com.mondial.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Report Header Page Object
 * Represents the Report Headers section within Report Writer
 */
public class ReportHeader extends BasePage {

    private JavascriptExecutor jse;

    @FindBy(xpath = "//span[contains(text(), 'Report')]")
    private WebElement reportMainMenu;

    @FindBy(xpath = "//li//span[contains(text(), 'Report Writer')]")
    private WebElement reportWriterMenu;

    @FindBy(xpath = "//li//a[contains(text(), 'Report Headers')]")
    private WebElement reportHeadersLink;

    @FindBy(xpath = "//div//h4")
    private WebElement heading;

    @FindBy(xpath = "//div[@class='container']//a[contains(text(),'Add Report Header')]")
    private WebElement addReportHeaderBtn;

    @FindBy(xpath = "//input[@id='report_header_name']")
    private WebElement headerName;

    @FindBy(xpath = "//input[@id='report_header_description']")
    private WebElement headerDescription;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement createBtn;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement cancelBtn;

    @FindBy(xpath = "//input[@id='report_header_row_ordinal']")
    private WebElement ordinal;

    @FindBy(xpath = "//select[@id='report_header_row_element_left']")
    private WebElement elementLeft;

    @FindBy(xpath = "//select[@id='report_header_row_element_center']")
    private WebElement elementCenter;

    @FindBy(xpath = "//select[@id='report_header_row_element_right']")
    private WebElement elementRight;

    @FindBy(xpath = "//input[@id='report_header_row_text_left']")
    private WebElement textLeft;

    @FindBy(xpath = "//input[@id='report_header_row_text_center']")
    private WebElement textCenter;

    @FindBy(xpath = "//input[@id='report_header_row_text_right']")
    private WebElement textRight;

    @FindBy(xpath = "//select[@id='report_header_row_format_code_id_left']")
    private WebElement formatCodeLeft;

    @FindBy(xpath = "//select[@id='report_header_row_format_code_id_center']")
    private WebElement formatCodeCenter;

    @FindBy(xpath = "//select[@id='report_header_row_format_code_id_right']")
    private WebElement formatCodeRight;

    @FindBy(xpath = "//input[@id='createReportHeaderRow']")
    private WebElement createRHRowBtn;

    @FindBy(xpath = "//div[@class='alert alert-success']")
    private WebElement successMessage;

    // Constructor
    public ReportHeader(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        jse = (JavascriptExecutor) driver;
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Report Headers page via Report > Report Writer menu
     */
    public void navigateToReportHeader() {
        clickElement(reportMainMenu);
        wait.until(ExpectedConditions.visibilityOf(reportWriterMenu));
        clickElement(reportWriterMenu);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(reportHeadersLink));
        clickElement(reportHeadersLink);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(addReportHeaderBtn));
    }

    // ============================================
    // CRUD METHODS
    // ============================================

    /**
     * Create a new Report Header.
     * After this method the browser is on the edit page with a success message.
     * Call cancelAndWaitForListing() to return to the listing.
     *
     * @param companyName - Unused, kept for API compatibility
     * @param name        - Name for the new report header
     */
    public void addNewReportHeader(String companyName, String name) {
        clickElement(addReportHeaderBtn);
        wait.until(ExpectedConditions.visibilityOf(createBtn));
        headerName.sendKeys(name);
        headerDescription.sendKeys("TestDescription");
        clickElement(createBtn);
        dismissAlert();
    }

    /**
     * Click Cancel on the edit form and wait for the listing to appear.
     */
    public void cancelAndWaitForListing() {
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addReportHeaderBtn));
    }

    /**
     * Edit an existing Report Header name
     * @param name - Current name of the report header
     * @return Updated name (original + "Updated")
     */
    public String editReportHeader(String name) {
        WebElement editLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[1]"));
        editLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        String updatedName = name + "Updated";
        headerName.clear();
        headerName.sendKeys(updatedName);
        wait.until(ExpectedConditions.elementToBeClickable(createBtn));
        jse.executeScript("arguments[0].click();", createBtn);
        dismissAlert();
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addReportHeaderBtn));
        return updatedName;
    }

    /**
     * Delete an existing Report Header
     * @param name - Name of the report header to delete
     */
    public void deleteReportHeader(String name) {
        WebElement deleteLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[2]"));
        deleteLink.click();
        driver.switchTo().alert().accept();
        dismissAlert();
    }

    /**
     * Create a Report Header Row on the current edit page.
     * Fills in all row fields and submits.
     */
    public void createRHRow() {
        ordinal.sendKeys("1");
        new Select(elementLeft).selectByIndex(2);
        new Select(elementCenter).selectByIndex(2);
        new Select(elementRight).selectByIndex(2);
        textLeft.sendKeys("TestLeft");
        textCenter.sendKeys("TestCenter");
        textRight.sendKeys("TestRight");
        new Select(formatCodeLeft).selectByIndex(2);
        new Select(formatCodeCenter).selectByIndex(2);
        new Select(formatCodeRight).selectByIndex(2);
        clickElement(createRHRowBtn);
        waitForPageLoad();
        dismissAlert();
    }

    /**
     * Delete a Report Header Row identified by its ordinal code
     * @param ordinalCode - Ordinal value of the row to delete
     */
    public void deleteRHRow(String ordinalCode) {
        WebElement path = driver.findElement(By.xpath(
                "//div[@ref='eContainer'][@role='rowgroup']//div[@row-index]"
                + "//div[contains(text(),'" + ordinalCode + "')]"
                + "//following-sibling::div[@col-id='action']"));
        jse.executeScript("arguments[0].scrollIntoView();", path);
        clickElement(path);
        waitForPageLoad();
        wait.until(ExpectedConditions.elementToBeClickable(createRHRowBtn));
    }

    /**
     * Click Cancel on the edit page and wait for the listing
     */
    public void clickCancelAndWaitForListing() {
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addReportHeaderBtn));
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /**
     * Verify if a report header exists in the listing table
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
     * Verify if a Report Header Row is present in the AG Grid by its ordinal code
     * @param ordinalCode - Ordinal value to search for
     * @return true if present, false otherwise
     */
    public boolean newRowPresent(String ordinalCode) {
        return driver.findElements(By.xpath(
                "//div[@ref='eContainer'][@role='rowgroup']//div[@row-index]"
                + "//div[contains(text(),'" + ordinalCode + "')]"))
                .size() > 0;
    }

    /**
     * Check if the Add Report Header button is displayed
     * @return true if displayed, false otherwise
     */
    public boolean isAddReportHeaderBtnDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addReportHeaderBtn));
            return addReportHeaderBtn.isDisplayed();
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

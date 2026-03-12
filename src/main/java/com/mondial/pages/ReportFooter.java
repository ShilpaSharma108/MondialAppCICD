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
 * Report Footer Page Object
 * Represents the Report Footers section within Report Writer
 */
public class ReportFooter extends BasePage {

    private JavascriptExecutor jse;

    @FindBy(xpath = "//span[contains(text(), 'Report')]")
    private WebElement reportMenu;

    @FindBy(xpath = "//li//span[contains(text(), 'Report Writer')]")
    private WebElement reportWriterMenu;

    @FindBy(xpath = "//li//a[contains(text(), 'Report Footers')]")
    private WebElement reportFootersLink;

    @FindBy(xpath = "//div//h4")
    private WebElement heading;

    @FindBy(xpath = "//div[@class='container']//a[contains(text(),'Add Report Footer')]")
    private WebElement addReportFooterBtn;

    @FindBy(xpath = "//input[@id='report_footer_name']")
    private WebElement footerName;

    @FindBy(xpath = "//input[@id='report_footer_description']")
    private WebElement footerDescription;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement createBtn;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement cancelBtn;

    @FindBy(xpath = "//input[@id='report_footer_row_ordinal']")
    private WebElement ordinal;

    @FindBy(xpath = "//select[@id='report_footer_row_element_left']")
    private WebElement elementLeft;

    @FindBy(xpath = "//select[@id='report_footer_row_element_center']")
    private WebElement elementCenter;

    @FindBy(xpath = "//select[@id='report_footer_row_element_right']")
    private WebElement elementRight;

    @FindBy(xpath = "//input[@id='report_footer_row_text_left']")
    private WebElement textLeft;

    @FindBy(xpath = "//input[@id='report_footer_row_text_center']")
    private WebElement textCenter;

    @FindBy(xpath = "//input[@id='report_footer_row_text_right']")
    private WebElement textRight;

    @FindBy(xpath = "//select[@id='report_footer_row_format_code_id_left']")
    private WebElement formatCodeLeft;

    @FindBy(xpath = "//select[@id='report_footer_row_format_code_id_center']")
    private WebElement formatCodeCenter;

    @FindBy(xpath = "//select[@id='report_footer_row_format_code_id_right']")
    private WebElement formatCodeRight;

    @FindBy(xpath = "//input[@id='createReportFooterRow']")
    private WebElement createRFRowBtn;

    @FindBy(xpath = "//div[@class='alert alert-success']")
    private WebElement successMessage;

    // Constructor
    public ReportFooter(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        jse = (JavascriptExecutor) driver;
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Report Footers page via Report > Report Writer menu
     */
    public void navigateToReportFooter() {
        clickElement(reportMenu);
        wait.until(ExpectedConditions.visibilityOf(reportWriterMenu));
        clickElement(reportWriterMenu);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(reportFootersLink));
        clickElement(reportFootersLink);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(addReportFooterBtn));
    }

    // ============================================
    // CRUD METHODS
    // ============================================

    /**
     * Create a new Report Footer.
     * After this method the browser is on the edit page with a success message.
     * Call cancelAndWaitForListing() to return to the listing.
     *
     * @param companyName - Unused, kept for API compatibility
     * @param name        - Name for the new report footer
     */
    public void addNewReportFooter(String companyName, String name) {
        clickElement(addReportFooterBtn);
        wait.until(ExpectedConditions.visibilityOf(createBtn));
        footerName.sendKeys(name);
        footerDescription.sendKeys("TestDescription");
        clickElement(createBtn);
        dismissAlert();
    }

    /**
     * Click Cancel on the edit form and wait for the listing to appear.
     */
    public void cancelAndWaitForListing() {
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addReportFooterBtn));
    }

    /**
     * Edit an existing Report Footer name
     * @param name - Current name of the report footer
     * @return Updated name (original + "Updated")
     */
    public String editReportFooter(String name) {
        WebElement editLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[1]"));
        editLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        String updatedName = name + "Updated";
        footerName.clear();
        footerName.sendKeys(updatedName);
        jse.executeScript("arguments[0].click();", createBtn);
        dismissAlert();
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addReportFooterBtn));
        return updatedName;
    }

    /**
     * Delete an existing Report Footer
     * @param name - Name of the report footer to delete
     */
    public void deleteReportFooter(String name) {
        WebElement deleteLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[2]"));
        deleteLink.click();
        driver.switchTo().alert().accept();
        dismissAlert();
    }

    /**
     * Create a Report Footer Row on the current edit page.
     * Fills in all row fields and submits.
     */
    public void createRFRow() {
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
        clickElement(createRFRowBtn);
        waitForPageLoad();
        dismissAlert();
    }

    /**
     * Delete a Report Footer Row identified by its ordinal code
     * @param ordinalCode - Ordinal value of the row to delete
     */
    public void deleteRFRow(String ordinalCode) {
        WebElement path = driver.findElement(By.xpath(
                "//div[@ref='eContainer'][@role='rowgroup']//div[@row-index]"
                + "//div[contains(text(),'" + ordinalCode + "')]"
                + "//following-sibling::div[@col-id='action']"));
        jse.executeScript("arguments[0].scrollIntoView();", path);
        clickElement(path);
        waitForPageLoad();
        wait.until(ExpectedConditions.elementToBeClickable(createRFRowBtn));
    }

    /**
     * Click Cancel on the edit page and wait for the listing
     */
    public void clickCancelAndWaitForListing() {
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addReportFooterBtn));
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /**
     * Verify if a report footer exists in the listing table
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
     * Verify if a Report Footer Row is present in the AG Grid by its ordinal code
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
     * Check if the Add Report Footer button is displayed
     * @return true if displayed, false otherwise
     */
    public boolean isAddReportFooterBtnDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addReportFooterBtn));
            return addReportFooterBtn.isDisplayed();
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

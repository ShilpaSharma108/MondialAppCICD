package com.mondial.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

/**
 * Column Layout Page Object
 * Represents the Column Layouts section within Report Writer
 */
public class ColumnLayout extends BasePage {

    private JavascriptExecutor jse;

    @FindBy(xpath = "//span[contains(text(), 'Report')]")
    private WebElement reportMenu;

    @FindBy(xpath = "//li//span[contains(text(), 'Report Writer')]")
    private WebElement reportWriterMenu;

    @FindBy(xpath = "//li//a[contains(text(), 'Column Layouts')]")
    private WebElement columnLayoutsLink;

    @FindBy(xpath = "//div//h4")
    private WebElement heading;

    @FindBy(xpath = "//div[@class='container']//a[contains(text(),'Add Column Layout')]")
    private WebElement addColumnLayoutBtn;

    @FindBy(xpath = "//input[@id='column_layout_name']")
    private WebElement columnName;

    @FindBy(xpath = "//input[@id='column_layout_description']")
    private WebElement columnDescription;

    @FindBy(xpath = "//button[@id='insertLeft']")
    private WebElement insertLeftBtn;

    @FindBy(xpath = "//button[@id='insertRight']")
    private WebElement insertRightBtn;

    @FindBy(xpath = "//button[@id='delete']")
    private WebElement deleteColumnBtn;

    @FindBy(xpath = "//select[@id='report_column_ordinal']")
    private WebElement columnDD;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement createBtn;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement cancelBtn;

    @FindBy(xpath = "//div[@class='alert alert-success']")
    private WebElement successMessage;

    // Constructor
    public ColumnLayout(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        jse = (JavascriptExecutor) driver;
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Column Layouts page via Report > Report Writer menu
     */
    public void navigateToColumnLayout() {
        clickElement(reportMenu);
        wait.until(ExpectedConditions.visibilityOf(reportWriterMenu));
        clickElement(reportWriterMenu);
        wait.until(ExpectedConditions.visibilityOf(columnLayoutsLink));
        clickElement(columnLayoutsLink);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(addColumnLayoutBtn));
    }

    // ============================================
    // CRUD METHODS
    // ============================================

    /**
     * Create a new Column Layout and dismiss the success alert.
     * After this method the browser is still on the edit/create page.
     * Call cancelAndWaitForListing() to return to the listing.
     *
     * @param companyName - Unused, kept for API compatibility
     * @param name        - Name for the new column layout
     */
    public void addNewColumnLayout(String companyName, String name) {
        clickElement(addColumnLayoutBtn);
        wait.until(ExpectedConditions.visibilityOf(createBtn));
        columnName.sendKeys(name);
        columnDescription.sendKeys("TestDescription");
        jse.executeScript("arguments[0].click();", createBtn);
        dismissAlert();
    }

    /**
     * Click Cancel and wait for the Column Layouts listing page to appear.
     */
    public void cancelAndWaitForListing() {
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addColumnLayoutBtn));
    }

    /**
     * Edit an existing Column Layout name
     * @param name - Current name of the column layout
     * @return Updated name (original + "Updated")
     */
    public String editColumnLayout(String name) {
        WebElement editLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[1]"));
        editLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        String updatedName = name + "Updated";
        columnName.sendKeys(updatedName);
        waitForPageLoad();
        jse.executeScript("arguments[0].click();", createBtn);
        waitForPageLoad();
        dismissAlert();
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addColumnLayoutBtn));
        return updatedName;
    }

    /**
     * Copy an existing Column Layout
     * @param name - Name of the column layout to copy
     * @return New copied name (original + "Copied")
     */
    public String copyColumnLayout(String name) {
        WebElement copyLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[2]"));
        copyLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        String newName = name + "Copied";
        columnName.sendKeys(newName);
        waitForPageLoad();
        jse.executeScript("arguments[0].click();", createBtn);
        waitForPageLoad();
        dismissAlert();
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addColumnLayoutBtn));
        return newName;
    }

    /**
     * Delete an existing Column Layout
     * @param name - Name of the column layout to delete
     */
    public void deleteColumnLayout(String name) {
        WebElement deleteLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[3]"));
        deleteLink.click();
        driver.switchTo().alert().accept();
        dismissAlert();
    }

    /**
     * Insert a new column to the left of the first column.
     * Enters text into the first column cell, then inserts left.
     * @param columnText - Text to type into the first column
     */
    public void insertColumnLeft(String columnText) {
        Select column = new Select(columnDD);
        WebElement column1 = driver.findElement(By.xpath(
                "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[2]"));
        column1.sendKeys(columnText);
        column1.sendKeys(Keys.TAB);
        waitForPageLoad();
        Assert.assertTrue(column1.getAttribute("innerText").contains(columnText));
        column.selectByIndex(1);
        clickElement(insertLeftBtn);
        waitForPageLoad();
        // Wait for AG Grid PROCESSING overlay to disappear before asserting
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'ag-overlay-loading')]")));
        Assert.assertFalse(textPresentLeft(columnText));
    }

    /**
     * Insert a new column to the right of the first column.
     * @param columnText - Text to type into the columns
     */
    public void insertColumnRight(String columnText) {
        Select selectedColumn = new Select(columnDD);
        WebElement column1 = driver.findElement(By.xpath(
                "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[@role='gridcell'][2]"));
        WebElement column2 = driver.findElement(By.xpath(
                "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[@role='gridcell'][3]"));
        column1.sendKeys(columnText);
        column1.sendKeys(Keys.TAB);
        waitForPageLoad();
        WebElement input2 = driver.findElement(By.xpath(
                "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[@role='gridcell'][3]//input"));
        input2.clear();
        input2.sendKeys(columnText);
        waitForPageLoad();
        input2.sendKeys(Keys.TAB);
        waitForPageLoad();
        Assert.assertTrue(column1.getAttribute("innerText").contains(columnText));
        Assert.assertTrue(column2.getAttribute("innerText").contains(columnText));
        selectedColumn.selectByIndex(1);
        waitForPageLoad();
        clickElement(insertRightBtn);
        waitForPageLoad();
        // Wait for AG Grid PROCESSING overlay to disappear before asserting
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'ag-overlay-loading')]")));
        Assert.assertFalse(textPresentRight(columnText));
    }

    /**
     * Delete two inserted columns by selecting each by index and clicking delete.
     * @param columnText - Column text used to verify deletion
     */
    public void deleteInsertedColumn(String columnText) {
        new Select(columnDD).selectByIndex(1);
        clickElement(deleteColumnBtn);
        waitForPageLoad();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'ag-overlay-loading')]")));
        new Select(columnDD).selectByIndex(2);
        clickElement(deleteColumnBtn);
        waitForPageLoad();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'ag-overlay-loading')]")));
        Assert.assertFalse(textPresentLeft(columnText));
        Assert.assertFalse(textPresentRight(columnText));
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /**
     * Verify if a column layout exists in the listing table
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
     * Check if the Add Column Layout button is displayed
     * @return true if displayed, false otherwise
     */
    public boolean isAddColumnLayoutBtnDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addColumnLayoutBtn));
            return addColumnLayoutBtn.isDisplayed();
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

    // ============================================
    // PRIVATE HELPER METHODS
    // ============================================

    /**
     * Check if the first column (gridcell[2]) still contains the given text.
     * Returns false if a new blank column has been inserted before it.
     */
    private boolean textPresentLeft(String columnText) {
        boolean isTextPresent = driver.findElements(By.xpath(
                "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[@role='gridcell'][2]"))
                .size() > 0;
        if (isTextPresent) {
            return driver.findElement(By.xpath(
                    "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[@role='gridcell'][2]"))
                    .getAttribute("innerText").contains(columnText);
        }
        return false;
    }

    /**
     * Check if the second column (gridcell[3]) still contains the given text.
     */
    private boolean textPresentRight(String columnText) {
        boolean isTextPresent = driver.findElements(By.xpath(
                "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[@role='gridcell'][3]"))
                .size() > 0;
        if (isTextPresent) {
            return driver.findElement(By.xpath(
                    "//div[@ref='eContainer']/div[@role='row'][@row-id='1']//div[@role='gridcell'][3]"))
                    .getAttribute("innerText").contains(columnText);
        }
        return false;
    }
}

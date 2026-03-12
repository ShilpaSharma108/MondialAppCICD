package com.mondial.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Format Codes Page Object
 * Represents the Format Codes section within Report Writer
 */
public class FormatCodes extends BasePage {

    private JavascriptExecutor jse;

    @FindBy(xpath = "//span[contains(text(), 'Report')]")
    private WebElement reportMenu;

    @FindBy(xpath = "//li//span[contains(text(), 'Report Writer')]")
    private WebElement reportWriterMenu;

    @FindBy(xpath = "//li//a[contains(text(), 'Format Codes')]")
    private WebElement formatCodesLink;

    @FindBy(xpath = "//div//h4")
    private WebElement heading;

    @FindBy(xpath = "//div[@class='container']//a[contains(text(),'Add Format Code')]")
    private WebElement addFormatCodeBtn;

    @FindBy(xpath = "//input[@id='format_code_name']")
    private WebElement formatCodeName;

    @FindBy(xpath = "//select[@id='format_code_font_size']")
    private WebElement fontSizeDD;

    @FindBy(xpath = "//div[@class='checkbox checkbox-primary']//label//preceding-sibling::input[contains(@name,'_des')][@type='checkbox']//following-sibling::label")
    private List<WebElement> descriptionList;

    @FindBy(xpath = "//div[@class='checkbox checkbox-primary']//label//preceding-sibling::input[contains(@name,'_num')][@type='checkbox']//following-sibling::label")
    private List<WebElement> numberList;

    @FindBy(xpath = "//input[@id='format_code_currency_symbol']//following-sibling::label")
    private WebElement currencySymbol;

    @FindBy(xpath = "//input[@id='format_code_indent_size']")
    private WebElement indentSize;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement createBtn;

    @FindBy(xpath = "//a[@id='format-code-remote-update']")
    private WebElement updateBtn;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement cancelBtn;

    @FindBy(xpath = "//div[@class='alert alert-success']")
    private WebElement successMessage;

    // Constructor
    public FormatCodes(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        jse = (JavascriptExecutor) driver;
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Format Codes page via Report > Report Writer menu
     */
    public void navigateToFormatCode() {
        clickElement(reportMenu);
        wait.until(ExpectedConditions.visibilityOf(reportWriterMenu));
        clickElement(reportWriterMenu);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(formatCodesLink));
        clickElement(formatCodesLink);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(addFormatCodeBtn));
    }

    // ============================================
    // CRUD METHODS
    // ============================================

    /**
     * Create a new Format Code.
     * After submission the listing page is shown with a success message.
     *
     * @param companyName - Unused, kept for API compatibility
     * @param name        - Name for the new format code
     */
    public void addNewFormatCode(String companyName, String name) {
        clickElement(addFormatCodeBtn);
        wait.until(ExpectedConditions.visibilityOf(createBtn));
        formatCodeName.sendKeys(name);
        new Select(fontSizeDD).selectByIndex(2);
        clickElement(createBtn);
        dismissAlert();
        wait.until(ExpectedConditions.visibilityOf(addFormatCodeBtn));
    }

    /**
     * Edit an existing Format Code name
     * @param name - Current name of the format code
     * @return Updated name (original + "Updated")
     */
    public String editFormatCode(String name) {
        WebElement editLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[1]"));
        editLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        String updatedName = name + "Updated";
        formatCodeName.clear();
        formatCodeName.sendKeys(updatedName);
        clickElement(updateBtn);
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addFormatCodeBtn));
        return updatedName;
    }

    /**
     * Delete an existing Format Code
     * @param name - Name of the format code to delete
     */
    public void deleteFormatCode(String name) {
        WebElement deleteLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[2]"));
        deleteLink.click();
        driver.switchTo().alert().accept();
        dismissAlert();
    }

    /**
     * Select all description and number checkboxes and set currency/indent,
     * then save and return to listing.
     * @param name - Name of the format code to modify
     */
    public void checkboxSelection(String name) {
        WebElement editLink = driver.findElement(By.xpath(
                "//table//tbody//td[contains(text(), '" + name
                + "')]//preceding-sibling::td//div//a[1]"));
        editLink.click();
        wait.until(ExpectedConditions.visibilityOf(cancelBtn));
        for (WebElement label : descriptionList) {
            System.out.println(label.getAttribute("innerText"));
            label.click();
        }
        for (WebElement label : numberList) {
            System.out.println(label.getAttribute("innerText"));
            label.click();
        }
        currencySymbol.click();
        indentSize.sendKeys("50");
        clickElement(updateBtn);
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(addFormatCodeBtn));
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /**
     * Verify if a format code exists in the listing table
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
     * Check if the Add Format Code button is displayed
     * @return true if displayed, false otherwise
     */
    public boolean isAddFormatCodeBtnDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addFormatCodeBtn));
            return addFormatCodeBtn.isDisplayed();
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

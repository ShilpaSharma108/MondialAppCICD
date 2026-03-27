package com.mondial.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Accounting Period Page Object
 * Represents the Accounting Periods section accessed from the Companies listing
 */
public class AccountingPeriodPage extends BasePage {

    private JavascriptExecutor jse;

    @FindBy(xpath = "//div//h4")
    private WebElement pageHeading;

    @FindBy(xpath = "//input[@id='datetimepicker1']")
    private WebElement dateBox;

    @FindBy(xpath = "//input[@type='submit'] | //button[@type='submit'] | //button[not(@type) and ancestor::form]")
    private WebElement submitBtn;

    @FindBy(xpath = "//table[@class='table table-striped']//tbody//tr")
    private List<WebElement> tableBody;

    // Shown when a period is open — clicking it closes (locks) the period
    @FindBy(xpath = "//tr[1]//a[@data-original-title='Close Fiscal Year']")
    private WebElement closeFiscalYearBtn;

    // Shown when a period is locked — clicking it opens (unlocks) the period
    @FindBy(xpath = "//tr[1]//a[@data-original-title='Open Fiscal Year']")
    private WebElement openFiscalYearBtn;

    // Constructor
    public AccountingPeriodPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        jse = (JavascriptExecutor) driver;
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to the Accounting Periods page for the given company
     * by clicking its link in the Companies listing table.
     *
     * @param companyName - Company to navigate into
     */
    public void navigateToAccountingPeriod(String companyName) {
        waitForPageLoad();
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table[@class='table table-striped']//tr[contains(., '"
                        + companyName
                        + "')]//td//a[@data-original-title='Accounting Periods']")));
        String hrefBefore = link.getAttribute("href");
        System.out.println("[DEBUG] Accounting Periods link href: " + hrefBefore);
        System.out.println("[DEBUG] URL before click: " + driver.getCurrentUrl());
        int windowsBefore = driver.getWindowHandles().size();
        scrollToElement(link);
        clickElement(link);
        waitForPageLoad();
        int windowsAfter = driver.getWindowHandles().size();
        System.out.println("[DEBUG] URL after click: " + driver.getCurrentUrl());
        System.out.println("[DEBUG] Windows before: " + windowsBefore + ", after: " + windowsAfter);
        if (windowsAfter > windowsBefore) {
            // Link opened a new tab — switch to it
            System.out.println("[DEBUG] New tab detected, switching...");
            String newTab = driver.getWindowHandles().stream()
                    .filter(h -> !h.equals(driver.getWindowHandle()))
                    .findFirst().orElse(null);
            if (newTab != null) {
                driver.switchTo().window(newTab);
                waitForPageLoad();
                System.out.println("[DEBUG] Switched to new tab, URL: " + driver.getCurrentUrl());
            }
        }
        // Wait for the date-entry form to be visible — more reliable than the submit button
        wait.until(ExpectedConditions.visibilityOf(dateBox));
    }

    // ============================================
    // CRUD METHODS
    // ============================================

    /**
     * Set (add) an accounting period via the date picker form and submit.
     * Uses JavaScript to write the value directly to bypass datepicker widget quirks.
     *
     * @param date - Date string in MM/dd/yyyy format
     */
    public void setAccountingPeriod(String date) {
        wait.until(ExpectedConditions.elementToBeClickable(dateBox));
        dateBox.click();
        dateBox.clear();
        jse.executeScript("arguments[0].value = arguments[1];", dateBox, date);
        // Submit the form directly — avoids dependency on submit button type/locator
        jse.executeScript("arguments[0].closest('form').submit();", dateBox);
        waitForPageLoad();
    }

    /**
     * Lock the first accounting period by clicking "Close Fiscal Year".
     * After this call, isLocked() should return true.
     */
    public void lockAccountingPeriod() {
        wait.until(ExpectedConditions.elementToBeClickable(closeFiscalYearBtn));
        closeFiscalYearBtn.click();
        waitForPageLoad();
    }

    /**
     * Unlock the first accounting period by clicking "Open Fiscal Year".
     * After this call, isUnlocked() should return true.
     */
    public void unlockAccountingPeriod() {
        wait.until(ExpectedConditions.elementToBeClickable(openFiscalYearBtn));
        openFiscalYearBtn.click();
        waitForPageLoad();
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /**
     * Check whether the given date appears in the accounting periods table.
     *
     * @param date - Date string to look for (same format used in setAccountingPeriod)
     * @return true if any row contains the date, false otherwise
     */
    public boolean verifyRecordPresentAP(String date) {
        waitForPageLoad();
        return tableBody.stream()
                .anyMatch(row -> row.getAttribute("innerText").contains(date));
    }

    /**
     * Return true if the first period is locked
     * (i.e. the "Open Fiscal Year" button is visible, meaning you can unlock it).
     */
    public boolean isLocked() {
        return driver.findElements(
                By.xpath("//a[@data-original-title='Open Fiscal Year']")).size() > 0;
    }

    /**
     * Return true if the first period is unlocked
     * (i.e. the "Close Fiscal Year" button is visible, meaning you can lock it).
     */
    public boolean isUnlocked() {
        return driver.findElements(
                By.xpath("//a[@data-original-title='Close Fiscal Year']")).size() > 0;
    }

    /**
     * Check if the accounting period date-entry form is visible (submit button present).
     *
     * @return true if the form submit button is displayed
     */
    public boolean isAccountingPeriodFormDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(dateBox));
            return dateBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the page heading text.
     *
     * @return Heading innerText
     */
    public String getPageHeading() {
        wait.until(ExpectedConditions.visibilityOf(pageHeading));
        return pageHeading.getAttribute("innerText");
    }
}

package com.mondial.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Exchange Rate Page Object
 * Covers both Exchange Rate Sources (CRUD) and Exchange Rates (show / sort / download)
 * accessed via Enterprise Setup > Exchange Rate Management
 */
public class ExchangeRatePage extends BasePage {

    // ── Exchange Rate Sources page ────────────────────────────────────────────

    @FindBy(xpath = "//a//span[contains(text(),'Enterprise Setup')]")
    private WebElement enterpriseSetup;

    @FindBy(xpath = "//a//span[contains(text(),'Exchange Rate Management')]")
    private WebElement exchangeRateMgmtMenu;

    @FindBy(xpath = "//a[contains(text(),'Exchange Rate Sources')]")
    private WebElement ersMenu;

    @FindBy(xpath = "//a[contains(text(),'Exchange Rates')]")
    private WebElement exchangeRatesMenu;

    @FindBy(xpath = "//h4[contains(text(),'Exchange Rate Sources')]")
    private WebElement erPageHeading;

    @FindBy(xpath = "//h4[contains(text(),'New Exchange Rate Source')]")
    private WebElement newERHeading;

    @FindBy(xpath = "//a[@class='btn btn-primary btn-large pull-right']")
    private WebElement createERSButton;

    @FindBy(xpath = "//input[@id='exchange_rate_source_name']")
    private WebElement ersNameInput;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement createBtn;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement cancelBtn;

    @FindBy(xpath = "//input[@type = 'submit'][@data-disable-with = 'Update']")
    private WebElement updateBtn;

    /** First Edit button in the table — used for unauthorised-access tests */
    @FindBy(xpath = "//a[@data-original-title = 'Edit']")
    private WebElement editBtn;

    /** First Delete button in the table — used for unauthorised-access tests */
    @FindBy(xpath = "//a[@data-original-title = 'Delete']")
    private WebElement deleteBtn;

    // CSV upload / download elements
    @FindBy(xpath = "//span[contains(text(),'Choose Csv')]")
    private WebElement chooseCSV;

    @FindBy(xpath = "//a[contains(text(),'Download template CSV file')]")
    private WebElement downloadTemplateCSV;

    @FindBy(xpath = "//input[@type = 'submit'][@data-disable-with = 'Upload CSV']")
    private WebElement uploadCSV;

    @FindBy(xpath = "//div[contains(@class,'alert') and contains(.,'Not Authorized')]")
    private WebElement notAuthorisedAlert;

    // ── Exchange Rates page ───────────────────────────────────────────────────

    @FindBy(xpath = "//select[@id = 'exchange_rate_exchange_rate_source_id']")
    private WebElement exchangeRateSourceDD;

    @FindBy(xpath = "//select[@id = 'exchange_rate_base_currency_id']")
    private WebElement baseCurrencyDD;

    @FindBy(xpath = "//select[@id = 'exchange_rate_currency_id']")
    private WebElement targetCurrencyDD;

    @FindBy(xpath = "//button[@id = 'submitButton']")
    private WebElement showCurrencyBtn;

    @FindBy(xpath = "//button[@id = 'downloadButton']")
    private WebElement downloadCSVBtn;

    @FindBy(xpath = "//h4[contains(text(),'Exchange Rates')]")
    private WebElement exchangeRatesPageHeading;

    @FindBy(xpath = "//div[@role='gridcell'][@col-id='exchange_rate_source_name']")
    private List<WebElement> tableResult;

    @FindBy(xpath = "//div[@role='columnheader'][@col-id='date']")
    private WebElement dateColumn;

    @FindBy(xpath = "//div[@role='columnheader'][@col-id='rate']")
    private WebElement rateColumn;

    // Constructor
    public ExchangeRatePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ============================================
    // NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Exchange Rate Sources listing via Enterprise Setup menu
     */
    public void navigateERSMenu() {
        waitForPageLoad();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a//span[contains(text(),'Enterprise Setup')]")));
        clickElement(enterpriseSetup);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a//span[contains(text(),'Exchange Rate Management')]")));
        clickElement(exchangeRateMgmtMenu);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(text(),'Exchange Rate Sources')]")));
        clickElement(ersMenu);
        waitForPageLoad();
        wait.until(ExpectedConditions.visibilityOf(erPageHeading));
    }

    /**
     * Navigate to Exchange Rates page via Enterprise Setup menu
     */
    public void navigateERatesMenu() {
        waitForPageLoad();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a//span[contains(text(),'Enterprise Setup')]")));
        clickElement(enterpriseSetup);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a//span[contains(text(),'Exchange Rate Management')]")));
        clickElement(exchangeRateMgmtMenu);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(text(),'Exchange Rates')]")));
        clickElement(exchangeRatesMenu);
        wait.until(ExpectedConditions.visibilityOf(exchangeRatesPageHeading));
    }

    // ============================================
    // CRUD METHODS
    // ============================================

    /**
     * Navigate to Exchange Rate Sources and create a new record
     * @param name - Name for the new Exchange Rate Source
     */
    public void exchangeRateCreation(String name) {
        navigateERSMenu();
        createERS(name);
    }

    /**
     * Create a new Exchange Rate Source (must already be on the listing page)
     * @param name - Name for the new Exchange Rate Source
     */
    public void createERS(String name) {
        clickElement(createERSButton);
        wait.until(ExpectedConditions.visibilityOf(newERHeading));
        ersNameInput.sendKeys(name);
        clickElement(createBtn);
        wait.until(ExpectedConditions.visibilityOf(createERSButton));
        System.out.println("New Exchange Rate Source ** " + name + " ** created successfully!");
    }

    /**
     * Click the Add Exchange Rate Source button
     */
    public void clickCreateERSButton() {
        clickElement(createERSButton);
    }

    /**
     * Click the Edit action link for a specific record in the table
     * @param name - Name of the Exchange Rate Source to edit
     */
    public void clickEditRecord(String name) {
        By editLinkLocator = By.xpath(
                "//table//td[contains(text(),'" + name
                + "')]//following-sibling::td//a[@data-original-title = 'Edit']");
        WebElement editLink = wait.until(ExpectedConditions.elementToBeClickable(editLinkLocator));
        scrollToElement(editLink);
        editLink.click();
    }

    /**
     * Click the first Edit button in the table (used for unauthorised-access tests)
     */
    public void clickFirstEditBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        editBtn.click();
    }

    /**
     * Click the last Edit button in the table (targets the most recently created record)
     */
    public void clickLastEditBtn() {
        List<WebElement> editBtns = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//a[@data-original-title = 'Edit']")));
        editBtns.get(editBtns.size() - 1).click();
    }

    /**
     * Click the last Delete button in the table (targets the most recently created record)
     */
    public void clickLastDeleteBtn() {
        List<WebElement> deleteBtns = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//a[@data-original-title = 'Delete']")));
        deleteBtns.get(deleteBtns.size() - 1).click();
    }

    /**
     * Rename a record on the edit form by appending "Updated" to the current name
     * @param name - Current Exchange Rate Source name
     * @return The updated name (original + "Updated")
     */
    public String renameRecord(String name) {
        String updatedName = name + "Updated";
        wait.until(ExpectedConditions.visibilityOf(ersNameInput));
        ersNameInput.clear();
        ersNameInput.sendKeys(updatedName);
        clickElement(updateBtn);
        wait.until(ExpectedConditions.visibilityOf(erPageHeading));
        return updatedName;
    }

    /**
     * Enter a modified name then click Cancel to abandon the rename
     * @param name - Current Exchange Rate Source name
     * @return The original name (unchanged, since cancel was clicked)
     */
    public String cancelRename(String name) {
        wait.until(ExpectedConditions.visibilityOf(ersNameInput));
        ersNameInput.clear();
        ersNameInput.sendKeys(name + "Renamed");
        clickElement(cancelBtn);
        wait.until(ExpectedConditions.visibilityOf(erPageHeading));
        return name;
    }

    /**
     * Delete an Exchange Rate Source by name and confirm the browser alert
     * @param name - Name of the Exchange Rate Source to delete
     */
    public void deleteRecord(String name) {
        By deleteLinkLocator = By.xpath(
                "//table//td[contains(text(),'" + name
                + "')]//following-sibling::td//a[@data-original-title = 'Delete']");
        WebElement deleteLink = wait.until(ExpectedConditions.elementToBeClickable(deleteLinkLocator));
        scrollToElement(deleteLink);
        deleteLink.click();
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }
    }

    /**
     * Click the Update button on the edit form and wait to return to the listing page
     */
    public void clickUpdateBtn() {
        clickElement(updateBtn);
        wait.until(ExpectedConditions.visibilityOf(createERSButton));
    }

    // ============================================
    // CSV UPLOAD / DOWNLOAD METHODS
    // ============================================

    /**
     * Click the Download Template CSV link on the edit form
     */
    public void clickDownloadTemplateCSV() {
        wait.until(ExpectedConditions.elementToBeClickable(downloadTemplateCSV));
        downloadTemplateCSV.click();
        waitForPageLoad();
    }

    /**
     * Poll until the downloaded file exists (up to 15 seconds)
     * @param filePath - Absolute path to the expected downloaded file
     * @return true if file exists within timeout, false otherwise
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
     * Upload a CSV file to the Exchange Rate Source edit form.
     * Sets the file path on the hidden input, clicks Upload CSV, then dismisses the confirmation.
     * @param filePath - Absolute path to the CSV file to upload
     */
    public void uploadCSVFile(String filePath) {
        waitForPageLoad();
        WebElement inputBox = driver.findElement(By.xpath("//input[@id='uploadFile']"));
        inputBox.sendKeys(filePath);
        clickElement(uploadCSV);
        dismissAlert();
    }

    /**
     * Click the Download CSV button on the Exchange Rates page
     */
    public void clickDownloadCSV() {
        clickElement(downloadCSVBtn);
    }

    // ============================================
    // EXCHANGE RATES METHODS
    // ============================================

    /**
     * Select specific exchange rate source, base currency and target currency then click Show Rates.
     * @param source       - Visible text of the Exchange Rate Source option (e.g. "European Central Bank")
     * @param baseCurrency - Visible text of the base currency (e.g. "USD")
     * @param targetCurrency - Visible text of the target currency (e.g. "GBP")
     * @return Number of result rows returned in the exchange rates grid
     */
    public int showRates(String source, String baseCurrency, String targetCurrency) {
        By ersDDBy    = By.xpath("//select[@id = 'exchange_rate_exchange_rate_source_id']");
        By baseDDBy   = By.xpath("//select[@id = 'exchange_rate_base_currency_id']");
        By targetDDBy = By.xpath("//select[@id = 'exchange_rate_currency_id']");

        // Wait for source dropdown to be populated with more than the default placeholder
        wait.until(driver -> new Select(driver.findElement(ersDDBy)).getOptions().size() > 1);
        selectByPartialText(ersDDBy, source);
        System.out.println("Exchange Rate Source selected: " + source);
        waitForPageLoad();

        wait.until(driver -> new Select(driver.findElement(baseDDBy)).getOptions().size() > 1);
        selectByPartialText(baseDDBy, baseCurrency);
        System.out.println("Base Currency selected: " + baseCurrency);

        wait.until(ExpectedConditions.elementToBeClickable(targetDDBy));
        selectByPartialText(targetDDBy, targetCurrency);
        System.out.println("Target Currency selected: " + targetCurrency);

        clickElement(showCurrencyBtn);
        waitForPageLoad();
        System.out.println("Table Size: " + tableResult.size());
        return tableResult.size();
    }

    /**
     * Select mismatched base and target currencies then click Show Rates.
     * Iterates through available exchange rate sources to find one with currency data.
     * @return Number of result rows returned in the exchange rates grid
     */
    public int showRates() {
        if (!selectExchangeRateSourceWithCurrencies()) {
            System.out.println("No exchange rate source with currency data found");
            return 0;
        }
        By baseDDBy   = By.xpath("//select[@id = 'exchange_rate_base_currency_id']");
        By targetDDBy = By.xpath("//select[@id = 'exchange_rate_currency_id']");
        String value1 = "xyz", value2 = "xyz";
        while (value1.equalsIgnoreCase(value2)) {
            wait.until(ExpectedConditions.elementToBeClickable(baseDDBy));
            Select baseDD = new Select(driver.findElement(baseDDBy));
            int baseSize = baseDD.getOptions().size();
            int selectNum1 = baseSize > 1 ? (int) (Math.random() * (baseSize - 1)) + 1 : 1;
            baseDD.selectByIndex(selectNum1);
            value1 = baseDD.getFirstSelectedOption().getAttribute("innerText");
            System.out.println("Base Currency Selected: " + value1);

            wait.until(ExpectedConditions.elementToBeClickable(targetDDBy));
            Select targetDD = new Select(driver.findElement(targetDDBy));
            int targetSize = targetDD.getOptions().size();
            int selectNum2 = targetSize > 1 ? (int) (Math.random() * (targetSize - 1)) + 1 : 1;
            targetDD.selectByIndex(selectNum2);
            value2 = targetDD.getFirstSelectedOption().getAttribute("innerText");
            System.out.println("Target Currency Selected: " + value2);
        }
        clickElement(showCurrencyBtn);
        waitForPageLoad();
        System.out.println("Table Size: " + tableResult.size());
        return tableResult.size();
    }

    // ============================================
    // SORT METHODS
    // ============================================

    /**
     * Click the Date column header once (ascending) then again (descending) and verify order.
     * @return true if both ascending and descending order are correct, false otherwise
     */
    public boolean sortDateColumn() {
        dateColumn.click();
        List<String> actual = getColumnValues("date");
        List<String> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        if (!actual.equals(expected)) {
            System.out.println("Dates not sorted in ascending order");
            return false;
        }
        dateColumn.click();
        actual = getColumnValues("date");
        List<String> expectedDesc = new ArrayList<>(actual);
        Collections.sort(expectedDesc, Collections.reverseOrder());
        if (!actual.equals(expectedDesc)) {
            System.out.println("Dates not sorted in descending order");
            return false;
        }
        return true;
    }

    /**
     * Click the Rate column header once (ascending) then again (descending) and verify order.
     * @return true if both ascending and descending order are correct, false otherwise
     */
    public boolean sortRateColumn() {
        rateColumn.click();
        List<String> actual = getColumnValues("rate");
        List<String> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        if (!actual.equals(expected)) {
            System.out.println("Rates not sorted in ascending order");
            return false;
        }
        rateColumn.click();
        actual = getColumnValues("rate");
        List<String> expectedDesc = new ArrayList<>(actual);
        Collections.sort(expectedDesc, Collections.reverseOrder());
        if (!actual.equals(expectedDesc)) {
            System.out.println("Rates not sorted in descending order");
            return false;
        }
        return true;
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /**
     * Check if a named Exchange Rate Source exists in the listing table
     * @param name - Name to search for
     * @return true if record is present, false otherwise
     */
    public boolean verifyRecordPresent(String name) {
        waitForPageLoad();
        return driver.findElements(By.xpath(
                "//table[@class ='table table-striped table-hover table-sm-responsive']//tbody//td[contains(.,'"
                + name + "')]")).size() > 0;
    }

    /**
     * Check if a "Not Authorized" alert is displayed on the page
     * @return true if displayed, false otherwise
     */
    public boolean isNotAuthorisedAlertDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'alert') and contains(.,'Not Authorized')]")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the text of the "Not Authorized" alert
     * @return Alert text, or empty string if not present
     */
    public String getNotAuthorisedAlertText() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'alert') and contains(.,'Not Authorized')]")));
            return alert.getAttribute("innerText").trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if the Exchange Rate Sources listing page heading is displayed
     * @return true if the heading is visible, false otherwise
     */
    public boolean isERSPageHeadingDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(erPageHeading));
            return erPageHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================
    // PRIVATE HELPERS
    // ============================================

    /**
     * Iterate through exchange rate source options to find one that populates
     * the base currency dropdown with at least one selectable currency.
     * Re-finds dropdowns on each iteration to avoid StaleElementReferenceException.
     * @return true if a valid source was found and selected, false otherwise
     */
    private boolean selectExchangeRateSourceWithCurrencies() {
        By ersDDBy  = By.xpath("//select[@id = 'exchange_rate_exchange_rate_source_id']");
        By baseDDBy = By.xpath("//select[@id = 'exchange_rate_base_currency_id']");

        int optionCount = new Select(driver.findElement(ersDDBy)).getOptions().size();
        for (int i = 1; i < optionCount; i++) {
            Select ersDD = new Select(driver.findElement(ersDDBy));
            ersDD.selectByIndex(i);
            String sourceName = ersDD.getFirstSelectedOption().getAttribute("innerText");
            System.out.println("Trying exchange rate source: " + sourceName);
            waitForPageLoad();
            try {
                wait.until(ExpectedConditions.elementToBeClickable(baseDDBy));
            } catch (Exception e) {
                continue;
            }
            Select baseDD = new Select(driver.findElement(baseDDBy));
            if (baseDD.getOptions().size() > 1) {
                System.out.println("Found source with currencies: " + sourceName);
                return true;
            }
        }
        return false;
    }

    /**
     * Select a dropdown option whose visible text contains the given partial text.
     * Useful when option labels have leading/trailing whitespace or extra tokens.
     * @param by          - Locator for the select element
     * @param partialText - Text that the desired option must contain
     */
    private void selectByPartialText(By by, String partialText) {
        Select select = new Select(driver.findElement(by));
        for (WebElement option : select.getOptions()) {
            if (option.getText().trim().contains(partialText)) {
                option.click();
                return;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException(
                "No option containing '" + partialText + "' found in dropdown: " + by);
    }

    /**
     * Collect all cell text values for a given ag-Grid column ID
     * @param colId - The col-id attribute value (e.g. "date", "rate")
     * @return List of cell text strings
     */
    private List<String> getColumnValues(String colId) {
        List<WebElement> cells = driver.findElements(
                By.xpath("//div[@role='gridcell'][@col-id='" + colId + "']"));
        List<String> values = new ArrayList<>();
        for (WebElement cell : cells) {
            values.add(cell.getText());
        }
        return values;
    }
}

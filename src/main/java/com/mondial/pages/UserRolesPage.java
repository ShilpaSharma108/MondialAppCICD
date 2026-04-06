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
 * User Roles Page Object
 * Represents the Enterprise Setup > Users page where each user row
 * contains per-company role dropdowns as columns.
 */
public class UserRolesPage extends BasePage {

    @FindBy(xpath = "//div//h4")
    private WebElement pageHeading;

    /** Stored after navigateToUserRoles() so assignRole/getAssignedRole can scope to the right row. */
    private String currentUserEmail;

    public UserRolesPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigate to Enterprise Setup → Users.
     * The Users table has one column per company with a role dropdown.
     *
     * @param userEmail - Email of the user whose row will be used for role assignment
     */
    public void navigateToUserRoles(String userEmail) {
        this.currentUserEmail = userEmail;
        waitForPageLoad();
        System.out.println("[UserRolesPage] Current URL before navigation: " + driver.getCurrentUrl());

        // Expand Enterprise Setup sidebar menu
        WebElement enterpriseSetup = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a//span[contains(text(),'Enterprise Setup')]")));
        scrollToElement(enterpriseSetup);
        enterpriseSetup.click();

        // Wait for Users sub-menu to appear and click it
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Users')]")));
        driver.findElement(By.xpath("//a[contains(text(),'Users')]")).click();

        // Wait until the Users page is loaded (Add User button is the landmark)
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='container']//a[contains(.,' User')]")));
        waitForPageLoad();
        System.out.println("[UserRolesPage] Users page loaded. URL: " + driver.getCurrentUrl());
    }

    /**
     * Assign a role for a specific company in the user's row.
     * Finds the company column from the table header, scrolls right to it,
     * and selects the role from the dropdown.
     *
     * @param companyName - Company whose column dropdown to use
     * @param role        - Role visible text (e.g. "Group Application Admin")
     */
    public void assignRole(String companyName, String role) {
        waitForPageLoad();
        int colIndex = getCompanyColumnIndex(companyName);
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//table[@class='table table-striped']//tbody//tr[contains(., '"
                        + currentUserEmail + "')]//td[" + colIndex + "]//select")));
        scrollToElement(dropdown);
        wait.until(ExpectedConditions.elementToBeClickable(dropdown));
        new Select(dropdown).selectByVisibleText(role);
        waitForPageLoad();
        System.out.println("[UserRolesPage] Assigned role '" + role + "' for company: " + companyName);
    }

    /**
     * Get the currently selected role for a company in the user's row.
     *
     * @param companyName - Company whose column dropdown to read
     * @return Selected role text
     */
    public String getAssignedRole(String companyName) {
        int colIndex = getCompanyColumnIndex(companyName);
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//table[@class='table table-striped']//tbody//tr[contains(., '"
                        + currentUserEmail + "')]//td[" + colIndex + "]//select")));
        scrollToElement(dropdown);
        return new Select(dropdown).getFirstSelectedOption().getText();
    }

    /**
     * Check if the Users table is displayed.
     */
    public boolean isPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table[@class='table table-striped']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageHeading() {
        wait.until(ExpectedConditions.visibilityOf(pageHeading));
        return pageHeading.getAttribute("innerText");
    }

    // ── private helpers ──────────────────────────────────────────────────────

    /**
     * Find the 1-based column index of the given company in the table header.
     */
    private int getCompanyColumnIndex(String companyName) {
        List<WebElement> headers = driver.findElements(
                By.xpath("//table[@class='table table-striped']//thead//th"));
        System.out.println("[UserRolesPage] Table headers found (" + headers.size() + "):");
        for (int i = 0; i < headers.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + headers.get(i).getText());
            if (headers.get(i).getText().contains(companyName)) {
                System.out.println("[UserRolesPage] Found company '" + companyName
                        + "' at column index " + (i + 1));
                return i + 1;
            }
        }
        // Also dump all rows to understand structure
        List<WebElement> rows = driver.findElements(
                By.xpath("//table[@class='table table-striped']//tbody//tr"));
        System.out.println("[UserRolesPage] Table rows found (" + rows.size() + "):");
        for (int i = 0; i < Math.min(rows.size(), 5); i++) {
            System.out.println("  row[" + (i + 1) + "]: " + rows.get(i).getText());
        }
        throw new RuntimeException("[UserRolesPage] Company column not found in header for: " + companyName);
    }
}

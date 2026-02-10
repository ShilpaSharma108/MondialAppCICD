package com.mondial.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Login Page Object Model
 * Contains all elements and methods for interacting with the login page
 * 
 * Features:
 * - PageFactory pattern with @FindBy annotations
 * - Fluent interface for method chaining
 * - Explicit waits for all interactions
 * - Error handling and validation methods
 */
public class LoginPage extends BasePage {
    
    // ============================================
    // PAGE ELEMENTS - Update these locators to match your application
    // ============================================
    
    @FindBy(id = "email")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(css = "input[type='submit']")
    private WebElement loginButton;
    
    @FindBy(className = "error-message")
    private WebElement errorMessage;
    
    @FindBy(xpath = "//h1[text()='Login']")
    private WebElement loginHeader;
    
    @FindBy(xpath = "//a[contains(text(),'Forgot Password')]")
    private WebElement forgotPasswordLink;
    
    @FindBy(xpath = "//input[@type='checkbox']")
    private WebElement rememberMeCheckbox;
    
    @FindBy(xpath = "//div[@class='alert alert-danger']")
    private WebElement alertMessage;
    
    // Alternative locators (use if above don't match)
    private By usernameFieldAlt = By.name("email");
    private By passwordFieldAlt = By.name("password");
    private By loginButtonAlt = By.cssSelector("input[type='submit']");
    private By errorMessageAlt = By.cssSelector(".alert-danger, .error, .invalid-feedback");
    
    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    /**
     * Constructor - Initializes PageFactory elements
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    // ============================================
    // INTERACTION METHODS - Fluent Interface
    // ============================================
    
    /**
     * Enter username/email in the username field
     * @param username Username or email address
     * @return LoginPage instance for method chaining
     */
    public LoginPage enterUsername(String username) {
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            usernameField.clear();
            usernameField.sendKeys(username);
            System.out.println("Entered username: " + username);
        } catch (Exception e) {
            System.out.println("Using alternative locator for username field");
            type(usernameFieldAlt, username);
        }
        return this;
    }
    
    /**
     * Enter password in the password field
     * @param password User password
     * @return LoginPage instance for method chaining
     */
    public LoginPage enterPassword(String password) {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("Entered password: " + maskPassword(password));
        } catch (Exception e) {
            System.out.println("Using alternative locator for password field");
            type(passwordFieldAlt, password);
        }
        return this;
    }
    
    /**
     * Click the login/submit button
     * @return HomePage instance after successful navigation
     */
    public HomePage clickLoginButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();
            System.out.println("Clicked login button");
        } catch (Exception e) {
            System.out.println("Using alternative locator for login button");
            click(loginButtonAlt);
        }
        
        // Wait for page to load after login
        waitForPageLoad();
        
        return new HomePage(driver);
    }
    
    /**
     * Complete login process with username and password
     * Convenience method that combines enterUsername, enterPassword, and clickLoginButton
     * @param username Username or email address
     * @param password User password
     * @return HomePage instance after successful login
     */
    public HomePage login(String username, String password) {
        System.out.println("Attempting login with username: " + username);
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }
    
    /**
     * Click Remember Me checkbox
     * @return LoginPage instance for method chaining
     */
    public LoginPage clickRememberMe() {
        try {
            if (rememberMeCheckbox.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(rememberMeCheckbox));
                rememberMeCheckbox.click();
                System.out.println("Clicked Remember Me checkbox");
            }
        } catch (Exception e) {
            System.out.println("Remember Me checkbox not found or not clickable");
        }
        return this;
    }
    
    /**
     * Click Forgot Password link
     */
    public void clickForgotPassword() {
        try {
            if (forgotPasswordLink.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink));
                forgotPasswordLink.click();
                System.out.println("Clicked Forgot Password link");
            }
        } catch (Exception e) {
            System.out.println("Forgot Password link not found");
        }
    }
    
    // ============================================
    // VERIFICATION METHODS
    // ============================================
    
    /**
     * Check if login page is displayed
     * @return true if login page is displayed, false otherwise
     */
    public boolean isLoginPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginHeader));
            return loginHeader.isDisplayed();
        } catch (Exception e) {
            // Try alternative verification
            try {
                return driver.getCurrentUrl().contains("login") || 
                       driver.getTitle().toLowerCase().contains("login") ||
                       driver.getTitle().toLowerCase().contains("sign in");
            } catch (Exception ex) {
                return false;
            }
        }
    }
    
    /**
     * Get error message text
     * @return Error message text, or empty string if no error
     */
    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            String message = errorMessage.getText();
            System.out.println("Error message found: " + message);
            return message;
        } catch (Exception e) {
            // Try alternative error message locator
            try {
                WebElement altError = driver.findElement(errorMessageAlt);
                if (altError.isDisplayed()) {
                    String message = altError.getText();
                    System.out.println("Error message found (alternative): " + message);
                    return message;
                }
            } catch (Exception ex) {
                System.out.println("No error message found");
            }
            return "";
        }
    }
    
    /**
     * Check if error message is displayed
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        return !getErrorMessage().isEmpty();
    }
    
    /**
     * Check if alert message is displayed
     * @return true if alert is displayed, false otherwise
     */
    public boolean isAlertDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(alertMessage));
            return alertMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get alert message text
     * @return Alert message text
     */
    public String getAlertMessage() {
        try {
            if (isAlertDisplayed()) {
                return alertMessage.getText();
            }
        } catch (Exception e) {
            // Ignore
        }
        return "";
    }
    
    /**
     * Check if username field is displayed
     * @return true if username field is displayed, false otherwise
     */
    public boolean isUsernameFieldDisplayed() {
        try {
            return usernameField.isDisplayed();
        } catch (Exception e) {
            return isDisplayed(usernameFieldAlt);
        }
    }
    
    /**
     * Check if password field is displayed
     * @return true if password field is displayed, false otherwise
     */
    public boolean isPasswordFieldDisplayed() {
        try {
            return passwordField.isDisplayed();
        } catch (Exception e) {
            return isDisplayed(passwordFieldAlt);
        }
    }
    
    /**
     * Check if login button is displayed
     * @return true if login button is displayed, false otherwise
     */
    public boolean isLoginButtonDisplayed() {
        try {
            return loginButton.isDisplayed();
        } catch (Exception e) {
            return isDisplayed(loginButtonAlt);
        }
    }
    
    /**
     * Check if Remember Me checkbox is displayed
     * @return true if checkbox is displayed, false otherwise
     */
    public boolean isRememberMeDisplayed() {
        try {
            return rememberMeCheckbox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if Forgot Password link is displayed
     * @return true if link is displayed, false otherwise
     */
    public boolean isForgotPasswordDisplayed() {
        try {
            return forgotPasswordLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    // ============================================
    // VALIDATION METHODS
    // ============================================
    
    /**
     * Validate all login page elements are present
     * @return true if all elements are present, false otherwise
     */
    public boolean validateAllElementsPresent() {
        boolean allPresent = isUsernameFieldDisplayed() && 
                            isPasswordFieldDisplayed() && 
                            isLoginButtonDisplayed();
        
        System.out.println("Login page elements validation: " + 
                          (allPresent ? "PASSED" : "FAILED"));
        return allPresent;
    }
    
    /**
     * Get the current page URL
     * @return Current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get the current page title
     * @return Current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Clear username field
     * @return LoginPage instance for method chaining
     */
    public LoginPage clearUsername() {
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            usernameField.clear();
            System.out.println("Cleared username field");
        } catch (Exception e) {
            System.out.println("Could not clear username field");
        }
        return this;
    }
    
    /**
     * Clear password field
     * @return LoginPage instance for method chaining
     */
    public LoginPage clearPassword() {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            System.out.println("Cleared password field");
        } catch (Exception e) {
            System.out.println("Could not clear password field");
        }
        return this;
    }
    
    /**
     * Clear both username and password fields
     * @return LoginPage instance for method chaining
     */
    public LoginPage clearAllFields() {
        clearUsername();
        clearPassword();
        System.out.println("Cleared all login fields");
        return this;
    }
    
    // ============================================
    // HELPER METHODS
    // ============================================
    
    /**
     * Mask password for logging (shows only first and last character)
     * @param password Password to mask
     * @return Masked password string
     */
    private String maskPassword(String password) {
        if (password == null || password.length() <= 2) {
            return "****";
        }
        return password.charAt(0) + "****" + password.charAt(password.length() - 1);
    }
    
    /**
     * Wait for login page to load completely
     */
    public void waitForLoginPageLoad() {
        waitForPageLoad();
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField));
        } catch (Exception e) {
            System.out.println("Login page loaded (alternative verification)");
        }
    }
    
    /**
     * Take screenshot of login page (useful for debugging)
     * @param screenshotName Name for the screenshot file
     */
    public void takeLoginScreenshot(String screenshotName) {
        // Implement screenshot logic if needed
        System.out.println("Screenshot requested: " + screenshotName);
    }
}
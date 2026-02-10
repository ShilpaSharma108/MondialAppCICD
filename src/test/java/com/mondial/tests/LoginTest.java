package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.mondial.pages.LoginPage;
import com.mondial.utils.DriverManager;

public class LoginTest extends BaseTest {

    @BeforeMethod
    public void navigateToLoginPage() {
        String baseUrl = config.getProperty("base.url");

        // Clear app domain cookies
        driver.manage().deleteAllCookies();

        // Clear auth domain cookies to fully destroy the SSO session
        String authUrl = baseUrl.replaceAll("://[^.]+\\.", "://auth.");
        driver.get(authUrl);
        driver.manage().deleteAllCookies();

        // Navigate to base URL - redirects to clean login page
        driver.get(baseUrl);
    }

    @Test(priority = 1, description = "Verify valid user can login successfully")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");
        
        loginPage.login(username, password);
        
        // Wait for navigation after login
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        System.out.println("Current URL after login: " + currentUrl);
        
        // Verify successful login by checking URL contains expected path
        Assert.assertTrue(currentUrl.contains("dashboard") || 
                         currentUrl.contains("home") || 
                         !currentUrl.contains("login"), 
                         "Login failed - still on login page");
    }

    @Test(priority = 2, description = "Verify error message for invalid login")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.login("invalid@example.com", "wrongpass");
        
        // Wait for error message
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verify still on login page or error displayed
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login") || 
                         loginPage.isLoginButtonDisplayed(), 
                         "Should remain on login page for invalid credentials");
    }

    @Test(priority = 3, description = "Verify login with empty credentials")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.clickLoginButton();
        
        // Verify still on login page
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), 
                         "Should remain on login page with empty credentials");
    }
}
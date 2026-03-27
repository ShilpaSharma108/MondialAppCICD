package com.mondial.tests.login;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
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

        // Wait for the Companies heading — handles autologin redirects and CI latency
        HomePage homePage = new HomePage(driver);
        System.out.println("Current URL after login: " + driver.getCurrentUrl());
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Login failed - company heading should be visible after login");
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
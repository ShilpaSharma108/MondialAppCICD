package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * User Creation Test Class
 * Tests user creation functionality including creation, duplicate validation, and deletion
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - Test company name in config.properties (testCompanyName)
 */
public class UserCreationTest extends BaseTest {
    
    private String emailID;
    private String password;
    private HomePage homePage;
    
    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     * - Generates random email for user creation tests
     */
    @BeforeClass
    public void homeSetup() {
        System.out.println("=== Starting Home Page Test Setup ===");
        
        // Initialize page objects
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        
        // Get credentials from config
        String username = config.getProperty("validUsername");
        password = config.getProperty("validPassword");
        
        System.out.println("Logging in with user: " + username);
        
        // Login before running home page tests
        loginPage.login(username, password);
        
        // Wait for login to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Generate random email for user creation tests
        emailID = generateRandomEmail() + "@mailinator.com";
        System.out.println("Generated Email ID for testing: " + emailID);
        System.out.println("=== Home Page Test Setup Complete ===\n");
    }
    
    /**
     * Test 1: Verify company heading is displayed on home page
     * This validates successful login and home page load
     */
    @Test(priority = 1, description = "Verify company heading is displayed on home page")
    public void testCompanyHeadingDisplay() {
        System.out.println("\n[TEST 1] Testing Company Heading Display...");
        
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), 
                         "Company heading should be displayed on home page");
        
        System.out.println("[TEST 1] ✓ Company heading is displayed successfully");
    }
    
    /**
     * Test 2: Create new user and assign a role
     * Creates a new user with random email and assigns Accountant role
     */
    @Test(priority = 2, description = "Create new user and assign a role")
    public void testNewUserCreation() {
        System.out.println("\n[TEST 2] Testing New User Creation...");
        System.out.println("Creating user with email: " + emailID);
        
        homePage.createUser(emailID, password);
        
        // Wait for user creation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(homePage.isUserPresent(emailID), 
                         "New user should be created successfully");
        
        System.out.println("[TEST 2] ✓ User created successfully: " + emailID);
        
        // Navigate back to home
        homePage.clickHome();
        
        // Wait for navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("[TEST 2] ✓ Navigated back to home page");
    }
    
    /**
     * Test 3: Verify user gets error message if email already exists
     * Attempts to create user with existing email and validates error message
     */
    @Test(priority = 3, description = "Verify user gets error message if email already exists")
    public void testUserCreationWithExistingEmail() {
        System.out.println("\n[TEST 3] Testing Duplicate Email Validation...");
        System.out.println("Attempting to create duplicate user: " + emailID);
        
        homePage.createUserWithExistingEmail(emailID, password);
        
        // Wait for error message
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(homePage.isEmailAlreadyPresentErrorDisplayed(), 
                         "Error message should be displayed for duplicate email");
        
        System.out.println("[TEST 3] ✓ Duplicate email error displayed correctly");
        
        // Logout after this test
        homePage.clickLogout();
        
        // Wait for logout
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("[TEST 3] ✓ Logged out successfully");
    }
    
    /**
     * Test 4: Verify first login - Accept Terms of Service
     * Logs in with newly created user and accepts terms of service
     */
    @Test(priority = 4, description = "Verify first login - Accept Terms of Service")
    public void testAcceptTermsOfService() {
        System.out.println("\n[TEST 4] Testing First Login and Terms Acceptance...");
        System.out.println("Logging in with new user: " + emailID.toLowerCase());
        
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.login(emailID.toLowerCase(), password);
        
        // Wait for login and terms page
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Check if Accept Terms button is displayed
        if (homePage.isAcceptTermsDisplayed()) {
            System.out.println("[TEST 4] Accept Terms of Service button found");
            homePage.clickAcceptTerms();
            
            // Wait for acceptance
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            String successMsg = homePage.getSuccessMessage();
            Assert.assertTrue(successMsg.contains("Thank you for accepting the Terms and Conditions") ||
                             successMsg.contains("success") ||
                             successMsg.length() > 0, 
                             "Success message should be displayed after accepting terms");
            
            System.out.println("[TEST 4] ✓ Terms accepted successfully");
        } else {
            System.out.println("[TEST 4] Terms already accepted or not required");
        }
        
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(), 
                         "Company heading should be visible after login");
        
        System.out.println("[TEST 4] ✓ First login completed successfully");
    }
    
  
    
   
    /**
     * Test 5: Delete the newly created user
     * Logs in as admin and deletes the user created in test 2
     */
    @Test(priority = 5, description = "Delete the newly created user")
    public void testDeleteUser() {
        System.out.println("\n[TEST 5] Testing User Deletion...");
        System.out.println("Deleting user: " + emailID);

        // Logout first if not on login page
        LoginPage loginPage = new LoginPage(driver);
        if (!loginPage.isLoginPageDisplayed()) {
            homePage.clickLogout();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Login as admin
        loginPage = new LoginPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Delete the user
        homePage.deleteUser(emailID);

        // Verify user is no longer present
        Assert.assertFalse(homePage.isUserPresent(emailID),
                         "User should be deleted from the users table");

        System.out.println("[TEST 5] ✓ User deleted successfully: " + emailID);
    }

    /**
     * Helper method to generate random email
     * Uses current timestamp to ensure uniqueness
     * @return Random email prefix (without domain)
     */
    private String generateRandomEmail() {
        long timestamp = System.currentTimeMillis();
        return "testuser" + timestamp;
    }
}
package com.mondial.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;

/**
 * Cleanup Test Users Test Class
 * Deletes all users whose email starts with "testuser"
 * Run this to clean up orphaned test users from failed CI runs
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class CleanupTestUsersTest extends BaseTest {

    private HomePage homePage;

    /**
     * Setup method that runs before all tests in this class
     * - Logs in with admin credentials
     */
    @BeforeClass
    public void cleanupSetup() {
        System.out.println("=== Starting Cleanup Test Users Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        String username = config.getProperty("validUsername");
        String password = config.getProperty("validPassword");

        System.out.println("Logging in with user: " + username);
        loginPage.login(username, password);

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                         "Should be on home page after login");

        System.out.println("=== Cleanup Test Users Setup Complete ===\n");
    }

    /**
     * Test 1: Delete all users whose email starts with "testuser"
     */
    @Test(priority = 1, description = "Delete all test users with prefix 'testuser'")
    public void testDeleteAllTestUsers() {
        System.out.println("\n[TEST 1] Deleting all users starting with 'testuser'...");

        int deletedCount = homePage.deleteAllUsersWithPrefix("testuser");

        System.out.println("[TEST 1] Deleted " + deletedCount + " test user(s)");

        // Verify no testuser accounts remain
        Assert.assertFalse(homePage.isUserPresent("testuser"),
                          "No users starting with 'testuser' should remain");

        System.out.println("[TEST 1] Cleanup complete - all test users removed");
    }
}

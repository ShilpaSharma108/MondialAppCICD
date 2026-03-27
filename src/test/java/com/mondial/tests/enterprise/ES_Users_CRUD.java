package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.UsersPage;

/**
 * Users CRUD Test Class
 * Tests Create, Read, Update, and Delete operations for Users
 * under Enterprise Setup > Users.
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ES_Users_CRUD extends BaseTest {

    private HomePage homePage;
    private UsersPage usersPage;
    private String userEmail;

    @BeforeClass
    public void usersSetup() {
        System.out.println("=== Starting Users CRUD Test Setup ===");

        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        usersPage = new UsersPage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        userEmail = ("testuser_" + System.currentTimeMillis() + "@mailinator.com").toLowerCase();
        System.out.println("Generated user email: " + userEmail);
        System.out.println("=== Users CRUD Test Setup Complete ===\n");
    }

    /**
     * Test 1: Navigate to Enterprise Setup > Users page
     */
    @Test(priority = 1, description = "Verify Navigation to Users Page")
    public void testNavigateToUsers() {
        System.out.println("\n[TEST 1] Navigating to Users page...");

        usersPage.navigateToUsersPage();

        Assert.assertTrue(usersPage.isAddUserBtnDisplayed(),
                "Add User button should be displayed");

        String heading = usersPage.getPageHeading();
        System.out.println("Page heading: " + heading);
        Assert.assertTrue(heading.contains("Users"),
                "Heading should contain 'Users'");

        System.out.println("[TEST 1] Successfully navigated to Users page");
    }

    /**
     * Test 2: Verify Cancel works, then create a new User
     */
    @Test(priority = 2, dependsOnMethods = {"testNavigateToUsers"}, description = "Verify Creation of a new User")
    public void testCreateUser() {
        System.out.println("\n[TEST 2] Creating user: " + userEmail);

        usersPage.verifyCancel(userEmail);
        Assert.assertTrue(usersPage.getPageHeading().contains("Users"),
                "Should return to Users listing after Cancel");

        usersPage.createUser(userEmail);

        Assert.assertTrue(usersPage.getSuccessMessage().contains("User was successfully created."),
                "Success message should confirm user creation");

        usersPage.waitForSuccessMessageToDisappear();

        Assert.assertTrue(usersPage.verifyRecordPresent(userEmail),
                "Newly created user should appear in the table");

        System.out.println("[TEST 2] User created successfully: " + userEmail);
    }

    /**
     * Test 3: Edit — cancel, then rename the user
     */
    @Test(priority = 3, dependsOnMethods = {"testCreateUser"}, description = "Verify Edit User functionality")
    public void testEditUser() {
        System.out.println("\n[TEST 3] Editing user: " + userEmail);

        usersPage.editRecord(userEmail);
        Assert.assertTrue(usersPage.isSubmitBtnDisplayed(),
                "Submit button should be displayed on edit page");

        usersPage.cancelAndWaitForListing();

        usersPage.editRecord(userEmail);
        userEmail = usersPage.renameRecord(userEmail);

        Assert.assertTrue(usersPage.verifyRecordPresent(userEmail),
                "Updated user email should appear in the table");

        System.out.println("[TEST 3] User updated to: " + userEmail);
    }

    /**
     * Test 4: Delete the user
     */
    @Test(priority = 4, dependsOnMethods = {"testEditUser"}, description = "Verify Delete User")
    public void testDeleteUser() {
        System.out.println("\n[TEST 4] Deleting user: " + userEmail);

        usersPage.deleteRecord(userEmail);

        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No confirmation dialog present");
        }

        Assert.assertTrue(usersPage.getSuccessMessage().contains("User was successfully deleted"),
                "Success message should confirm user deletion");

        usersPage.waitForSuccessMessageToDisappear();

        Assert.assertFalse(usersPage.verifyRecordPresent(userEmail),
                "Deleted user should no longer appear in the table");

        System.out.println("[TEST 4] User deleted successfully: " + userEmail);
    }
}

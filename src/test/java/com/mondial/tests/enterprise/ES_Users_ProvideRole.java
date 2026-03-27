package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.UsersPage;

/**
 * Verifies that a role can be assigned to a newly created user,
 * and that the user can then be deleted.
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ES_Users_ProvideRole extends BaseTest {

    private HomePage homePage;
    private UsersPage usersPage;
    private String userEmail;

    @BeforeClass
    public void setup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        usersPage = new UsersPage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        userEmail = ("testuser_" + System.currentTimeMillis() + "@mailinator.com").toLowerCase();
        System.out.println("Generated user email: " + userEmail);
    }

    @Test(priority = 1, description = "Create a new user and verify it appears in the table")
    public void testCreateUser() {
        System.out.println("\n[TEST 1] Creating user: " + userEmail);

        usersPage.navigateToUsersPage();
        Assert.assertTrue(usersPage.getPageHeading().contains("Users"),
                "Should be on Users page");

        usersPage.createUser(userEmail);

        Assert.assertTrue(usersPage.getSuccessMessage().contains("User was successfully created."),
                "Success message should confirm user creation");
        usersPage.waitForSuccessMessageToDisappear();

        Assert.assertTrue(usersPage.verifyRecordPresent(userEmail),
                "Newly created user should appear in the table");

        System.out.println("[TEST 1] User created successfully: " + userEmail);
    }

    @Test(priority = 2, description = "Assign the Accountant role to the created user and delete")
    public void testProvideRoleToUser() {
        System.out.println("\n[TEST 2] Assigning role to user: " + userEmail);

        usersPage.selectRole(userEmail);

        System.out.println("[TEST 2] Role assigned successfully: " + userEmail);

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

        System.out.println("[TEST 2] User deleted successfully: " + userEmail);
    }
}

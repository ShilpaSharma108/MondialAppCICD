package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.UsersPage;

/**
 * Verifies password validation error messages on the Create User form.
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ES_Users_ErrorMessages extends BaseTest {

    private HomePage homePage;
    private UsersPage usersPage;
    private final String userEmail = "TestErrorMessages@mondialsoftware.com";

    @BeforeClass
    public void setup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        usersPage = new UsersPage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");
    }

    @Test(priority = 1, description = "Verify password validation error messages on Create User form")
    public void testPasswordValidationErrors() {
        System.out.println("\n[TEST 1] Verifying password validation error messages...");

        usersPage.navigateToUsersPage();
        usersPage.navigateToCreateForm();

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials(userEmail, "Mondial123456", "Mondial123456")
                .contains("Password must contain at least 1 special character"),
                "Should require special character");

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials(userEmail, "mondial@123456", "mondial@123456")
                .contains("Password must contain at least 1 uppercase letter"),
                "Should require uppercase letter");

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials(userEmail, "MONDIAL@123456", "MONDIAL@123456")
                .contains("Password must contain at least 1 lowercase letter"),
                "Should require lowercase letter");

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials(userEmail, "Mondial@mondial", "Mondial@mondial")
                .contains("Password must contain at least 1 number"),
                "Should require a number");

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials(userEmail, "Mondial@123456", "Nondial@123456")
                .contains("Password confirmation doesn't match Password"),
                "Should flag mismatched passwords");

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials(userEmail, "Mondial@123456", "")
                .contains("Password confirmation doesn't match Password"),
                "Should flag empty confirmation");

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials(userEmail, "", "Mondial@123456")
                .contains("Password can't be blank"),
                "Should flag blank password");

        Assert.assertTrue(usersPage.errorMessage_invalidCredentials("", "Mondial@123456", "Mondial@123456")
                .contains("Email can't be blank"),
                "Should flag blank email");

        System.out.println("[TEST 1] All password validation error messages verified");
    }

    @Test(priority = 2, description = "Verify error when email has already been taken")
    public void testEmailAlreadyTakenError() {
        System.out.println("\n[TEST 2] Verifying email already taken error message...");

        usersPage.navigateToUsersPage();
        usersPage.navigateToCreateForm();

        Assert.assertTrue(
                usersPage.errorMessage_invalidCredentials(config.getProperty("validUsername"), "Testing@1234", "Testing@1234")
                        .contains("Email has already been taken"),
                "Should flag email already taken");

        System.out.println("[TEST 2] Email already taken error message verified");
    }
}

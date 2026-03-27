package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.pages.UsersPage;

/**
 * Verifies that a Consumer (non-admin) user cannot access the Users section
 * under Enterprise Setup.
 *
 * Prerequisites:
 * - Consumer credentials in config.properties (Consumer, validPassword)
 */
public class ES_Users_UnauthorisedUser extends BaseTest {

    private HomePage homePage;
    private UsersPage usersPage;

    @BeforeClass
    public void setup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        usersPage = new UsersPage(driver);

        loginPage.login(config.getProperty("Consumer"), config.getProperty("validPassword"));

        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after Consumer login");
    }

    @Test(priority = 1, description = "Verify Consumer cannot access Users under Enterprise Setup")
    public void testUsersMenuNotAccessibleForConsumer() {
        System.out.println("\n[TEST 1] Verifying Consumer cannot see Users menu...");

        usersPage.clickEnterpriseSetup();

        Assert.assertTrue(usersPage.isUsersMenuNotPresent(),
                "Users menu should NOT be visible to Consumer role");

        System.out.println("[TEST 1] Confirmed: Users menu is not accessible for Consumer role");
    }
}

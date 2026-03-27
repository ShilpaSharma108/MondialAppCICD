package com.mondial.tests.company;

import com.mondial.tests.BaseTest;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

/** Temporary discovery test — prints Add Company form fields, does NOT submit */
public class DiscoverCompanyForm extends BaseTest {

    private HomePage homePage;

    @BeforeClass
    public void setup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        homePage.isCompanyHeadingDisplayed();
    }

    @Test
    public void discoverAddCompanyForm() {
        homePage.clickAddCompany();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        System.out.println("\n=== URL: " + driver.getCurrentUrl() + " ===");

        System.out.println("\n--- INPUT FIELDS ---");
        for (WebElement e : driver.findElements(By.tagName("input"))) {
            System.out.println("  id='" + e.getAttribute("id")
                    + "'  name='" + e.getAttribute("name")
                    + "'  type='" + e.getAttribute("type")
                    + "'  value='" + e.getAttribute("value") + "'");
        }

        System.out.println("\n--- SELECT FIELDS ---");
        for (WebElement e : driver.findElements(By.tagName("select"))) {
            System.out.println("  id='" + e.getAttribute("id")
                    + "'  name='" + e.getAttribute("name") + "'");
        }

        System.out.println("\n--- TEXTAREA FIELDS ---");
        for (WebElement e : driver.findElements(By.tagName("textarea"))) {
            System.out.println("  id='" + e.getAttribute("id")
                    + "'  name='" + e.getAttribute("name") + "'");
        }
    }
}

package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;
import com.mondial.pages.ExchangeRatePage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.utils.DriverManager;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Exchange Rate Source - CSV Upload and Download Test
 * Verifies that the template CSV can be downloaded from the ERS edit form
 * and that a CSV file can be uploaded successfully
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 */
public class ES_ExchangeVerifyCSVUploadDownload extends BaseTest {

    private final String TEMPLATE_CSV_PATH =
            DriverManager.getDownloadDir() + java.io.File.separator + "exchange_rates_csv_upload_template.csv";

    private HomePage homePage;
    private ExchangeRatePage exchangeRatePage;
    private String ersName;

    /**
     * Setup: login, initialise page objects, generate ERS name and delete any stale template CSV
     */
    @BeforeClass
    public void setup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        exchangeRatePage = new ExchangeRatePage(driver);

        // Remove stale template file before test to ensure a fresh download
        new java.io.File(TEMPLATE_CSV_PATH).delete();

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
        ersName = "ERS_" + System.currentTimeMillis();
        System.out.println("Generated ERS name: " + ersName);
    }

    /**
     * Test 1: Create ERS, navigate to its edit form, and download the template CSV
     */
    @Test(priority = 1, description = "Verify CSV template file can be downloaded from ERS edit form")
    public void downloadCSVFile() {
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        exchangeRatePage.exchangeRateCreation(ersName);
        Assert.assertTrue(exchangeRatePage.verifyRecordPresent(ersName),
                "ERS should be present before navigating to edit");

        exchangeRatePage.clickEditRecord(ersName);
        exchangeRatePage.clickDownloadTemplateCSV();

        Assert.assertTrue(exchangeRatePage.isFileDownloaded(TEMPLATE_CSV_PATH),
                "Template CSV should be downloaded at: " + TEMPLATE_CSV_PATH);

        System.out.println("[TEST 1] Template CSV downloaded: " + TEMPLATE_CSV_PATH);
    }

    /**
     * Test 2: Upload the downloaded template CSV, save and clean up
     */
    @Test(priority = 2, dependsOnMethods = {"downloadCSVFile"},
          description = "Verify CSV file can be uploaded to an Exchange Rate Source")
    public void uploadCSVFile() {
        System.out.println("[TEST 2] Uploading file: " + TEMPLATE_CSV_PATH);

        exchangeRatePage.uploadCSVFile(TEMPLATE_CSV_PATH);
        exchangeRatePage.clickUpdateBtn();

        Assert.assertTrue(exchangeRatePage.verifyRecordPresent(ersName),
                "ERS should still be present after CSV upload and save");

        exchangeRatePage.deleteRecord(ersName);

        Assert.assertFalse(exchangeRatePage.verifyRecordPresent(ersName),
                "ERS should be removed after deletion");

        System.out.println("[TEST 2] CSV upload verified and ERS cleaned up");
    }
}

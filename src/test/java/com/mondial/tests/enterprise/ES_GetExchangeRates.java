package com.mondial.tests.enterprise;

import com.mondial.tests.BaseTest;
import com.mondial.pages.ExchangeRatePage;
import com.mondial.pages.HomePage;
import com.mondial.pages.LoginPage;
import com.mondial.utils.DriverManager;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Exchange Rates - Show and Download Test
 * Verifies that exchange rates can be displayed and the CSV download works
 *
 * Prerequisites:
 * - Valid admin credentials in config.properties (validUsername, validPassword)
 * - At least one Exchange Rate Source with data must exist in the system
 */
public class ES_GetExchangeRates extends BaseTest {

    private HomePage homePage;
    private ExchangeRatePage exchangeRatePage;

    /**
     * Setup: login and initialise page objects
     */
    @BeforeClass
    public void setup() {
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        exchangeRatePage = new ExchangeRatePage(driver);

        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));
    }

    /**
     * Test 1: Navigate to Exchange Rates, select European Central Bank / USD / GBP and verify rates are shown
     */
    @Test(priority = 1, description = "Show Exchange Rates for European Central Bank - USD to GBP")
    public void getExchangeRateERS() {
        Assert.assertTrue(homePage.isCompanyHeadingDisplayed(),
                "Company heading should be displayed after login");

        exchangeRatePage.navigateERatesMenu();

        int rowCount = exchangeRatePage.showRates("European Central Bank", "USD", "GBP");
        Assert.assertTrue(rowCount > 0,
                "Exchange rate results table should not be empty for European Central Bank USD -> GBP");
    }

    /**
     * Test 2: Download the exchange rates CSV and verify the file is created
     */
    @Test(priority = 2, dependsOnMethods = {"getExchangeRateERS"}, description = "Download File")
    public void downloadExchangeRatesFile() {
        exchangeRatePage.clickDownloadCSV();

        String downloadDir = DriverManager.getDownloadDir();
        Assert.assertTrue(isFileDownloaded(downloadDir, "exchange_rates"),
                "exchange_rates CSV file should be downloaded to: " + downloadDir);
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Check whether a file whose name starts with the given prefix exists in a directory
     * @param directoryPath - Path to the directory to check
     * @param filePrefix    - Expected file name prefix
     * @return true if a matching file is found, false otherwise
     */
    private boolean isFileDownloaded(String directoryPath, String filePrefix) {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Download directory not found: " + directoryPath);
            return false;
        }
        int maxWaitSeconds = 15;
        for (int i = 0; i < maxWaitSeconds; i++) {
            File[] files = dir.listFiles((d, name) -> name.startsWith(filePrefix));
            if (files != null && files.length > 0) {
                System.out.println("Downloaded file found: " + files[0].getName());
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("No file with prefix '" + filePrefix + "' found in: " + directoryPath);
        return false;
    }
}

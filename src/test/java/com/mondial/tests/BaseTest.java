package com.mondial.tests;

import org.testng.annotations.*;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mondial.utils.DriverManager;
import com.mondial.utils.ConfigReader;

/**
 * Base Test Class
 * Parent class for all test classes providing common setup and teardown functionality
 * 
 * Features:
 * - Browser initialization and cleanup
 * - Screenshot capture on test failure
 * - Configurable browser and URL from properties or system properties
 * - Test execution logging
 * - CI/CD compatible (headless mode auto-detection)
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected ConfigReader config = new ConfigReader();
    protected static final String SCREENSHOT_DIR = "screenshots/";
    
    /**
     * Setup method executed before each test class
     * Initializes browser, navigates to base URL, maximizes window
     */
    @BeforeClass
    public void setUp() {
        System.out.println("\n========================================");
        System.out.println("Starting Test Suite Setup");
        System.out.println("========================================");
        
        // Get browser from system property (for CI/CD) or config file
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = ConfigReader.getBrowser();
            System.out.println("Using browser from config: " + browser);
        } else {
            System.out.println("Using browser from system property: " + browser);
        }
        
        // Initialize WebDriver
        System.out.println("Initializing WebDriver...");
        DriverManager.setDriver(browser);
        driver = DriverManager.getDriver();
        System.out.println("✓ WebDriver initialized successfully");
        
        // Get base URL from config
        String baseUrl = getBaseUrl();
        System.out.println("Navigating to: " + baseUrl);
        
        // Navigate to application
        driver.get(baseUrl);
        
        // Maximize window
        driver.manage().window().maximize();
        System.out.println("✓ Browser window maximized");
        
        // Print test environment info
        printTestEnvironment(browser, baseUrl);
        
        System.out.println("========================================");
        System.out.println("Test Suite Setup Complete");
        System.out.println("========================================\n");
    }
    
    /**
     * Teardown method executed after each test class
     * Closes browser and cleans up resources
     */
    @AfterClass
    public void tearDown() {
        System.out.println("\n========================================");
        System.out.println("Starting Test Suite Teardown");
        System.out.println("========================================");
        
        if (driver != null) {
            System.out.println("Closing browser...");
            DriverManager.quitDriver();
            System.out.println("✓ Browser closed successfully");
        }
        
        System.out.println("========================================");
        System.out.println("Test Suite Teardown Complete");
        System.out.println("========================================\n");
    }
    
    /**
     * Method executed after each test method
     * Captures screenshot on test failure
     * @param result TestNG test result object
     */
    @AfterMethod
    public void afterMethod(ITestResult result) {
        String testName = result.getName();
        
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("\n✗ TEST FAILED: " + testName);
            System.out.println("Failure Reason: " + result.getThrowable().getMessage());
            captureScreenshot(testName);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("\n✓ TEST PASSED: " + testName);
        } else if (result.getStatus() == ITestResult.SKIP) {
            System.out.println("\n⊘ TEST SKIPPED: " + testName);
        }
    }
    
    /**
     * Method executed before each test method
     * Logs test start information
     * @param result TestNG test result object
     */
    @BeforeMethod
    public void beforeMethod(java.lang.reflect.Method method) {
        System.out.println("\n----------------------------------------");
        System.out.println("Starting Test: " + method.getName());
        
        // Get test description if available
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null && !testAnnotation.description().isEmpty()) {
            System.out.println("Description: " + testAnnotation.description());
        }
        
        System.out.println("----------------------------------------");
    }
    
    /**
     * Get base URL from config with fallback options
     * @return Base URL string
     */
    private String getBaseUrl() {
        // Try system property first (for CI/CD override)
        String baseUrl = System.getProperty("baseUrl");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            return baseUrl;
        }
        
        // Try base.url property
        baseUrl = config.getProperty("base.url");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            return baseUrl;
        }

        // Try baseUrl property (alternative naming)
        baseUrl = config.getProperty("baseUrl");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            return baseUrl;
        }

        // Fallback to getBaseUrl method
        baseUrl = ConfigReader.getBaseUrl();
        if (baseUrl != null && !baseUrl.isEmpty()) {
            return baseUrl;
        }
        
        // Final fallback
        System.out.println("WARNING: Base URL not found in config, using default");
        return "https://f6a9211c-8880-4c32-9bc6-6a6019d10bd6.dev.mondialsoftware.com";
    }
    
    /**
     * Capture screenshot and save to file
     * @param testName Name of the test (used for screenshot filename)
     */
    protected void captureScreenshot(String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            
            // Generate filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String destination = SCREENSHOT_DIR + fileName;
            
            // Copy screenshot to destination
            File finalDestination = new File(destination);
            FileUtils.copyFile(source, finalDestination);
            
            System.out.println("✓ Screenshot captured: " + destination);
        } catch (IOException e) {
            System.out.println("✗ Failed to capture screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Print test environment information
     * @param browser Browser name
     * @param baseUrl Application URL
     */
    private void printTestEnvironment(String browser, String baseUrl) {
        System.out.println("\n--- Test Environment ---");
        System.out.println("Browser: " + browser);
        System.out.println("URL: " + baseUrl);
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Java Version: " + System.getProperty("java.version"));
        
        // Check if running in CI
        String ciEnv = System.getenv("CI");
        if (ciEnv != null && !ciEnv.isEmpty()) {
            System.out.println("CI/CD Mode: YES (Headless)");
        } else {
            System.out.println("CI/CD Mode: NO (Normal)");
        }
        System.out.println("------------------------\n");
    }
    
    /**
     * Get WebDriver instance
     * Useful for test classes that need direct driver access
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Navigate to a specific URL
     * @param url URL to navigate to
     */
    protected void navigateTo(String url) {
        System.out.println("Navigating to: " + url);
        driver.get(url);
    }
    
    /**
     * Refresh the current page
     */
    protected void refreshPage() {
        System.out.println("Refreshing page...");
        driver.navigate().refresh();
    }
    
    /**
     * Navigate back to previous page
     */
    protected void navigateBack() {
        System.out.println("Navigating back...");
        driver.navigate().back();
    }
    
    /**
     * Navigate forward to next page
     */
    protected void navigateForward() {
        System.out.println("Navigating forward...");
        driver.navigate().forward();
    }
    
    /**
     * Get current page URL
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get current page title
     * @return Current page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Wait for specified milliseconds
     * Use sparingly - prefer explicit waits in page objects
     * @param milliseconds Time to wait
     */
    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted: " + e.getMessage());
        }
    }
    
    /**
     * Close current browser window
     */
    protected void closeCurrentWindow() {
        System.out.println("Closing current window...");
        driver.close();
    }
    
    /**
     * Switch to a specific window by index
     * @param windowIndex Index of window to switch to
     */
    protected void switchToWindow(int windowIndex) {
        Object[] windows = driver.getWindowHandles().toArray();
        if (windowIndex < windows.length) {
            driver.switchTo().window(windows[windowIndex].toString());
            System.out.println("Switched to window: " + windowIndex);
        } else {
            System.out.println("Window index " + windowIndex + " does not exist");
        }
    }
    
    /**
     * Get the number of open windows
     * @return Number of windows
     */
    protected int getWindowCount() {
        return driver.getWindowHandles().size();
    }
    
    /**
     * Delete all cookies
     */
    protected void deleteAllCookies() {
        System.out.println("Deleting all cookies...");
        driver.manage().deleteAllCookies();
    }
    
    /**
     * Capture screenshot for passed tests (optional)
     * @param testName Name of the test
     */
    protected void captureScreenshotForPassed(String testName) {
        captureScreenshot(testName + "_PASSED");
    }
}
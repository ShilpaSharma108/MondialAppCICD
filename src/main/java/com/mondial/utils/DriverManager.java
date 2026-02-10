package com.mondial.utils;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ConfigReader config = new ConfigReader();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            initializeDriver();
        }
        return driver.get();
    }

    public static void setDriver(String browser) {
        System.setProperty("browser", browser);
        initializeDriver();
    }

    private static void initializeDriver() {
        String browser = getBrowserFromSystemOrConfig();
        boolean headless = getHeadlessFromSystemOrConfig();
        String os = getOSFromSystemOrConfig();

        System.out.println("=================================");
        System.out.println("Initializing Driver...");
        System.out.println("OS: " + os);
        System.out.println("Browser: " + browser);
        System.out.println("Headless: " + headless);
        System.out.println("=================================");

        switch (browser.toLowerCase()) {
            case "chrome":
                setupChrome(headless, os);
                break;

            case "firefox":
                setupFirefox(headless, os);
                break;

            case "edge":
                setupEdge(headless, os);
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser + 
                    ". Supported browsers: chrome, firefox, edge");
        }

        driver.get().manage().timeouts().implicitlyWait(
            Duration.ofSeconds(Integer.parseInt(config.getProperty("implicitWait")))
        );
        driver.get().manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(Integer.parseInt(config.getProperty("pageLoadTimeout")))
        );
        driver.get().manage().window().maximize();
    }

    private static void setupChrome(boolean headless, String os) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        // Common Chrome arguments
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Set experimental options correctly
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Add preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        
        // OS specific options
        if (os.equalsIgnoreCase("ubuntu")) {
            options.addArguments("--disable-setuid-sandbox");
            options.addArguments("--remote-debugging-port=9222");
        }
        
        driver.set(new ChromeDriver(options));
    }

    private static void setupFirefox(boolean headless, String os) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Common Firefox arguments
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        // Disable automation flags
        options.addPreference("dom.webdriver.enabled", false);
        options.addPreference("useAutomationExtension", false);
        
        driver.set(new FirefoxDriver(options));
    }

    private static void setupEdge(boolean headless, String os) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        // Common Edge arguments
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Set experimental options correctly
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        
        driver.set(new EdgeDriver(options));
    }

    private static String getBrowserFromSystemOrConfig() {
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = config.getProperty("browser");
        }
        return browser != null ? browser : "chrome";
    }

    private static boolean getHeadlessFromSystemOrConfig() {
        String headless = System.getProperty("headless");
        if (headless == null || headless.isEmpty()) {
            headless = config.getProperty("headless");
        }
        return Boolean.parseBoolean(headless);
    }

    private static String getOSFromSystemOrConfig() {
        String os = System.getProperty("os");
        if (os == null || os.isEmpty()) {
            os = config.getProperty("os");
        }
        return os != null ? os : "windows";
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static String getBrowserName() {
        return getBrowserFromSystemOrConfig();
    }

    public static String getOSName() {
        return getOSFromSystemOrConfig();
    }
}

package com.mondial.utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;
    private static ConfigReader instance;

    public ConfigReader() {
        loadProperties();
    }

    private static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        String os = System.getProperty("os");

        // Determine which config file to load
        String configFile;
        if (os != null && os.equalsIgnoreCase("ubuntu")) {
            configFile = "src/main/resources/ubantu-config.properties";
        } else if (os != null && os.equalsIgnoreCase("windows")) {
            configFile = "src/main/resources/windows-config.properties";
        } else {
            configFile = "src/main/resources/config.properties";
        }

        try {
            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
            fis.close();
            System.out.println("Loaded configuration from: " + configFile);
        } catch (IOException e) {
            // Fallback to default config
            System.out.println("Could not load " + configFile + ", loading default config.properties");
            try {
                FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
                properties.load(fis);
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Failed to load any config.properties file");
            }
        }
    }

    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isEmpty()) {
            value = properties.getProperty(key);
        }
        return value;
    }

    public static String getBrowser() {
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = getInstance().getProperty("browser");
        }
        return browser != null ? browser : "chrome";
    }

    public static String getBaseUrl() {
        String baseUrl = System.getProperty("baseUrl");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = getInstance().getProperty("baseUrl");
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = getInstance().getProperty("base.url");
        }
        return baseUrl;
    }

    public static String getStaticProperty(String key) {
        return getInstance().getProperty(key);
    }
}

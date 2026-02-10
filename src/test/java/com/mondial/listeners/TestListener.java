package com.mondial.listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.mondial.utils.ConfigReader;
import com.mondial.utils.DriverManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    
    private ConfigReader config = new ConfigReader();

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("=================================");
        System.out.println("Starting Test: " + result.getName());
        System.out.println("=================================");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✓ Test PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("✗ Test FAILED: " + result.getName());
        System.out.println("Failure Reason: " + result.getThrowable().getMessage());
        
        if (Boolean.parseBoolean(config.getProperty("captureScreenshotOnFailure"))) {
            captureScreenshot(result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⊘ Test SKIPPED: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n========================================");
        System.out.println("Starting Test Suite: " + context.getName());
        System.out.println("========================================\n");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n========================================");
        System.out.println("Finished Test Suite: " + context.getName());
        System.out.println("Total Tests: " + context.getAllTestMethods().length);
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("========================================\n");
    }

    private void captureScreenshot(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            File source = ts.getScreenshotAs(OutputType.FILE);
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            
            // Create screenshots directory if it doesn't exist
            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }
            
            File destination = new File("screenshots/" + fileName);
            FileUtils.copyFile(source, destination);
            
            System.out.println("Screenshot captured: " + destination.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}

package com.parabank.automation.listeners;

import com.aventstack.extentreports.Status;
import com.parabank.automation.logger.Log;
import com.parabank.automation.reports.ExtentReport;
import com.parabank.automation.utilities.ScreenshotUtility;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        Log.info(TestListener.class, "Test suite started: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReport.startTest(result.getMethod().getMethodName());
        Log.info(TestListener.class, "Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReport.logPass("Test passed: " + result.getMethod().getMethodName());
        Log.info(TestListener.class, "Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String screenshotPath = ScreenshotUtility.captureScreenshot(testName);

        ExtentReport.logFail("Test failed: " + testName);
        if (result.getThrowable() != null) {
            ExtentReport.getTest().log(Status.FAIL, result.getThrowable());
        }
        ExtentReport.attachScreenshot(screenshotPath);
        Log.error(TestListener.class, "Test failed: " + testName, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentReport.getTest() != null) {
            ExtentReport.getTest().log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
            if (result.getThrowable() != null) {
                ExtentReport.getTest().log(Status.SKIP, result.getThrowable());
            }
        }
        Log.warn(TestListener.class, "Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReport.flush();
        Log.info(TestListener.class, "Test suite finished: " + context.getName());
    }
}

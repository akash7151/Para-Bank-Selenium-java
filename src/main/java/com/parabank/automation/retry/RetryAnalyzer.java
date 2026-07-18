package com.parabank.automation.retry;

import com.parabank.automation.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < ConfigReader.getRetryCount()) {
            retryCount++;
            return true;
        }
        return false;
    }
}

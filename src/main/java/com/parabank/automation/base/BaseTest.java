package com.parabank.automation.base;

import com.parabank.automation.config.ConfigReader;
import com.parabank.automation.config.EnvironmentManager;
import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.factory.PageObjectManager;
import com.parabank.automation.logger.Log;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected final Logger logger = Log.getLogger(getClass());
    protected PageObjectManager pages;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        pages = new PageObjectManager();
        logger.info("Launching browser for environment: {}", EnvironmentManager.getCurrentEnvironment().name());
        DriverFactory.getDriver().get(ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Closing browser after test execution");
        DriverFactory.quitDriver();
        if (pages != null) {
            pages.reset();
        }
        pages = null;
    }
}

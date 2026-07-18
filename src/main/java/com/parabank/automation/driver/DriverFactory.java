package com.parabank.automation.driver;

import com.parabank.automation.config.ConfigReader;
import com.parabank.automation.enums.BrowserType;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(initDriver());
        }
        return driverThreadLocal.get();
    }

    private static WebDriver initDriver() {
        BrowserType browserType = BrowserType.fromString(ConfigReader.getBrowser());
        WebDriver driver = BrowserFactory.createBrowser(browserType);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        driver.manage().window().maximize();
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            Log.info(DriverFactory.class, "Browser closed successfully");
        }
    }
}

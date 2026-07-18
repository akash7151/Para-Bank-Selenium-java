package com.parabank.automation.driver;

import com.parabank.automation.config.ConfigReader;
import com.parabank.automation.enums.BrowserType;
import com.parabank.automation.logger.Log;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class BrowserFactory {

    private BrowserFactory() {
    }

    public static WebDriver createBrowser(BrowserType browserType) {
        boolean headless = ConfigReader.isHeadless();

        return switch (browserType) {
            case FIREFOX -> createFirefox(headless);
            case EDGE -> createEdge(headless);
            default -> createChrome(headless);
        };
    }

    private static WebDriver createChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized", "--disable-notifications", "--remote-allow-origins=*");
        Log.info(BrowserFactory.class, "Launching Chrome browser (headless=" + headless + ")");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        Log.info(BrowserFactory.class, "Launching Firefox browser (headless=" + headless + ")");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdge(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized");
        Log.info(BrowserFactory.class, "Launching Edge browser (headless=" + headless + ")");
        return new EdgeDriver(options);
    }
}

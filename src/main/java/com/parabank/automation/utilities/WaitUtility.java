package com.parabank.automation.utilities;

import com.parabank.automation.config.ConfigReader;
import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class WaitUtility {

    private WaitUtility() {
    }

    public static WebDriverWait getWait() {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public static WebElement waitForElement(By locator, WaitStrategy strategy) {
        return switch (strategy) {
            case CLICKABLE -> waitForElementClickable(locator);
            case PRESENCE -> getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
            case INVISIBLE -> {
                waitForElementInvisible(locator);
                yield null;
            }
            case NONE -> DriverFactory.getDriver().findElement(locator);
            default -> waitForElementVisible(locator);
        };
    }

    public static WebElement waitForElementVisible(By locator) {
        Log.debug(WaitUtility.class, "Waiting for element to be visible: " + locator);
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementClickable(By locator) {
        Log.debug(WaitUtility.class, "Waiting for element to be clickable: " + locator);
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForElementInvisible(By locator) {
        Log.debug(WaitUtility.class, "Waiting for element to be invisible: " + locator);
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForTextPresent(By locator, String text) {
        return getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static List<WebElement> waitForAllElementsVisible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static void waitForPageTitle(String title) {
        getWait().until(ExpectedConditions.titleContains(title));
    }

    public static void waitForUrlContains(String urlFragment) {
        getWait().until(ExpectedConditions.urlContains(urlFragment));
    }

    public static void hardWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.warn(WaitUtility.class, "Hard wait interrupted");
        }
    }
}

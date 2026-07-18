package com.parabank.automation.utilities;

import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class JavaScriptUtility {

    private JavaScriptUtility() {
    }

    private static JavascriptExecutor getExecutor() {
        WebDriver driver = DriverFactory.getDriver();
        if (!(driver instanceof JavascriptExecutor)) {
            throw new IllegalStateException("Current driver does not support JavaScript execution");
        }
        return (JavascriptExecutor) driver;
    }

    public static void click(WebElement element) {
        Log.debug(JavaScriptUtility.class, "Performing JavaScript click");
        getExecutor().executeScript("arguments[0].click();", element);
    }

    public static void scrollToElement(WebElement element) {
        Log.debug(JavaScriptUtility.class, "Scrolling to element via JavaScript");
        getExecutor().executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void highlightElement(WebElement element) {
        getExecutor().executeScript(
                "arguments[0].style.border='2px solid red'", element);
    }

    public static Object executeScript(String script, Object... args) {
        return getExecutor().executeScript(script, args);
    }
}

package com.parabank.automation.utilities;

import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.exceptions.FrameworkException;
import org.openqa.selenium.Alert;

public final class AlertUtility {

    private AlertUtility() {
    }

    public static Alert getAlert() {
        return DriverFactory.getDriver().switchTo().alert();
    }

    public static String getAlertText() {
        return getAlert().getText();
    }

    public static void acceptAlert() {
        getAlert().accept();
    }

    public static void dismissAlert() {
        getAlert().dismiss();
    }

    public static void typeInAlert(String text) {
        getAlert().sendKeys(text);
    }

    public static boolean isAlertPresent() {
        try {
            DriverFactory.getDriver().switchTo().alert();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static void acceptAlertIfPresent() {
        if (isAlertPresent()) {
            acceptAlert();
        }
    }

    public static void handleAlert(String expectedText, boolean accept) {
        if (!isAlertPresent()) {
            throw new FrameworkException("Expected alert was not displayed");
        }
        String actualText = getAlertText();
        if (!actualText.contains(expectedText)) {
            throw new FrameworkException("Unexpected alert text. Expected: " + expectedText + ", Actual: " + actualText);
        }
        if (accept) {
            acceptAlert();
        } else {
            dismissAlert();
        }
    }
}

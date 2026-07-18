package com.parabank.automation.base;

import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.elements.Button;
import com.parabank.automation.elements.CheckBox;
import com.parabank.automation.elements.DropDown;
import com.parabank.automation.elements.RadioButton;
import com.parabank.automation.elements.TextBox;
import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.interfaces.WebActions;
import com.parabank.automation.logger.Log;
import com.parabank.automation.utilities.JavaScriptUtility;
import com.parabank.automation.utilities.ScreenshotUtility;
import com.parabank.automation.utilities.WaitUtility;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final Logger logger;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        this.logger = Log.getLogger(getClass());
    }

    protected Button button(By locator) {
        return new Button(locator);
    }

    protected TextBox textBox(By locator) {
        return new TextBox(locator);
    }

    protected DropDown dropDown(By locator) {
        return new DropDown(locator);
    }

    protected CheckBox checkBox(By locator) {
        return new CheckBox(locator);
    }

    protected RadioButton radioButton(By locator) {
        return new RadioButton(locator);
    }

    protected void performAction(WebActions element) {
        element.click();
    }

    protected void click(By locator) {
        button(locator).click();
    }

    protected void type(By locator, String text) {
        textBox(locator).type(text);
    }

    protected String getText(By locator) {
        String text = textBox(locator).getText();
        logger.info("Retrieved text from {}: {}", locator, text);
        return text;
    }

    protected void selectDropdownByVisibleText(By locator, String visibleText) {
        dropDown(locator).selectByVisibleText(visibleText);
    }

    protected void selectDropdownByValue(By locator, String value) {
        dropDown(locator).selectByValue(value);
    }

    protected void scrollToElement(By locator) {
        WebElement element = WaitUtility.waitForElement(locator, WaitStrategy.VISIBLE);
        JavaScriptUtility.scrollToElement(element);
    }

    protected void javascriptClick(By locator) {
        button(locator).jsClick();
    }

    protected void hover(By locator) {
        WebElement element = WaitUtility.waitForElement(locator, WaitStrategy.VISIBLE);
        new Actions(driver).moveToElement(element).perform();
    }

    protected boolean isElementDisplayed(By locator) {
        return textBox(locator).isDisplayed();
    }

    protected boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected boolean isElementVisible(By locator) {
        try {
            return isElementPresent(locator) && driver.findElement(locator).isDisplayed();
        } catch (Exception exception) {
            return false;
        }
    }

    protected String getTextIfVisible(By locator) {
        if (!isElementVisible(locator)) {
            return "";
        }
        return driver.findElement(locator).getText().trim();
    }

    protected String takeScreenshot(String name) {
        return ScreenshotUtility.captureScreenshot(name);
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

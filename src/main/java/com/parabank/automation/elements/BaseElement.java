package com.parabank.automation.elements;

import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.interfaces.WebActions;
import com.parabank.automation.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class BaseElement implements WebActions {

    protected final By locator;

    protected BaseElement(By locator) {
        this.locator = locator;
    }

    protected WebElement findElement(WaitStrategy strategy) {
        return WaitUtility.waitForElement(locator, strategy);
    }

    @Override
    public boolean isDisplayed() {
        try {
            return findElement(WaitStrategy.VISIBLE).isDisplayed();
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public boolean isEnabled() {
        try {
            return findElement(WaitStrategy.VISIBLE).isEnabled();
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public String getText() {
        return findElement(WaitStrategy.VISIBLE).getText();
    }

    public By getLocator() {
        return locator;
    }
}

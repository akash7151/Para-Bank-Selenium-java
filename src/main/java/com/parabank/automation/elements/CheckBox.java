package com.parabank.automation.elements;

import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckBox extends BaseElement {

    public CheckBox(By locator) {
        super(locator);
    }

    @Override
    public void click() {
        Log.info(CheckBox.class, "Clicking checkbox: " + locator);
        findElement(WaitStrategy.CLICKABLE).click();
    }

    @Override
    public void type(String text) {
        throw new UnsupportedOperationException("CheckBox does not support type action");
    }

    public boolean isSelected() {
        return findElement(WaitStrategy.VISIBLE).isSelected();
    }

    public void check() {
        WebElement element = findElement(WaitStrategy.VISIBLE);
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void uncheck() {
        WebElement element = findElement(WaitStrategy.VISIBLE);
        if (element.isSelected()) {
            element.click();
        }
    }
}

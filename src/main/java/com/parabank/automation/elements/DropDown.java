package com.parabank.automation.elements;

import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DropDown extends BaseElement {

    public DropDown(By locator) {
        super(locator);
    }

    @Override
    public void click() {
        findElement(WaitStrategy.CLICKABLE).click();
    }

    @Override
    public void type(String text) {
        selectByVisibleText(text);
    }

    public void selectByVisibleText(String visibleText) {
        Log.info(DropDown.class, "Selecting '" + visibleText + "' from dropdown: " + locator);
        new Select(findElement(WaitStrategy.VISIBLE)).selectByVisibleText(visibleText);
    }

    public void selectByValue(String value) {
        new Select(findElement(WaitStrategy.VISIBLE)).selectByValue(value);
    }

    public void selectByIndex(int index) {
        new Select(findElement(WaitStrategy.VISIBLE)).selectByIndex(index);
    }
}

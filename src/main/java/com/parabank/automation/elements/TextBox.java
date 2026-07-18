package com.parabank.automation.elements;

import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TextBox extends BaseElement {

    public TextBox(By locator) {
        super(locator);
    }

    @Override
    public void click() {
        findElement(WaitStrategy.CLICKABLE).click();
    }

    @Override
    public void type(String text) {
        Log.info(TextBox.class, "Typing into field: " + locator);
        WebElement element = findElement(WaitStrategy.VISIBLE);
        element.clear();
        element.sendKeys(text);
    }

    public String getValue() {
        return findElement(WaitStrategy.VISIBLE).getAttribute("value");
    }
}

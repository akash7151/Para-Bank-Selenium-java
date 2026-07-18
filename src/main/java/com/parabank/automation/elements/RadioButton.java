package com.parabank.automation.elements;

import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RadioButton extends BaseElement {

    public RadioButton(By locator) {
        super(locator);
    }

    @Override
    public void click() {
        Log.info(RadioButton.class, "Selecting radio button: " + locator);
        findElement(WaitStrategy.CLICKABLE).click();
    }

    @Override
    public void type(String text) {
        throw new UnsupportedOperationException("RadioButton does not support type action");
    }

    public boolean isSelected() {
        return findElement(WaitStrategy.VISIBLE).isSelected();
    }

    public void select() {
        WebElement element = findElement(WaitStrategy.VISIBLE);
        if (!element.isSelected()) {
            element.click();
        }
    }
}

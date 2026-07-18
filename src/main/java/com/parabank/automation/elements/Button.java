package com.parabank.automation.elements;

import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.logger.Log;
import com.parabank.automation.utilities.JavaScriptUtility;
import org.openqa.selenium.By;

public class Button extends BaseElement {

    public Button(By locator) {
        super(locator);
    }

    @Override
    public void click() {
        Log.info(Button.class, "Clicking button: " + locator);
        findElement(WaitStrategy.CLICKABLE).click();
    }

    public void jsClick() {
        JavaScriptUtility.click(findElement(WaitStrategy.VISIBLE));
    }

    @Override
    public void type(String text) {
        throw new UnsupportedOperationException("Button does not support type action");
    }
}

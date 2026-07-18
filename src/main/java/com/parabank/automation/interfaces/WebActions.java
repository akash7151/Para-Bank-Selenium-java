package com.parabank.automation.interfaces;

/**
 * Contract for reusable UI element wrappers (polymorphism across Button, TextBox, etc.).
 */
public interface WebActions {

    void click();

    void type(String text);

    String getText();

    boolean isDisplayed();

    boolean isEnabled();
}

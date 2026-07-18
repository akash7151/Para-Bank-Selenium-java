package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RegisterPage extends BasePage {

    private static final By FIRST_NAME = By.id("customer.firstName");
    private static final By LAST_NAME = By.id("customer.lastName");
    private static final By ADDRESS = By.id("customer.address.street");
    private static final By CITY = By.id("customer.address.city");
    private static final By STATE = By.id("customer.address.state");
    private static final By ZIP_CODE = By.id("customer.address.zipCode");
    private static final By PHONE = By.id("customer.phoneNumber");
    private static final By SSN = By.id("customer.ssn");
    private static final By USERNAME = By.id("customer.username");
    private static final By PASSWORD = By.id("customer.password");
    private static final By CONFIRM_PASSWORD = By.id("repeatedPassword");
    private static final By REGISTER_BUTTON = By.cssSelector("input[value='Register']");
    private static final By SUCCESS_HEADING = By.cssSelector(".title");
    private static final By VALIDATION_ERRORS = By.cssSelector(".error");

    public RegisterPage enterFirstName(String firstName) {
        type(FIRST_NAME, firstName);
        return this;
    }

    public RegisterPage enterLastName(String lastName) {
        type(LAST_NAME, lastName);
        return this;
    }

    public RegisterPage enterAddress(String address) {
        type(ADDRESS, address);
        return this;
    }

    public RegisterPage enterCity(String city) {
        type(CITY, city);
        return this;
    }

    public RegisterPage enterState(String state) {
        type(STATE, state);
        return this;
    }

    public RegisterPage enterZipCode(String zipCode) {
        type(ZIP_CODE, zipCode);
        return this;
    }

    public RegisterPage enterPhone(String phone) {
        type(PHONE, phone);
        return this;
    }

    public RegisterPage enterSsn(String ssn) {
        type(SSN, ssn);
        return this;
    }

    public RegisterPage enterUsername(String username) {
        type(USERNAME, username);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        type(PASSWORD, password);
        return this;
    }

    public RegisterPage enterConfirmPassword(String confirmPassword) {
        type(CONFIRM_PASSWORD, confirmPassword);
        return this;
    }

    public RegisterPage fillRegistrationForm(String firstName, String lastName, String address,
                                           String city, String state, String zipCode,
                                           String phone, String ssn, String username,
                                           String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterAddress(address);
        enterCity(city);
        enterState(state);
        enterZipCode(zipCode);
        enterPhone(phone);
        enterSsn(ssn);
        enterUsername(username);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        return this;
    }

    public RegisterPage clickRegister() {
        click(REGISTER_BUTTON);
        return this;
    }

    public String getSuccessMessage() {
        return getTextIfVisible(SUCCESS_HEADING);
    }

    public boolean isRegistrationSuccessful() {
        return getSuccessMessage().toLowerCase().contains("welcome");
    }

    public List<WebElement> getValidationErrors() {
        return driver.findElements(VALIDATION_ERRORS);
    }

    public String getFirstValidationError() {
        List<WebElement> errors = getValidationErrors();
        return errors.isEmpty() ? "" : errors.get(0).getText();
    }
}

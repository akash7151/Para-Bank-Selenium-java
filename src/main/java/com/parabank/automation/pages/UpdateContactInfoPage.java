package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.utilities.WaitUtility;
import org.openqa.selenium.By;

public class UpdateContactInfoPage extends BasePage {

    private static final By FIRST_NAME = By.name("customer.firstName");
    private static final By LAST_NAME = By.name("customer.lastName");
    private static final By ADDRESS = By.name("customer.address.street");
    private static final By CITY = By.name("customer.address.city");
    private static final By STATE = By.name("customer.address.state");
    private static final By ZIP_CODE = By.name("customer.address.zipCode");
    private static final By PHONE = By.name("customer.phoneNumber");
    private static final By UPDATE_PROFILE = By.cssSelector("input[value='Update Profile']");
    private static final By SUCCESS_MESSAGE = By.cssSelector("#updateProfileResult h1");

    public UpdateContactInfoPage enterFirstName(String firstName) {
        type(FIRST_NAME, firstName);
        return this;
    }

    public UpdateContactInfoPage enterLastName(String lastName) {
        type(LAST_NAME, lastName);
        return this;
    }

    public UpdateContactInfoPage enterAddress(String address) {
        type(ADDRESS, address);
        return this;
    }

    public UpdateContactInfoPage enterCity(String city) {
        type(CITY, city);
        return this;
    }

    public UpdateContactInfoPage enterState(String state) {
        type(STATE, state);
        return this;
    }

    public UpdateContactInfoPage enterZipCode(String zipCode) {
        type(ZIP_CODE, zipCode);
        return this;
    }

    public UpdateContactInfoPage enterPhone(String phone) {
        type(PHONE, phone);
        return this;
    }

    public UpdateContactInfoPage updateContactInfo(String firstName, String lastName, String address,
                                                   String city, String state, String zipCode, String phone) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterAddress(address);
        enterCity(city);
        enterState(state);
        enterZipCode(zipCode);
        enterPhone(phone);
        click(UPDATE_PROFILE);
        WaitUtility.hardWait(1);
        return this;
    }

    public UpdateContactInfoPage clickUpdateProfile() {
        click(UPDATE_PROFILE);
        WaitUtility.hardWait(1);
        return this;
    }

    public String getSuccessMessage() {
        if (isElementVisible(SUCCESS_MESSAGE)) {
            return getTextIfVisible(SUCCESS_MESSAGE);
        }
        return getTextIfVisible(By.id("updateProfileResult"));
    }

    public boolean isUpdateSuccessful() {
        String message = getSuccessMessage().toLowerCase();
        return message.contains("updated") || message.contains("success");
    }

    public boolean isUpdateContactPageDisplayed() {
        return isElementVisible(FIRST_NAME) && isElementVisible(UPDATE_PROFILE);
    }
}

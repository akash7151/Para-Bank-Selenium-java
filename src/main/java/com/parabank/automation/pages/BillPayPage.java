package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import org.openqa.selenium.By;

public class BillPayPage extends BasePage {

    private static final By PAYEE_NAME = By.name("payee.name");
    private static final By ADDRESS = By.name("payee.address.street");
    private static final By CITY = By.name("payee.address.city");
    private static final By STATE = By.name("payee.address.state");
    private static final By ZIP_CODE = By.name("payee.address.zipCode");
    private static final By PHONE = By.name("payee.phoneNumber");
    private static final By ACCOUNT_NUMBER = By.name("payee.accountNumber");
    private static final By VERIFY_ACCOUNT = By.name("verifyAccount");
    private static final By AMOUNT = By.name("amount");
    private static final By SEND_PAYMENT = By.cssSelector("input[value='Send Payment']");
    private static final By SUCCESS_MESSAGE = By.cssSelector("#billpayResult h1");
    private static final By CONFIRMATION_AMOUNT = By.cssSelector("#billpayResult .amount");

    public BillPayPage enterPayeeName(String name) {
        type(PAYEE_NAME, name);
        return this;
    }

    public BillPayPage enterAddress(String address) {
        type(ADDRESS, address);
        return this;
    }

    public BillPayPage enterCity(String city) {
        type(CITY, city);
        return this;
    }

    public BillPayPage enterState(String state) {
        type(STATE, state);
        return this;
    }

    public BillPayPage enterZipCode(String zipCode) {
        type(ZIP_CODE, zipCode);
        return this;
    }

    public BillPayPage enterPhone(String phone) {
        type(PHONE, phone);
        return this;
    }

    public BillPayPage enterAccountNumber(String accountNumber) {
        type(ACCOUNT_NUMBER, accountNumber);
        return this;
    }

    public BillPayPage enterVerifyAccount(String verifyAccount) {
        type(VERIFY_ACCOUNT, verifyAccount);
        return this;
    }

    public BillPayPage enterAmount(String amount) {
        type(AMOUNT, amount);
        return this;
    }

    public BillPayPage fillBillPayForm(String name, String address, String city, String state,
                                       String zipCode, String phone, String accountNumber,
                                       String verifyAccount, String amount) {
        enterPayeeName(name);
        enterAddress(address);
        enterCity(city);
        enterState(state);
        enterZipCode(zipCode);
        enterPhone(phone);
        enterAccountNumber(accountNumber);
        enterVerifyAccount(verifyAccount);
        enterAmount(amount);
        return this;
    }

    public BillPayPage clickSendPayment() {
        click(SEND_PAYMENT);
        return this;
    }

    public String getSuccessMessage() {
        return getTextIfVisible(SUCCESS_MESSAGE);
    }

    public String getConfirmationAmount() {
        return getText(CONFIRMATION_AMOUNT);
    }

    public boolean isPaymentSuccessful() {
        if (!isElementVisible(SUCCESS_MESSAGE)) {
            return false;
        }
        return getSuccessMessage().contains("Bill Payment Complete");
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(By.cssSelector(".error"));
    }
}

package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.elements.DropDown;
import com.parabank.automation.utilities.WaitUtility;
import org.openqa.selenium.By;

public class OpenAccountPage extends BasePage {

    private static final By ACCOUNT_TYPE = By.id("type");
    private static final By FROM_ACCOUNT = By.id("fromAccountId");
    private static final By OPEN_ACCOUNT_BUTTON = By.cssSelector("input[value='Open New Account']");
    private static final By SUCCESS_MESSAGE = By.cssSelector("#openAccountResult h1");
    private static final By NEW_ACCOUNT_ID = By.id("newAccountId");
    private static final By PAGE_TITLE = By.cssSelector(".title");

    private final DropDown accountTypeDropdown = new DropDown(ACCOUNT_TYPE);
    private final DropDown fromAccountDropdown = new DropDown(FROM_ACCOUNT);

    public OpenAccountPage selectAccountType(String accountType) {
        accountTypeDropdown.selectByVisibleText(accountType);
        return this;
    }

    public OpenAccountPage selectFromAccount(int index) {
        fromAccountDropdown.selectByIndex(index);
        return this;
    }

    public OpenAccountPage clickOpenAccount() {
        click(OPEN_ACCOUNT_BUTTON);
        WaitUtility.hardWait(1);
        return this;
    }

    public OpenAccountPage openNewAccount(String accountType) {
        selectAccountType(accountType);
        selectFromAccount(0);
        return clickOpenAccount();
    }

    public String getSuccessMessage() {
        return getTextIfVisible(SUCCESS_MESSAGE);
    }

    public String getNewAccountId() {
        return getTextIfVisible(NEW_ACCOUNT_ID);
    }

    public boolean isAccountOpenedSuccessfully() {
        return getSuccessMessage().contains("Account Opened");
    }

    public boolean isOpenAccountPageDisplayed() {
        return getText(PAGE_TITLE).contains("Open New Account");
    }
}

package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.utilities.WaitUtility;
import com.parabank.automation.utilities.WebTableUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class AccountsOverviewPage extends BasePage {

    private static final By ACCOUNTS_TABLE = By.id("accountTable");
    private static final By ACCOUNT_ROWS = By.cssSelector("#accountTable tbody tr");
    private static final By PAGE_TITLE = By.cssSelector(".title");

    public boolean isAccountsOverviewDisplayed() {
        return getPageHeading().contains("Accounts Overview");
    }

    public String getPageHeading() {
        return getText(PAGE_TITLE);
    }

    public List<WebElement> getAccountRows() {
        WaitUtility.waitForElement(ACCOUNTS_TABLE, WaitStrategy.VISIBLE);
        return driver.findElements(ACCOUNT_ROWS);
    }

    public int getAccountCount() {
        return getAccountRows().size();
    }

    public List<Map<String, String>> getAccountsData() {
        return WebTableUtility.getTableData(ACCOUNTS_TABLE);
    }

    public String getAccountBalance(int rowIndex) {
        return WebTableUtility.getCellData(ACCOUNTS_TABLE, rowIndex, 1);
    }
}

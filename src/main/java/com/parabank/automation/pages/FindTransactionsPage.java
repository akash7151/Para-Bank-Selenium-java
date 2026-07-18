package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.elements.RadioButton;
import com.parabank.automation.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FindTransactionsPage extends BasePage {

    private static final By FIND_BY_DATE = By.id("findByDate");
    private static final By FIND_BY_AMOUNT = By.id("findByAmount");
    private static final By FIND_BY_DATE_RANGE = By.id("findByDateRange");
    private static final By TRANSACTION_ID = By.id("transactionId");
    private static final By FROM_DATE = By.id("fromDate");
    private static final By TO_DATE = By.id("toDate");
    private static final By AMOUNT = By.id("amount");
    private static final By FIND_TRANSACTIONS = By.cssSelector("input[value='Find Transactions']");
    private static final By TRANSACTION_TABLE = By.id("transactionTable");
    private static final By TRANSACTION_ROWS = By.cssSelector("#transactionTable tbody tr");

    private final RadioButton findByDateRadio = new RadioButton(FIND_BY_DATE);
    private final RadioButton findByAmountRadio = new RadioButton(FIND_BY_AMOUNT);
    private final RadioButton findByDateRangeRadio = new RadioButton(FIND_BY_DATE_RANGE);

    public FindTransactionsPage searchByTransactionId(String id) {
        findByDateRadio.select();
        type(TRANSACTION_ID, id);
        click(FIND_TRANSACTIONS);
        return this;
    }

    public FindTransactionsPage searchByDate(String fromDate) {
        findByDateRadio.select();
        type(FROM_DATE, fromDate);
        click(FIND_TRANSACTIONS);
        return this;
    }

    public FindTransactionsPage searchByAmount(String amount) {
        findByAmountRadio.select();
        type(AMOUNT, amount);
        click(FIND_TRANSACTIONS);
        return this;
    }

    public FindTransactionsPage searchByDateRange(String fromDate, String toDate) {
        findByDateRangeRadio.select();
        type(FROM_DATE, fromDate);
        type(TO_DATE, toDate);
        click(FIND_TRANSACTIONS);
        return this;
    }

    public List<WebElement> getTransactionRows() {
        WaitUtility.hardWait(1);
        return driver.findElements(TRANSACTION_ROWS);
    }

    public int getTransactionCount() {
        return getTransactionRows().size();
    }

    public boolean isTransactionTableDisplayed() {
        return isElementVisible(TRANSACTION_TABLE);
    }
}

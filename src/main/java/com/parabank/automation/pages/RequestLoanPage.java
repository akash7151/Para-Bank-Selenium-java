package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RequestLoanPage extends BasePage {

    private static final By LOAN_AMOUNT = By.id("amount");
    private static final By DOWN_PAYMENT = By.id("downPayment");
    private static final By FROM_ACCOUNT = By.id("fromAccountId");
    private static final By APPLY_NOW = By.cssSelector("input[value='Apply Now']");
    private static final By LOAN_RESULT = By.cssSelector("#requestLoanResult h1");
    private static final By LOAN_RESULT_SECTION = By.id("requestLoanResult");
    private static final By LOAN_ID = By.id("loanId");
    private static final By NEW_ACCOUNT_LINK = By.cssSelector("#requestLoanResult a");
    private static final By ERROR_MESSAGE = By.cssSelector(".error");

    public RequestLoanPage enterLoanAmount(String amount) {
        type(LOAN_AMOUNT, amount);
        return this;
    }

    public RequestLoanPage enterDownPayment(String downPayment) {
        type(DOWN_PAYMENT, downPayment);
        return this;
    }

    public RequestLoanPage selectFromAccount(int index) {
        selectDropdownByValue(FROM_ACCOUNT, getAccountValueAtIndex(index));
        return this;
    }

    private String getAccountValueAtIndex(int index) {
        return driver.findElement(FROM_ACCOUNT)
                .findElements(By.tagName("option"))
                .get(index)
                .getAttribute("value");
    }

    public RequestLoanPage clickApplyNow() {
        click(APPLY_NOW);
        WaitUtility.hardWait(1);
        return this;
    }

    public RequestLoanPage requestLoan(String amount, String downPayment, int fromAccountIndex) {
        enterLoanAmount(amount);
        enterDownPayment(downPayment);
        selectFromAccount(fromAccountIndex);
        return clickApplyNow();
    }

    public String getLoanResultMessage() {
        return getTextIfVisible(LOAN_RESULT);
    }

    public String getLoanId() {
        if (isElementPresent(NEW_ACCOUNT_LINK)) {
            WebElement accountLink = WaitUtility.waitForElement(NEW_ACCOUNT_LINK, WaitStrategy.VISIBLE);
            String accountNumber = accountLink.getText().trim();
            if (!accountNumber.isBlank()) {
                return accountNumber;
            }
        }
        if (!isElementPresent(LOAN_ID)) {
            return "";
        }
        WebElement loanLink = WaitUtility.waitForElement(LOAN_ID, WaitStrategy.VISIBLE);
        String text = loanLink.getText().trim();
        if (!text.isBlank()) {
            return text;
        }
        String href = loanLink.getAttribute("href");
        if (href != null && href.contains("id=")) {
            return href.substring(href.lastIndexOf('=') + 1).trim();
        }
        return "";
    }

    private String getFullLoanResultText() {
        if (!isElementVisible(LOAN_RESULT_SECTION)) {
            return "";
        }
        return driver.findElement(LOAN_RESULT_SECTION).getText().toLowerCase();
    }

    public boolean isLoanApproved() {
        String resultText = getFullLoanResultText();
        return resultText.contains("approved")
                || resultText.contains("processed")
                || resultText.contains("congratulations");
    }

    public boolean isLoanDenied() {
        if (isElementVisible(ERROR_MESSAGE)) {
            return true;
        }
        String resultText = getFullLoanResultText();
        if (resultText.isBlank()) {
            return true;
        }
        return resultText.contains("denied")
                || resultText.contains("not approve")
                || resultText.contains("cannot");
    }
}

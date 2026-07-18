package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.utilities.WaitUtility;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private static final By ACCOUNTS_OVERVIEW = By.linkText("Accounts Overview");
    private static final By OPEN_NEW_ACCOUNT = By.linkText("Open New Account");
    private static final By TRANSFER_FUNDS = By.linkText("Transfer Funds");
    private static final By BILL_PAY = By.linkText("Bill Pay");
    private static final By REQUEST_LOAN = By.linkText("Request Loan");
    private static final By FIND_TRANSACTIONS = By.linkText("Find Transactions");
    private static final By UPDATE_CONTACT_INFO = By.linkText("Update Contact Info");
    private static final By LOGOUT = By.linkText("Log Out");
    private static final By WELCOME_MESSAGE = By.xpath("//p[contains(@class,'smallText') and contains(.,'Welcome')]");

    public boolean isUserLoggedIn() {
        return isElementDisplayed(ACCOUNTS_OVERVIEW);
    }

    public String getWelcomeMessage() {
        return getText(WELCOME_MESSAGE);
    }

    public AccountsOverviewPage navigateToAccountsOverview() {
        click(ACCOUNTS_OVERVIEW);
        return new AccountsOverviewPage();
    }

    public OpenAccountPage navigateToOpenNewAccount() {
        click(OPEN_NEW_ACCOUNT);
        return new OpenAccountPage();
    }

    public TransferFundsPage navigateToTransferFunds() {
        click(TRANSFER_FUNDS);
        return new TransferFundsPage();
    }

    public BillPayPage navigateToBillPay() {
        click(BILL_PAY);
        return new BillPayPage();
    }

    public RequestLoanPage navigateToRequestLoan() {
        click(REQUEST_LOAN);
        return new RequestLoanPage();
    }

    public FindTransactionsPage navigateToFindTransactions() {
        click(FIND_TRANSACTIONS);
        return new FindTransactionsPage();
    }

    public UpdateContactInfoPage navigateToUpdateContactInfo() {
        click(UPDATE_CONTACT_INFO);
        WaitUtility.waitForElement(By.name("customer.firstName"), WaitStrategy.VISIBLE);
        return new UpdateContactInfoPage();
    }

    public LoginPage logout() {
        click(LOGOUT);
        return new LoginPage();
    }
}

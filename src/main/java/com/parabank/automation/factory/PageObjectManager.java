package com.parabank.automation.factory;

import com.parabank.automation.pages.*;

/**
 * Factory + lazy singleton per page instance within a test thread.
 */
public class PageObjectManager {

    private LoginPage loginPage;
    private HomePage homePage;
    private RegisterPage registerPage;
    private OpenAccountPage openAccountPage;
    private AccountsOverviewPage accountsOverviewPage;
    private TransferFundsPage transferFundsPage;
    private BillPayPage billPayPage;
    private FindTransactionsPage findTransactionsPage;
    private RequestLoanPage requestLoanPage;
    private UpdateContactInfoPage updateContactInfoPage;

    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    public HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage();
        }
        return homePage;
    }

    public RegisterPage getRegisterPage() {
        if (registerPage == null) {
            registerPage = new RegisterPage();
        }
        return registerPage;
    }

    public OpenAccountPage getOpenAccountPage() {
        if (openAccountPage == null) {
            openAccountPage = new OpenAccountPage();
        }
        return openAccountPage;
    }

    public AccountsOverviewPage getAccountsOverviewPage() {
        if (accountsOverviewPage == null) {
            accountsOverviewPage = new AccountsOverviewPage();
        }
        return accountsOverviewPage;
    }

    public TransferFundsPage getTransferFundsPage() {
        if (transferFundsPage == null) {
            transferFundsPage = new TransferFundsPage();
        }
        return transferFundsPage;
    }

    public BillPayPage getBillPayPage() {
        if (billPayPage == null) {
            billPayPage = new BillPayPage();
        }
        return billPayPage;
    }

    public FindTransactionsPage getFindTransactionsPage() {
        if (findTransactionsPage == null) {
            findTransactionsPage = new FindTransactionsPage();
        }
        return findTransactionsPage;
    }

    public RequestLoanPage getRequestLoanPage() {
        if (requestLoanPage == null) {
            requestLoanPage = new RequestLoanPage();
        }
        return requestLoanPage;
    }

    public UpdateContactInfoPage getUpdateContactInfoPage() {
        if (updateContactInfoPage == null) {
            updateContactInfoPage = new UpdateContactInfoPage();
        }
        return updateContactInfoPage;
    }

    public void reset() {
        loginPage = null;
        homePage = null;
        registerPage = null;
        openAccountPage = null;
        accountsOverviewPage = null;
        transferFundsPage = null;
        billPayPage = null;
        findTransactionsPage = null;
        requestLoanPage = null;
        updateContactInfoPage = null;
    }
}

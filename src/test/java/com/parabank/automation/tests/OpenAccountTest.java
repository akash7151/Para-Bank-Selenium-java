package com.parabank.automation.tests;

import com.parabank.automation.base.AuthenticatedTestBase;
import com.parabank.automation.retry.RetryAnalyzer;
import com.parabank.automation.utilities.DateUtility;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OpenAccountTest extends AuthenticatedTestBase {

    @Test(description = "Open Savings Account", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify user can open a new savings account")
    @Severity(SeverityLevel.CRITICAL)
    public void testOpenSavingsAccount() {
        var openAccountPage = homePage.navigateToOpenNewAccount();
        openAccountPage.openNewAccount("SAVINGS");

        Assert.assertTrue(openAccountPage.isAccountOpenedSuccessfully(), "Savings account should be opened");
        Assert.assertFalse(openAccountPage.getNewAccountId().isBlank(), "New account ID should be displayed");
    }

    @Test(description = "Open Checking Account", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify user can open a new checking account")
    @Severity(SeverityLevel.NORMAL)
    public void testOpenCheckingAccount() {
        var openAccountPage = homePage.navigateToOpenNewAccount();
        openAccountPage.openNewAccount("CHECKING");

        Assert.assertTrue(openAccountPage.isAccountOpenedSuccessfully(), "Checking account should be opened");
    }
}

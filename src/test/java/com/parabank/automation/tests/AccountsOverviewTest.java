package com.parabank.automation.tests;

import com.parabank.automation.base.AuthenticatedTestBase;
import com.parabank.automation.retry.RetryAnalyzer;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AccountsOverviewTest extends AuthenticatedTestBase {

    @Test(description = "Accounts Overview Display", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify accounts overview page displays account table")
    @Severity(SeverityLevel.CRITICAL)
    public void testAccountsOverviewDisplayed() {
        var overviewPage = homePage.navigateToAccountsOverview();

        Assert.assertTrue(overviewPage.isAccountsOverviewDisplayed(), "Accounts Overview page should be displayed");
        Assert.assertTrue(overviewPage.getAccountCount() > 0, "At least one account should be listed");
    }

    @Test(description = "Accounts Table Data", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify accounts table contains balance data")
    @Severity(SeverityLevel.NORMAL)
    public void testAccountsTableHasData() {
        var overviewPage = homePage.navigateToAccountsOverview();

        Assert.assertFalse(overviewPage.getAccountsData().isEmpty(), "Accounts table data should not be empty");
        Assert.assertFalse(overviewPage.getAccountBalance(0).isBlank(), "Account balance should be displayed");
    }
}

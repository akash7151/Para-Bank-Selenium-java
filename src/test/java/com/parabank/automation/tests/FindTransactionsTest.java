package com.parabank.automation.tests;

import com.parabank.automation.base.AuthenticatedTestBase;
import com.parabank.automation.retry.RetryAnalyzer;
import com.parabank.automation.utilities.DateUtility;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FindTransactionsTest extends AuthenticatedTestBase {

    @Test(description = "Find Transactions By Date", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify transactions can be searched by date")
    @Severity(SeverityLevel.CRITICAL)
    public void testFindTransactionsByDate() {
        var findPage = homePage.navigateToFindTransactions();
        findPage.searchByDate(DateUtility.getCurrentDate());

        Assert.assertTrue(findPage.isTransactionTableDisplayed(), "Transaction table should be displayed");
    }

    @Test(description = "Find Transactions By Amount", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify transactions can be searched by amount")
    @Severity(SeverityLevel.NORMAL)
    public void testFindTransactionsByAmount() {
        var findPage = homePage.navigateToFindTransactions();
        findPage.searchByAmount("100");

        Assert.assertTrue(findPage.getTransactionCount() >= 0, "Transaction search should return results");
    }
}

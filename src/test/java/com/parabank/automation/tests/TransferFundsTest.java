package com.parabank.automation.tests;

import com.parabank.automation.base.AuthenticatedTestBase;
import com.parabank.automation.dataprovider.ExcelDataProvider;
import com.parabank.automation.retry.RetryAnalyzer;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class TransferFundsTest extends AuthenticatedTestBase {

    @Test(description = "Valid Transfer", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify funds can be transferred between accounts")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidTransfer() {
        var transferPage = homePage.navigateToTransferFunds();
        transferPage.transferFunds("1", 0, 1);

        Assert.assertTrue(transferPage.isTransferSuccessful(),
                "Transfer should complete successfully");
    }

    @Test(dataProvider = "transferData", dataProviderClass = ExcelDataProvider.class,
            description = "Transfer validation scenarios", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify transfer validation for invalid amounts and same account")
    @Severity(SeverityLevel.NORMAL)
    public void testTransferValidation(Map<String, String> data) {
        var transferPage = homePage.navigateToTransferFunds();
        int fromIndex = Integer.parseInt(data.get("fromIndex"));
        int toIndex = Integer.parseInt(data.get("toIndex"));

        transferPage.transferFunds(data.get("amount"), fromIndex, toIndex);

        if ("true".equalsIgnoreCase(data.get("shouldPass"))) {
            Assert.assertTrue(transferPage.isTransferSuccessful(),
                    "Transfer should succeed for: " + data.get("testCase"));
        } else {
            Assert.assertTrue(transferPage.validateTransferScenario(
                            data.get("testCase"), data.get("amount")),
                    "Transfer validation scenario not reflected for: " + data.get("testCase")
                            + ". Details: " + transferPage.getTransferDetailsMessage());
        }
    }
}

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

public class BillPayTest extends AuthenticatedTestBase {

    @Test(description = "Valid Bill Payment", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify bill payment with valid payee details")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidBillPayment() {
        var billPayPage = homePage.navigateToBillPay();
        billPayPage.fillBillPayForm(
                "Test Payee", "123 Main St", "Boston", "MA",
                "02108", "6175551234", "12345", "12345", "50.00"
        ).clickSendPayment();

        Assert.assertTrue(billPayPage.isPaymentSuccessful(), "Bill payment should complete successfully");
    }

    @Test(dataProvider = "billPayData", dataProviderClass = ExcelDataProvider.class,
            description = "Bill pay validation scenarios", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify bill pay validation scenarios")
    @Severity(SeverityLevel.NORMAL)
    public void testBillPayValidation(Map<String, String> data) {
        var billPayPage = homePage.navigateToBillPay();
        billPayPage.fillBillPayForm(
                data.get("name"), data.get("address"), data.get("city"), data.get("state"),
                data.get("zipCode"), data.get("phone"), data.get("accountNumber"),
                data.get("verifyAccount"), data.get("amount")
        ).clickSendPayment();

        if ("true".equalsIgnoreCase(data.get("shouldPass"))) {
            Assert.assertTrue(billPayPage.isPaymentSuccessful(),
                    "Bill pay should succeed for: " + data.get("testCase"));
        } else {
            Assert.assertFalse(billPayPage.isPaymentSuccessful(),
                    "Bill pay should fail for: " + data.get("testCase"));
        }
    }
}

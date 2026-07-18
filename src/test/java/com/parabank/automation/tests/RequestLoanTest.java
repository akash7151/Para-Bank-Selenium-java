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

public class RequestLoanTest extends AuthenticatedTestBase {

    @Test(description = "Loan Approved", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify loan request with valid amount gets approved")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoanApproved() {
        var loanPage = homePage.navigateToRequestLoan();
        loanPage.requestLoan("1000", "100", 0);

        Assert.assertTrue(loanPage.isLoanApproved(), "Loan should be approved");
        Assert.assertFalse(loanPage.getLoanId().isBlank(), "New account number should be displayed after loan approval");
    }

    @Test(dataProvider = "loanData", dataProviderClass = ExcelDataProvider.class,
            description = "Loan validation scenarios", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify loan request validation scenarios")
    @Severity(SeverityLevel.NORMAL)
    public void testLoanValidation(Map<String, String> data) {
        var loanPage = homePage.navigateToRequestLoan();
        loanPage.requestLoan(data.get("amount"), data.get("downPayment"), 0);

        if ("approved".equalsIgnoreCase(data.get("expectedResult"))) {
            Assert.assertTrue(loanPage.isLoanApproved(),
                    "Loan should be approved for: " + data.get("testCase"));
        } else {
            Assert.assertTrue(loanPage.isLoanDenied(),
                    "Loan should be denied for: " + data.get("testCase"));
        }
    }
}

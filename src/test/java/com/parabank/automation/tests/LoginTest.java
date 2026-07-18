package com.parabank.automation.tests;

import com.parabank.automation.base.BaseTest;
import com.parabank.automation.config.ConfigReader;
import com.parabank.automation.dataprovider.ExcelDataProvider;
import com.parabank.automation.retry.RetryAnalyzer;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginTest extends BaseTest {

    @Test(description = "TC01 - Valid Login", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify user can login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidLogin() {
        var homePage = pages.getLoginPage()
                .login(ConfigReader.getUsername(), ConfigReader.getPassword());

        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in successfully");
        Assert.assertTrue(homePage.getWelcomeMessage().toLowerCase().contains("welcome"),
                "Welcome message should be displayed after login");
    }

    @Test(dataProvider = "loginData", dataProviderClass = ExcelDataProvider.class,
            description = "TC02-TC05 - Login validation scenarios", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify login validation for invalid and blank credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginValidation(Map<String, String> data) {
        var loginPage = pages.getLoginPage();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for: " + data.get("testCase"));
        String actualError = normalizeMessage(loginPage.getErrorMessage());
        String expectedError = normalizeMessage(data.get("expectedError"));
        Assert.assertTrue(actualError.contains(expectedError),
                "Expected error message not found for: " + data.get("testCase")
                        + ". Actual: " + loginPage.getErrorMessage());
    }

    private String normalizeMessage(String message) {
        return message == null ? "" : message.toLowerCase().replace(".", "").trim();
    }

    @Test(description = "TC06 - Logout", dependsOnMethods = "testValidLogin", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify user can logout successfully")
    @Severity(SeverityLevel.NORMAL)
    public void testLogout() {
        var logoutPage = pages.getLoginPage()
                .login(ConfigReader.getUsername(), ConfigReader.getPassword())
                .logout();

        Assert.assertTrue(logoutPage.isLoginPageDisplayed(),
                "User should be redirected to login page after logout");
    }
}

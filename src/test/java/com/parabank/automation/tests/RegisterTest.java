package com.parabank.automation.tests;

import com.parabank.automation.base.BaseTest;
import com.parabank.automation.config.ConfigReader;
import com.parabank.automation.dataprovider.ExcelDataProvider;
import com.parabank.automation.retry.RetryAnalyzer;
import com.parabank.automation.testdata.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class RegisterTest extends BaseTest {

    @Test(description = "TC07 - Register New User", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify new user registration with valid data")
    @Severity(SeverityLevel.CRITICAL)
    public void testRegisterNewUser() {
        TestData user = TestData.builder();

        var registerPage = pages.getLoginPage().clickRegisterLink();
        registerPage.fillRegistrationForm(
                user.getFirstName(), user.getLastName(), user.getAddress(),
                user.getCity(), user.getState(), user.getZipCode(),
                user.getPhone(), user.getSsn(), user.getUsername(),
                user.getPassword(), user.getConfirmPassword()
        ).clickRegister();

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration should be successful for new user");
    }

    @Test(dataProvider = "registerData", dataProviderClass = ExcelDataProvider.class,
            description = "TC08-TC10 - Registration validation", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify registration validation scenarios")
    @Severity(SeverityLevel.NORMAL)
    public void testRegistrationValidation(Map<String, String> data) {
        var registerPage = pages.getLoginPage().clickRegisterLink();

        if ("TC09".equals(data.get("testCase"))) {
            registerPage.fillRegistrationForm(
                    data.get("firstName"), data.get("lastName"), data.get("address"),
                    data.get("city"), data.get("state"), data.get("zipCode"),
                    data.get("phone"), data.get("ssn"),
                    ConfigReader.getUsername(), data.get("password"), data.get("confirmPassword")
            ).clickRegister();

            Assert.assertFalse(registerPage.isRegistrationSuccessful(),
                    "Registration should fail for existing username");
        } else {
            registerPage.fillRegistrationForm(
                    data.get("firstName"), data.get("lastName"), data.get("address"),
                    data.get("city"), data.get("state"), data.get("zipCode"),
                    data.get("phone"), data.get("ssn"), data.get("username"),
                    data.get("password"), data.get("confirmPassword")
            ).clickRegister();

            if ("TC08".equals(data.get("testCase"))) {
                Assert.assertFalse(registerPage.getValidationErrors().isEmpty(),
                        "Validation errors should be displayed for mandatory fields");
            } else {
                Assert.assertFalse(registerPage.isRegistrationSuccessful(),
                        "Registration should fail for: " + data.get("testCase"));
            }
        }
    }
}

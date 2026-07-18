package com.parabank.automation.base;

import com.parabank.automation.config.ConfigReader;
import com.parabank.automation.pages.HomePage;
import org.testng.annotations.BeforeMethod;

public abstract class AuthenticatedTestBase extends BaseTest {

    protected HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        homePage = pages.getLoginPage().login(ConfigReader.getUsername(), ConfigReader.getPassword());
    }
}

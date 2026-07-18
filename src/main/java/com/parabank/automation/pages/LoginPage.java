package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.elements.Button;
import com.parabank.automation.elements.TextBox;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("input[value='Log In']");
    private static final By REGISTER_LINK = By.linkText("Register");
    private static final By ERROR_MESSAGE = By.cssSelector(".error");

    private final TextBox usernameField = new TextBox(USERNAME);
    private final TextBox passwordField = new TextBox(PASSWORD);
    private final Button loginButton = new Button(LOGIN_BUTTON);
    private final Button registerLink = new Button(REGISTER_LINK);

    public LoginPage enterUsername(String username) {
        usernameField.type(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        passwordField.type(password);
        return this;
    }

    public HomePage clickLogin() {
        loginButton.click();
        return new HomePage();
    }

    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public RegisterPage clickRegisterLink() {
        registerLink.click();
        return new RegisterPage();
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isElementDisplayed(ERROR_MESSAGE);
    }

    public boolean isLoginPageDisplayed() {
        return loginButton.isDisplayed();
    }
}

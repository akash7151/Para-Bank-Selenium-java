package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.elements.DropDown;
import com.parabank.automation.utilities.WaitUtility;
import org.openqa.selenium.By;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferFundsPage extends BasePage {

    private static final By AMOUNT = By.id("amount");
    private static final By FROM_ACCOUNT = By.id("fromAccountId");
    private static final By TO_ACCOUNT = By.id("toAccountId");
    private static final By TRANSFER_BUTTON = By.cssSelector("input[value='Transfer']");
    private static final By SUCCESS_HEADING = By.xpath("//h1[contains(.,'Transfer Complete')]");
    private static final By TRANSFER_DETAILS = By.xpath("//h1[contains(.,'Transfer Complete')]/following-sibling::p[1]");
    private static final By ERROR_MESSAGE = By.cssSelector(".error");
    private static final Pattern ACCOUNT_TRANSFER_PATTERN =
            Pattern.compile("from account #(\\d+) to account #(\\d+)", Pattern.CASE_INSENSITIVE);

    private final DropDown fromAccountDropdown = new DropDown(FROM_ACCOUNT);
    private final DropDown toAccountDropdown = new DropDown(TO_ACCOUNT);

    public TransferFundsPage enterAmount(String amount) {
        type(AMOUNT, amount);
        return this;
    }

    public TransferFundsPage selectFromAccount(int index) {
        fromAccountDropdown.selectByIndex(index);
        return this;
    }

    public TransferFundsPage selectToAccount(int index) {
        toAccountDropdown.selectByIndex(index);
        return this;
    }

    public TransferFundsPage clickTransfer() {
        click(TRANSFER_BUTTON);
        waitForTransferResult();
        return this;
    }

    private void waitForTransferResult() {
        try {
            WaitUtility.getWait().until(driver -> {
                if (isElementVisible(SUCCESS_HEADING)) {
                    return true;
                }
                String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
                return pageText.contains("transfer complete")
                        || pageText.contains("has been transferred")
                        || isElementVisible(ERROR_MESSAGE);
            });
        } catch (Exception ignored) {
            WaitUtility.hardWait(1);
        }
    }

    public TransferFundsPage transferFunds(String amount, int fromIndex, int toIndex) {
        enterAmount(amount);
        selectFromAccount(fromIndex);
        selectToAccount(toIndex);
        return clickTransfer();
    }

    public String getSuccessMessage() {
        if (isElementVisible(SUCCESS_HEADING)) {
            return getTextIfVisible(SUCCESS_HEADING);
        }
        return "";
    }

    public String getErrorMessage() {
        return getTextIfVisible(ERROR_MESSAGE);
    }

    public boolean isTransferSuccessful() {
        if (isElementVisible(SUCCESS_HEADING)) {
            return true;
        }
        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        return pageText.contains("transfer complete") || pageText.contains("has been transferred");
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(ERROR_MESSAGE) && !getErrorMessage().isBlank();
    }

    public String getTransferDetailsMessage() {
        if (isElementVisible(TRANSFER_DETAILS)) {
            return getTextIfVisible(TRANSFER_DETAILS);
        }
        String pageText = driver.findElement(By.tagName("body")).getText();
        for (String line : pageText.split("\\R")) {
            if (line.toLowerCase().contains("has been transferred")) {
                return line.trim();
            }
        }
        return "";
    }

    public boolean isSameAccountTransfer() {
        Matcher matcher = ACCOUNT_TRANSFER_PATTERN.matcher(getTransferDetailsMessage());
        return matcher.find() && matcher.group(1).equals(matcher.group(2));
    }

    public boolean validateTransferScenario(String testCase, String amount) {
        if (isErrorDisplayed()) {
            return true;
        }
        if (!isTransferSuccessful()) {
            return false;
        }
        String details = getTransferDetailsMessage();
        return switch (testCase) {
            case "Same Account" -> isSameAccountTransfer();
            case "Zero Amount" -> details.contains("$0.00");
            case "Negative Amount" -> details.contains("-$") || details.startsWith("-");
            case "Large Amount" -> details.contains(amount.replace(",", ""));
            default -> false;
        };
    }
}

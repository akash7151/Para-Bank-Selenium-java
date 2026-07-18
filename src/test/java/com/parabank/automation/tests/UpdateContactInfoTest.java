package com.parabank.automation.tests;

import com.parabank.automation.base.AuthenticatedTestBase;
import com.parabank.automation.retry.RetryAnalyzer;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateContactInfoTest extends AuthenticatedTestBase {

    @Test(description = "Update Contact Info", retryAnalyzer = RetryAnalyzer.class)
    @Description("Verify user can update contact information")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateContactInfo() {
        var contactPage = homePage.navigateToUpdateContactInfo();

        contactPage.updateContactInfo(
                "John", "Smith", "743 Main St", "Boston", "MA", "02108", "6175551234"
        );

        Assert.assertTrue(contactPage.isUpdateSuccessful(), "Contact info should be updated successfully");
    }
}

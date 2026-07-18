package com.parabank.automation.dataprovider;

import com.parabank.automation.constants.FrameworkConstants;
import com.parabank.automation.utilities.ExcelUtility;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.Map;

public class ExcelDataProvider {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return convertToObjectArray(ExcelUtility.getSheetData(FrameworkConstants.LOGIN_SHEET));
    }

    @DataProvider(name = "registerData")
    public Object[][] getRegisterData() {
        return convertToObjectArray(ExcelUtility.getSheetData(FrameworkConstants.REGISTER_SHEET));
    }

    @DataProvider(name = "transferData")
    public Object[][] getTransferData() {
        return convertToObjectArray(ExcelUtility.getSheetData(FrameworkConstants.TRANSFER_SHEET));
    }

    @DataProvider(name = "billPayData")
    public Object[][] getBillPayData() {
        return convertToObjectArray(ExcelUtility.getSheetData(FrameworkConstants.BILLPAY_SHEET));
    }

    @DataProvider(name = "loanData")
    public Object[][] getLoanData() {
        return convertToObjectArray(ExcelUtility.getSheetData(FrameworkConstants.LOAN_SHEET));
    }

    private Object[][] convertToObjectArray(List<Map<String, String>> data) {
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }
}

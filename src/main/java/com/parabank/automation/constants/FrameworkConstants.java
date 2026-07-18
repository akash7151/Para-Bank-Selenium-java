package com.parabank.automation.constants;

import java.nio.file.Paths;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    public static final String CONFIG_FILE = "config.properties";
    public static final String TEST_DATA_FILE = "testdata.xlsx";
    public static final String EXTENT_CONFIG_FILE = "extent-config.xml";

    public static final String PROJECT_ROOT = System.getProperty("user.dir");
    public static final String REPORTS_DIR = Paths.get(PROJECT_ROOT, "reports").toString();
    public static final String SCREENSHOTS_DIR = Paths.get(PROJECT_ROOT, "screenshots").toString();
    public static final String LOGS_DIR = Paths.get(PROJECT_ROOT, "logs").toString();
    public static final String TEST_OUTPUT_DIR = Paths.get(PROJECT_ROOT, "test-output").toString();

    public static final String LOGIN_SHEET = "Login";
    public static final String REGISTER_SHEET = "Register";
    public static final String TRANSFER_SHEET = "TransferFunds";
    public static final String BILLPAY_SHEET = "BillPay";
    public static final String LOAN_SHEET = "Loan";

    public static final String DEFAULT_ENV = "qa";
}

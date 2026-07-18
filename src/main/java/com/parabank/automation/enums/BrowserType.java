package com.parabank.automation.enums;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public static BrowserType fromString(String browser) {
        for (BrowserType type : values()) {
            if (type.browserName.equalsIgnoreCase(browser) || type.name().equalsIgnoreCase(browser)) {
                return type;
            }
        }
        return CHROME;
    }
}

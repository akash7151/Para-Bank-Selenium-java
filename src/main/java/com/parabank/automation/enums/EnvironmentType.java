package com.parabank.automation.enums;

import com.parabank.automation.exceptions.FrameworkException;

public enum EnvironmentType {
    QA("qa"),
    STAGE("stage"),
    PROD("prod");

    private final String envName;

    EnvironmentType(String envName) {
        this.envName = envName;
    }

    public String getEnvName() {
        return envName;
    }

    public String getPropertiesFile() {
        return envName + ".properties";
    }

    public static EnvironmentType fromString(String value) {
        for (EnvironmentType environment : values()) {
            if (environment.envName.equalsIgnoreCase(value)) {
                return environment;
            }
        }
        throw new FrameworkException("Unsupported environment: " + value + ". Use qa, stage, or prod.");
    }
}

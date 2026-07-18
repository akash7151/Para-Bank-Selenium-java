package com.parabank.automation.config;

import com.parabank.automation.enums.EnvironmentType;

public final class EnvironmentManager {

    private EnvironmentManager() {
    }

    public static EnvironmentType getCurrentEnvironment() {
        String env = System.getProperty("env", System.getenv().getOrDefault("ENV", "qa"));
        return EnvironmentType.fromString(env);
    }
}

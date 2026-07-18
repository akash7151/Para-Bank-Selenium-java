package com.parabank.automation.config;

import com.parabank.automation.constants.FrameworkConstants;
import com.parabank.automation.exceptions.FrameworkException;
import com.parabank.automation.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {
    }

    private static void loadProperties() {
        loadFile(FrameworkConstants.CONFIG_FILE);
        loadFile(EnvironmentManager.getCurrentEnvironment().getPropertiesFile());
        Log.info(ConfigReader.class, "Configuration loaded for environment: "
                + EnvironmentManager.getCurrentEnvironment().name());
    }

    private static void loadFile(String fileName) {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FrameworkException("Unable to find configuration file: " + fileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load configuration file: " + fileName, e);
        }
    }

    public static String getProperty(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new FrameworkException("Property not found: " + key);
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        return properties.getProperty(key, defaultValue).trim();
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getBrowser() {
        return getProperty("browser", "chrome").toLowerCase();
    }

    public static boolean isHeadless() {
        return getBooleanProperty("headless");
    }

    public static int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }

    public static int getExplicitWait() {
        return getIntProperty("explicit.wait");
    }

    public static int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }

    public static String getUsername() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }

    public static int getRetryCount() {
        return getIntProperty("retry.count");
    }
}

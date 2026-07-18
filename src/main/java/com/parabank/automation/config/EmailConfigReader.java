package com.parabank.automation.config;

import com.parabank.automation.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class EmailConfigReader {

    private static final Map<String, String> values = new HashMap<>();

    static {
        loadDefaults();
        loadClasspathProperties("email.properties");
        loadDotEnvFile(resolvePath(System.getenv("EMAIL_REPORT_FILE")));
        loadDotEnvFile(Path.of(".env.report"));
        overlayEnvironmentVariables();
    }

    private EmailConfigReader() {
    }

    private static void loadDefaults() {
        put("email.enabled", "false");
        put("email.smtp.host", "smtp.gmail.com");
        put("email.smtp.port", "587");
        put("email.smtp.secure", "false");
        put("email.to", "akashdake9@gmail.com,akashdake16@gmail.com");
        put("email.subject", "[ParaBank Selenium] Automation Report");
        put("email.attach.reports", "true");
    }

    private static void loadClasspathProperties(String fileName) {
        try (InputStream inputStream = EmailConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                return;
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((key, value) -> put(String.valueOf(key), String.valueOf(value)));
        } catch (IOException e) {
            Log.warn(EmailConfigReader.class, "Unable to load " + fileName + ": " + e.getMessage());
        }
    }

    private static void loadDotEnvFile(Path path) {
        if (path == null || !Files.isRegularFile(path)) {
            return;
        }
        try {
            Files.readAllLines(path).stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .filter(line -> line.contains("="))
                    .forEach(line -> {
                        int index = line.indexOf('=');
                        put(mapDotEnvKey(line.substring(0, index).trim()), line.substring(index + 1).trim());
                    });
            Log.info(EmailConfigReader.class, "Loaded email settings from " + path.toAbsolutePath());
        } catch (IOException e) {
            Log.warn(EmailConfigReader.class, "Unable to read " + path + ": " + e.getMessage());
        }
    }

    private static Path resolvePath(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Path.of(value.trim());
    }

    private static String mapDotEnvKey(String key) {
        return switch (key) {
            case "EMAIL_ENABLED" -> "email.enabled";
            case "SMTP_HOST" -> "email.smtp.host";
            case "SMTP_PORT" -> "email.smtp.port";
            case "SMTP_SECURE" -> "email.smtp.secure";
            case "SMTP_USER" -> "email.smtp.user";
            case "SMTP_PASS" -> "email.smtp.pass";
            case "EMAIL_FROM" -> "email.from";
            case "EMAIL_TO" -> "email.to";
            case "EMAIL_SUBJECT" -> "email.subject";
            case "EMAIL_ATTACH_REPORTS" -> "email.attach.reports";
            default -> key.toLowerCase().replace('_', '.');
        };
    }

    private static void overlayEnvironmentVariables() {
        putIfPresent("email.enabled", System.getenv("EMAIL_ENABLED"));
        putIfPresent("email.smtp.host", System.getenv("SMTP_HOST"));
        putIfPresent("email.smtp.port", System.getenv("SMTP_PORT"));
        putIfPresent("email.smtp.secure", System.getenv("SMTP_SECURE"));
        putIfPresent("email.smtp.user", System.getenv("SMTP_USER"));
        putIfPresent("email.smtp.pass", System.getenv("SMTP_PASS"));
        putIfPresent("email.from", firstNonBlank(System.getenv("EMAIL_FROM"), System.getenv("SMTP_USER")));
        putIfPresent("email.to", firstNonBlank(System.getenv("EMAIL_TO"), System.getenv("REPORT_RECIPIENTS")));
        putIfPresent("email.subject", System.getenv("EMAIL_SUBJECT"));
        putIfPresent("email.attach.reports", System.getenv("EMAIL_ATTACH_REPORTS"));
        putIfPresent("build.url", System.getenv("BUILD_URL"));
    }

    private static void putIfPresent(String key, String value) {
        if (value != null && !value.isBlank()) {
            put(key, value.trim());
        }
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first.trim();
        }
        if (second != null && !second.isBlank()) {
            return second.trim();
        }
        return null;
    }

    private static void put(String key, String value) {
        if (value != null && !value.isBlank()) {
            values.put(key, value.trim());
        }
    }

    public static boolean isEmailEnabled() {
        return Boolean.parseBoolean(values.getOrDefault("email.enabled", "false"));
    }

    public static boolean attachReports() {
        return Boolean.parseBoolean(values.getOrDefault("email.attach.reports", "true"));
    }

    public static String getSmtpHost() {
        return values.get("email.smtp.host");
    }

    public static int getSmtpPort() {
        return Integer.parseInt(values.getOrDefault("email.smtp.port", "587"));
    }

    public static boolean isSmtpSecure() {
        return Boolean.parseBoolean(values.getOrDefault("email.smtp.secure", "false"));
    }

    public static String getSmtpUser() {
        return values.get("email.smtp.user");
    }

    public static String getSmtpPassword() {
        return values.get("email.smtp.pass");
    }

    public static String getFromAddress() {
        return firstNonBlank(values.get("email.from"), values.get("email.smtp.user"));
    }

    public static String getRecipients() {
        return values.get("email.to");
    }

    public static String getSubject() {
        return values.get("email.subject");
    }

    public static String getBuildUrl() {
        return values.getOrDefault("build.url", "");
    }

    public static boolean isConfigured() {
        return getSmtpHost() != null
                && getSmtpUser() != null
                && getSmtpPassword() != null
                && getRecipients() != null;
    }
}

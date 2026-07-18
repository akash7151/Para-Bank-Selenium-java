package com.parabank.automation.utilities;

import com.parabank.automation.config.EmailConfigReader;
import com.parabank.automation.logger.Log;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class AllureReportMailer {

    private static final Path ALLURE_REPORT_DIR = Path.of("target", "site", "allure-maven-plugin");
    private static final Path ALLURE_ZIP = Path.of("reports", "email-artifacts", "allure-report.zip");
    private static final Path TESTNG_RESULTS = Path.of("target", "surefire-reports", "testng-results.xml");

    private AllureReportMailer() {
    }

    public static void main(String[] args) {
        if (!EmailConfigReader.isEmailEnabled()) {
            Log.info(AllureReportMailer.class, "EMAIL_ENABLED is not true. Reports were generated only.");
            return;
        }
        if (!EmailConfigReader.isConfigured()) {
            Log.warn(AllureReportMailer.class, "Email enabled but SMTP settings are incomplete. Skipping email.");
            return;
        }

        try {
            Path allureZip = prepareAllureZip();
            String summary = buildSummary();
            boolean attachReports = EmailConfigReader.attachReports();
            List<Path> attachments = attachReports ? EmailUtility.existingFiles(allureZip) : List.of();
            EmailUtility.sendReportEmail(summary, attachments, attachReports);
        } catch (Exception exception) {
            Log.error(AllureReportMailer.class, "Failed to send Allure report email", exception);
            if (Boolean.parseBoolean(System.getenv().getOrDefault("EMAIL_FAIL_BUILD", "false"))) {
                System.exit(1);
            }
        }
    }

    private static Path prepareAllureZip() {
        if (!Files.isDirectory(ALLURE_REPORT_DIR)) {
            Log.warn(AllureReportMailer.class, "Allure report directory not found: " + ALLURE_REPORT_DIR);
            return ALLURE_ZIP;
        }
        return ZipUtility.zipDirectory(ALLURE_REPORT_DIR, ALLURE_ZIP);
    }

    private static String buildSummary() {
        String testSummary = TestResultSummaryReader.readSummary(TESTNG_RESULTS);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String buildUrl = EmailConfigReader.getBuildUrl();
        StringBuilder builder = new StringBuilder();
        builder.append("Status: Automation run completed\n");
        builder.append("Finished: ").append(timestamp).append('\n');
        builder.append(testSummary);
        if (!buildUrl.isBlank()) {
            builder.append("\nJenkins: ").append(buildUrl);
        }
        return builder.toString();
    }
}

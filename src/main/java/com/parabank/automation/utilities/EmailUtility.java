package com.parabank.automation.utilities;

import com.parabank.automation.config.EmailConfigReader;
import com.parabank.automation.exceptions.FrameworkException;
import com.parabank.automation.logger.Log;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class EmailUtility {

    private EmailUtility() {
    }

    public static void sendReportEmail(String summary, List<Path> attachments, boolean attachReports) {
        if (!EmailConfigReader.isEmailEnabled()) {
            Log.info(EmailUtility.class, "Email disabled. Skipping report email.");
            return;
        }
        if (!EmailConfigReader.isConfigured()) {
            throw new FrameworkException("Email is enabled but SMTP settings are incomplete.");
        }

        try {
            send(summary, attachments, attachReports);
        } catch (MessagingException firstError) {
            if (attachReports && isBlockedAttachmentError(firstError)) {
                Log.warn(EmailUtility.class, "Report ZIP blocked by provider; retrying with summary only.");
                try {
                    send(summary, List.of(), false);
                } catch (MessagingException retryError) {
                    throw new FrameworkException("Failed to send report email after retry", retryError);
                }
                return;
            }
            throw new FrameworkException("Failed to send report email", firstError);
        }
    }

    private static void send(String summary, List<Path> attachments, boolean attachReports)
            throws MessagingException {
        Session session = Session.getInstance(buildMailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        EmailConfigReader.getSmtpUser(),
                        EmailConfigReader.getSmtpPassword());
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EmailConfigReader.getFromAddress()));
        for (String recipient : EmailConfigReader.getRecipients().split(",")) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.trim()));
        }
        message.setSubject(EmailConfigReader.getSubject());

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(buildHtmlPart(summary, attachReports, !attachments.isEmpty()));

        if (attachReports) {
            for (Path attachment : attachments) {
                if (Files.isRegularFile(attachment)) {
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    try {
                        bodyPart.attachFile(attachment.toFile());
                    } catch (IOException ioException) {
                        throw new MessagingException("Unable to attach file: " + attachment, ioException);
                    }
                    multipart.addBodyPart(bodyPart);
                }
            }
        }

        message.setContent(multipart);
        Transport.send(message);
        Log.info(EmailUtility.class, "Report email sent to " + EmailConfigReader.getRecipients());
    }

    private static Properties buildMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", EmailConfigReader.getSmtpHost());
        properties.put("mail.smtp.port", String.valueOf(EmailConfigReader.getSmtpPort()));
        properties.put("mail.smtp.starttls.enable", String.valueOf(!EmailConfigReader.isSmtpSecure()));
        properties.put("mail.smtp.ssl.enable", String.valueOf(EmailConfigReader.isSmtpSecure()));
        return properties;
    }

    private static MimeBodyPart buildHtmlPart(String summary, boolean attachReports, boolean hasAttachments)
            throws MessagingException {
        String buildUrl = EmailConfigReader.getBuildUrl();
        String attachmentNote = attachReports && hasAttachments
                ? "<p><b>Attached:</b> Allure HTML report ZIP</p>"
                : "<p>Allure report ZIP is archived on Jenkins (some providers block HTML ZIP attachments).</p>";
        String jenkinsLink = buildUrl.isBlank()
                ? ""
                : "<p><a href=\"" + buildUrl + "\">Open Jenkins build</a></p>";

        String html = """
                <h2>ParaBank Selenium Automation Report</h2>
                <pre style="font-family: Consolas, monospace; background:#f6f8fa; padding:12px;">%s</pre>
                %s
                %s
                <p style="color:#666;font-size:12px;">ParaBank Automation Framework</p>
                """.formatted(summary, attachmentNote, jenkinsLink);

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(html, "text/html; charset=UTF-8");
        return bodyPart;
    }

    private static boolean isBlockedAttachmentError(MessagingException error) {
        String message = error.getMessage() == null ? "" : error.getMessage().toLowerCase();
        return message.contains("552") || message.contains("blocked") || message.contains("security");
    }

    public static List<Path> existingFiles(Path... paths) {
        List<Path> attachments = new ArrayList<>();
        for (Path path : paths) {
            if (path != null && Files.isRegularFile(path)) {
                attachments.add(path);
            }
        }
        return attachments;
    }
}

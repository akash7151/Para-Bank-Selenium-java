package com.parabank.automation.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtility {

    private static final DateTimeFormatter DEFAULT_DATE = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private DateUtility() {
    }

    public static String getCurrentDate() {
        return LocalDate.now().format(DEFAULT_DATE);
    }

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP);
    }

    public static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String addDaysToCurrentDate(int days) {
        return LocalDate.now().plusDays(days).format(DEFAULT_DATE);
    }
}

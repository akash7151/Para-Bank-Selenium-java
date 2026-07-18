package com.parabank.automation.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Log {

    private Log() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    public static void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    public static void error(Class<?> clazz, String message, Throwable throwable) {
        getLogger(clazz).error(message, throwable);
    }
}

package com.parabank.automation.utilities;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomDataUtility {

    private RandomDataUtility() {
    }

    public static String generateUsername() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateEmail() {
        return generateUsername() + "@testmail.com";
    }

    public static String generatePhoneNumber() {
        return String.format("617555%04d", ThreadLocalRandom.current().nextInt(1000, 9999));
    }

    public static String generateSsn() {
        return String.format("%03d-%02d-%04d",
                ThreadLocalRandom.current().nextInt(100, 999),
                ThreadLocalRandom.current().nextInt(10, 99),
                ThreadLocalRandom.current().nextInt(1000, 9999));
    }

    public static int generateAmount(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}

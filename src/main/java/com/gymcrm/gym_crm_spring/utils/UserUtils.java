package com.gymcrm.gym_crm_spring.utils;


import com.gymcrm.gym_crm_spring.domain.User;
import java.security.SecureRandom;
import java.util.List;

public final class UserUtils {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private UserUtils() {}

    /**
     * Generates a username in the format firstName.lastName.
     * If there are already users (in both entities) with the same prefix,
     * a numeric suffix is appended.
     * existingUsers — the list of all users used to check for collisions.
     */

    public static String generateUsername(String firstName, String lastName, List<? extends User> existingUsers) {
        String base = firstName + "." + lastName;
        long count = existingUsers.stream()
                .filter(u -> u.getUsername() != null && u.getUsername().startsWith(base))
                .count();
        return count == 0 ? base : base + count;
    }

    public static String generatePassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static String generatePassword() {
        return generatePassword(10); // default 10 chars
    }
}

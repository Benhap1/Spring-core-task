package com.gymcrm.gym_crm_spring.utils;

import com.gymcrm.gym_crm_spring.domain.User;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility class for working with users:
 * - username generation
 * - password generation
 */
public final class UserUtils {
    private static final String PASSWORD_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private UserUtils() {
    }

    /**
     * Generates a unique username in the format: firstName.lastName.
     * If the username is already taken, a numeric suffix is appended:
     * firstName.lastName1, firstName.lastName2, etc.
     *
     * @param firstName     user first name (nullable)
     * @param lastName      user last name (nullable)
     * @param existingUsers list of existing users (nullable or empty)
     * @return unique username
     */
    public static String generateUsername(String firstName, String lastName, List<? extends User> existingUsers) {
        String f = firstName == null ? "" : firstName.trim().toLowerCase();
        String l = lastName == null ? "" : lastName.trim().toLowerCase();
        String base = f + "." + l;

        Set<String> existing = new HashSet<>();
        if (existingUsers != null) {
            for (User u : existingUsers) {
                if (u != null && u.getUsername() != null) {
                    existing.add(u.getUsername().toLowerCase());
                }
            }
        }

        if (!existing.contains(base)) {
            return base;
        }

        int i = 1;
        while (existing.contains(base + i)) {
            i++;
        }
        return base + i;
    }

    /**
     * Generates a random password of a given length.
     * The password consists of uppercase letters, lowercase letters and digits.
     *
     * @param length password length (must be > 0)
     * @return generated password
     */
    public static String generatePassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be positive");
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(PASSWORD_CHARS.charAt(RANDOM.nextInt(PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random password with default length of 10 characters.
     *
     * @return generated password
     */
    public static String generatePassword() {
        return generatePassword(10);
    }
}

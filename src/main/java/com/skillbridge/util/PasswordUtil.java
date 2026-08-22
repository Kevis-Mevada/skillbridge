package com.skillbridge.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil provides secure password hashing and verification using BCrypt.
 */
public class PasswordUtil {

    private static final int LOG_ROUNDS = 10;

    private PasswordUtil() {}

    /**
     * Hashes a plain-text password using BCrypt with a secure salt.
     * 
     * @param plainPassword the plain-text password
     * @return the hashed password string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     * 
     * @param plainPassword the plain-text password entered by the user
     * @param hashedPassword the stored BCrypt hash from the database
     * @return true if matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // In case of invalid hash format
            return false;
        }
    }
}

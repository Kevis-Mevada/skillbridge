package com.skillbridge.model;

/**
 * Role defines the user roles supported across the SkillBridge platform.
 */
public enum Role {
    STUDENT,
    RECRUITER,
    ADMIN;

    public static Role fromString(String roleStr) {
        if (roleStr == null || roleStr.trim().isEmpty()) {
            return STUDENT;
        }
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return STUDENT;
        }
    }
}

package com.skillbridge.model;

/**
 * JobType represents the type of opportunity offered.
 */
public enum JobType {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    INTERNSHIP("Internship"),
    REMOTE("Remote"),
    CONTRACT("Contract");

    private final String displayName;

    JobType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static JobType fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return FULL_TIME;
        }
        try {
            return JobType.valueOf(str.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return FULL_TIME;
        }
    }
}

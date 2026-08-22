package com.skillbridge.model;

/**
 * InterviewStatus represents the current state of a scheduled interview round.
 */
public enum InterviewStatus {
    SCHEDULED("Scheduled", "badge-primary"),
    COMPLETED("Completed", "badge-success"),
    CANCELLED("Cancelled", "badge-danger"),
    RESCHEDULED("Rescheduled", "badge-warning");

    private final String displayName;
    private final String badgeClass;

    InterviewStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static InterviewStatus fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return SCHEDULED;
        }
        try {
            return InterviewStatus.valueOf(str.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return SCHEDULED;
        }
    }
}

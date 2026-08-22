package com.skillbridge.model;

/**
 * ApplicationStatus defines the 6 stages of candidate application lifecycle.
 */
public enum ApplicationStatus {
    APPLIED("Applied", "badge-primary"),
    UNDER_REVIEW("Under Review", "badge-warning"),
    SHORTLISTED("Shortlisted", "badge-info"),
    INTERVIEW("Interview Scheduled", "badge-secondary"),
    SELECTED("Selected / Offer", "badge-success"),
    REJECTED("Not Selected", "badge-danger");

    private final String displayName;
    private final String badgeClass;

    ApplicationStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static ApplicationStatus fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return APPLIED;
        }
        try {
            return ApplicationStatus.valueOf(str.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return APPLIED;
        }
    }
}

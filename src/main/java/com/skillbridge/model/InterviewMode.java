package com.skillbridge.model;

/**
 * InterviewMode represents the communication channel for the interview.
 */
public enum InterviewMode {
    ONLINE("Online Meeting (Google Meet / Zoom)"),
    IN_PERSON("In-Person (Office Headquarters)"),
    PHONE("Phone Call");

    private final String displayName;

    InterviewMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static InterviewMode fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return ONLINE;
        }
        try {
            return InterviewMode.valueOf(str.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ONLINE;
        }
    }
}

package com.skillbridge.model;

/**
 * ExperienceLevel represents candidate experience requirements.
 */
public enum ExperienceLevel {
    FRESHER("Fresher / Student"),
    EXP_0_1("0-1 Years"),
    EXP_1_3("1-3 Years"),
    EXP_3_PLUS("3+ Years");

    private final String displayName;

    ExperienceLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ExperienceLevel fromDbValue(String dbVal) {
        if (dbVal == null || dbVal.trim().isEmpty()) {
            return FRESHER;
        }
        String clean = dbVal.trim().toUpperCase();
        switch (clean) {
            case "0-1_YEARS":
            case "EXP_0_1":
                return EXP_0_1;
            case "1-3_YEARS":
            case "EXP_1_3":
                return EXP_1_3;
            case "3+_YEARS":
            case "EXP_3_PLUS":
                return EXP_3_PLUS;
            case "FRESHER":
            default:
                return FRESHER;
        }
    }

    public String toDbValue() {
        switch (this) {
            case EXP_0_1:
                return "0-1_YEARS";
            case EXP_1_3:
                return "1-3_YEARS";
            case EXP_3_PLUS:
                return "3+_YEARS";
            case FRESHER:
            default:
                return "FRESHER";
        }
    }
}

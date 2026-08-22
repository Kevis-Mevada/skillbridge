package com.skillbridge.model;

import java.io.Serializable;

/**
 * RecruiterDashboardStats encapsulates metrics and applicant pipeline counts for recruiters.
 */
public class RecruiterDashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private int activeJobsCount;
    private int totalApplicantsCount;
    private int underReviewCount;
    private int shortlistedCount;
    private int scheduledInterviewsCount;
    private int hiredCount;

    public RecruiterDashboardStats() {}

    public int getActiveJobsCount() {
        return activeJobsCount;
    }

    public void setActiveJobsCount(int activeJobsCount) {
        this.activeJobsCount = activeJobsCount;
    }

    public int getTotalApplicantsCount() {
        return totalApplicantsCount;
    }

    public void setTotalApplicantsCount(int totalApplicantsCount) {
        this.totalApplicantsCount = totalApplicantsCount;
    }

    public int getUnderReviewCount() {
        return underReviewCount;
    }

    public void setUnderReviewCount(int underReviewCount) {
        this.underReviewCount = underReviewCount;
    }

    public int getShortlistedCount() {
        return shortlistedCount;
    }

    public void setShortlistedCount(int shortlistedCount) {
        this.shortlistedCount = shortlistedCount;
    }

    public int getScheduledInterviewsCount() {
        return scheduledInterviewsCount;
    }

    public void setScheduledInterviewsCount(int scheduledInterviewsCount) {
        this.scheduledInterviewsCount = scheduledInterviewsCount;
    }

    public int getHiredCount() {
        return hiredCount;
    }

    public void setHiredCount(int hiredCount) {
        this.hiredCount = hiredCount;
    }
}

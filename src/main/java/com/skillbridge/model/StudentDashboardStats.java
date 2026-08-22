package com.skillbridge.model;

import java.io.Serializable;

/**
 * StudentDashboardStats holds summary metrics and counts for the Student Dashboard.
 */
public class StudentDashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalApplications;
    private int underReviewCount;
    private int shortlistedCount;
    private int upcomingInterviewsCount;
    private int selectedCount;
    private int savedJobsCount;
    private int profileCompletionPercentage;

    public StudentDashboardStats() {}

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
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

    public int getUpcomingInterviewsCount() {
        return upcomingInterviewsCount;
    }

    public void setUpcomingInterviewsCount(int upcomingInterviewsCount) {
        this.upcomingInterviewsCount = upcomingInterviewsCount;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public int getSavedJobsCount() {
        return savedJobsCount;
    }

    public void setSavedJobsCount(int savedJobsCount) {
        this.savedJobsCount = savedJobsCount;
    }

    public int getProfileCompletionPercentage() {
        return profileCompletionPercentage;
    }

    public void setProfileCompletionPercentage(int profileCompletionPercentage) {
        this.profileCompletionPercentage = profileCompletionPercentage;
    }
}

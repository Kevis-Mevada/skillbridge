package com.skillbridge.model;

import java.io.Serializable;

/**
 * AdminDashboardStats holds platform-wide system metrics for administrative oversight.
 */
public class AdminDashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalStudents;
    private int totalRecruiters;
    private int totalJobs;
    private int activeJobs;
    private int totalApplications;
    private int totalInterviews;

    public AdminDashboardStats() {}

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalRecruiters() {
        return totalRecruiters;
    }

    public void setTotalRecruiters(int totalRecruiters) {
        this.totalRecruiters = totalRecruiters;
    }

    public int getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(int totalJobs) {
        this.totalJobs = totalJobs;
    }

    public int getActiveJobs() {
        return activeJobs;
    }

    public void setActiveJobs(int activeJobs) {
        this.activeJobs = activeJobs;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public int getTotalInterviews() {
        return totalInterviews;
    }

    public void setTotalInterviews(int totalInterviews) {
        this.totalInterviews = totalInterviews;
    }
}

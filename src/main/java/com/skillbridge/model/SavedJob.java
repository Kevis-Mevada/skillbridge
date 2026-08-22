package com.skillbridge.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * SavedJob entity representing bookmarked opportunities.
 */
public class SavedJob implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int studentId;
    private int jobId;
    private Timestamp savedAt;

    // Joined Job metadata
    private String jobTitle;
    private String companyName;
    private String location;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private boolean jobActive;

    public SavedJob() {}

    public SavedJob(int studentId, int jobId) {
        this.studentId = studentId;
        this.jobId = jobId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public Timestamp getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Timestamp savedAt) {
        this.savedAt = savedAt;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    public boolean isJobActive() {
        return jobActive;
    }

    public void setJobActive(boolean jobActive) {
        this.jobActive = jobActive;
    }
}

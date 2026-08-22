package com.skillbridge.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Application entity representing a student's submission to a specific job posting.
 */
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int jobId;
    private int studentId;
    private String coverLetter;
    private String resumeUrl;
    private ApplicationStatus status;
    private String notes;
    private Timestamp appliedAt;
    private Timestamp updatedAt;

    // Joined Job and Recruiter metadata
    private String jobTitle;
    private String companyName;
    private String jobLocation;
    private JobType jobType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;

    // Joined Student Profile and User metadata
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String studentHeadline;
    private BigDecimal studentCgpa;
    private String studentGithub;
    private String studentLinkedin;

    public Application() {
        this.status = ApplicationStatus.APPLIED;
    }

    public Application(int jobId, int studentId, String coverLetter, String resumeUrl) {
        this.jobId = jobId;
        this.studentId = studentId;
        this.coverLetter = coverLetter;
        this.resumeUrl = resumeUrl;
        this.status = ApplicationStatus.APPLIED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Timestamp appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getStudentHeadline() {
        return studentHeadline;
    }

    public void setStudentHeadline(String studentHeadline) {
        this.studentHeadline = studentHeadline;
    }

    public BigDecimal getStudentCgpa() {
        return studentCgpa;
    }

    public void setStudentCgpa(BigDecimal studentCgpa) {
        this.studentCgpa = studentCgpa;
    }

    public String getStudentGithub() {
        return studentGithub;
    }

    public void setStudentGithub(String studentGithub) {
        this.studentGithub = studentGithub;
    }

    public String getStudentLinkedin() {
        return studentLinkedin;
    }

    public void setStudentLinkedin(String studentLinkedin) {
        this.studentLinkedin = studentLinkedin;
    }
}

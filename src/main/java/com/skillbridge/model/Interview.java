package com.skillbridge.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Interview entity representing a scheduled interview round for a student application.
 */
public class Interview implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int applicationId;
    private Date interviewDate;
    private Time interviewTime;
    private InterviewMode interviewMode;
    private String meetingLinkOrLocation;
    private String roundName;
    private String instructions;
    private InterviewStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Joined Application, Job, and Company metadata
    private String jobTitle;
    private String companyName;
    private String companyLocation;
    private int jobId;
    private int studentId;

    // Joined Student Candidate metadata
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String resumeUrl;

    public Interview() {
        this.interviewMode = InterviewMode.ONLINE;
        this.roundName = "Technical Round 1";
        this.status = InterviewStatus.SCHEDULED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public Time getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(Time interviewTime) {
        this.interviewTime = interviewTime;
    }

    public InterviewMode getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(InterviewMode interviewMode) {
        this.interviewMode = interviewMode;
    }

    public String getMeetingLinkOrLocation() {
        return meetingLinkOrLocation;
    }

    public void setMeetingLinkOrLocation(String meetingLinkOrLocation) {
        this.meetingLinkOrLocation = meetingLinkOrLocation;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
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

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
}

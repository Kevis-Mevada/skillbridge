package com.skillbridge.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Education model representing student degree, college/university, and academic scores.
 */
public class Education implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int studentId;
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private Date startDate;
    private Date endDate;
    private BigDecimal gradePercentage;
    private Timestamp createdAt;

    public Education() {}

    public Education(int studentId, String institution, String degree, String fieldOfStudy, Date startDate, Date endDate, BigDecimal gradePercentage) {
        this.studentId = studentId;
        this.institution = institution;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gradePercentage = gradePercentage;
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

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getGradePercentage() {
        return gradePercentage;
    }

    public void setGradePercentage(BigDecimal gradePercentage) {
        this.gradePercentage = gradePercentage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

package com.skillbridge.dao;

import com.skillbridge.model.Application;
import com.skillbridge.model.ApplicationStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * ApplicationDAO defines database operations for student job applications and status tracking.
 */
public interface ApplicationDAO {

    Application save(Application application) throws SQLException;

    boolean updateStatus(int applicationId, ApplicationStatus status, String notes) throws SQLException;

    Optional<Application> findById(int id) throws SQLException;

    Optional<Application> findByJobAndStudent(int jobId, int studentId) throws SQLException;

    List<Application> findByStudentId(int studentId) throws SQLException;

    List<Application> findByJobId(int jobId) throws SQLException;

    List<Application> findByRecruiterId(int recruiterId) throws SQLException;

    boolean hasStudentApplied(int jobId, int studentId) throws SQLException;

    boolean delete(int applicationId, int studentId) throws SQLException;

    int countTotalApplications() throws SQLException;

    List<Application> findAll() throws SQLException;
}

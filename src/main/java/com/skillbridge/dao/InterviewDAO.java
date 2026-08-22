package com.skillbridge.dao;

import com.skillbridge.model.Interview;
import com.skillbridge.model.InterviewStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * InterviewDAO defines operations for scheduling, updating, and querying interview rounds.
 */
public interface InterviewDAO {

    Interview save(Interview interview) throws SQLException;

    boolean update(Interview interview) throws SQLException;

    boolean updateStatus(int interviewId, InterviewStatus status) throws SQLException;

    Optional<Interview> findById(int id) throws SQLException;

    List<Interview> findByApplicationId(int applicationId) throws SQLException;

    List<Interview> findByStudentId(int studentId) throws SQLException;

    List<Interview> findByRecruiterId(int recruiterId) throws SQLException;

    int countTotalInterviews() throws SQLException;
}

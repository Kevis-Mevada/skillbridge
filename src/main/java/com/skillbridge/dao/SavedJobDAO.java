package com.skillbridge.dao;

import com.skillbridge.model.SavedJob;
import java.sql.SQLException;
import java.util.List;

/**
 * SavedJobDAO defines operations for student bookmarked jobs.
 */
public interface SavedJobDAO {

    boolean save(int studentId, int jobId) throws SQLException;

    boolean delete(int studentId, int jobId) throws SQLException;

    boolean isJobSaved(int studentId, int jobId) throws SQLException;

    List<SavedJob> findByStudentId(int studentId) throws SQLException;

    int countByStudentId(int studentId) throws SQLException;
}

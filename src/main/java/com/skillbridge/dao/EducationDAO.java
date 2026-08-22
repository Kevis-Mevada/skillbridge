package com.skillbridge.dao;

import com.skillbridge.model.Education;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * EducationDAO defines operations for managing student educational qualifications.
 */
public interface EducationDAO {

    Education save(Education education) throws SQLException;

    boolean update(Education education) throws SQLException;

    boolean delete(int id, int studentProfileId) throws SQLException;

    Optional<Education> findById(int id) throws SQLException;

    List<Education> findByStudentProfileId(int studentProfileId) throws SQLException;
}

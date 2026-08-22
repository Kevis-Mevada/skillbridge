package com.skillbridge.dao;

import com.skillbridge.model.StudentProfile;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * StudentProfileDAO defines data access operations for student profile management.
 */
public interface StudentProfileDAO {

    StudentProfile save(StudentProfile profile) throws SQLException;

    Optional<StudentProfile> findById(int id) throws SQLException;

    Optional<StudentProfile> findByUserId(int userId) throws SQLException;

    boolean update(StudentProfile profile) throws SQLException;

    List<StudentProfile> findAll() throws SQLException;

    int countTotalStudents() throws SQLException;
}

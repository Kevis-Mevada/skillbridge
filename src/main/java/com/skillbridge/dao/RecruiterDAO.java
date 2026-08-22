package com.skillbridge.dao;

import com.skillbridge.model.RecruiterProfile;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * RecruiterDAO defines data access operations for recruiter and company profiles.
 */
public interface RecruiterDAO {

    RecruiterProfile save(RecruiterProfile profile) throws SQLException;

    Optional<RecruiterProfile> findById(int id) throws SQLException;

    Optional<RecruiterProfile> findByUserId(int userId) throws SQLException;

    boolean update(RecruiterProfile profile) throws SQLException;

    List<RecruiterProfile> findAll() throws SQLException;

    int countTotalRecruiters() throws SQLException;
}

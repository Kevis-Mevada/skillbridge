package com.skillbridge.dao;

import com.skillbridge.model.Job;
import com.skillbridge.model.JobFilterCriteria;
import com.skillbridge.model.Skill;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * JobDAO defines database operations for job & internship postings.
 */
public interface JobDAO {

    Job save(Job job, List<Integer> skillIds) throws SQLException;

    boolean update(Job job, List<Integer> skillIds) throws SQLException;

    boolean delete(int jobId, int recruiterId) throws SQLException;

    Optional<Job> findById(int id) throws SQLException;

    List<Job> findByRecruiterId(int recruiterId) throws SQLException;

    List<Job> searchJobs(JobFilterCriteria criteria) throws SQLException;

    int countJobs(JobFilterCriteria criteria) throws SQLException;

    List<Skill> getJobSkills(int jobId) throws SQLException;

    boolean setJobActiveStatus(int jobId, int recruiterId, boolean active) throws SQLException;

    int countTotalActiveJobs() throws SQLException;

    List<Job> findRecentJobs(int limit) throws SQLException;
}

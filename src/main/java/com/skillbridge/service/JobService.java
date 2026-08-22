package com.skillbridge.service;

import com.skillbridge.model.Job;
import com.skillbridge.model.JobFilterCriteria;
import com.skillbridge.model.Skill;

import java.util.List;
import java.util.Optional;

/**
 * JobService defines business operations for job searching, multi-filter discovery,
 * and recruiter job management.
 */
public interface JobService {

    List<Job> searchJobs(JobFilterCriteria criteria) throws Exception;

    int countJobs(JobFilterCriteria criteria) throws Exception;

    Optional<Job> getJobDetails(int jobId) throws Exception;

    List<Job> getJobsByRecruiter(int recruiterId) throws Exception;

    Job postJob(Job job, List<Integer> skillIds) throws Exception;

    boolean updateJob(Job job, List<Integer> skillIds) throws Exception;

    boolean deleteJob(int jobId, int recruiterId) throws Exception;

    boolean toggleJobStatus(int jobId, int recruiterId, boolean active) throws Exception;

    List<Skill> getAllSkills() throws Exception;

    List<Job> getRecentJobs(int limit) throws Exception;
}

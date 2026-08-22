package com.skillbridge.service;

import com.skillbridge.dao.JobDAO;
import com.skillbridge.dao.JobDAOImpl;
import com.skillbridge.dao.SkillDAO;
import com.skillbridge.dao.SkillDAOImpl;
import com.skillbridge.model.Job;
import com.skillbridge.model.JobFilterCriteria;
import com.skillbridge.model.Skill;

import java.util.List;
import java.util.Optional;

/**
 * JobServiceImpl implements JobService business logic.
 */
public class JobServiceImpl implements JobService {

    private final JobDAO jobDAO;
    private final SkillDAO skillDAO;

    public JobServiceImpl() {
        this.jobDAO = new JobDAOImpl();
        this.skillDAO = new SkillDAOImpl();
    }

    public JobServiceImpl(JobDAO jobDAO, SkillDAO skillDAO) {
        this.jobDAO = jobDAO;
        this.skillDAO = skillDAO;
    }

    @Override
    public List<Job> searchJobs(JobFilterCriteria criteria) throws Exception {
        if (criteria == null) {
            criteria = new JobFilterCriteria();
        }
        return jobDAO.searchJobs(criteria);
    }

    @Override
    public int countJobs(JobFilterCriteria criteria) throws Exception {
        if (criteria == null) {
            criteria = new JobFilterCriteria();
        }
        return jobDAO.countJobs(criteria);
    }

    @Override
    public Optional<Job> getJobDetails(int jobId) throws Exception {
        return jobDAO.findById(jobId);
    }

    @Override
    public List<Job> getJobsByRecruiter(int recruiterId) throws Exception {
        return jobDAO.findByRecruiterId(recruiterId);
    }

    @Override
    public Job postJob(Job job, List<Integer> skillIds) throws Exception {
        if (job == null) throw new IllegalArgumentException("Job cannot be null.");
        if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Job title is required.");
        }
        if (job.getDescription() == null || job.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Job description is required.");
        }
        if (job.getLocation() == null || job.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Job location is required.");
        }

        return jobDAO.save(job, skillIds);
    }

    @Override
    public boolean updateJob(Job job, List<Integer> skillIds) throws Exception {
        if (job == null || job.getId() <= 0) {
            throw new IllegalArgumentException("Valid Job ID is required for update.");
        }
        return jobDAO.update(job, skillIds);
    }

    @Override
    public boolean deleteJob(int jobId, int recruiterId) throws Exception {
        return jobDAO.delete(jobId, recruiterId);
    }

    @Override
    public boolean toggleJobStatus(int jobId, int recruiterId, boolean active) throws Exception {
        return jobDAO.setJobActiveStatus(jobId, recruiterId, active);
    }

    @Override
    public List<Skill> getAllSkills() throws Exception {
        return skillDAO.findAll();
    }

    @Override
    public List<Job> getRecentJobs(int limit) throws Exception {
        return jobDAO.findRecentJobs(limit);
    }
}

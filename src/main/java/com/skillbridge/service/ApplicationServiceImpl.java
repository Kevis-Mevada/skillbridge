package com.skillbridge.service;

import com.skillbridge.dao.*;
import com.skillbridge.model.Application;
import com.skillbridge.model.Job;
import com.skillbridge.model.SavedJob;
import com.skillbridge.model.StudentProfile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * ApplicationServiceImpl implements ApplicationService.
 */
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOGGER = Logger.getLogger(ApplicationServiceImpl.class.getName());

    private final ApplicationDAO applicationDAO;
    private final SavedJobDAO savedJobDAO;
    private final StudentProfileDAO studentProfileDAO;
    private final JobDAO jobDAO;

    public ApplicationServiceImpl() {
        this.applicationDAO = new ApplicationDAOImpl();
        this.savedJobDAO = new SavedJobDAOImpl();
        this.studentProfileDAO = new StudentProfileDAOImpl();
        this.jobDAO = new JobDAOImpl();
    }

    public ApplicationServiceImpl(ApplicationDAO applicationDAO, SavedJobDAO savedJobDAO, StudentProfileDAO studentProfileDAO, JobDAO jobDAO) {
        this.applicationDAO = applicationDAO;
        this.savedJobDAO = savedJobDAO;
        this.studentProfileDAO = studentProfileDAO;
        this.jobDAO = jobDAO;
    }

    @Override
    public Application applyForJob(int jobId, int userId, String coverLetter, String customResumeUrl) throws Exception {
        // 1. Verify Job exists and is active
        Optional<Job> optionalJob = jobDAO.findById(jobId);
        if (optionalJob.isEmpty() || !optionalJob.get().isActive()) {
            throw new IllegalArgumentException("This job posting is no longer active or available.");
        }

        // 2. Obtain Student Profile
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        StudentProfile profile;
        if (optionalProfile.isEmpty()) {
            StudentProfile newProfile = new StudentProfile(userId);
            profile = studentProfileDAO.save(newProfile);
        } else {
            profile = optionalProfile.get();
        }

        // 3. Prevent duplicate application
        if (applicationDAO.hasStudentApplied(jobId, profile.getId())) {
            throw new IllegalStateException("You have already submitted an application for this opportunity.");
        }

        // 4. Determine Resume URL
        String finalResumeUrl = (customResumeUrl != null && !customResumeUrl.trim().isEmpty())
            ? customResumeUrl.trim()
            : profile.getResumeUrl();

        Application app = new Application(jobId, profile.getId(), coverLetter != null ? coverLetter.trim() : "", finalResumeUrl);
        return applicationDAO.save(app);
    }

    @Override
    public List<Application> getStudentApplications(int userId) throws Exception {
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        if (optionalProfile.isEmpty()) {
            return Collections.emptyList();
        }
        return applicationDAO.findByStudentId(optionalProfile.get().getId());
    }

    @Override
    public Optional<Application> getApplicationById(int applicationId) throws Exception {
        return applicationDAO.findById(applicationId);
    }

    @Override
    public boolean withdrawApplication(int applicationId, int userId) throws Exception {
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        if (optionalProfile.isEmpty()) {
            return false;
        }
        return applicationDAO.delete(applicationId, optionalProfile.get().getId());
    }

    @Override
    public boolean toggleSaveJob(int userId, int jobId) throws Exception {
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        int studentId;
        if (optionalProfile.isEmpty()) {
            StudentProfile newProfile = new StudentProfile(userId);
            StudentProfile saved = studentProfileDAO.save(newProfile);
            studentId = saved.getId();
        } else {
            studentId = optionalProfile.get().getId();
        }

        if (savedJobDAO.isJobSaved(studentId, jobId)) {
            savedJobDAO.delete(studentId, jobId);
            return false; // Removed
        } else {
            savedJobDAO.save(studentId, jobId);
            return true; // Added
        }
    }

    @Override
    public List<SavedJob> getSavedJobs(int userId) throws Exception {
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        if (optionalProfile.isEmpty()) {
            return Collections.emptyList();
        }
        return savedJobDAO.findByStudentId(optionalProfile.get().getId());
    }

    @Override
    public boolean removeSavedJob(int userId, int jobId) throws Exception {
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        if (optionalProfile.isEmpty()) {
            return false;
        }
        return savedJobDAO.delete(optionalProfile.get().getId(), jobId);
    }

    @Override
    public boolean isJobSaved(int userId, int jobId) throws Exception {
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        if (optionalProfile.isEmpty()) {
            return false;
        }
        return savedJobDAO.isJobSaved(optionalProfile.get().getId(), jobId);
    }
}

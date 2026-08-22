package com.skillbridge.service;

import com.skillbridge.model.Application;
import com.skillbridge.model.SavedJob;

import java.util.List;
import java.util.Optional;

/**
 * ApplicationService defines business logic for applying to jobs, tracking application statuses,
 * and managing bookmarked opportunities.
 */
public interface ApplicationService {

    Application applyForJob(int jobId, int userId, String coverLetter, String customResumeUrl) throws Exception;

    List<Application> getStudentApplications(int userId) throws Exception;

    Optional<Application> getApplicationById(int applicationId) throws Exception;

    boolean withdrawApplication(int applicationId, int userId) throws Exception;

    boolean toggleSaveJob(int userId, int jobId) throws Exception;

    List<SavedJob> getSavedJobs(int userId) throws Exception;

    boolean removeSavedJob(int userId, int jobId) throws Exception;

    boolean isJobSaved(int userId, int jobId) throws Exception;
}

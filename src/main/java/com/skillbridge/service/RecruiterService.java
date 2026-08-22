package com.skillbridge.service;

import com.skillbridge.model.Application;
import com.skillbridge.model.ApplicationStatus;
import com.skillbridge.model.RecruiterDashboardStats;
import com.skillbridge.model.RecruiterProfile;

import java.util.List;
import java.util.Optional;

/**
 * RecruiterService defines business operations for company profiles, hiring pipeline stats,
 * and applicant status updates.
 */
public interface RecruiterService {

    Optional<RecruiterProfile> getProfileByUserId(int userId) throws Exception;

    Optional<RecruiterProfile> getProfileById(int id) throws Exception;

    boolean updateProfile(RecruiterProfile profile, String fullName, String phone) throws Exception;

    RecruiterDashboardStats getDashboardStats(int recruiterProfileId) throws Exception;

    List<Application> getApplicantsForRecruiter(int recruiterProfileId, Integer jobId, String statusFilter) throws Exception;

    boolean updateApplicationStatus(int applicationId, ApplicationStatus status, String notes, int recruiterProfileId) throws Exception;
}

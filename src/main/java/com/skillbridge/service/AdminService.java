package com.skillbridge.service;

import com.skillbridge.model.*;

import java.util.List;

/**
 * AdminService defines platform-wide governance, moderation, and metric analytics.
 */
public interface AdminService {

    AdminDashboardStats getDashboardStats() throws Exception;

    List<StudentProfile> getAllStudents() throws Exception;

    List<RecruiterProfile> getAllRecruiters() throws Exception;

    List<Job> getAllJobs() throws Exception;

    List<Application> getAllApplications() throws Exception;

    boolean toggleUserStatus(int userId, boolean active) throws Exception;

    boolean deleteUser(int userId) throws Exception;

    boolean toggleJobStatus(int jobId, boolean active) throws Exception;

    boolean deleteJob(int jobId) throws Exception;
}

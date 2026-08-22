package com.skillbridge.service;

import com.skillbridge.model.Interview;
import com.skillbridge.model.InterviewStatus;

import java.util.List;
import java.util.Optional;

/**
 * InterviewService defines business operations for scheduling interviews,
 * notifying students, and updating round outcomes.
 */
public interface InterviewService {

    Interview scheduleInterview(Interview interview, int recruiterUserId) throws Exception;

    boolean updateInterview(Interview interview, int recruiterUserId) throws Exception;

    boolean updateStatus(int interviewId, InterviewStatus status, int userId) throws Exception;

    Optional<Interview> getInterviewById(int id) throws Exception;

    List<Interview> getInterviewsForStudent(int studentUserId) throws Exception;

    List<Interview> getInterviewsForRecruiter(int recruiterUserId) throws Exception;

    List<Interview> getInterviewsForApplication(int applicationId) throws Exception;
}

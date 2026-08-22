package com.skillbridge.service;

import com.skillbridge.dao.*;
import com.skillbridge.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * InterviewServiceImpl implements InterviewService.
 */
public class InterviewServiceImpl implements InterviewService {

    private static final Logger LOGGER = Logger.getLogger(InterviewServiceImpl.class.getName());

    private final InterviewDAO interviewDAO;
    private final ApplicationDAO applicationDAO;
    private final RecruiterDAO recruiterDAO;
    private final StudentProfileDAO studentProfileDAO;

    public InterviewServiceImpl() {
        this.interviewDAO = new InterviewDAOImpl();
        this.applicationDAO = new ApplicationDAOImpl();
        this.recruiterDAO = new RecruiterDAOImpl();
        this.studentProfileDAO = new StudentProfileDAOImpl();
    }

    @Override
    public Interview scheduleInterview(Interview interview, int recruiterUserId) throws Exception {
        if (interview == null || interview.getApplicationId() <= 0) {
            throw new IllegalArgumentException("Valid candidate application is required.");
        }
        if (interview.getInterviewDate() == null || interview.getInterviewTime() == null) {
            throw new IllegalArgumentException("Interview date and time are required.");
        }
        if (interview.getMeetingLinkOrLocation() == null || interview.getMeetingLinkOrLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Meeting link or office address is required.");
        }

        // Save Interview
        Interview saved = interviewDAO.save(interview);

        // Auto-transition Application status to INTERVIEW
        applicationDAO.updateStatus(interview.getApplicationId(), ApplicationStatus.INTERVIEW, 
            "Interview scheduled: " + interview.getRoundName() + " on " + interview.getInterviewDate() + " at " + interview.getInterviewTime());

        return saved;
    }

    @Override
    public boolean updateInterview(Interview interview, int recruiterUserId) throws Exception {
        return interviewDAO.update(interview);
    }

    @Override
    public boolean updateStatus(int interviewId, InterviewStatus status, int userId) throws Exception {
        return interviewDAO.updateStatus(interviewId, status);
    }

    @Override
    public Optional<Interview> getInterviewById(int id) throws Exception {
        return interviewDAO.findById(id);
    }

    @Override
    public List<Interview> getInterviewsForStudent(int studentUserId) throws Exception {
        Optional<StudentProfile> optStudent = studentProfileDAO.findByUserId(studentUserId);
        if (optStudent.isEmpty()) {
            return Collections.emptyList();
        }
        return interviewDAO.findByStudentId(optStudent.get().getId());
    }

    @Override
    public List<Interview> getInterviewsForRecruiter(int recruiterUserId) throws Exception {
        Optional<RecruiterProfile> optRecruiter = recruiterDAO.findByUserId(recruiterUserId);
        if (optRecruiter.isEmpty()) {
            return Collections.emptyList();
        }
        return interviewDAO.findByRecruiterId(optRecruiter.get().getId());
    }

    @Override
    public List<Interview> getInterviewsForApplication(int applicationId) throws Exception {
        return interviewDAO.findByApplicationId(applicationId);
    }
}

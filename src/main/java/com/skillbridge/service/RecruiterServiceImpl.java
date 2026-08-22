package com.skillbridge.service;

import com.skillbridge.dao.*;
import com.skillbridge.model.*;
import com.skillbridge.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RecruiterServiceImpl implements RecruiterService.
 */
public class RecruiterServiceImpl implements RecruiterService {

    private static final Logger LOGGER = Logger.getLogger(RecruiterServiceImpl.class.getName());

    private final RecruiterDAO recruiterDAO;
    private final UserDAO userDAO;
    private final ApplicationDAO applicationDAO;
    private final JobDAO jobDAO;

    public RecruiterServiceImpl() {
        this.recruiterDAO = new RecruiterDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.applicationDAO = new ApplicationDAOImpl();
        this.jobDAO = new JobDAOImpl();
    }

    @Override
    public Optional<RecruiterProfile> getProfileByUserId(int userId) throws Exception {
        Optional<RecruiterProfile> optProfile = recruiterDAO.findByUserId(userId);
        if (optProfile.isPresent()) {
            return optProfile;
        }

        // Auto-initialize if missing
        RecruiterProfile newProfile = new RecruiterProfile(userId, "My Company");
        RecruiterProfile saved = recruiterDAO.save(newProfile);
        if (saved != null) {
            return recruiterDAO.findByUserId(userId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<RecruiterProfile> getProfileById(int id) throws Exception {
        return recruiterDAO.findById(id);
    }

    @Override
    public boolean updateProfile(RecruiterProfile profile, String fullName, String phone) throws Exception {
        if (profile == null) return false;

        boolean updated = recruiterDAO.update(profile);

        if (fullName != null && !fullName.trim().isEmpty()) {
            Optional<User> optUser = userDAO.findById(profile.getUserId());
            if (optUser.isPresent()) {
                User user = optUser.get();
                user.setFullName(fullName.trim());
                user.setPhone(phone != null ? phone.trim() : null);
                userDAO.update(user);
            }
        }

        return updated;
    }

    @Override
    public RecruiterDashboardStats getDashboardStats(int recruiterProfileId) throws Exception {
        RecruiterDashboardStats stats = new RecruiterDashboardStats();

        String sql = 
            "SELECT " +
            "  (SELECT COUNT(*) FROM jobs WHERE recruiter_id = ? AND is_active = TRUE) as active_jobs, " +
            "  (SELECT COUNT(*) FROM applications a JOIN jobs j ON a.job_id = j.id WHERE j.recruiter_id = ?) as total_apps, " +
            "  (SELECT COUNT(*) FROM applications a JOIN jobs j ON a.job_id = j.id WHERE j.recruiter_id = ? AND a.status = 'UNDER_REVIEW') as under_review, " +
            "  (SELECT COUNT(*) FROM applications a JOIN jobs j ON a.job_id = j.id WHERE j.recruiter_id = ? AND a.status = 'SHORTLISTED') as shortlisted, " +
            "  (SELECT COUNT(*) FROM applications a JOIN jobs j ON a.job_id = j.id WHERE j.recruiter_id = ? AND a.status = 'INTERVIEW') as interview, " +
            "  (SELECT COUNT(*) FROM applications a JOIN jobs j ON a.job_id = j.id WHERE j.recruiter_id = ? AND a.status = 'SELECTED') as hired";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 6; i++) {
                ps.setInt(i, recruiterProfileId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.setActiveJobsCount(rs.getInt("active_jobs"));
                    stats.setTotalApplicantsCount(rs.getInt("total_apps"));
                    stats.setUnderReviewCount(rs.getInt("under_review"));
                    stats.setShortlistedCount(rs.getInt("shortlisted"));
                    stats.setScheduledInterviewsCount(rs.getInt("interview"));
                    stats.setHiredCount(rs.getInt("hired"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error aggregating recruiter dashboard stats", e);
        }

        return stats;
    }

    @Override
    public List<Application> getApplicantsForRecruiter(int recruiterProfileId, Integer jobId, String statusFilter) throws Exception {
        StringBuilder sql = new StringBuilder(
            "SELECT a.*, " +
            "j.title as job_title, j.location as job_location, j.job_type, j.salary_min, j.salary_max, " +
            "rp.company_name, " +
            "u.full_name as student_name, u.email as student_email, u.phone as student_phone, " +
            "sp.headline as student_headline, sp.cgpa as student_cgpa, sp.github_url as student_github, sp.linkedin_url as student_linkedin " +
            "FROM applications a " +
            "JOIN jobs j ON a.job_id = j.id " +
            "JOIN recruiter_profiles rp ON j.recruiter_id = rp.id " +
            "JOIN student_profiles sp ON a.student_id = sp.id " +
            "JOIN users u ON sp.user_id = u.id " +
            "WHERE j.recruiter_id = ? "
        );

        List<Object> params = new ArrayList<>();
        params.add(recruiterProfileId);

        if (jobId != null && jobId > 0) {
            sql.append("AND a.job_id = ? ");
            params.add(jobId);
        }

        if (statusFilter != null && !statusFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(statusFilter.trim())) {
            sql.append("AND a.status = ? ");
            params.add(statusFilter.trim().toUpperCase());
        }

        sql.append("ORDER BY a.applied_at DESC");

        List<Application> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application app = new Application();
                    app.setId(rs.getInt("id"));
                    app.setJobId(rs.getInt("job_id"));
                    app.setStudentId(rs.getInt("student_id"));
                    app.setCoverLetter(rs.getString("cover_letter"));
                    app.setResumeUrl(rs.getString("resume_url"));
                    app.setStatus(ApplicationStatus.fromString(rs.getString("status")));
                    app.setNotes(rs.getString("notes"));
                    app.setAppliedAt(rs.getTimestamp("applied_at"));
                    app.setUpdatedAt(rs.getTimestamp("updated_at"));

                    app.setJobTitle(rs.getString("job_title"));
                    app.setJobLocation(rs.getString("job_location"));
                    app.setJobType(JobType.fromString(rs.getString("job_type")));
                    app.setSalaryMin(rs.getBigDecimal("salary_min"));
                    app.setSalaryMax(rs.getBigDecimal("salary_max"));
                    app.setCompanyName(rs.getString("company_name"));

                    app.setStudentName(rs.getString("student_name"));
                    app.setStudentEmail(rs.getString("student_email"));
                    app.setStudentPhone(rs.getString("student_phone"));
                    app.setStudentHeadline(rs.getString("student_headline"));
                    app.setStudentCgpa(rs.getBigDecimal("student_cgpa"));
                    app.setStudentGithub(rs.getString("student_github"));
                    app.setStudentLinkedin(rs.getString("student_linkedin"));

                    list.add(app);
                }
            }
        }
        return list;
    }

    @Override
    public boolean updateApplicationStatus(int applicationId, ApplicationStatus status, String notes, int recruiterProfileId) throws Exception {
        // Verify application belongs to this recruiter's job
        Optional<Application> optionalApp = applicationDAO.findById(applicationId);
        if (optionalApp.isEmpty()) {
            throw new IllegalArgumentException("Application not found.");
        }

        Application app = optionalApp.get();
        Optional<Job> optionalJob = jobDAO.findById(app.getJobId());
        if (optionalJob.isEmpty() || optionalJob.get().getRecruiterId() != recruiterProfileId) {
            throw new IllegalAccessException("Unauthorized to update this candidate application.");
        }

        return applicationDAO.updateStatus(applicationId, status, notes);
    }
}

package com.skillbridge.dao;

import com.skillbridge.model.Application;
import com.skillbridge.model.ApplicationStatus;
import com.skillbridge.model.JobType;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ApplicationDAOImpl implements ApplicationDAO with safe PreparedStatement queries.
 */
public class ApplicationDAOImpl implements ApplicationDAO {

    private static final Logger LOGGER = Logger.getLogger(ApplicationDAOImpl.class.getName());

    private static final String SQL_BASE_SELECT = 
        "SELECT a.*, " +
        "j.title as job_title, j.location as job_location, j.job_type, j.salary_min, j.salary_max, " +
        "rp.company_name, " +
        "u.full_name as student_name, u.email as student_email, u.phone as student_phone, " +
        "sp.headline as student_headline, sp.cgpa as student_cgpa, sp.github_url as student_github, sp.linkedin_url as student_linkedin " +
        "FROM applications a " +
        "JOIN jobs j ON a.job_id = j.id " +
        "JOIN recruiter_profiles rp ON j.recruiter_id = rp.id " +
        "JOIN student_profiles sp ON a.student_id = sp.id " +
        "JOIN users u ON sp.user_id = u.id ";

    @Override
    public Application save(Application application) throws SQLException {
        String sql = 
            "INSERT INTO applications (job_id, student_id, cover_letter, resume_url, status, notes) " +
            "VALUES (?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT (job_id, student_id) DO UPDATE SET " +
            "cover_letter = EXCLUDED.cover_letter, resume_url = EXCLUDED.resume_url, status = 'APPLIED', updated_at = CURRENT_TIMESTAMP " +
            "RETURNING id, applied_at, updated_at";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, application.getJobId());
            ps.setInt(2, application.getStudentId());
            ps.setString(3, application.getCoverLetter());
            ps.setString(4, application.getResumeUrl());
            ps.setString(5, application.getStatus().name());
            ps.setString(6, application.getNotes());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    application.setId(rs.getInt("id"));
                    application.setAppliedAt(rs.getTimestamp("applied_at"));
                    application.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return application;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving application for jobId: " + application.getJobId() + ", studentId: " + application.getStudentId(), e);
            throw e;
        }
        return application;
    }

    @Override
    public boolean updateStatus(int applicationId, ApplicationStatus status, String notes) throws SQLException {
        String sql = "UPDATE applications SET status = ?, notes = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, notes);
            ps.setInt(3, applicationId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Application> findById(int id) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE a.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToApplication(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Application> findByJobAndStudent(int jobId, int studentId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE a.job_id = ? AND a.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToApplication(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Application> findByStudentId(int studentId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE a.student_id = ? ORDER BY a.applied_at DESC";
        List<Application> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToApplication(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Application> findByJobId(int jobId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE a.job_id = ? ORDER BY a.applied_at DESC";
        List<Application> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToApplication(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Application> findByRecruiterId(int recruiterId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE j.recruiter_id = ? ORDER BY a.applied_at DESC";
        List<Application> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recruiterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToApplication(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean hasStudentApplied(int jobId, int studentId) throws SQLException {
        String sql = "SELECT 1 FROM applications WHERE job_id = ? AND student_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean delete(int applicationId, int studentId) throws SQLException {
        String sql = (studentId > 0)
            ? "DELETE FROM applications WHERE id = ? AND student_id = ?"
            : "DELETE FROM applications WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            if (studentId > 0) {
                ps.setInt(2, studentId);
            }
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int countTotalApplications() throws SQLException {
        String sql = "SELECT COUNT(*) FROM applications";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public List<Application> findAll() throws SQLException {
        String sql = SQL_BASE_SELECT + "ORDER BY a.applied_at DESC";
        List<Application> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToApplication(rs));
            }
        }
        return list;
    }

    private Application mapResultSetToApplication(ResultSet rs) throws SQLException {
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

        // Job details
        app.setJobTitle(rs.getString("job_title"));
        app.setJobLocation(rs.getString("job_location"));
        app.setJobType(JobType.fromString(rs.getString("job_type")));
        app.setSalaryMin(rs.getBigDecimal("salary_min"));
        app.setSalaryMax(rs.getBigDecimal("salary_max"));
        app.setCompanyName(rs.getString("company_name"));

        // Student details
        app.setStudentName(rs.getString("student_name"));
        app.setStudentEmail(rs.getString("student_email"));
        app.setStudentPhone(rs.getString("student_phone"));
        app.setStudentHeadline(rs.getString("student_headline"));
        app.setStudentCgpa(rs.getBigDecimal("student_cgpa"));
        app.setStudentGithub(rs.getString("student_github"));
        app.setStudentLinkedin(rs.getString("student_linkedin"));

        return app;
    }
}

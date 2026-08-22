package com.skillbridge.dao;

import com.skillbridge.model.ExperienceLevel;
import com.skillbridge.model.JobType;
import com.skillbridge.model.SavedJob;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SavedJobDAOImpl implements SavedJobDAO with PreparedStatement queries.
 */
public class SavedJobDAOImpl implements SavedJobDAO {

    private static final Logger LOGGER = Logger.getLogger(SavedJobDAOImpl.class.getName());

    @Override
    public boolean save(int studentId, int jobId) throws SQLException {
        String sql = "INSERT INTO saved_jobs (student_id, job_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, jobId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving job bookmark", e);
            throw e;
        }
    }

    @Override
    public boolean delete(int studentId, int jobId) throws SQLException {
        String sql = "DELETE FROM saved_jobs WHERE student_id = ? AND job_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, jobId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean isJobSaved(int studentId, int jobId) throws SQLException {
        String sql = "SELECT 1 FROM saved_jobs WHERE student_id = ? AND job_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<SavedJob> findByStudentId(int studentId) throws SQLException {
        String sql = 
            "SELECT sj.id, sj.student_id, sj.job_id, sj.saved_at, " +
            "j.title as job_title, j.location, j.job_type, j.experience_level, j.salary_min, j.salary_max, j.is_active, " +
            "rp.company_name " +
            "FROM saved_jobs sj " +
            "JOIN jobs j ON sj.job_id = j.id " +
            "JOIN recruiter_profiles rp ON j.recruiter_id = rp.id " +
            "WHERE sj.student_id = ? ORDER BY sj.saved_at DESC";

        List<SavedJob> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SavedJob sj = new SavedJob();
                    sj.setId(rs.getInt("id"));
                    sj.setStudentId(rs.getInt("student_id"));
                    sj.setJobId(rs.getInt("job_id"));
                    sj.setSavedAt(rs.getTimestamp("saved_at"));
                    sj.setJobTitle(rs.getString("job_title"));
                    sj.setLocation(rs.getString("location"));
                    sj.setJobType(JobType.fromString(rs.getString("job_type")));
                    sj.setExperienceLevel(ExperienceLevel.fromDbValue(rs.getString("experience_level")));
                    sj.setSalaryMin(rs.getBigDecimal("salary_min"));
                    sj.setSalaryMax(rs.getBigDecimal("salary_max"));
                    sj.setJobActive(rs.getBoolean("is_active"));
                    sj.setCompanyName(rs.getString("company_name"));
                    list.add(sj);
                }
            }
        }
        return list;
    }

    @Override
    public int countByStudentId(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM saved_jobs WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}

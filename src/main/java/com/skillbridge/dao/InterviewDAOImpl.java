package com.skillbridge.dao;

import com.skillbridge.model.Interview;
import com.skillbridge.model.InterviewMode;
import com.skillbridge.model.InterviewStatus;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * InterviewDAOImpl implements InterviewDAO with safe PreparedStatement queries.
 */
public class InterviewDAOImpl implements InterviewDAO {

    private static final Logger LOGGER = Logger.getLogger(InterviewDAOImpl.class.getName());

    private static final String SQL_BASE_SELECT = 
        "SELECT i.*, " +
        "a.job_id, a.student_id, a.resume_url, " +
        "j.title as job_title, " +
        "rp.company_name, rp.location as company_location, " +
        "u.full_name as student_name, u.email as student_email, u.phone as student_phone " +
        "FROM interviews i " +
        "JOIN applications a ON i.application_id = a.id " +
        "JOIN jobs j ON a.job_id = j.id " +
        "JOIN recruiter_profiles rp ON j.recruiter_id = rp.id " +
        "JOIN student_profiles sp ON a.student_id = sp.id " +
        "JOIN users u ON sp.user_id = u.id ";

    @Override
    public Interview save(Interview interview) throws SQLException {
        String sql = 
            "INSERT INTO interviews (application_id, interview_date, interview_time, interview_mode, meeting_link_or_location, round_name, instructions, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, interview.getApplicationId());
            ps.setDate(2, interview.getInterviewDate());
            ps.setTime(3, interview.getInterviewTime());
            ps.setString(4, interview.getInterviewMode().name());
            ps.setString(5, interview.getMeetingLinkOrLocation().trim());
            ps.setString(6, interview.getRoundName().trim());
            ps.setString(7, interview.getInstructions());
            ps.setString(8, interview.getStatus().name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    interview.setId(rs.getInt("id"));
                    interview.setCreatedAt(rs.getTimestamp("created_at"));
                    interview.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return interview;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting interview record", e);
            throw e;
        }
        return interview;
    }

    @Override
    public boolean update(Interview interview) throws SQLException {
        String sql = 
            "UPDATE interviews SET interview_date = ?, interview_time = ?, interview_mode = ?, meeting_link_or_location = ?, " +
            "round_name = ?, instructions = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, interview.getInterviewDate());
            ps.setTime(2, interview.getInterviewTime());
            ps.setString(3, interview.getInterviewMode().name());
            ps.setString(4, interview.getMeetingLinkOrLocation().trim());
            ps.setString(5, interview.getRoundName().trim());
            ps.setString(6, interview.getInstructions());
            ps.setString(7, interview.getStatus().name());
            ps.setInt(8, interview.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int interviewId, InterviewStatus status) throws SQLException {
        String sql = "UPDATE interviews SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setInt(2, interviewId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Interview> findById(int id) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE i.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToInterview(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Interview> findByApplicationId(int applicationId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE i.application_id = ? ORDER BY i.interview_date ASC, i.interview_time ASC";
        List<Interview> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToInterview(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Interview> findByStudentId(int studentId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE a.student_id = ? ORDER BY i.interview_date ASC, i.interview_time ASC";
        List<Interview> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToInterview(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Interview> findByRecruiterId(int recruiterId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE j.recruiter_id = ? ORDER BY i.interview_date ASC, i.interview_time ASC";
        List<Interview> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recruiterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToInterview(rs));
                }
            }
        }
        return list;
    }

    @Override
    public int countTotalInterviews() throws SQLException {
        String sql = "SELECT COUNT(*) FROM interviews";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Interview mapResultSetToInterview(ResultSet rs) throws SQLException {
        Interview item = new Interview();
        item.setId(rs.getInt("id"));
        item.setApplicationId(rs.getInt("application_id"));
        item.setInterviewDate(rs.getDate("interview_date"));
        item.setInterviewTime(rs.getTime("interview_time"));
        item.setInterviewMode(InterviewMode.fromString(rs.getString("interview_mode")));
        item.setMeetingLinkOrLocation(rs.getString("meeting_link_or_location"));
        item.setRoundName(rs.getString("round_name"));
        item.setInstructions(rs.getString("instructions"));
        item.setStatus(InterviewStatus.fromString(rs.getString("status")));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        item.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Join metadata
        item.setJobId(rs.getInt("job_id"));
        item.setStudentId(rs.getInt("student_id"));
        item.setJobTitle(rs.getString("job_title"));
        item.setCompanyName(rs.getString("company_name"));
        item.setCompanyLocation(rs.getString("company_location"));
        item.setStudentName(rs.getString("student_name"));
        item.setStudentEmail(rs.getString("student_email"));
        item.setStudentPhone(rs.getString("student_phone"));
        item.setResumeUrl(rs.getString("resume_url"));

        return item;
    }
}

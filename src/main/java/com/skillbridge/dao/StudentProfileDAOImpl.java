package com.skillbridge.dao;

import com.skillbridge.model.Role;
import com.skillbridge.model.StudentProfile;
import com.skillbridge.model.User;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StudentProfileDAOImpl implements StudentProfileDAO using JDBC PreparedStatement.
 */
public class StudentProfileDAOImpl implements StudentProfileDAO {

    private static final Logger LOGGER = Logger.getLogger(StudentProfileDAOImpl.class.getName());

    private static final String SQL_INSERT = 
        "INSERT INTO student_profiles (user_id, headline, bio, resume_url, github_url, linkedin_url, portfolio_url, graduation_year, cgpa, current_location) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";

    private static final String SQL_FIND_BY_ID = 
        "SELECT sp.*, u.email, u.full_name, u.role, u.phone, u.is_active, u.created_at as u_created_at " +
        "FROM student_profiles sp JOIN users u ON sp.user_id = u.id WHERE sp.id = ?";

    private static final String SQL_FIND_BY_USER_ID = 
        "SELECT sp.*, u.email, u.full_name, u.role, u.phone, u.is_active, u.created_at as u_created_at " +
        "FROM student_profiles sp JOIN users u ON sp.user_id = u.id WHERE sp.user_id = ?";

    private static final String SQL_UPDATE = 
        "UPDATE student_profiles SET headline = ?, bio = ?, resume_url = ?, github_url = ?, linkedin_url = ?, portfolio_url = ?, " +
        "graduation_year = ?, cgpa = ?, current_location = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String SQL_FIND_ALL = 
        "SELECT sp.*, u.email, u.full_name, u.role, u.phone, u.is_active, u.created_at as u_created_at " +
        "FROM student_profiles sp JOIN users u ON sp.user_id = u.id ORDER BY sp.created_at DESC";

    private static final String SQL_COUNT = 
        "SELECT COUNT(*) FROM student_profiles";

    @Override
    public StudentProfile save(StudentProfile profile) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getHeadline());
            ps.setString(3, profile.getBio());
            ps.setString(4, profile.getResumeUrl());
            ps.setString(5, profile.getGithubUrl());
            ps.setString(6, profile.getLinkedinUrl());
            ps.setString(7, profile.getPortfolioUrl());
            if (profile.getGraduationYear() != null) {
                ps.setInt(8, profile.getGraduationYear());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            if (profile.getCgpa() != null) {
                ps.setBigDecimal(9, profile.getCgpa());
            } else {
                ps.setNull(9, Types.NUMERIC);
            }
            ps.setString(10, profile.getCurrentLocation());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    profile.setId(rs.getInt("id"));
                    profile.setCreatedAt(rs.getTimestamp("created_at"));
                    profile.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return profile;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting student profile for userId: " + profile.getUserId(), e);
            throw e;
        }
        return null;
    }

    @Override
    public Optional<StudentProfile> findById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStudentProfile(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentProfile> findByUserId(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStudentProfile(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean update(StudentProfile profile) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, profile.getHeadline());
            ps.setString(2, profile.getBio());
            ps.setString(3, profile.getResumeUrl());
            ps.setString(4, profile.getGithubUrl());
            ps.setString(5, profile.getLinkedinUrl());
            ps.setString(6, profile.getPortfolioUrl());
            if (profile.getGraduationYear() != null) {
                ps.setInt(7, profile.getGraduationYear());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            if (profile.getCgpa() != null) {
                ps.setBigDecimal(8, profile.getCgpa());
            } else {
                ps.setNull(8, Types.NUMERIC);
            }
            ps.setString(9, profile.getCurrentLocation());
            ps.setInt(10, profile.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<StudentProfile> findAll() throws SQLException {
        List<StudentProfile> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToStudentProfile(rs));
            }
        }
        return list;
    }

    @Override
    public int countTotalStudents() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private StudentProfile mapResultSetToStudentProfile(ResultSet rs) throws SQLException {
        StudentProfile p = new StudentProfile();
        p.setId(rs.getInt("id"));
        p.setUserId(rs.getInt("user_id"));
        p.setHeadline(rs.getString("headline"));
        p.setBio(rs.getString("bio"));
        p.setResumeUrl(rs.getString("resume_url"));
        p.setGithubUrl(rs.getString("github_url"));
        p.setLinkedinUrl(rs.getString("linkedin_url"));
        p.setPortfolioUrl(rs.getString("portfolio_url"));
        
        int gradYear = rs.getInt("graduation_year");
        if (!rs.wasNull()) {
            p.setGraduationYear(gradYear);
        }
        p.setCgpa(rs.getBigDecimal("cgpa"));
        p.setCurrentLocation(rs.getString("current_location"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Join basic user details
        User u = new User();
        u.setId(p.getUserId());
        u.setEmail(rs.getString("email"));
        u.setFullName(rs.getString("full_name"));
        u.setRole(Role.fromString(rs.getString("role")));
        u.setPhone(rs.getString("phone"));
        u.setActive(rs.getBoolean("is_active"));
        p.setUser(u);

        return p;
    }
}

package com.skillbridge.dao;

import com.skillbridge.model.RecruiterProfile;
import com.skillbridge.model.Role;
import com.skillbridge.model.User;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RecruiterDAOImpl implements RecruiterDAO using JDBC PreparedStatement.
 */
public class RecruiterDAOImpl implements RecruiterDAO {

    private static final Logger LOGGER = Logger.getLogger(RecruiterDAOImpl.class.getName());

    private static final String SQL_INSERT = 
        "INSERT INTO recruiter_profiles (user_id, company_name, company_website, company_logo, company_description, company_size, industry, location) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";

    private static final String SQL_FIND_BY_ID = 
        "SELECT rp.*, u.email, u.full_name, u.role, u.phone, u.is_active, u.created_at as u_created_at " +
        "FROM recruiter_profiles rp JOIN users u ON rp.user_id = u.id WHERE rp.id = ?";

    private static final String SQL_FIND_BY_USER_ID = 
        "SELECT rp.*, u.email, u.full_name, u.role, u.phone, u.is_active, u.created_at as u_created_at " +
        "FROM recruiter_profiles rp JOIN users u ON rp.user_id = u.id WHERE rp.user_id = ?";

    private static final String SQL_UPDATE = 
        "UPDATE recruiter_profiles SET company_name = ?, company_website = ?, company_logo = ?, company_description = ?, " +
        "company_size = ?, industry = ?, location = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String SQL_FIND_ALL = 
        "SELECT rp.*, u.email, u.full_name, u.role, u.phone, u.is_active, u.created_at as u_created_at " +
        "FROM recruiter_profiles rp JOIN users u ON rp.user_id = u.id ORDER BY rp.created_at DESC";

    private static final String SQL_COUNT = 
        "SELECT COUNT(*) FROM recruiter_profiles";

    @Override
    public RecruiterProfile save(RecruiterProfile profile) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getCompanyName());
            ps.setString(3, profile.getCompanyWebsite());
            ps.setString(4, profile.getCompanyLogo());
            ps.setString(5, profile.getCompanyDescription());
            ps.setString(6, profile.getCompanySize());
            ps.setString(7, profile.getIndustry());
            ps.setString(8, profile.getLocation());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    profile.setId(rs.getInt("id"));
                    profile.setCreatedAt(rs.getTimestamp("created_at"));
                    profile.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return profile;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting recruiter profile for userId: " + profile.getUserId(), e);
            throw e;
        }
        return null;
    }

    @Override
    public Optional<RecruiterProfile> findById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRecruiterProfile(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<RecruiterProfile> findByUserId(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRecruiterProfile(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean update(RecruiterProfile profile) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, profile.getCompanyName());
            ps.setString(2, profile.getCompanyWebsite());
            ps.setString(3, profile.getCompanyLogo());
            ps.setString(4, profile.getCompanyDescription());
            ps.setString(5, profile.getCompanySize());
            ps.setString(6, profile.getIndustry());
            ps.setString(7, profile.getLocation());
            ps.setInt(8, profile.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<RecruiterProfile> findAll() throws SQLException {
        List<RecruiterProfile> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToRecruiterProfile(rs));
            }
        }
        return list;
    }

    @Override
    public int countTotalRecruiters() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private RecruiterProfile mapResultSetToRecruiterProfile(ResultSet rs) throws SQLException {
        RecruiterProfile p = new RecruiterProfile();
        p.setId(rs.getInt("id"));
        p.setUserId(rs.getInt("user_id"));
        p.setCompanyName(rs.getString("company_name"));
        p.setCompanyWebsite(rs.getString("company_website"));
        p.setCompanyLogo(rs.getString("company_logo"));
        p.setCompanyDescription(rs.getString("company_description"));
        p.setCompanySize(rs.getString("company_size"));
        p.setIndustry(rs.getString("industry"));
        p.setLocation(rs.getString("location"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));

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

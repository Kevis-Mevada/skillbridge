package com.skillbridge.dao;

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
 * UserDAOImpl implements UserDAO using JDBC PreparedStatement and try-with-resources.
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

    private static final String SQL_INSERT = 
        "INSERT INTO users (email, password_hash, full_name, role, phone, is_active) VALUES (?, ?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";

    private static final String SQL_FIND_BY_ID = 
        "SELECT id, email, password_hash, full_name, role, phone, is_active, created_at, updated_at FROM users WHERE id = ?";

    private static final String SQL_FIND_BY_EMAIL = 
        "SELECT id, email, password_hash, full_name, role, phone, is_active, created_at, updated_at FROM users WHERE email = ?";

    private static final String SQL_UPDATE = 
        "UPDATE users SET full_name = ?, phone = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String SQL_UPDATE_PASSWORD = 
        "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String SQL_UPDATE_STATUS = 
        "UPDATE users SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String SQL_EXISTS_EMAIL = 
        "SELECT 1 FROM users WHERE email = ? LIMIT 1";

    private static final String SQL_FIND_ALL = 
        "SELECT id, email, password_hash, full_name, role, phone, is_active, created_at, updated_at FROM users ORDER BY created_at DESC";

    private static final String SQL_FIND_BY_ROLE = 
        "SELECT id, email, password_hash, full_name, role, phone, is_active, created_at, updated_at FROM users WHERE role = ? ORDER BY created_at DESC";

    private static final String SQL_DELETE = 
        "DELETE FROM users WHERE id = ?";

    private static final String SQL_COUNT_BY_ROLE = 
        "SELECT COUNT(*) FROM users WHERE role = ?";

    @Override
    public User save(User user) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, user.getEmail().trim().toLowerCase());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName().trim());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getPhone() != null ? user.getPhone().trim() : null);
            ps.setBoolean(6, user.isActive());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving user: " + user.getEmail(), e);
            throw e;
        }
        return null;
    }

    @Override
    public Optional<User> findById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        if (email == null) return Optional.empty();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {

            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean update(User user) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, user.getFullName().trim());
            ps.setString(2, user.getPhone() != null ? user.getPhone().trim() : null);
            ps.setInt(3, user.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPasswordHash) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PASSWORD)) {

            ps.setString(1, newPasswordHash);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int id, boolean active) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS)) {

            ps.setBoolean(1, active);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        if (email == null) return false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_EMAIL)) {

            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        }
        return list;
    }

    @Override
    public List<User> findByRole(Role role) throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROLE)) {

            ps.setString(1, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToUser(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int countByRole(Role role) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_BY_ROLE)) {

            ps.setString(1, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(Role.fromString(rs.getString("role")));
        user.setPhone(rs.getString("phone"));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}

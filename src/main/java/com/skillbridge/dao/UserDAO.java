package com.skillbridge.dao;

import com.skillbridge.model.Role;
import com.skillbridge.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * UserDAO defines standard CRUD and query operations for the users table.
 */
public interface UserDAO {

    /**
     * Inserts a new user record into the database and sets the generated ID.
     */
    User save(User user) throws SQLException;

    /**
     * Finds a user by primary key ID.
     */
    Optional<User> findById(int id) throws SQLException;

    /**
     * Finds a user by unique email address.
     */
    Optional<User> findByEmail(String email) throws SQLException;

    /**
     * Updates an existing user's information.
     */
    boolean update(User user) throws SQLException;

    /**
     * Updates the password hash for a specific user ID.
     */
    boolean updatePassword(int userId, String newPasswordHash) throws SQLException;

    /**
     * Activates or deactivates a user account.
     */
    boolean updateStatus(int id, boolean active) throws SQLException;

    /**
     * Checks whether an email address is already registered.
     */
    boolean emailExists(String email) throws SQLException;

    /**
     * Retrieves all users.
     */
    List<User> findAll() throws SQLException;

    /**
     * Retrieves users filtered by role.
     */
    List<User> findByRole(Role role) throws SQLException;

    /**
     * Deletes a user by ID.
     */
    boolean delete(int id) throws SQLException;

    /**
     * Counts total users by role.
     */
    int countByRole(Role role) throws SQLException;
}

package com.skillbridge.service;

import com.skillbridge.model.Role;
import com.skillbridge.model.User;

import java.util.List;
import java.util.Optional;

/**
 * UserService defines business logic for registration, authentication, and user profile management.
 */
public interface UserService {

    /**
     * Registers a new user with hashed password and creates their default profile.
     * 
     * @param user the user object containing email, name, role, phone
     * @param plainPassword the plain-text password to hash
     * @param companyName required if role is RECRUITER
     * @return the saved user entity
     * @throws IllegalArgumentException if validation fails or email already exists
     * @throws Exception for database failure
     */
    User register(User user, String plainPassword, String companyName) throws Exception;

    /**
     * Authenticates a user by email and password.
     * 
     * @param email user email
     * @param plainPassword user entered password
     * @return Optional containing the active User if credentials match, empty otherwise
     * @throws Exception on system or database error
     */
    Optional<User> authenticate(String email, String plainPassword) throws Exception;

    Optional<User> getUserById(int id) throws Exception;

    Optional<User> getUserByEmail(String email) throws Exception;

    boolean isEmailRegistered(String email) throws Exception;

    boolean updateUser(User user) throws Exception;

    boolean changePassword(int userId, String oldPassword, String newPassword) throws Exception;

    boolean toggleUserStatus(int userId, boolean active) throws Exception;

    List<User> getAllUsers() throws Exception;

    List<User> getUsersByRole(Role role) throws Exception;
}

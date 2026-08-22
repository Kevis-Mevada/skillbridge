package com.skillbridge.service;

import com.skillbridge.dao.*;
import com.skillbridge.model.RecruiterProfile;
import com.skillbridge.model.Role;
import com.skillbridge.model.StudentProfile;
import com.skillbridge.model.User;
import com.skillbridge.util.PasswordUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserServiceImpl implements UserService business logic.
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private final UserDAO userDAO;
    private final StudentProfileDAO studentProfileDAO;
    private final RecruiterDAO recruiterDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
        this.studentProfileDAO = new StudentProfileDAOImpl();
        this.recruiterDAO = new RecruiterDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO, StudentProfileDAO studentProfileDAO, RecruiterDAO recruiterDAO) {
        this.userDAO = userDAO;
        this.studentProfileDAO = studentProfileDAO;
        this.recruiterDAO = recruiterDAO;
    }

    @Override
    public User register(User user, String plainPassword, String companyName) throws Exception {
        // 1. Validation
        if (user == null || user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is required.");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        if (user.getRole() == null) {
            user.setRole(Role.STUDENT);
        }

        String normalizedEmail = user.getEmail().trim().toLowerCase();
        if (userDAO.emailExists(normalizedEmail)) {
            throw new IllegalArgumentException("An account with email '" + normalizedEmail + "' already exists.");
        }

        if (user.getRole() == Role.RECRUITER) {
            if (companyName == null || companyName.trim().isEmpty()) {
                throw new IllegalArgumentException("Company name is required for recruiter registration.");
            }
        }

        // 2. Hash Password
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(hashedPassword);
        user.setActive(true);

        // 3. Save User
        User savedUser = userDAO.save(user);
        if (savedUser == null || savedUser.getId() == 0) {
            throw new RuntimeException("Failed to register user. Database error.");
        }

        // 4. Create corresponding Profile row
        try {
            if (savedUser.getRole() == Role.STUDENT) {
                StudentProfile studentProfile = new StudentProfile(savedUser.getId());
                studentProfile.setHeadline("Aspiring Tech Professional");
                studentProfileDAO.save(studentProfile);
            } else if (savedUser.getRole() == Role.RECRUITER) {
                RecruiterProfile recruiterProfile = new RecruiterProfile(savedUser.getId(), companyName.trim());
                recruiterDAO.save(recruiterProfile);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Profile row creation had a warning: " + e.getMessage(), e);
            // Non-fatal if user row exists, but logged
        }

        return savedUser;
    }

    @Override
    public Optional<User> authenticate(String email, String plainPassword) throws Exception {
        if (email == null || plainPassword == null || email.trim().isEmpty() || plainPassword.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> optionalUser = userDAO.findByEmail(email.trim().toLowerCase());
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        if (!user.isActive()) {
            LOGGER.info("Authentication rejected: Account is deactivated for email: " + email);
            throw new IllegalStateException("Your account has been deactivated. Please contact the administrator.");
        }

        boolean passwordMatches = PasswordUtil.checkPassword(plainPassword, user.getPasswordHash());
        if (passwordMatches) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(int id) throws Exception {
        return userDAO.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws Exception {
        if (email == null) return Optional.empty();
        return userDAO.findByEmail(email.trim().toLowerCase());
    }

    @Override
    public boolean isEmailRegistered(String email) throws Exception {
        if (email == null) return false;
        return userDAO.emailExists(email.trim().toLowerCase());
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        return userDAO.update(user);
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws Exception {
        Optional<User> optionalUser = userDAO.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        User user = optionalUser.get();
        if (!PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password does not match.");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long.");
        }

        String newHash = PasswordUtil.hashPassword(newPassword);
        return userDAO.updatePassword(userId, newHash);
    }

    @Override
    public boolean toggleUserStatus(int userId, boolean active) throws Exception {
        return userDAO.updateStatus(userId, active);
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        return userDAO.findAll();
    }

    @Override
    public List<User> getUsersByRole(Role role) throws Exception {
        return userDAO.findByRole(role);
    }
}

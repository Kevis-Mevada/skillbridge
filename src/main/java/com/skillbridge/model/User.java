package com.skillbridge.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User model representing platform credentials and core identity.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String email;
    private String passwordHash;
    private String fullName;
    private Role role;
    private String phone;
    private boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {
        this.active = true;
    }

    public User(String email, String passwordHash, String fullName, Role role, String phone) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.active = true;
    }

    public User(int id, String email, String passwordHash, String fullName, Role role, String phone, boolean active, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}

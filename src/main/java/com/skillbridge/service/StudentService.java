package com.skillbridge.service;

import com.skillbridge.model.*;

import java.util.List;
import java.util.Optional;

/**
 * StudentService manages business logic for student profiles, academic history,
 * skill development, and dashboard metrics.
 */
public interface StudentService {

    Optional<StudentProfile> getProfileByUserId(int userId) throws Exception;

    Optional<StudentProfile> getProfileById(int id) throws Exception;

    boolean updateProfile(StudentProfile profile, String fullName, String phone) throws Exception;

    List<Education> getEducationList(int studentProfileId) throws Exception;

    Education addEducation(Education education) throws Exception;

    boolean updateEducation(Education education) throws Exception;

    boolean deleteEducation(int educationId, int studentProfileId) throws Exception;

    List<StudentSkill> getStudentSkills(int studentProfileId) throws Exception;

    List<Skill> getAllMasterSkills() throws Exception;

    boolean addSkillToStudent(int studentProfileId, Integer skillId, String customSkillName, String proficiencyLevel) throws Exception;

    boolean removeSkillFromStudent(int studentProfileId, int skillId) throws Exception;

    StudentDashboardStats getDashboardStats(int studentProfileId) throws Exception;
}

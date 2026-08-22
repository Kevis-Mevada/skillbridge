package com.skillbridge.service;

import com.skillbridge.dao.*;
import com.skillbridge.model.*;
import com.skillbridge.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StudentServiceImpl implements StudentService.
 */
public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = Logger.getLogger(StudentServiceImpl.class.getName());

    private final StudentProfileDAO studentProfileDAO;
    private final EducationDAO educationDAO;
    private final SkillDAO skillDAO;
    private final UserDAO userDAO;

    public StudentServiceImpl() {
        this.studentProfileDAO = new StudentProfileDAOImpl();
        this.educationDAO = new EducationDAOImpl();
        this.skillDAO = new SkillDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    public StudentServiceImpl(StudentProfileDAO studentProfileDAO, EducationDAO educationDAO, SkillDAO skillDAO, UserDAO userDAO) {
        this.studentProfileDAO = studentProfileDAO;
        this.educationDAO = educationDAO;
        this.skillDAO = skillDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Optional<StudentProfile> getProfileByUserId(int userId) throws Exception {
        Optional<StudentProfile> optionalProfile = studentProfileDAO.findByUserId(userId);
        if (optionalProfile.isPresent()) {
            return optionalProfile;
        }

        // Auto-initialize if profile row is missing
        StudentProfile newProfile = new StudentProfile(userId);
        newProfile.setHeadline("Aspiring Developer / Student");
        StudentProfile saved = studentProfileDAO.save(newProfile);
        if (saved != null) {
            return studentProfileDAO.findByUserId(userId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentProfile> getProfileById(int id) throws Exception {
        return studentProfileDAO.findById(id);
    }

    @Override
    public boolean updateProfile(StudentProfile profile, String fullName, String phone) throws Exception {
        if (profile == null) return false;

        // 1. Update Student Profile attributes
        boolean profileUpdated = studentProfileDAO.update(profile);

        // 2. Update User Name and Phone in users table
        if (fullName != null && !fullName.trim().isEmpty()) {
            Optional<User> optUser = userDAO.findById(profile.getUserId());
            if (optUser.isPresent()) {
                User user = optUser.get();
                user.setFullName(fullName.trim());
                user.setPhone(phone != null ? phone.trim() : null);
                userDAO.update(user);
            }
        }

        return profileUpdated;
    }

    @Override
    public List<Education> getEducationList(int studentProfileId) throws Exception {
        return educationDAO.findByStudentProfileId(studentProfileId);
    }

    @Override
    public Education addEducation(Education education) throws Exception {
        if (education == null) throw new IllegalArgumentException("Education details cannot be null.");
        if (education.getInstitution() == null || education.getInstitution().trim().isEmpty()) {
            throw new IllegalArgumentException("Institution/College name is required.");
        }
        if (education.getDegree() == null || education.getDegree().trim().isEmpty()) {
            throw new IllegalArgumentException("Degree name is required.");
        }
        if (education.getFieldOfStudy() == null || education.getFieldOfStudy().trim().isEmpty()) {
            throw new IllegalArgumentException("Field of study is required.");
        }

        return educationDAO.save(education);
    }

    @Override
    public boolean updateEducation(Education education) throws Exception {
        if (education == null || education.getId() == 0) {
            throw new IllegalArgumentException("Valid education ID is required for update.");
        }
        return educationDAO.update(education);
    }

    @Override
    public boolean deleteEducation(int educationId, int studentProfileId) throws Exception {
        return educationDAO.delete(educationId, studentProfileId);
    }

    @Override
    public List<StudentSkill> getStudentSkills(int studentProfileId) throws Exception {
        return skillDAO.findSkillsByStudentId(studentProfileId);
    }

    @Override
    public List<Skill> getAllMasterSkills() throws Exception {
        return skillDAO.findAll();
    }

    @Override
    public boolean addSkillToStudent(int studentProfileId, Integer skillId, String customSkillName, String proficiencyLevel) throws Exception {
        int targetSkillId = 0;

        if (skillId != null && skillId > 0) {
            targetSkillId = skillId;
        } else if (customSkillName != null && !customSkillName.trim().isEmpty()) {
            String trimmedName = customSkillName.trim();
            Optional<Skill> existing = skillDAO.findByName(trimmedName);
            if (existing.isPresent()) {
                targetSkillId = existing.get().getId();
            } else {
                Skill newSkill = new Skill(trimmedName, "Technical");
                Skill saved = skillDAO.save(newSkill);
                targetSkillId = saved.getId();
            }
        } else {
            throw new IllegalArgumentException("Please select or enter a valid skill.");
        }

        return skillDAO.addSkillToStudent(studentProfileId, targetSkillId, proficiencyLevel);
    }

    @Override
    public boolean removeSkillFromStudent(int studentProfileId, int skillId) throws Exception {
        return skillDAO.removeSkillFromStudent(studentProfileId, skillId);
    }

    @Override
    public StudentDashboardStats getDashboardStats(int studentProfileId) throws Exception {
        StudentDashboardStats stats = new StudentDashboardStats();

        String sqlApps = 
            "SELECT " +
            "  COUNT(*) as total, " +
            "  COUNT(CASE WHEN status = 'UNDER_REVIEW' THEN 1 END) as under_review, " +
            "  COUNT(CASE WHEN status = 'SHORTLISTED' THEN 1 END) as shortlisted, " +
            "  COUNT(CASE WHEN status = 'INTERVIEW' THEN 1 END) as interview, " +
            "  COUNT(CASE WHEN status = 'SELECTED' THEN 1 END) as selected " +
            "FROM applications WHERE student_id = ?";

        String sqlSaved = "SELECT COUNT(*) FROM saved_jobs WHERE student_id = ?";
        String sqlInterviews = 
            "SELECT COUNT(*) FROM interviews i " +
            "JOIN applications a ON i.application_id = a.id " +
            "WHERE a.student_id = ? AND i.status = 'SCHEDULED' AND i.interview_date >= CURRENT_DATE";

        try (Connection conn = DBConnection.getConnection()) {
            // Applications Stats
            try (PreparedStatement ps = conn.prepareStatement(sqlApps)) {
                ps.setInt(1, studentProfileId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stats.setTotalApplications(rs.getInt("total"));
                        stats.setUnderReviewCount(rs.getInt("under_review"));
                        stats.setShortlistedCount(rs.getInt("shortlisted"));
                        stats.setSelectedCount(rs.getInt("selected"));
                    }
                }
            }

            // Saved Jobs
            try (PreparedStatement ps = conn.prepareStatement(sqlSaved)) {
                ps.setInt(1, studentProfileId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stats.setSavedJobsCount(rs.getInt(1));
                    }
                }
            }

            // Upcoming Interviews
            try (PreparedStatement ps = conn.prepareStatement(sqlInterviews)) {
                ps.setInt(1, studentProfileId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stats.setUpcomingInterviewsCount(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error aggregating student dashboard stats for studentId: " + studentProfileId, e);
        }

        // Calculate Profile Completion Score
        int completionScore = 0;
        Optional<StudentProfile> optProf = studentProfileDAO.findById(studentProfileId);
        if (optProf.isPresent()) {
            StudentProfile sp = optProf.get();
            if (sp.getHeadline() != null && !sp.getHeadline().trim().isEmpty() &&
                sp.getBio() != null && !sp.getBio().trim().isEmpty()) {
                completionScore += 25;
            }
            if ((sp.getResumeUrl() != null && !sp.getResumeUrl().trim().isEmpty()) ||
                (sp.getLinkedinUrl() != null && !sp.getLinkedinUrl().trim().isEmpty()) ||
                (sp.getGithubUrl() != null && !sp.getGithubUrl().trim().isEmpty())) {
                completionScore += 25;
            }
        }

        List<Education> eduList = educationDAO.findByStudentProfileId(studentProfileId);
        if (!eduList.isEmpty()) {
            completionScore += 25;
        }

        List<StudentSkill> skillsList = skillDAO.findSkillsByStudentId(studentProfileId);
        if (!skillsList.isEmpty()) {
            completionScore += 25;
        }

        stats.setProfileCompletionPercentage(completionScore);
        return stats;
    }
}

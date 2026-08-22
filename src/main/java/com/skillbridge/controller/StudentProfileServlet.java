package com.skillbridge.controller;

import com.skillbridge.model.*;
import com.skillbridge.service.StudentService;
import com.skillbridge.service.StudentServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StudentProfileServlet manages student profile editing, academic education CRUD,
 * and skill additions/removals.
 */
@WebServlet(name = "StudentProfileServlet", urlPatterns = {"/student/profile"})
public class StudentProfileServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StudentProfileServlet.class.getName());
    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        this.studentService = new StudentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Optional<StudentProfile> optProfile = studentService.getProfileByUserId(user.getId());
            if (optProfile.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/student/dashboard");
                return;
            }

            StudentProfile profile = optProfile.get();
            List<Education> educationList = studentService.getEducationList(profile.getId());
            List<StudentSkill> studentSkills = studentService.getStudentSkills(profile.getId());
            List<Skill> masterSkills = studentService.getAllMasterSkills();

            req.setAttribute("profile", profile);
            req.setAttribute("educationList", educationList);
            req.setAttribute("studentSkills", studentSkills);
            req.setAttribute("masterSkills", masterSkills);

            // Flash messages
            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }
            if (session.getAttribute("flashError") != null) {
                req.setAttribute("errorMessage", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            req.getRequestDispatcher("/WEB-INF/views/student/student-profile.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading student profile", e);
            resp.sendRedirect(req.getContextPath() + "/student/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "updateBasic";

        try {
            Optional<StudentProfile> optProfile = studentService.getProfileByUserId(user.getId());
            if (optProfile.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/student/dashboard");
                return;
            }

            StudentProfile profile = optProfile.get();

            switch (action) {
                case "updateBasic":
                    handleUpdateBasic(req, session, profile, user);
                    break;
                case "addEducation":
                    handleAddEducation(req, session, profile);
                    break;
                case "deleteEducation":
                    handleDeleteEducation(req, session, profile);
                    break;
                case "addSkill":
                    handleAddSkill(req, session, profile);
                    break;
                case "removeSkill":
                    handleRemoveSkill(req, session, profile);
                    break;
                default:
                    session.setAttribute("flashError", "Unrecognized profile action.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing student profile action: " + action, e);
            session.setAttribute("flashError", "Failed to update profile: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/student/profile");
    }

    private void handleUpdateBasic(HttpServletRequest req, HttpSession session, StudentProfile profile, User user) throws Exception {
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String headline = req.getParameter("headline");
        String bio = req.getParameter("bio");
        String resumeUrl = req.getParameter("resumeUrl");
        String githubUrl = req.getParameter("githubUrl");
        String linkedinUrl = req.getParameter("linkedinUrl");
        String portfolioUrl = req.getParameter("portfolioUrl");
        String graduationYearStr = req.getParameter("graduationYear");
        String cgpaStr = req.getParameter("cgpa");
        String currentLocation = req.getParameter("currentLocation");

        profile.setHeadline(headline);
        profile.setBio(bio);
        profile.setResumeUrl(resumeUrl);
        profile.setGithubUrl(githubUrl);
        profile.setLinkedinUrl(linkedinUrl);
        profile.setPortfolioUrl(portfolioUrl);
        profile.setCurrentLocation(currentLocation);

        if (graduationYearStr != null && !graduationYearStr.trim().isEmpty()) {
            try {
                profile.setGraduationYear(Integer.parseInt(graduationYearStr.trim()));
            } catch (NumberFormatException ignored) {}
        } else {
            profile.setGraduationYear(null);
        }

        if (cgpaStr != null && !cgpaStr.trim().isEmpty()) {
            try {
                profile.setCgpa(new BigDecimal(cgpaStr.trim()));
            } catch (Exception ignored) {}
        } else {
            profile.setCgpa(null);
        }

        studentService.updateProfile(profile, fullName, phone);

        // Update user session attributes
        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName.trim());
            user.setPhone(phone);
            session.setAttribute("currentUser", user);
            session.setAttribute("userName", fullName.trim());
        }

        session.setAttribute("flashSuccess", "Profile details updated successfully!");
    }

    private void handleAddEducation(HttpServletRequest req, HttpSession session, StudentProfile profile) throws Exception {
        String institution = req.getParameter("institution");
        String degree = req.getParameter("degree");
        String fieldOfStudy = req.getParameter("fieldOfStudy");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");
        String gradeStr = req.getParameter("gradePercentage");

        Date startDate = (startDateStr != null && !startDateStr.trim().isEmpty()) ? Date.valueOf(startDateStr) : null;
        Date endDate = (endDateStr != null && !endDateStr.trim().isEmpty()) ? Date.valueOf(endDateStr) : null;
        BigDecimal grade = null;
        if (gradeStr != null && !gradeStr.trim().isEmpty()) {
            try {
                grade = new BigDecimal(gradeStr.trim());
            } catch (Exception ignored) {}
        }

        Education edu = new Education(profile.getId(), institution, degree, fieldOfStudy, startDate, endDate, grade);
        studentService.addEducation(edu);

        session.setAttribute("flashSuccess", "Education record added successfully!");
    }

    private void handleDeleteEducation(HttpServletRequest req, HttpSession session, StudentProfile profile) throws Exception {
        String eduIdStr = req.getParameter("educationId");
        if (eduIdStr != null && !eduIdStr.trim().isEmpty()) {
            int eduId = Integer.parseInt(eduIdStr.trim());
            studentService.deleteEducation(eduId, profile.getId());
            session.setAttribute("flashSuccess", "Education record removed.");
        }
    }

    private void handleAddSkill(HttpServletRequest req, HttpSession session, StudentProfile profile) throws Exception {
        String skillIdStr = req.getParameter("skillId");
        String customSkillName = req.getParameter("customSkillName");
        String proficiencyLevel = req.getParameter("proficiencyLevel");

        Integer skillId = (skillIdStr != null && !skillIdStr.trim().isEmpty()) ? Integer.parseInt(skillIdStr.trim()) : null;

        studentService.addSkillToStudent(profile.getId(), skillId, customSkillName, proficiencyLevel);
        session.setAttribute("flashSuccess", "Skill added to your profile!");
    }

    private void handleRemoveSkill(HttpServletRequest req, HttpSession session, StudentProfile profile) throws Exception {
        String skillIdStr = req.getParameter("skillId");
        if (skillIdStr != null && !skillIdStr.trim().isEmpty()) {
            int skillId = Integer.parseInt(skillIdStr.trim());
            studentService.removeSkillFromStudent(profile.getId(), skillId);
            session.setAttribute("flashSuccess", "Skill removed from your profile.");
        }
    }
}

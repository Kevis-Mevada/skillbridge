package com.skillbridge.controller;

import com.skillbridge.dao.StudentProfileDAO;
import com.skillbridge.dao.StudentProfileDAOImpl;
import com.skillbridge.model.Job;
import com.skillbridge.model.Role;
import com.skillbridge.model.StudentProfile;
import com.skillbridge.model.User;
import com.skillbridge.service.JobService;
import com.skillbridge.service.JobServiceImpl;
import com.skillbridge.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JobDetailsServlet renders full job details, company profile, and application status.
 */
@WebServlet(name = "JobDetailsServlet", urlPatterns = {"/jobs/details", "/job-details"})
public class JobDetailsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(JobDetailsServlet.class.getName());
    private JobService jobService;
    private StudentProfileDAO studentProfileDAO;

    @Override
    public void init() throws ServletException {
        this.jobService = new JobServiceImpl();
        this.studentProfileDAO = new StudentProfileDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
            return;
        }

        try {
            int jobId = Integer.parseInt(idStr.trim());
            Optional<Job> optionalJob = jobService.getJobDetails(jobId);

            if (optionalJob.isEmpty()) {
                req.setAttribute("errorMessage", "The requested job or internship posting could not be found.");
                req.getRequestDispatcher("/jobs.jsp").forward(req, resp);
                return;
            }

            Job job = optionalJob.get();
            req.setAttribute("job", job);

            // Check if student user is logged in to inspect application and bookmark status
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("currentUser") != null) {
                User user = (User) session.getAttribute("currentUser");
                if (user.getRole() == Role.STUDENT) {
                    Optional<StudentProfile> optStudent = studentProfileDAO.findByUserId(user.getId());
                    if (optStudent.isPresent()) {
                        int studentId = optStudent.get().getId();
                        checkApplicationAndSavedStatus(req, jobId, studentId);
                    }
                }
            }

            req.getRequestDispatcher("/job-details.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading job details", e);
            resp.sendRedirect(req.getContextPath() + "/jobs");
        }
    }

    private void checkApplicationAndSavedStatus(HttpServletRequest req, int jobId, int studentId) {
        String sqlApp = "SELECT id, status, applied_at FROM applications WHERE job_id = ? AND student_id = ?";
        String sqlSave = "SELECT id FROM saved_jobs WHERE job_id = ? AND student_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Check Application
            try (PreparedStatement ps = conn.prepareStatement(sqlApp)) {
                ps.setInt(1, jobId);
                ps.setInt(2, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        req.setAttribute("hasApplied", true);
                        req.setAttribute("applicationStatus", rs.getString("status"));
                        req.setAttribute("appliedAt", rs.getTimestamp("applied_at"));
                    } else {
                        req.setAttribute("hasApplied", false);
                    }
                }
            }

            // Check Saved Job
            try (PreparedStatement ps = conn.prepareStatement(sqlSave)) {
                ps.setInt(1, jobId);
                ps.setInt(2, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    req.setAttribute("isSaved", rs.next());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error checking student job status", e);
        }
    }
}

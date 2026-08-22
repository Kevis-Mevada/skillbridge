package com.skillbridge.controller;

import com.skillbridge.model.Job;
import com.skillbridge.model.StudentProfile;
import com.skillbridge.model.User;
import com.skillbridge.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ApplyJobServlet processes student job application submissions.
 */
@WebServlet(name = "ApplyJobServlet", urlPatterns = {"/student/apply"})
public class ApplyJobServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ApplyJobServlet.class.getName());
    private ApplicationService applicationService;
    private JobService jobService;
    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        this.applicationService = new ApplicationServiceImpl();
        this.jobService = new JobServiceImpl();
        this.studentService = new StudentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String jobIdStr = req.getParameter("jobId");
        if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
            return;
        }

        try {
            int jobId = Integer.parseInt(jobIdStr.trim());
            Optional<Job> optionalJob = jobService.getJobDetails(jobId);
            if (optionalJob.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/jobs");
                return;
            }

            Optional<StudentProfile> optionalProfile = studentService.getProfileByUserId(user.getId());

            req.setAttribute("job", optionalJob.get());
            optionalProfile.ifPresent(profile -> req.setAttribute("profile", profile));

            req.getRequestDispatcher("/WEB-INF/views/student/apply-job.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading apply view", e);
            resp.sendRedirect(req.getContextPath() + "/jobs");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String jobIdStr = req.getParameter("jobId");
        String coverLetter = req.getParameter("coverLetter");
        String resumeUrl = req.getParameter("resumeUrl");

        if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
            return;
        }

        try {
            int jobId = Integer.parseInt(jobIdStr.trim());
            applicationService.applyForJob(jobId, user.getId(), coverLetter, resumeUrl);

            session.setAttribute("flashSuccess", "Your application has been submitted successfully! You can track its status below.");
            resp.sendRedirect(req.getContextPath() + "/student/applications");
        } catch (IllegalStateException e) {
            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/jobs/details?id=" + jobIdStr);
        } catch (IllegalArgumentException e) {
            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/jobs/details?id=" + jobIdStr);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error submitting job application", e);
            session.setAttribute("flashError", "Failed to submit application. Please try again.");
            resp.sendRedirect(req.getContextPath() + "/jobs/details?id=" + jobIdStr);
        }
    }
}

package com.skillbridge.controller;

import com.skillbridge.model.Job;
import com.skillbridge.model.RecruiterProfile;
import com.skillbridge.model.User;
import com.skillbridge.service.JobService;
import com.skillbridge.service.JobServiceImpl;
import com.skillbridge.service.RecruiterService;
import com.skillbridge.service.RecruiterServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ManageJobsServlet displays a recruiter's active and closed jobs with toggle and delete actions.
 */
@WebServlet(name = "ManageJobsServlet", urlPatterns = {"/recruiter/manage-jobs"})
public class ManageJobsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ManageJobsServlet.class.getName());
    private JobService jobService;
    private RecruiterService recruiterService;

    @Override
    public void init() throws ServletException {
        this.jobService = new JobServiceImpl();
        this.recruiterService = new RecruiterServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        try {
            Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());
            if (optProfile.isPresent()) {
                List<Job> jobs = jobService.getJobsByRecruiter(optProfile.get().getId());
                req.setAttribute("jobs", jobs);
            }

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }
            if (session.getAttribute("flashError") != null) {
                req.setAttribute("errorMessage", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            req.getRequestDispatcher("/WEB-INF/views/recruiter/manage-jobs.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving recruiter jobs", e);
            req.setAttribute("errorMessage", "Failed to retrieve your jobs list.");
            req.getRequestDispatcher("/WEB-INF/views/recruiter/manage-jobs.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String action = req.getParameter("action");
        String jobIdStr = req.getParameter("jobId");

        if (jobIdStr != null && !jobIdStr.trim().isEmpty()) {
            try {
                int jobId = Integer.parseInt(jobIdStr.trim());
                Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());

                if (optProfile.isPresent()) {
                    int recruiterId = optProfile.get().getId();

                    if ("delete".equalsIgnoreCase(action)) {
                        jobService.deleteJob(jobId, recruiterId);
                        session.setAttribute("flashSuccess", "Job posting removed permanently.");
                    } else if ("toggle".equalsIgnoreCase(action)) {
                        String currentStatus = req.getParameter("currentStatus");
                        boolean newStatus = !"true".equalsIgnoreCase(currentStatus);
                        jobService.toggleJobStatus(jobId, recruiterId, newStatus);
                        session.setAttribute("flashSuccess", "Job status updated to " + (newStatus ? "ACTIVE" : "CLOSED"));
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error handling job action: " + action, e);
                session.setAttribute("flashError", "Failed to perform action on job.");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/recruiter/manage-jobs");
    }
}

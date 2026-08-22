package com.skillbridge.controller;

import com.skillbridge.model.Application;
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
 * RecruiterApplicantsServlet manages candidate applications submitted to recruiter postings.
 */
@WebServlet(name = "RecruiterApplicantsServlet", urlPatterns = {"/recruiter/applicants"})
public class RecruiterApplicantsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RecruiterApplicantsServlet.class.getName());
    private RecruiterService recruiterService;
    private JobService jobService;

    @Override
    public void init() throws ServletException {
        this.recruiterService = new RecruiterServiceImpl();
        this.jobService = new JobServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String jobIdStr = req.getParameter("jobId");
        String statusFilter = req.getParameter("status");

        try {
            Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());
            if (optProfile.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/recruiter/dashboard");
                return;
            }

            int recruiterId = optProfile.get().getId();
            Integer jobId = (jobIdStr != null && !jobIdStr.trim().isEmpty()) ? Integer.parseInt(jobIdStr.trim()) : null;

            List<Application> applicants = recruiterService.getApplicantsForRecruiter(recruiterId, jobId, statusFilter);
            List<Job> postedJobs = jobService.getJobsByRecruiter(recruiterId);

            req.setAttribute("applicants", applicants);
            req.setAttribute("postedJobs", postedJobs);
            req.setAttribute("selectedJobId", jobId);
            req.setAttribute("selectedStatus", statusFilter != null ? statusFilter : "ALL");

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }
            if (session.getAttribute("flashError") != null) {
                req.setAttribute("errorMessage", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            req.getRequestDispatcher("/WEB-INF/views/recruiter/applicants.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving applicants", e);
            req.setAttribute("errorMessage", "Unable to load candidate applications.");
            req.getRequestDispatcher("/WEB-INF/views/recruiter/applicants.jsp").forward(req, resp);
        }
    }
}

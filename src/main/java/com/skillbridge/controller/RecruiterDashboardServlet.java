package com.skillbridge.controller;

import com.skillbridge.model.Job;
import com.skillbridge.model.RecruiterDashboardStats;
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
 * RecruiterDashboardServlet displays recruiter overview metrics and recently posted jobs.
 */
@WebServlet(name = "RecruiterDashboardServlet", urlPatterns = {"/recruiter/dashboard"})
public class RecruiterDashboardServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RecruiterDashboardServlet.class.getName());
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

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());
            if (optProfile.isPresent()) {
                RecruiterProfile profile = optProfile.get();
                RecruiterDashboardStats stats = recruiterService.getDashboardStats(profile.getId());
                List<Job> postedJobs = jobService.getJobsByRecruiter(profile.getId());

                req.setAttribute("profile", profile);
                req.setAttribute("stats", stats);
                req.setAttribute("postedJobs", postedJobs);
            }

            req.getRequestDispatcher("/WEB-INF/views/recruiter/recruiter-dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading recruiter dashboard", e);
            req.setAttribute("errorMessage", "Unable to load recruiter dashboard.");
            req.getRequestDispatcher("/WEB-INF/views/recruiter/recruiter-dashboard.jsp").forward(req, resp);
        }
    }
}

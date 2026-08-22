package com.skillbridge.controller;

import com.skillbridge.model.SavedJob;
import com.skillbridge.model.User;
import com.skillbridge.service.ApplicationService;
import com.skillbridge.service.ApplicationServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SavedJobsServlet handles bookmarking opportunities and displaying saved jobs.
 */
@WebServlet(name = "SavedJobsServlet", urlPatterns = {"/student/saved-jobs", "/student/save-job"})
public class SavedJobsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SavedJobsServlet.class.getName());
    private ApplicationService applicationService;

    @Override
    public void init() throws ServletException {
        this.applicationService = new ApplicationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        try {
            List<SavedJob> savedJobs = applicationService.getSavedJobs(user.getId());
            req.setAttribute("savedJobs", savedJobs);

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }

            req.getRequestDispatcher("/WEB-INF/views/student/saved-jobs.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading saved jobs", e);
            resp.sendRedirect(req.getContextPath() + "/student/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String jobIdStr = req.getParameter("jobId");
        String action = req.getParameter("action");

        if (jobIdStr != null && !jobIdStr.trim().isEmpty()) {
            try {
                int jobId = Integer.parseInt(jobIdStr.trim());
                if ("remove".equalsIgnoreCase(action)) {
                    applicationService.removeSavedJob(user.getId(), jobId);
                    session.setAttribute("flashSuccess", "Job removed from saved list.");
                } else {
                    boolean isSaved = applicationService.toggleSaveJob(user.getId(), jobId);
                    if (isSaved) {
                        session.setAttribute("flashSuccess", "Job saved to your bookmarks!");
                    } else {
                        session.setAttribute("flashSuccess", "Job removed from bookmarks.");
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error saving/removing job", e);
            }
        }

        String referer = req.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/student/saved-jobs");
        }
    }
}

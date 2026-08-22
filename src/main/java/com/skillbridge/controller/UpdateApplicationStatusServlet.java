package com.skillbridge.controller;

import com.skillbridge.model.ApplicationStatus;
import com.skillbridge.model.RecruiterProfile;
import com.skillbridge.model.User;
import com.skillbridge.service.RecruiterService;
import com.skillbridge.service.RecruiterServiceImpl;
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
 * UpdateApplicationStatusServlet transitions candidate pipeline stages (Shortlisted, Under Review, etc.).
 */
@WebServlet(name = "UpdateApplicationStatusServlet", urlPatterns = {"/recruiter/application/status"})
public class UpdateApplicationStatusServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateApplicationStatusServlet.class.getName());
    private RecruiterService recruiterService;

    @Override
    public void init() throws ServletException {
        this.recruiterService = new RecruiterServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String appIdStr = req.getParameter("applicationId");
        String statusStr = req.getParameter("status");
        String notes = req.getParameter("notes");

        try {
            int applicationId = Integer.parseInt(appIdStr.trim());
            ApplicationStatus status = ApplicationStatus.fromString(statusStr);

            Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());
            if (optProfile.isPresent()) {
                boolean updated = recruiterService.updateApplicationStatus(applicationId, status, notes, optProfile.get().getId());
                if (updated) {
                    session.setAttribute("flashSuccess", "Application status updated to " + status.getDisplayName() + "!");
                } else {
                    session.setAttribute("flashError", "Unable to update application status.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating candidate status", e);
            session.setAttribute("flashError", "Failed to update candidate status: " + e.getMessage());
        }

        String referer = req.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/recruiter/applicants");
        }
    }
}

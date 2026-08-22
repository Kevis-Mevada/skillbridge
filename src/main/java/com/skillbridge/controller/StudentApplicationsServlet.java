package com.skillbridge.controller;

import com.skillbridge.model.Application;
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
 * StudentApplicationsServlet displays submitted applications and application progress.
 */
@WebServlet(name = "StudentApplicationsServlet", urlPatterns = {"/student/applications"})
public class StudentApplicationsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StudentApplicationsServlet.class.getName());
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
            List<Application> applications = applicationService.getStudentApplications(user.getId());
            req.setAttribute("applications", applications);

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }
            if (session.getAttribute("flashError") != null) {
                req.setAttribute("errorMessage", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            req.getRequestDispatcher("/WEB-INF/views/student/my-applications.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading student applications", e);
            req.setAttribute("errorMessage", "Unable to load applications.");
            req.getRequestDispatcher("/WEB-INF/views/student/my-applications.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String action = req.getParameter("action");
        String appIdStr = req.getParameter("applicationId");

        if ("withdraw".equalsIgnoreCase(action) && appIdStr != null && !appIdStr.trim().isEmpty()) {
            try {
                int appId = Integer.parseInt(appIdStr.trim());
                boolean withdrawn = applicationService.withdrawApplication(appId, user.getId());
                if (withdrawn) {
                    session.setAttribute("flashSuccess", "Application withdrawn successfully.");
                } else {
                    session.setAttribute("flashError", "Failed to withdraw application.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error withdrawing application", e);
                session.setAttribute("flashError", "Error processing withdrawal.");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/student/applications");
    }
}

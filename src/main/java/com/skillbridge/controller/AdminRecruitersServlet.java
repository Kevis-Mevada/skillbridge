package com.skillbridge.controller;

import com.skillbridge.model.RecruiterProfile;
import com.skillbridge.service.AdminService;
import com.skillbridge.service.AdminServiceImpl;
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
 * AdminRecruitersServlet manages employer/company accounts and verification status.
 */
@WebServlet(name = "AdminRecruitersServlet", urlPatterns = {"/admin/recruiters"})
public class AdminRecruitersServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminRecruitersServlet.class.getName());
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        try {
            List<RecruiterProfile> recruiters = adminService.getAllRecruiters();
            req.setAttribute("recruiters", recruiters);

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }
            if (session.getAttribute("flashError") != null) {
                req.setAttribute("errorMessage", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            req.getRequestDispatcher("/WEB-INF/views/admin/manage-recruiters.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading recruiters list", e);
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        String action = req.getParameter("action");
        String userIdStr = req.getParameter("userId");

        if (userIdStr != null && !userIdStr.trim().isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdStr.trim());

                if ("toggle".equalsIgnoreCase(action)) {
                    String currentStatus = req.getParameter("currentStatus");
                    boolean newStatus = !"true".equalsIgnoreCase(currentStatus);
                    adminService.toggleUserStatus(userId, newStatus);
                    session.setAttribute("flashSuccess", "Recruiter account status updated to " + (newStatus ? "ACTIVE" : "DEACTIVATED"));
                } else if ("delete".equalsIgnoreCase(action)) {
                    adminService.deleteUser(userId);
                    session.setAttribute("flashSuccess", "Recruiter user and company profile deleted.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing recruiter user action", e);
                session.setAttribute("flashError", "Failed to update recruiter status: " + e.getMessage());
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/recruiters");
    }
}

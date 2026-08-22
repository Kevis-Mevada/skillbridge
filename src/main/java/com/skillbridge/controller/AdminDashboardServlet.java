package com.skillbridge.controller;

import com.skillbridge.model.AdminDashboardStats;
import com.skillbridge.model.Job;
import com.skillbridge.model.User;
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
 * AdminDashboardServlet serves overall system statistics and governance metrics.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminDashboardServlet.class.getName());
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminServiceImpl();
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
            AdminDashboardStats stats = adminService.getDashboardStats();
            List<Job> recentJobs = adminService.getAllJobs();

            req.setAttribute("stats", stats);
            req.setAttribute("recentJobs", recentJobs);

            req.getRequestDispatcher("/WEB-INF/views/admin/admin-dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading admin dashboard", e);
            req.setAttribute("errorMessage", "Unable to load administration dashboard.");
            req.getRequestDispatcher("/WEB-INF/views/admin/admin-dashboard.jsp").forward(req, resp);
        }
    }
}

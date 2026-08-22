package com.skillbridge.controller;

import com.skillbridge.model.Application;
import com.skillbridge.service.AdminService;
import com.skillbridge.service.AdminServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AdminApplicationsServlet provides governance overview of all applications submitted across the system.
 */
@WebServlet(name = "AdminApplicationsServlet", urlPatterns = {"/admin/applications"})
public class AdminApplicationsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminApplicationsServlet.class.getName());
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Application> allApplications = adminService.getAllApplications();
            req.setAttribute("applications", allApplications);
            req.getRequestDispatcher("/WEB-INF/views/admin/manage-applications.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving platform applications", e);
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }
}

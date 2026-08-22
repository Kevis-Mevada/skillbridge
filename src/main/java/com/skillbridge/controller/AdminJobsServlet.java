package com.skillbridge.controller;

import com.skillbridge.model.Job;
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
 * AdminJobsServlet manages all opportunity postings across the platform.
 */
@WebServlet(name = "AdminJobsServlet", urlPatterns = {"/admin/jobs"})
public class AdminJobsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminJobsServlet.class.getName());
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        try {
            List<Job> allJobs = adminService.getAllJobs();
            req.setAttribute("jobs", allJobs);

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }
            if (session.getAttribute("flashError") != null) {
                req.setAttribute("errorMessage", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            req.getRequestDispatcher("/WEB-INF/views/admin/manage-jobs.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading platform jobs", e);
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        String action = req.getParameter("action");
        String jobIdStr = req.getParameter("jobId");

        if (jobIdStr != null && !jobIdStr.trim().isEmpty()) {
            try {
                int jobId = Integer.parseInt(jobIdStr.trim());

                if ("toggle".equalsIgnoreCase(action)) {
                    String currentStatus = req.getParameter("currentStatus");
                    boolean newStatus = !"true".equalsIgnoreCase(currentStatus);
                    adminService.toggleJobStatus(jobId, newStatus);
                    session.setAttribute("flashSuccess", "Job posting status changed to " + (newStatus ? "ACTIVE" : "CLOSED"));
                } else if ("delete".equalsIgnoreCase(action)) {
                    adminService.deleteJob(jobId);
                    session.setAttribute("flashSuccess", "Inappropriate job posting removed by Admin.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error moderating job posting", e);
                session.setAttribute("flashError", "Failed to update job status.");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/jobs");
    }
}

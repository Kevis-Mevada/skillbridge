package com.skillbridge.controller;

import com.skillbridge.model.StudentDashboardStats;
import com.skillbridge.model.StudentProfile;
import com.skillbridge.model.User;
import com.skillbridge.service.StudentService;
import com.skillbridge.service.StudentServiceImpl;
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
 * StudentDashboardServlet serves the student overview dashboard.
 */
@WebServlet(name = "StudentDashboardServlet", urlPatterns = {"/student/dashboard"})
public class StudentDashboardServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StudentDashboardServlet.class.getName());
    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        this.studentService = new StudentServiceImpl();
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
            Optional<StudentProfile> optProfile = studentService.getProfileByUserId(user.getId());
            if (optProfile.isPresent()) {
                StudentProfile profile = optProfile.get();
                StudentDashboardStats stats = studentService.getDashboardStats(profile.getId());

                req.setAttribute("profile", profile);
                req.setAttribute("stats", stats);
            }

            req.getRequestDispatcher("/WEB-INF/views/student/student-dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading student dashboard for userId: " + user.getId(), e);
            req.setAttribute("errorMessage", "Unable to load dashboard data. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/student/student-dashboard.jsp").forward(req, resp);
        }
    }
}

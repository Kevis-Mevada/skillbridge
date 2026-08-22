package com.skillbridge.controller;

import com.skillbridge.model.Role;
import com.skillbridge.model.User;
import com.skillbridge.service.UserService;
import com.skillbridge.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RegisterServlet handles student and recruiter user registration.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            User user = (User) session.getAttribute("currentUser");
            redirectToDashboard(user, resp, req.getContextPath());
            return;
        }

        String requestedRole = req.getParameter("role");
        req.setAttribute("selectedRole", requestedRole != null ? requestedRole.toUpperCase() : "STUDENT");
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String roleStr = req.getParameter("role");
        String phone = req.getParameter("phone");
        String companyName = req.getParameter("companyName");

        // Preserve input for redisplay in case of errors
        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);
        req.setAttribute("companyName", companyName);
        req.setAttribute("selectedRole", roleStr != null ? roleStr.toUpperCase() : "STUDENT");

        // Basic client-side/server-side parity validations
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            req.setAttribute("errorMessage", "All required fields must be filled.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Passwords do not match. Please re-enter.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        Role role = Role.fromString(roleStr);
        if (role == Role.ADMIN) {
            // Admin accounts cannot be self-registered via public form
            role = Role.STUDENT;
        }

        if (role == Role.RECRUITER && (companyName == null || companyName.trim().isEmpty())) {
            req.setAttribute("errorMessage", "Company name is required for recruiter registration.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            User newUser = new User(email, null, fullName, role, phone);
            userService.register(newUser, password, companyName);

            // Set success flash message and redirect to login
            HttpSession session = req.getSession(true);
            session.setAttribute("flashSuccess", "Registration successful! You can now log in with your credentials.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Registration server error", e);
            req.setAttribute("errorMessage", "An error occurred during registration. Please try again later.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    private void redirectToDashboard(User user, HttpServletResponse resp, String contextPath) throws IOException {
        switch (user.getRole()) {
            case STUDENT:
                resp.sendRedirect(contextPath + "/student/dashboard");
                break;
            case RECRUITER:
                resp.sendRedirect(contextPath + "/recruiter/dashboard");
                break;
            case ADMIN:
                resp.sendRedirect(contextPath + "/admin/dashboard");
                break;
        }
    }
}

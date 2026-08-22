package com.skillbridge.controller;

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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LoginServlet handles user authentication and session creation.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
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

        // Pull flash message if set by register or logout
        if (session != null && session.getAttribute("flashSuccess") != null) {
            req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
            session.removeAttribute("flashSuccess");
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        req.setAttribute("email", email);

        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("errorMessage", "Please provide both email and password.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            Optional<User> authResult = userService.authenticate(email, password);

            if (authResult.isPresent()) {
                User user = authResult.get();

                // Prevent Session Fixation by creating a clean session
                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("currentUser", user);
                newSession.setAttribute("userId", user.getId());
                newSession.setAttribute("userEmail", user.getEmail());
                newSession.setAttribute("userRole", user.getRole().name());
                newSession.setAttribute("userName", user.getFullName());

                LOGGER.info("User logged in successfully: " + user.getEmail() + " (" + user.getRole() + ")");
                redirectToDashboard(user, resp, req.getContextPath());
            } else {
                req.setAttribute("errorMessage", "Invalid email or password.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (IllegalStateException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login error", e);
            req.setAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
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

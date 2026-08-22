package com.skillbridge.controller;

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
 * RecruiterProfileServlet manages company information and recruiter contact settings.
 */
@WebServlet(name = "RecruiterProfileServlet", urlPatterns = {"/recruiter/profile"})
public class RecruiterProfileServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RecruiterProfileServlet.class.getName());
    private RecruiterService recruiterService;

    @Override
    public void init() throws ServletException {
        this.recruiterService = new RecruiterServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        try {
            Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());
            optProfile.ifPresent(profile -> req.setAttribute("profile", profile));

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }

            req.getRequestDispatcher("/WEB-INF/views/recruiter/recruiter-profile.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading recruiter profile", e);
            resp.sendRedirect(req.getContextPath() + "/recruiter/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String companyName = req.getParameter("companyName");
        String companyWebsite = req.getParameter("companyWebsite");
        String companyDescription = req.getParameter("companyDescription");
        String companySize = req.getParameter("companySize");
        String industry = req.getParameter("industry");
        String location = req.getParameter("location");

        try {
            Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());
            if (optProfile.isPresent()) {
                RecruiterProfile profile = optProfile.get();
                profile.setCompanyName(companyName);
                profile.setCompanyWebsite(companyWebsite);
                profile.setCompanyDescription(companyDescription);
                profile.setCompanySize(companySize);
                profile.setIndustry(industry);
                profile.setLocation(location);

                recruiterService.updateProfile(profile, fullName, phone);

                // Update session
                user.setFullName(fullName);
                user.setPhone(phone);
                session.setAttribute("currentUser", user);
                session.setAttribute("userName", fullName);

                session.setAttribute("flashSuccess", "Company and recruiter profile updated successfully!");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating recruiter profile", e);
            session.setAttribute("flashError", "Failed to update profile.");
        }

        resp.sendRedirect(req.getContextPath() + "/recruiter/profile");
    }
}

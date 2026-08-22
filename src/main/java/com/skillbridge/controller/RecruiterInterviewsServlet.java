package com.skillbridge.controller;

import com.skillbridge.model.Interview;
import com.skillbridge.model.InterviewStatus;
import com.skillbridge.model.User;
import com.skillbridge.service.InterviewService;
import com.skillbridge.service.InterviewServiceImpl;
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
 * RecruiterInterviewsServlet manages recruiter's scheduled interview rounds.
 */
@WebServlet(name = "RecruiterInterviewsServlet", urlPatterns = {"/recruiter/interviews"})
public class RecruiterInterviewsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RecruiterInterviewsServlet.class.getName());
    private InterviewService interviewService;

    @Override
    public void init() throws ServletException {
        this.interviewService = new InterviewServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        try {
            List<Interview> interviews = interviewService.getInterviewsForRecruiter(user.getId());
            req.setAttribute("interviews", interviews);

            if (session.getAttribute("flashSuccess") != null) {
                req.setAttribute("successMessage", session.getAttribute("flashSuccess"));
                session.removeAttribute("flashSuccess");
            }
            if (session.getAttribute("flashError") != null) {
                req.setAttribute("errorMessage", session.getAttribute("flashError"));
                session.removeAttribute("flashError");
            }

            req.getRequestDispatcher("/WEB-INF/views/recruiter/interviews.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading recruiter interviews", e);
            resp.sendRedirect(req.getContextPath() + "/recruiter/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String interviewIdStr = req.getParameter("interviewId");
        String statusStr = req.getParameter("status");

        try {
            int interviewId = Integer.parseInt(interviewIdStr.trim());
            InterviewStatus status = InterviewStatus.fromString(statusStr);

            interviewService.updateStatus(interviewId, status, user.getId());
            session.setAttribute("flashSuccess", "Interview status marked as " + status.getDisplayName() + "!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating interview status", e);
            session.setAttribute("flashError", "Failed to update interview status.");
        }

        resp.sendRedirect(req.getContextPath() + "/recruiter/interviews");
    }
}

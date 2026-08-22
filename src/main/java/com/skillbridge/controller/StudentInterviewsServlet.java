package com.skillbridge.controller;

import com.skillbridge.model.Interview;
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
 * StudentInterviewsServlet serves the student's interview schedule and meeting links.
 */
@WebServlet(name = "StudentInterviewsServlet", urlPatterns = {"/student/interviews"})
public class StudentInterviewsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StudentInterviewsServlet.class.getName());
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
            List<Interview> interviews = interviewService.getInterviewsForStudent(user.getId());
            req.setAttribute("interviews", interviews);

            req.getRequestDispatcher("/WEB-INF/views/student/interviews.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading student interviews", e);
            resp.sendRedirect(req.getContextPath() + "/student/dashboard");
        }
    }
}

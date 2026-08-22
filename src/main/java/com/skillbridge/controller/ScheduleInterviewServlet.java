package com.skillbridge.controller;

import com.skillbridge.model.*;
import com.skillbridge.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ScheduleInterviewServlet allows recruiters to schedule interview rounds for candidates.
 */
@WebServlet(name = "ScheduleInterviewServlet", urlPatterns = {"/recruiter/schedule-interview"})
public class ScheduleInterviewServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ScheduleInterviewServlet.class.getName());
    private InterviewService interviewService;
    private ApplicationService applicationService;

    @Override
    public void init() throws ServletException {
        this.interviewService = new InterviewServiceImpl();
        this.applicationService = new ApplicationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String appIdStr = req.getParameter("applicationId");
        if (appIdStr == null || appIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/recruiter/applicants");
            return;
        }

        try {
            int applicationId = Integer.parseInt(appIdStr.trim());
            Optional<Application> optionalApp = applicationService.getApplicationById(applicationId);

            if (optionalApp.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/recruiter/applicants");
                return;
            }

            req.setAttribute("application", optionalApp.get());
            req.getRequestDispatcher("/WEB-INF/views/recruiter/schedule-interview.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading schedule interview view", e);
            resp.sendRedirect(req.getContextPath() + "/recruiter/applicants");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String appIdStr = req.getParameter("applicationId");
        String interviewDateStr = req.getParameter("interviewDate");
        String interviewTimeStr = req.getParameter("interviewTime");
        String interviewModeStr = req.getParameter("interviewMode");
        String meetingLinkOrLocation = req.getParameter("meetingLinkOrLocation");
        String roundName = req.getParameter("roundName");
        String instructions = req.getParameter("instructions");

        try {
            int applicationId = Integer.parseInt(appIdStr.trim());
            Date interviewDate = Date.valueOf(interviewDateStr.trim());
            
            // Format time string to HH:mm:ss if only HH:mm is provided
            String formattedTime = interviewTimeStr.trim();
            if (formattedTime.length() == 5) {
                formattedTime += ":00";
            }
            Time interviewTime = Time.valueOf(formattedTime);

            Interview interview = new Interview();
            interview.setApplicationId(applicationId);
            interview.setInterviewDate(interviewDate);
            interview.setInterviewTime(interviewTime);
            interview.setInterviewMode(InterviewMode.fromString(interviewModeStr));
            interview.setMeetingLinkOrLocation(meetingLinkOrLocation);
            interview.setRoundName(roundName != null ? roundName.trim() : "Technical Round 1");
            interview.setInstructions(instructions);
            interview.setStatus(InterviewStatus.SCHEDULED);

            interviewService.scheduleInterview(interview, user.getId());

            session.setAttribute("flashSuccess", "Interview scheduled successfully and student application status updated!");
            resp.sendRedirect(req.getContextPath() + "/recruiter/interviews");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error scheduling interview", e);
            session.setAttribute("flashError", "Failed to schedule interview: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/recruiter/schedule-interview?applicationId=" + appIdStr);
        }
    }
}

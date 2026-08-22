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
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PostJobServlet handles new internship and job posting creation.
 */
@WebServlet(name = "PostJobServlet", urlPatterns = {"/recruiter/post-job"})
public class PostJobServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(PostJobServlet.class.getName());
    private JobService jobService;
    private RecruiterService recruiterService;

    @Override
    public void init() throws ServletException {
        this.jobService = new JobServiceImpl();
        this.recruiterService = new RecruiterServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Skill> masterSkills = jobService.getAllSkills();
            req.setAttribute("masterSkills", masterSkills);
            req.getRequestDispatcher("/WEB-INF/views/recruiter/post-job.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading post job view", e);
            resp.sendRedirect(req.getContextPath() + "/recruiter/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String requirements = req.getParameter("requirements");
        String responsibilities = req.getParameter("responsibilities");
        String jobTypeStr = req.getParameter("jobType");
        String location = req.getParameter("location");
        String experienceLevelStr = req.getParameter("experienceLevel");
        String salaryMinStr = req.getParameter("salaryMin");
        String salaryMaxStr = req.getParameter("salaryMax");
        String vacanciesStr = req.getParameter("vacancies");
        String deadlineStr = req.getParameter("deadline");
        String[] skillIdsArray = req.getParameterValues("skillIds");

        try {
            Optional<RecruiterProfile> optProfile = recruiterService.getProfileByUserId(user.getId());
            if (optProfile.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/recruiter/dashboard");
                return;
            }

            Job job = new Job();
            job.setRecruiterId(optProfile.get().getId());
            job.setTitle(title);
            job.setDescription(description);
            job.setRequirements(requirements);
            job.setResponsibilities(responsibilities);
            job.setJobType(JobType.fromString(jobTypeStr));
            job.setLocation(location);
            job.setExperienceLevel(ExperienceLevel.fromDbValue(experienceLevelStr));

            if (salaryMinStr != null && !salaryMinStr.trim().isEmpty()) {
                job.setSalaryMin(new BigDecimal(salaryMinStr.trim()));
            }
            if (salaryMaxStr != null && !salaryMaxStr.trim().isEmpty()) {
                job.setSalaryMax(new BigDecimal(salaryMaxStr.trim()));
            }

            int vacancies = 1;
            if (vacanciesStr != null && !vacanciesStr.trim().isEmpty()) {
                try {
                    vacancies = Integer.parseInt(vacanciesStr.trim());
                } catch (NumberFormatException ignored) {}
            }
            job.setVacancies(vacancies);

            if (deadlineStr != null && !deadlineStr.trim().isEmpty()) {
                job.setDeadline(Date.valueOf(deadlineStr.trim()));
            }

            job.setActive(true);

            List<Integer> skillIds = new ArrayList<>();
            if (skillIdsArray != null) {
                for (String sId : skillIdsArray) {
                    try {
                        skillIds.add(Integer.parseInt(sId.trim()));
                    } catch (NumberFormatException ignored) {}
                }
            }

            jobService.postJob(job, skillIds);
            session.setAttribute("flashSuccess", "Job posting published successfully!");
            resp.sendRedirect(req.getContextPath() + "/recruiter/manage-jobs");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error posting job", e);
            req.setAttribute("errorMessage", "Failed to create job posting: " + e.getMessage());
            doGet(req, resp);
        }
    }
}

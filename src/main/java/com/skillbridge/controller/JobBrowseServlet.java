package com.skillbridge.controller;

import com.skillbridge.model.*;
import com.skillbridge.service.JobService;
import com.skillbridge.service.JobServiceImpl;
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
 * JobBrowseServlet serves the job and internship search page with multi-criteria filtering.
 */
@WebServlet(name = "JobBrowseServlet", urlPatterns = {"/jobs", "/student/jobs"})
public class JobBrowseServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(JobBrowseServlet.class.getName());
    private JobService jobService;

    @Override
    public void init() throws ServletException {
        this.jobService = new JobServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String keyword = req.getParameter("keyword");
            String jobTypeStr = req.getParameter("type");
            String expLevelStr = req.getParameter("experience");
            String location = req.getParameter("location");
            String skillIdStr = req.getParameter("skillId");
            String pageStr = req.getParameter("page");

            JobFilterCriteria criteria = new JobFilterCriteria();
            criteria.setKeyword(keyword);

            if (jobTypeStr != null && !jobTypeStr.trim().isEmpty()) {
                try {
                    criteria.setJobType(JobType.valueOf(jobTypeStr.trim().toUpperCase()));
                } catch (IllegalArgumentException ignored) {}
            }

            if (expLevelStr != null && !expLevelStr.trim().isEmpty()) {
                criteria.setExperienceLevel(ExperienceLevel.fromDbValue(expLevelStr));
            }

            criteria.setLocation(location);

            if (skillIdStr != null && !skillIdStr.trim().isEmpty()) {
                try {
                    criteria.setSkillId(Integer.parseInt(skillIdStr.trim()));
                } catch (NumberFormatException ignored) {}
            }

            int page = 1;
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageStr.trim());
                } catch (NumberFormatException ignored) {}
            }
            criteria.setPage(page);
            criteria.setLimit(9); // 9 cards per page for a 3x3 grid

            List<Job> jobList = jobService.searchJobs(criteria);
            int totalJobs = jobService.countJobs(criteria);
            int totalPages = (int) Math.ceil((double) totalJobs / criteria.getLimit());
            List<Skill> allSkills = jobService.getAllSkills();

            req.setAttribute("jobList", jobList);
            req.setAttribute("totalJobs", totalJobs);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("allSkills", allSkills);
            req.setAttribute("criteria", criteria);

            req.getRequestDispatcher("/jobs.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching jobs", e);
            req.setAttribute("errorMessage", "Failed to retrieve job listings.");
            req.getRequestDispatcher("/jobs.jsp").forward(req, resp);
        }
    }
}

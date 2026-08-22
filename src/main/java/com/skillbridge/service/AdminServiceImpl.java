package com.skillbridge.service;

import com.skillbridge.dao.*;
import com.skillbridge.model.*;
import com.skillbridge.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AdminServiceImpl implements AdminService.
 */
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = Logger.getLogger(AdminServiceImpl.class.getName());

    private final UserDAO userDAO;
    private final StudentProfileDAO studentProfileDAO;
    private final RecruiterDAO recruiterDAO;
    private final JobDAO jobDAO;
    private final ApplicationDAO applicationDAO;
    private final InterviewDAO interviewDAO;

    public AdminServiceImpl() {
        this.userDAO = new UserDAOImpl();
        this.studentProfileDAO = new StudentProfileDAOImpl();
        this.recruiterDAO = new RecruiterDAOImpl();
        this.jobDAO = new JobDAOImpl();
        this.applicationDAO = new ApplicationDAOImpl();
        this.interviewDAO = new InterviewDAOImpl();
    }

    @Override
    public AdminDashboardStats getDashboardStats() throws Exception {
        AdminDashboardStats stats = new AdminDashboardStats();

        String sql = 
            "SELECT " +
            "  (SELECT COUNT(*) FROM users WHERE role = 'STUDENT') as total_students, " +
            "  (SELECT COUNT(*) FROM users WHERE role = 'RECRUITER') as total_recruiters, " +
            "  (SELECT COUNT(*) FROM jobs) as total_jobs, " +
            "  (SELECT COUNT(*) FROM jobs WHERE is_active = TRUE) as active_jobs, " +
            "  (SELECT COUNT(*) FROM applications) as total_apps, " +
            "  (SELECT COUNT(*) FROM interviews) as total_interviews";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                stats.setTotalStudents(rs.getInt("total_students"));
                stats.setTotalRecruiters(rs.getInt("total_recruiters"));
                stats.setTotalJobs(rs.getInt("total_jobs"));
                stats.setActiveJobs(rs.getInt("active_jobs"));
                stats.setTotalApplications(rs.getInt("total_apps"));
                stats.setTotalInterviews(rs.getInt("total_interviews"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving admin dashboard stats", e);
        }

        return stats;
    }

    @Override
    public List<StudentProfile> getAllStudents() throws Exception {
        return studentProfileDAO.findAll();
    }

    @Override
    public List<RecruiterProfile> getAllRecruiters() throws Exception {
        return recruiterDAO.findAll();
    }

    @Override
    public List<Job> getAllJobs() throws Exception {
        JobFilterCriteria criteria = new JobFilterCriteria();
        criteria.setActiveOnly(false); // include closed jobs as well
        criteria.setLimit(100);
        return jobDAO.searchJobs(criteria);
    }

    @Override
    public List<Application> getAllApplications() throws Exception {
        return applicationDAO.findAll();
    }

    @Override
    public boolean toggleUserStatus(int userId, boolean active) throws Exception {
        return userDAO.updateStatus(userId, active);
    }

    @Override
    public boolean deleteUser(int userId) throws Exception {
        return userDAO.delete(userId);
    }

    @Override
    public boolean toggleJobStatus(int jobId, boolean active) throws Exception {
        return jobDAO.setJobActiveStatus(jobId, 0, active);
    }

    @Override
    public boolean deleteJob(int jobId) throws Exception {
        return jobDAO.delete(jobId, 0);
    }
}

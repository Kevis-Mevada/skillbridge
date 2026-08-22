package com.skillbridge.dao;

import com.skillbridge.model.*;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JobDAOImpl implements JobDAO with secure PreparedStatement filtering and joins.
 */
public class JobDAOImpl implements JobDAO {

    private static final Logger LOGGER = Logger.getLogger(JobDAOImpl.class.getName());

    private static final String SQL_BASE_SELECT = 
        "SELECT j.*, rp.company_name, rp.company_website, rp.company_logo, rp.company_description, rp.location as company_location, " +
        "u.full_name as recruiter_name, u.email as recruiter_email, " +
        "(SELECT COUNT(*) FROM applications a WHERE a.job_id = j.id) as applicant_count " +
        "FROM jobs j " +
        "JOIN recruiter_profiles rp ON j.recruiter_id = rp.id " +
        "JOIN users u ON rp.user_id = u.id ";

    @Override
    public Job save(Job job, List<Integer> skillIds) throws SQLException {
        String sqlInsert = 
            "INSERT INTO jobs (recruiter_id, title, description, requirements, responsibilities, job_type, location, experience_level, salary_min, salary_max, vacancies, deadline, is_active) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, job.getRecruiterId());
                ps.setString(2, job.getTitle().trim());
                ps.setString(3, job.getDescription().trim());
                ps.setString(4, job.getRequirements());
                ps.setString(5, job.getResponsibilities());
                ps.setString(6, job.getJobType().name());
                ps.setString(7, job.getLocation().trim());
                ps.setString(8, job.getExperienceLevel().toDbValue());
                ps.setBigDecimal(9, job.getSalaryMin());
                ps.setBigDecimal(10, job.getSalaryMax());
                ps.setInt(11, job.getVacancies());
                ps.setDate(12, job.getDeadline());
                ps.setBoolean(13, job.isActive());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        job.setId(rs.getInt("id"));
                        job.setCreatedAt(rs.getTimestamp("created_at"));
                        job.setUpdatedAt(rs.getTimestamp("updated_at"));
                    }
                }
            }

            // Insert required skills
            if (skillIds != null && !skillIds.isEmpty() && job.getId() > 0) {
                String sqlSkill = "INSERT INTO job_skills (job_id, skill_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
                try (PreparedStatement psSkill = conn.prepareStatement(sqlSkill)) {
                    for (Integer sId : skillIds) {
                        if (sId != null && sId > 0) {
                            psSkill.setInt(1, job.getId());
                            psSkill.setInt(2, sId);
                            psSkill.addBatch();
                        }
                    }
                    psSkill.executeBatch();
                }
            }

            conn.commit();
            return job;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting job posting: " + job.getTitle(), e);
            throw e;
        }
    }

    @Override
    public boolean update(Job job, List<Integer> skillIds) throws SQLException {
        String sqlUpdate = 
            "UPDATE jobs SET title = ?, description = ?, requirements = ?, responsibilities = ?, job_type = ?, location = ?, " +
            "experience_level = ?, salary_min = ?, salary_max = ?, vacancies = ?, deadline = ?, is_active = ?, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = ? AND recruiter_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            boolean updated;
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setString(1, job.getTitle().trim());
                ps.setString(2, job.getDescription().trim());
                ps.setString(3, job.getRequirements());
                ps.setString(4, job.getResponsibilities());
                ps.setString(5, job.getJobType().name());
                ps.setString(6, job.getLocation().trim());
                ps.setString(7, job.getExperienceLevel().toDbValue());
                ps.setBigDecimal(8, job.getSalaryMin());
                ps.setBigDecimal(9, job.getSalaryMax());
                ps.setInt(10, job.getVacancies());
                ps.setDate(11, job.getDeadline());
                ps.setBoolean(12, job.isActive());
                ps.setInt(13, job.getId());
                ps.setInt(14, job.getRecruiterId());

                updated = ps.executeUpdate() > 0;
            }

            if (updated && skillIds != null) {
                // Delete old skills and re-insert
                try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM job_skills WHERE job_id = ?")) {
                    psDel.setInt(1, job.getId());
                    psDel.executeUpdate();
                }

                String sqlSkill = "INSERT INTO job_skills (job_id, skill_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
                try (PreparedStatement psSkill = conn.prepareStatement(sqlSkill)) {
                    for (Integer sId : skillIds) {
                        if (sId != null && sId > 0) {
                            psSkill.setInt(1, job.getId());
                            psSkill.setInt(2, sId);
                            psSkill.addBatch();
                        }
                    }
                    psSkill.executeBatch();
                }
            }

            conn.commit();
            return updated;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating job ID: " + job.getId(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(int jobId, int recruiterId) throws SQLException {
        String sql = (recruiterId > 0) 
            ? "DELETE FROM jobs WHERE id = ? AND recruiter_id = ?" 
            : "DELETE FROM jobs WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            if (recruiterId > 0) {
                ps.setInt(2, recruiterId);
            }
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Job> findById(int id) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE j.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Job job = mapResultSetToJob(rs);
                    job.setRequiredSkills(getJobSkills(job.getId()));
                    return Optional.of(job);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Job> findByRecruiterId(int recruiterId) throws SQLException {
        String sql = SQL_BASE_SELECT + "WHERE j.recruiter_id = ? ORDER BY j.created_at DESC";
        List<Job> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recruiterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = mapResultSetToJob(rs);
                    job.setRequiredSkills(getJobSkills(job.getId()));
                    list.add(job);
                }
            }
        }
        return list;
    }

    @Override
    public List<Job> searchJobs(JobFilterCriteria criteria) throws SQLException {
        List<Job> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQL_BASE_SELECT + "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        buildWhereConditions(criteria, sql, params);

        sql.append("ORDER BY j.created_at DESC LIMIT ? OFFSET ?");
        params.add(criteria.getLimit());
        params.add(criteria.getOffset());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = mapResultSetToJob(rs);
                    job.setRequiredSkills(getJobSkills(job.getId()));
                    list.add(job);
                }
            }
        }
        return list;
    }

    @Override
    public int countJobs(JobFilterCriteria criteria) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM jobs j JOIN recruiter_profiles rp ON j.recruiter_id = rp.id WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        buildWhereConditions(criteria, sql, params);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private void buildWhereConditions(JobFilterCriteria criteria, StringBuilder sql, List<Object> params) {
        if (criteria == null) return;

        if (criteria.getActiveOnly() != null && criteria.getActiveOnly()) {
            sql.append("AND j.is_active = TRUE ");
        }

        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            sql.append("AND (LOWER(j.title) LIKE ? OR LOWER(j.description) LIKE ? OR LOWER(rp.company_name) LIKE ?) ");
            String kw = "%" + criteria.getKeyword().trim().toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        if (criteria.getJobType() != null) {
            sql.append("AND j.job_type = ? ");
            params.add(criteria.getJobType().name());
        }

        if (criteria.getExperienceLevel() != null) {
            sql.append("AND j.experience_level = ? ");
            params.add(criteria.getExperienceLevel().toDbValue());
        }

        if (criteria.getLocation() != null && !criteria.getLocation().trim().isEmpty()) {
            sql.append("AND LOWER(j.location) LIKE ? ");
            params.add("%" + criteria.getLocation().trim().toLowerCase() + "%");
        }

        if (criteria.getSkillId() != null && criteria.getSkillId() > 0) {
            sql.append("AND EXISTS (SELECT 1 FROM job_skills js WHERE js.job_id = j.id AND js.skill_id = ?) ");
            params.add(criteria.getSkillId());
        }
    }

    @Override
    public List<Skill> getJobSkills(int jobId) throws SQLException {
        String sql = 
            "SELECT s.id, s.name, s.category FROM skills s " +
            "JOIN job_skills js ON s.id = js.skill_id " +
            "WHERE js.job_id = ? ORDER BY s.name ASC";

        List<Skill> skills = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    skills.add(new Skill(rs.getInt("id"), rs.getString("name"), rs.getString("category")));
                }
            }
        }
        return skills;
    }

    @Override
    public boolean setJobActiveStatus(int jobId, int recruiterId, boolean active) throws SQLException {
        String sql = (recruiterId > 0)
            ? "UPDATE jobs SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND recruiter_id = ?"
            : "UPDATE jobs SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setInt(2, jobId);
            if (recruiterId > 0) {
                ps.setInt(3, recruiterId);
            }
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int countTotalActiveJobs() throws SQLException {
        String sql = "SELECT COUNT(*) FROM jobs WHERE is_active = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public List<Job> findRecentJobs(int limit) throws SQLException {
        JobFilterCriteria criteria = new JobFilterCriteria();
        criteria.setActiveOnly(true);
        criteria.setLimit(limit);
        return searchJobs(criteria);
    }

    private Job mapResultSetToJob(ResultSet rs) throws SQLException {
        Job job = new Job();
        job.setId(rs.getInt("id"));
        job.setRecruiterId(rs.getInt("recruiter_id"));
        job.setTitle(rs.getString("title"));
        job.setDescription(rs.getString("description"));
        job.setRequirements(rs.getString("requirements"));
        job.setResponsibilities(rs.getString("responsibilities"));
        job.setJobType(JobType.fromString(rs.getString("job_type")));
        job.setLocation(rs.getString("location"));
        job.setExperienceLevel(ExperienceLevel.fromDbValue(rs.getString("experience_level")));
        job.setSalaryMin(rs.getBigDecimal("salary_min"));
        job.setSalaryMax(rs.getBigDecimal("salary_max"));
        job.setVacancies(rs.getInt("vacancies"));
        job.setDeadline(rs.getDate("deadline"));
        job.setActive(rs.getBoolean("is_active"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        job.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Join details
        job.setCompanyName(rs.getString("company_name"));
        job.setCompanyWebsite(rs.getString("company_website"));
        job.setCompanyLogo(rs.getString("company_logo"));
        job.setCompanyDescription(rs.getString("company_description"));
        job.setCompanyLocation(rs.getString("company_location"));
        job.setRecruiterName(rs.getString("recruiter_name"));
        job.setRecruiterEmail(rs.getString("recruiter_email"));
        job.setApplicantCount(rs.getInt("applicant_count"));

        return job;
    }
}

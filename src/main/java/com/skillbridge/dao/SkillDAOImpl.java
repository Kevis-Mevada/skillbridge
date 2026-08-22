package com.skillbridge.dao;

import com.skillbridge.model.Skill;
import com.skillbridge.model.StudentSkill;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SkillDAOImpl implements SkillDAO with safe PreparedStatement queries.
 */
public class SkillDAOImpl implements SkillDAO {

    private static final Logger LOGGER = Logger.getLogger(SkillDAOImpl.class.getName());

    private static final String SQL_INSERT_SKILL = 
        "INSERT INTO skills (name, category) VALUES (?, ?) ON CONFLICT (name) DO UPDATE SET category = EXCLUDED.category RETURNING id";

    private static final String SQL_FIND_BY_ID = 
        "SELECT id, name, category FROM skills WHERE id = ?";

    private static final String SQL_FIND_BY_NAME = 
        "SELECT id, name, category FROM skills WHERE LOWER(name) = LOWER(?)";

    private static final String SQL_FIND_ALL = 
        "SELECT id, name, category FROM skills ORDER BY category, name ASC";

    private static final String SQL_FIND_BY_STUDENT = 
        "SELECT ss.id, ss.student_id, ss.skill_id, ss.proficiency_level, s.name as skill_name, s.category " +
        "FROM student_skills ss " +
        "JOIN skills s ON ss.skill_id = s.id " +
        "WHERE ss.student_id = ? ORDER BY s.name ASC";

    private static final String SQL_ADD_STUDENT_SKILL = 
        "INSERT INTO student_skills (student_id, skill_id, proficiency_level) VALUES (?, ?, ?) " +
        "ON CONFLICT (student_id, skill_id) DO UPDATE SET proficiency_level = EXCLUDED.proficiency_level";

    private static final String SQL_REMOVE_STUDENT_SKILL = 
        "DELETE FROM student_skills WHERE student_id = ? AND skill_id = ?";

    private static final String SQL_EXISTS_STUDENT_SKILL = 
        "SELECT 1 FROM student_skills WHERE student_id = ? AND skill_id = ? LIMIT 1";

    @Override
    public Skill save(Skill skill) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_SKILL)) {

            ps.setString(1, skill.getName().trim());
            ps.setString(2, skill.getCategory() != null ? skill.getCategory().trim() : "Technical");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    skill.setId(rs.getInt("id"));
                    return skill;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting skill: " + skill.getName(), e);
            throw e;
        }
        return skill;
    }

    @Override
    public Optional<Skill> findById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Skill(rs.getInt("id"), rs.getString("name"), rs.getString("category")));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Skill> findByName(String name) throws SQLException {
        if (name == null) return Optional.empty();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_NAME)) {

            ps.setString(1, name.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Skill(rs.getInt("id"), rs.getString("name"), rs.getString("category")));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Skill> findAll() throws SQLException {
        List<Skill> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Skill(rs.getInt("id"), rs.getString("name"), rs.getString("category")));
            }
        }
        return list;
    }

    @Override
    public List<StudentSkill> findSkillsByStudentId(int studentProfileId) throws SQLException {
        List<StudentSkill> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_STUDENT)) {

            ps.setInt(1, studentProfileId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StudentSkill ss = new StudentSkill();
                    ss.setId(rs.getInt("id"));
                    ss.setStudentId(rs.getInt("student_id"));
                    ss.setSkillId(rs.getInt("skill_id"));
                    ss.setProficiencyLevel(rs.getString("proficiency_level"));
                    ss.setSkillName(rs.getString("skill_name"));
                    ss.setCategory(rs.getString("category"));
                    list.add(ss);
                }
            }
        }
        return list;
    }

    @Override
    public boolean addSkillToStudent(int studentProfileId, int skillId, String proficiencyLevel) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ADD_STUDENT_SKILL)) {

            ps.setInt(1, studentProfileId);
            ps.setInt(2, skillId);
            ps.setString(3, proficiencyLevel != null ? proficiencyLevel : "Intermediate");

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removeSkillFromStudent(int studentProfileId, int skillId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REMOVE_STUDENT_SKILL)) {

            ps.setInt(1, studentProfileId);
            ps.setInt(2, skillId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean studentHasSkill(int studentProfileId, int skillId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_STUDENT_SKILL)) {

            ps.setInt(1, studentProfileId);
            ps.setInt(2, skillId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

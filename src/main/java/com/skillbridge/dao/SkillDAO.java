package com.skillbridge.dao;

import com.skillbridge.model.Skill;
import com.skillbridge.model.StudentSkill;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * SkillDAO manages skill entities and student-skill associations.
 */
public interface SkillDAO {

    Skill save(Skill skill) throws SQLException;

    Optional<Skill> findById(int id) throws SQLException;

    Optional<Skill> findByName(String name) throws SQLException;

    List<Skill> findAll() throws SQLException;

    List<StudentSkill> findSkillsByStudentId(int studentProfileId) throws SQLException;

    boolean addSkillToStudent(int studentProfileId, int skillId, String proficiencyLevel) throws SQLException;

    boolean removeSkillFromStudent(int studentProfileId, int skillId) throws SQLException;

    boolean studentHasSkill(int studentProfileId, int skillId) throws SQLException;
}

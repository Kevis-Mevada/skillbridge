package com.skillbridge.model;

import java.io.Serializable;

/**
 * StudentSkill entity representing the many-to-many relationship between student and skill.
 */
public class StudentSkill implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int studentId;
    private int skillId;
    private String proficiencyLevel; // Beginner, Intermediate, Advanced, Expert

    // Joined skill metadata
    private String skillName;
    private String category;

    public StudentSkill() {}

    public StudentSkill(int studentId, int skillId, String proficiencyLevel) {
        this.studentId = studentId;
        this.skillId = skillId;
        this.proficiencyLevel = proficiencyLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

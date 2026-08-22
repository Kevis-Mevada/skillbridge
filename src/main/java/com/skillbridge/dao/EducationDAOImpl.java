package com.skillbridge.dao;

import com.skillbridge.model.Education;
import com.skillbridge.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EducationDAOImpl implements EducationDAO using JDBC PreparedStatement.
 */
public class EducationDAOImpl implements EducationDAO {

    private static final Logger LOGGER = Logger.getLogger(EducationDAOImpl.class.getName());

    private static final String SQL_INSERT = 
        "INSERT INTO education (student_id, institution, degree, field_of_study, start_date, end_date, grade_percentage) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";

    private static final String SQL_UPDATE = 
        "UPDATE education SET institution = ?, degree = ?, field_of_study = ?, start_date = ?, end_date = ?, grade_percentage = ? " +
        "WHERE id = ? AND student_id = ?";

    private static final String SQL_DELETE = 
        "DELETE FROM education WHERE id = ? AND student_id = ?";

    private static final String SQL_FIND_BY_ID = 
        "SELECT id, student_id, institution, degree, field_of_study, start_date, end_date, grade_percentage, created_at " +
        "FROM education WHERE id = ?";

    private static final String SQL_FIND_BY_STUDENT = 
        "SELECT id, student_id, institution, degree, field_of_study, start_date, end_date, grade_percentage, created_at " +
        "FROM education WHERE student_id = ? ORDER BY COALESCE(end_date, CURRENT_DATE) DESC, id DESC";

    @Override
    public Education save(Education education) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, education.getStudentId());
            ps.setString(2, education.getInstitution().trim());
            ps.setString(3, education.getDegree().trim());
            ps.setString(4, education.getFieldOfStudy().trim());
            ps.setDate(5, education.getStartDate());
            ps.setDate(6, education.getEndDate());
            if (education.getGradePercentage() != null) {
                ps.setBigDecimal(7, education.getGradePercentage());
            } else {
                ps.setNull(7, Types.NUMERIC);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    education.setId(rs.getInt("id"));
                    education.setCreatedAt(rs.getTimestamp("created_at"));
                    return education;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting education record for studentId: " + education.getStudentId(), e);
            throw e;
        }
        return education;
    }

    @Override
    public boolean update(Education education) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, education.getInstitution().trim());
            ps.setString(2, education.getDegree().trim());
            ps.setString(3, education.getFieldOfStudy().trim());
            ps.setDate(4, education.getStartDate());
            ps.setDate(5, education.getEndDate());
            if (education.getGradePercentage() != null) {
                ps.setBigDecimal(6, education.getGradePercentage());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }
            ps.setInt(7, education.getId());
            ps.setInt(8, education.getStudentId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id, int studentProfileId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            ps.setInt(2, studentProfileId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Education> findById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEducation(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Education> findByStudentProfileId(int studentProfileId) throws SQLException {
        List<Education> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_STUDENT)) {

            ps.setInt(1, studentProfileId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEducation(rs));
                }
            }
        }
        return list;
    }

    private Education mapResultSetToEducation(ResultSet rs) throws SQLException {
        Education edu = new Education();
        edu.setId(rs.getInt("id"));
        edu.setStudentId(rs.getInt("student_id"));
        edu.setInstitution(rs.getString("institution"));
        edu.setDegree(rs.getString("degree"));
        edu.setFieldOfStudy(rs.getString("field_of_study"));
        edu.setStartDate(rs.getDate("start_date"));
        edu.setEndDate(rs.getDate("end_date"));
        edu.setGradePercentage(rs.getBigDecimal("grade_percentage"));
        edu.setCreatedAt(rs.getTimestamp("created_at"));
        return edu;
    }
}

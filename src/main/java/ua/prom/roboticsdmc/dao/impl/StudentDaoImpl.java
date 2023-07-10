package ua.prom.roboticsdmc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Student;

@Repository
public class StudentDaoImpl extends AbstractCrudDaoImpl<Integer, Student> implements StudentDao {

    private static final String SAVE_QUERY = "INSERT INTO school_app_schema.students (first_name, last_name, group_id) VALUES (?,?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school_app_schema.students WHERE student_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school_app_schema.students ORDER BY student_id ASC";
    private static final String FIND_ALL_PAGINATION_QUERY = "SELECT * FROM school_app_schema.students ORDER BY student_id ASC LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE school_app_schema.students SET first_name=?, last_name=?, group_id=? WHERE student_id=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school_app_schema.students WHERE student_id=?";
    private static final String FIND_STUDENTS_BY_COURSE_NAME_QUERY = "SELECT * FROM school_app_schema.students "
            + "INNER JOIN school_app_schema.students_courses "
            + "ON school_app_schema.students.student_id = school_app_schema.students_courses.student_id "
            + "INNER JOIN school_app_schema.courses "
            + "ON school_app_schema.students_courses.course_id = school_app_schema.courses.course_id "
            + "WHERE school_app_schema.courses.course_name = ? " + "ORDER BY school_app_schema.students.student_id ASC";

    public StudentDaoImpl(ConnectorDB connectorDB) {
        super(connectorDB, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
    }
    
    @Override
    protected Student mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Student.builder()
                .withStudentId(resultSet.getInt("student_id"))
                .withGroupId(resultSet.getInt("group_id"))
                .withFirstName(resultSet.getString("first_name"))
                .withLastName(resultSet.getString("last_name"))
                .build();
    }
    
    @Override
    protected void mapEntityToPreparedStatementSave(Student student, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, student.getFirstName());
        preparedStatement.setString(2, student.getLastName());
        preparedStatement.setInt(3, student.getGroupId());
    }
    
    @Override
    protected void mapEntityToPreparedStatementUpdate(Student student, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, student.getFirstName());
        preparedStatement.setString(2, student.getLastName());
        preparedStatement.setInt(3, student.getGroupId());
        preparedStatement.setInt(4, student.getStudentId());
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
 
        List<Student> courseStudents = new ArrayList<>();

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_STUDENTS_BY_COURSE_NAME_QUERY)) {

            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                courseStudents.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Can't get students who related to the course..", e);
        }
        return courseStudents;
    }
}

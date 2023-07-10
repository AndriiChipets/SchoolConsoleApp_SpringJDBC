package ua.prom.roboticsdmc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Course;

@Repository
public class CourseDaoImpl extends AbstractCrudDaoImpl<Integer, Course> implements CourseDao {

    private static final String SAVE_QUERY = "INSERT INTO school_app_schema.courses (course_name, course_description) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school_app_schema.courses WHERE course_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school_app_schema.courses ORDER BY course_id ASC";
    private static final String FIND_ALL_PEGINATION_QUERY = "SELECT * FROM school_app_schema.courses ORDER BY course_id ASC LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE school_app_schema.courses SET course_name=?, course_description=? WHERE course_id=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school_app_schema.courses WHERE course_id=?";
    private static final String GET_ALL_STUDENT_COURSES_BY_STUDENT_ID_QUERY = "SELECT * FROM school_app_schema.courses "
            + "INNER JOIN school_app_schema.students_courses "
            + "ON school_app_schema.courses.course_id = school_app_schema.students_courses.course_id "
            + "WHERE school_app_schema.students_courses.student_id = ? ";
    private static final String ADD_STUDENT_TO_COURSE_QUERY = "INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (?, ?)";
    private static final String DELETE_STUDENT_FROM_COURSE_QUERY = "DELETE FROM school_app_schema.students_courses WHERE student_id=? AND course_id = ?";;

    public CourseDaoImpl(ConnectorDB connectorDB) {
        super(connectorDB, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PEGINATION_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
    }

    @Override
    protected Course mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Course.builder().withCourseId(resultSet.getInt("course_id"))
                .withCourseName(resultSet.getString("course_name"))
                .withCourseDescription(resultSet.getString("course_description")).build();
    }

    @Override
    protected void mapEntityToPreparedStatementSave(Course course, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, course.getCourseName());
        preparedStatement.setString(2, course.getCourseDescription());
    }

    @Override
    protected void mapEntityToPreparedStatementUpdate(Course course, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, course.getCourseName());
        preparedStatement.setString(2, course.getCourseDescription());
        preparedStatement.setInt(3, course.getCourseId());
    }

    @Override
    public List<Course> getAllStudentCoursesByStudentID(Integer studentId) {

        List<Course> studentCourses = new ArrayList<>();

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(GET_ALL_STUDENT_COURSES_BY_STUDENT_ID_QUERY)) {

            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                studentCourses.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Can't get all courses for specified studentID..", e);
        }
        return studentCourses;
    }

    @Override
    public void addStudentToCourse(Integer studentId, Integer coursesId) {

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_STUDENT_TO_COURSE_QUERY)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, coursesId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Student is not added to the course..", e);
        }
    }

    @Override
    public void removeStudentFromCourse(Integer studentId, Integer courseId) {

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT_FROM_COURSE_QUERY)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Student is not deleted from the course..", e);
        }
    }
}

package ua.prom.roboticsdmc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Course;

public class CourseDaoImpl extends AbstractCrudDaoImpl<Integer, Course> implements CourseDao {

    private static final String SAVE_QUERY = "INSERT INTO school_app_schema.courses (course_name, course_description) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school_app_schema.courses WHERE course_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school_app_schema.courses ORDER BY course_id ASC";
    private static final String FIND_ALL_PEGINATION_QUERY = "SELECT * FROM school_app_schema.courses ORDER BY course_id ASC LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE school_app_schema.courses SET course_name=?, course_description=? WHERE course_id=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school_app_schema.courses WHERE course_id=?";

    public CourseDaoImpl(ConnectorDB connectorDB) {
        super(connectorDB, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PEGINATION_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
    }
    
    @Override
    protected Course mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Course.builder()
                .withCourseId(resultSet.getInt("course_id"))
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
        String sql = "SELECT * FROM school_app_schema.courses " + "INNER JOIN school_app_schema.students_courses "
                + "ON school_app_schema.courses.course_id = school_app_schema.students_courses.course_id "
                + "WHERE school_app_schema.students_courses.student_id = ? ";

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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

        String sql = "INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (?, ?)";

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, coursesId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Student is not added to the course..", e);
        }
    }

    @Override
    public void removeStudentFromCourse(Integer studentId, Integer courseId) {

        String sql = "DELETE FROM school_app_schema.students_courses WHERE student_id=? AND course_id = ?";

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Student is not deleted from the course..", e);
        }
    }
    
    @Override
    public void fillRandomStudentCourseTable(List<List<Integer>> studentIdCoursesId) {

        String sql = "INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (?, ?)";

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < studentIdCoursesId.size(); i++) {
                List<Integer> coursesId = studentIdCoursesId.get(i);
                for (int j = 0; j < coursesId.size(); j++) {
                    preparedStatement.setInt(1, i + 1);
                    preparedStatement.setInt(2, coursesId.get(j));
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("CourseID and studentID are not added to the students_course table..",
                    e);
        }
    }
}

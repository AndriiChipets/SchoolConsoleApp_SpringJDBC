package ua.prom.roboticsdmc.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.prom.roboticsdmc.dao.CourseDao;
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

    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PEGINATION_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
    }
    
    @Override
    protected RowMapper<Course> createRowMapper() {
        return (rs, rowNum) -> {
            return Course.builder()
            .withCourseId(rs.getInt("course_id"))
            .withCourseName(rs.getString("course_name"))
            .withCourseDescription(rs.getString("course_description"))
            .build();
        };
    }
    
    @Override
    protected Object[] getEntityPropertiesToSave(Course course) {
        return new Object[] { 
                course.getCourseName(), 
                course.getCourseDescription() };
    }

    @Override
    protected Object[] getEntityPropertiesToUpdate(Course course) {
        return new Object[] { 
                course.getCourseName(), 
                course.getCourseDescription(), 
                course.getCourseId() };
    }

    @Override
    public List<Course> getAllStudentCoursesByStudentID(Integer studentId) {
        return jdbcTemplate.query(GET_ALL_STUDENT_COURSES_BY_STUDENT_ID_QUERY, createRowMapper(), studentId);
    }

    @Override
    public void addStudentToCourse(Integer studentId, Integer coursesId) {
        jdbcTemplate.update(ADD_STUDENT_TO_COURSE_QUERY, studentId, coursesId);
    }

    @Override
    public void removeStudentFromCourse(Integer studentId, Integer courseId) {
        jdbcTemplate.update(DELETE_STUDENT_FROM_COURSE_QUERY, studentId, courseId);
    }
}

package ua.prom.roboticsdmc.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.prom.roboticsdmc.dao.StudentDao;
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

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
    }
    
    @Override
    protected RowMapper<Student> createRowMapper() {
        return (rs, rowNum) -> {
            return Student.builder()
                    .withStudentId(rs.getInt("student_id"))
                    .withGroupId(rs.getInt("group_id"))
                    .withFirstName(rs.getString("first_name"))
                    .withLastName(rs.getString("last_name"))
                    .build();
        };
    }
    
    @Override
    protected Object[] getEntityPropertiesToSave(Student student) {
        return new Object[] { 
                student.getFirstName(), 
                student.getLastName(), 
                student.getGroupId() };
    }

    @Override
    protected Object[] getEntityPropertiesToUpdate(Student student) {
        return new Object[] {
                student.getFirstName(),
                student.getLastName(),
                student.getGroupId(),
                student.getStudentId() };
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        return jdbcTemplate.query(FIND_STUDENTS_BY_COURSE_NAME_QUERY, createRowMapper(), courseName);
    }
}

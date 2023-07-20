package ua.prom.roboticsdmc.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import ua.prom.roboticsdmc.config.SchoolApplicationConfig;
import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.domain.Course;
import ua.prom.roboticsdmc.testcontainer.PostgresqlTestContainer;

@JdbcTest
@ContextConfiguration(classes=SchoolApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { 
                "/sql/schema.sql", 
                "/sql/dataCourse.sql", 
                "/sql/dataGroup.sql", 
                "/sql/dataStudent.sql",
                "/sql/dataStudentCourse.sql" 
                }, 
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@DisplayName("CourseDaoImplTest")

class CourseDaoImplTest {
    
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = PostgresqlTestContainer.getInstance();

    @Autowired
    JdbcTemplate jdbcTemplate;
    CourseDao courseDao;

    @BeforeEach
    void setUp() {
        courseDao = new CourseDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("save method should add Course to the table")
    void save_shouldAddCourseToTable_whenEnteredDataIsCorrect() {

        int expectedCourseId = 7;
        String courseName = "Ukranian";
        Course addedCourse = Course.builder().withCourseName(courseName).build();
        Optional<Course> expectedCourse = Optional.of(Course.builder()
                        .withCourseId(expectedCourseId)
                        .withCourseName("Ukranian")
                        .build());

        courseDao.save(addedCourse);

        assertEquals(expectedCourse, courseDao.findById(expectedCourseId));
    }

    @Test
    @DisplayName("saveAll method should add Courses to the table")
    void saveAll_shouldAddAllCoursesToTable_whenEnteredDataIsCorrect() {

        List<Course> addedCourses = new ArrayList<Course>(
                Arrays.asList(Course.builder()
                        .withCourseName("Ukranian")
                        .build(),
                        Course.builder()
                        .withCourseName("Physics")
                        .build()));

        List<Course> expectedCourses = new ArrayList<Course>(
                Arrays.asList(
                        Course.builder()
                        .withCourseId(1)
                        .withCourseName("Math")
                        .build(),
                        Course.builder()
                        .withCourseId(2)
                        .withCourseName("Biology")
                        .build(),
                        Course.builder()
                        .withCourseId(3)
                        .withCourseName("Philosophy")
                        .build(),
                        Course.builder()
                        .withCourseId(4)
                        .withCourseName("Literature")
                        .build(),
                        Course.builder()
                        .withCourseId(5)
                        .withCourseName("English")
                        .build(),
                        Course.builder()
                        .withCourseId(6)
                        .withCourseName("Chemistry")
                        .build(),
                        Course.builder()
                        .withCourseId(7)
                        .withCourseName("Ukranian").build(),
                        Course.builder()
                        .withCourseId(8)
                        .withCourseName("Physics")
                        .build()));

        courseDao.saveAll(addedCourses);
        assertEquals(expectedCourses, courseDao.findAll());
    }

    @Test
    @DisplayName("finfById method should return Course from the table id Course exists")
    void findById_shouldReturnCourse_whenThereIsSomeCourseInTableWithEnteredCourseId() {

        int courseId = 1;
        Optional<Course> expectedCourse = Optional.of(
                Course.builder()
                .withCourseId(1)
                .withCourseName("Math")
                .build());

        assertEquals(expectedCourse, courseDao.findById(courseId));
    }

    @Test
    @DisplayName("findById method should return empty Optional if Course not exists")
    void findById_shouldReturnEmptyOptional_whenThereIsNotAnyCourseInTableWithEnteredCourseId() {

        int courseId = 100;

        assertEquals(Optional.empty(), courseDao.findById(courseId));
    }

    @Test
    @DisplayName("findAll method without pagination should return all Courses from the table if Courses exist")
    void findAll_shouldReturnAllListOfCourses_whenThereAreSomeCoursesInTable() {

        List<Course> expectedCourses = new ArrayList<Course>(
                Arrays.asList(
                        Course.builder()
                        .withCourseId(1)
                        .withCourseName("Math")
                        .build(),
                        Course.builder()
                        .withCourseId(2)
                        .withCourseName("Biology")
                        .build(),
                        Course.builder()
                        .withCourseId(3)
                        .withCourseName("Philosophy")
                        .build(),
                        Course.builder()
                        .withCourseId(4)
                        .withCourseName("Literature")
                        .build(),
                        Course.builder()
                        .withCourseId(5)
                        .withCourseName("English")
                        .build(),
                        Course.builder()
                        .withCourseId(6)
                        .withCourseName("Chemistry")
                        .build()));

        assertEquals(expectedCourses, courseDao.findAll());
    }

    @Test
    @DisplayName("findAll method with pagination should return Courses with defined offset and limit")
    void findAll_withPaginationShouldReturnDefinedListOfCourses_whenThereAreCoursesInTableWithOffsetAndLimit() {

        int rowOffset = 2;
        int rowLimit = 3;
        List<Course> expectedCourses = new ArrayList<Course>(Arrays.asList(
                        Course.builder()
                        .withCourseId(3)
                        .withCourseName("Philosophy")
                        .build(),
                        Course.builder()
                        .withCourseId(4)
                        .withCourseName("Literature")
                        .build(),
                        Course.builder()
                        .withCourseId(5)
                        .withCourseName("English")
                        .build()));

        assertEquals(expectedCourses, courseDao.findAll(rowOffset, rowLimit));
    }

    @Test
    @DisplayName("update method should update Course in the table")
    void update_shouldUpdateCourseInTable_whenEnteredDataIsCorrect() {

        int courseId = 1;
        String courseName = "Geometry";
        Course updatedCourse = Course.builder().withCourseId(courseId).withCourseName(courseName).build();
        Optional<Course> expectedCourse = Optional.of(
                Course.builder()
                .withCourseId(courseId)
                .withCourseName(courseName)
                .build());
        
        courseDao.update(updatedCourse);

        assertEquals(expectedCourse, courseDao.findById(courseId));
    }

    @Test
    @DisplayName("deleteById method should delete Course from the table")
    void deleteById_shouldDeleteCourse_whenThereIsSomeCourseInTableWithEnteredCourseId() {

        int courseId = 1;

        courseDao.deleteById(courseId);

        assertEquals(Optional.empty(), courseDao.findById(courseId));
    }

    @Test
    @DisplayName("getAllStudentCoursesByStudentID method should list of Courses from the table")
    void getAllStudentCoursesByStudentID_shouldReturnListOfCourseRelatedToStudent_whenThereAreAnyCoursesRelatedWithEnteredCourseId() {

        int studentId = 1;
        List<Course> expectedCourses = new ArrayList<Course>(Arrays.asList(
                Course.builder()
                .withCourseId(1)
                .withCourseName("Math")
                .build(),
                Course.builder()
                .withCourseId(3)
                .withCourseName("Philosophy")
                .build()));

        assertEquals(expectedCourses, courseDao.getAllStudentCoursesByStudentID(studentId));
    }

    @Test
    @DisplayName("addStudentToCourse method should add Student to Course")
    void addStudentToCourse_shouldAddStudentToCourse_whenEnteredDataIsCorrect() {

        int studentId = 2;
        int courseId = 5;
        List<Course> expectedCourse = null;

        courseDao.addStudentToCourse(studentId, courseId);
        expectedCourse = courseDao.getAllStudentCoursesByStudentID(studentId);

        assertTrue(expectedCourse.stream().anyMatch(course -> course.getCourseId() == courseId));
    }

    @Test
    @DisplayName("removeStudentFromCourse method should deleteStudent from Course")
    void removeStudentFromCourse_shoulRemoveStudentFromCourse_whenEnteredDataIsCorrect() {

        int studentId = 1;
        int courseId = 1;

        courseDao.removeStudentFromCourse(studentId, courseId);
        List<Course> expectedCourse = courseDao.getAllStudentCoursesByStudentID(studentId);

        assertTrue(expectedCourse.stream().noneMatch(course -> course.getCourseId() == courseId));
    }
}

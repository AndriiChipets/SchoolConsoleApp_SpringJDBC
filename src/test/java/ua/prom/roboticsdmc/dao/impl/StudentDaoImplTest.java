package ua.prom.roboticsdmc.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import ua.prom.roboticsdmc.config.SchoolApplicationConfig;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Student;

@JdbcTest
@ContextConfiguration(classes=SchoolApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/schemaH2.sql", "/sql/dataCourse.sql", "/sql/dataGroup.sql", "/sql/dataStudent.sql",
        "/sql/dataStudentCourse.sql" }, 
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@DisplayName("StudentDaoImplTest")

class StudentDaoImplTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    StudentDao studentDao;

    @BeforeEach
    void setUp() {
        studentDao = new StudentDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("save method should add Student to the table")
    void save_shouldAddStudentToTheTable_whenEnteredDataIsCorrect() {

        int expectedStudentId = 10;
        int groupId = 4;
        String firstName = "James";
        String lastName = "Garcia";
        Student addedStudent = Student.builder()
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
        Optional<Student> expectedStudent = Optional.of(
                Student.builder()
                .withStudentId(expectedStudentId)
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build());

        studentDao.save(addedStudent);

        assertEquals(expectedStudent, studentDao.findById(expectedStudentId));
    }

    @Test
    @DisplayName("saveAll method should add Students from the table")
    void saveAll_shouldAddStudentsToTable_whenEnteredDataIsCorrect() {

        List<Student> addedStudents = new ArrayList<Student>(Arrays.asList(
                Student.builder()
                .withGroupId(5)
                .withFirstName("Christopher")
                .withLastName("Thomas")
                .build(),
                Student.builder()
                .withGroupId(3)
                .withFirstName("Patricia")
                .withLastName("Wilson")
                .build()));
        List<Student> expectedStudents = new ArrayList<Student>(Arrays.asList(
                Student.builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("Michael")
                .withLastName("Thomas")
                .build(),
                Student.builder()
                .withStudentId(2)
                .withGroupId(2)
                .withFirstName("Christopher")
                .withLastName("Garcia")
                .build(),
                Student.builder()
                .withStudentId(3)
                .withGroupId(2)
                .withFirstName("Patricia")
                .withLastName("Garcia")
                .build(),
                Student.builder()
                .withStudentId(4)
                .withGroupId(4)
                .withFirstName("Patricia")
                .withLastName("Jackson")
                .build(),
                Student.builder()
                .withStudentId(5)
                .withGroupId(5)
                .withFirstName("William")
                .withLastName("Wilson")
                .build(),
                Student.builder()
                .withStudentId(6)
                .withGroupId(4)
                .withFirstName("James")
                .withLastName("Williams")
                .build(),
                Student.builder()
                .withStudentId(7)
                .withGroupId(2)
                .withFirstName("Robert")
                .withLastName("Rodriguez")
                .build(),
                Student.builder()
                .withStudentId(8)
                .withGroupId(1)
                .withFirstName("John")
                .withLastName("Martinez")
                .build(),
                Student.builder()
                .withStudentId(9)
                .withGroupId(5)
                .withFirstName("Karen")
                .withLastName("Garcia")
                .build(),
                Student.builder()
                .withStudentId(10)
                .withGroupId(5)
                .withFirstName("Christopher")
                .withLastName("Thomas")
                .build(),
                Student.builder()
                .withStudentId(11)
                .withGroupId(3)
                .withFirstName("Patricia")
                .withLastName("Wilson")
                .build()));

        studentDao.saveAll(addedStudents);
        assertEquals(expectedStudents, studentDao.findAll());
    }

    @Test
    @DisplayName("findById method should return Student if Student exists")
    void findById_shouldReturnStudent_whenThereIsSomeStudentInTableWithEnteredStudentId() {

        int studentId = 1;
        Optional<Student> expectedStudent = Optional.of(
                Student.builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("Michael")
                .withLastName("Thomas")
                .build());

        assertEquals(expectedStudent, studentDao.findById(studentId));
    }

    @Test
    @DisplayName("findById method should return empty Optional if Student not exists")
    void findById_shouldReturnEmptyOptional_whenThereIsNotAnyStudentInTableWithEnteredStudentId() {

        int studentId = 100;

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> studentDao.findById(studentId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());
    }

    @Test
    @DisplayName("findAll method without pagination should return all Students if Students exist")
    void findAll_shouldReturnAllStudents_whenThereAreSomeStudentsInTable() {

        List<Student> expectedStudents = new ArrayList<Student>(Arrays.asList(
                Student.builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("Michael")
                .withLastName("Thomas")
                .build(),
                Student.builder()
                .withStudentId(2)
                .withGroupId(2)
                .withFirstName("Christopher")
                .withLastName("Garcia")
                .build(),
                Student.builder()
                .withStudentId(3)
                .withGroupId(2)
                .withFirstName("Patricia")
                .withLastName("Garcia")
                .build(),
                Student.builder()
                .withStudentId(4)
                .withGroupId(4)
                .withFirstName("Patricia")
                .withLastName("Jackson")
                .build(),
                Student.builder()
                .withStudentId(5)
                .withGroupId(5)
                .withFirstName("William")
                .withLastName("Wilson")
                .build(),
                Student.builder()
                .withStudentId(6)
                .withGroupId(4)
                .withFirstName("James")
                .withLastName("Williams")
                .build(),
                Student.builder()
                .withStudentId(7)
                .withGroupId(2)
                .withFirstName("Robert")
                .withLastName("Rodriguez")
                .build(),
                Student.builder()
                .withStudentId(8)
                .withGroupId(1)
                .withFirstName("John")
                .withLastName("Martinez")
                .build(),
                Student.builder()
                .withStudentId(9)
                .withGroupId(5)
                .withFirstName("Karen")
                .withLastName("Garcia")
                .build()));

        assertEquals(expectedStudents, studentDao.findAll());
    }

    @Test
    @DisplayName("findAll method with pagination should return Students with defined offset and limit")
    void findAll_withPaginationShouldReturnDefinedListOfStudents_whenThereAreStudentsInTableWithOffsetAndLimit() {

        int rowOffset = 2;
        int rowLimit = 2;
        List<Student> expectedStudents = new ArrayList<Student>(Arrays.asList(
                Student.builder()
                .withStudentId(3)
                .withGroupId(2)
                .withFirstName("Patricia")
                .withLastName("Garcia")
                .build(),
                Student.builder()
                .withStudentId(4)
                .withGroupId(4)
                .withFirstName("Patricia")
                .withLastName("Jackson")
                .build()));

        assertEquals(expectedStudents, studentDao.findAll(rowOffset, rowLimit));
    }

    @Test
    @DisplayName("update method should update Student in the table")
    void update_shouldUpdateStudentInTable_whenEnteredDataIsCorrect() {

        int studentId = 1;
        int groupId = 1;
        String firstName = "James";
        String lastName = "Garcia";
        Student updatedStudent = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
        Optional<Student> expectedStudent = Optional.of(
                Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build());

        studentDao.update(updatedStudent);

        assertEquals(expectedStudent, studentDao.findById(studentId));
    }

    @Test
    @DisplayName("deleteById method should delete Student from the table")
    void deleteById_shouldDeleteCStudent_whenThereIsSomeStudentInTableWithEnteredStudentId() {

        int studentId = 1;

        studentDao.deleteById(studentId);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> studentDao.findById(studentId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());
    }

    @Test
    @DisplayName("findStudentsByCourseName method should return List of Students")
    void findStudentsByCourseName_shouldReturnListOfStudentsRelatedToCourse_whenThereAreAnyStudentsRelatedToEnteredCourseName() {

        String courseName = "Biology";
        List<Student> expectedStudents = new ArrayList<Student>(Arrays.asList(
                Student.builder()
                .withStudentId(3)
                .withGroupId(2)
                .withFirstName("Patricia")
                .withLastName("Garcia")
                .build(),
                Student.builder()
                .withStudentId(4)
                .withGroupId(4)
                .withFirstName("Patricia")
                .withLastName("Jackson")
                .build(),
                Student.builder()
                .withStudentId(6)
                .withGroupId(4)
                .withFirstName("James")
                .withLastName("Williams")
                .build(),
                Student.builder()
                .withStudentId(7)
                .withGroupId(2)
                .withFirstName("Robert")
                .withLastName("Rodriguez")
                .build(),
                Student.builder()
                .withStudentId(9)
                .withGroupId(5)
                .withFirstName("Karen")
                .withLastName("Garcia")
                .build()));

        assertEquals(expectedStudents, studentDao.findStudentsByCourseName(courseName));
    }
}

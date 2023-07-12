package ua.prom.roboticsdmc.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Course;
import ua.prom.roboticsdmc.tableutility.TableUtility;

@DisplayName("CourseDaoImplTest")
@ExtendWith(MockitoExtension.class)

class CourseDaoImplTest {

    CourseDao courseDao = new CourseDaoImpl(TableUtility.CONNECTOR_DB_TEST);

    @Mock
    private ConnectorDB mockConnectorDB;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private CourseDaoImpl mockCourseDao;

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenConnectionIsProblem() throws SQLException {

        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockCourseDao.findById(courseId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
    }

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenPrepearedStatementIsProblem() throws SQLException {

        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockCourseDao.findById(courseId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenResultSetIsProblem() throws SQLException {

        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockCourseDao.findById(courseId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenResultSetNextTrueIsProblem() throws SQLException {

        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockCourseDao.findById(courseId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenSetIntMethodIsIncorrect() throws SQLException {

        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(SQLException.class).when(mockPreparedStatement).setInt(anyInt(), anyInt());

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockCourseDao.findById(courseId));
        assertEquals("preparedStatement.setInt(1, integer) is failed..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenGetIntMethodIsIncorrect() throws SQLException {

        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockCourseDao.findById(courseId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();
        courseDao.save(addedCourse);

        assertEquals(expectedCourse, courseDao.findById(expectedCourseId));
    }

    @Test
    @DisplayName("save method should throw DataBaseSqlRuntimeException")
    void save_shouldThrowDataBaseSqlRuntimeException_whenEnteredCourseNameIsAlreadyExist() {

        String courseName = "Math";
        Course addedCourse = Course.builder().withCourseName(courseName).build();

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> courseDao.save(addedCourse));
        assertEquals("Element is not added to the table..", exception.getMessage());
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
                        .withCourseName("Ukranian").build(),
                        Course.builder()
                        .withCourseId(2)
                        .withCourseName("Physics")
                        .build()));

        TableUtility.createTablesAndSchema();
        courseDao.saveAll(addedCourses);
        assertEquals(expectedCourses, courseDao.findAll());
    }

    @Test
    @DisplayName("saveAll method should throw DataBaseSqlRuntimeException")
    void saveAll_shouldThrowDataBaseSqlRuntimeException_whenSomeOfEnteredCourseNameIsAlreadyExist() {

        List<Course> addedCourses = new ArrayList<Course>(Arrays.asList(
                Course.builder()
                .withCourseName("Math")
                .build(),
                Course.builder()
                .withCourseName("Physics")
                .build()));

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> courseDao.saveAll(addedCourses));
        assertEquals("Elements are not added to the table..", exception.getMessage());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();

        assertEquals(expectedCourse, courseDao.findById(courseId));
    }

    @Test
    @DisplayName("findById method should return empty Optional if Course not exists")
    void findById_shouldReturnEmptyOptional_whenThereIsNotAnyCourseInTableWithEnteredCourseId() {

        int courseId = 100;

        TableUtility.createTablesAndSchema();

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

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();

        assertEquals(expectedCourses, courseDao.findAll());
    }

    @Test
    @DisplayName("findAll method without pagination should return empty List from the table if Courses not exist")
    void findAll_shouldReturnEmptyList_whenThereAreNotAnyCourseInTable() {

        TableUtility.createTablesAndSchema();

        assertEquals(Collections.emptyList(), courseDao.findAll());
    }

    @Test
    @DisplayName("findAll method without pagination should DataBaseSqlRuntimeException")
    void findAll_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockCourseDao.findAll());
            assertEquals("Can't get all elements from the table..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();

        assertEquals(expectedCourses, courseDao.findAll(rowOffset, rowLimit));
    }

    @Test
    @DisplayName("findAll method with pagination should DataBaseSqlRuntimeException")
    void findAll_withPaginationShouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem()
            throws SQLException {

        int rowOffset = 2;
        int rowLimit = 3;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockCourseDao.findAll(rowOffset, rowLimit));
        assertEquals("Can't get elements from the table..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();
        courseDao.update(updatedCourse);

        assertEquals(expectedCourse, courseDao.findById(courseId));
    }

    @Test
    @DisplayName("update method should DataBaseSqlRuntimeException")
    void update_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int courseId = 1;
        String courseName = "Geometry";
        Course updatedCourse = Course.builder().withCourseId(courseId).withCourseName(courseName).build();

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockCourseDao.update(updatedCourse));
        assertEquals("Element is not updated..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("deleteById method should delete Course from the table")
    void deleteById_shouldDeleteCourse_whenThereIsSomeCourseInTableWithEnteredCourseId() {

        int courseId = 1;

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();
        courseDao.deleteById(courseId);

        assertEquals(Optional.empty(), courseDao.findById(courseId));
    }

    @Test
    @DisplayName("deleteById method should DataBaseSqlRuntimeException")
    void deleteById_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockCourseDao.deleteById(courseId));
        assertEquals("Element is not deleted from the table..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();
        TableUtility.fillStudentTableDefaultData();
        TableUtility.fillStudentCourseTableDefaultData();

        assertEquals(expectedCourses, courseDao.getAllStudentCoursesByStudentID(studentId));
    }

    @Test
    @DisplayName("getAllStudentCoursesByStudentID method should DataBaseSqlRuntimeException")
    void getAllStudentCoursesByStudentID_shouldTrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem()
            throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockCourseDao.getAllStudentCoursesByStudentID(studentId));
        assertEquals("Can't get all courses for specified studentID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("addStudentToCourse method should add Student to Course")
    void addStudentToCourse_shouldAddStudentToCourse_whenEnteredDataIsCorrect() {

        int studentId = 2;
        int courseId = 5;
        List<Course> expectedCourse = null;

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();
        TableUtility.fillStudentTableDefaultData();
        courseDao.addStudentToCourse(studentId, courseId);
        expectedCourse = courseDao.getAllStudentCoursesByStudentID(studentId);

        assertTrue(expectedCourse.stream().anyMatch(course -> course.getCourseId() == courseId));
    }

    @Test
    @DisplayName("addStudentToCourse method should DataBaseSqlRuntimeException")
    void addStudentToCourse_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem()
            throws SQLException {

        int studentId = 2;
        int courseId = 5;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockCourseDao.addStudentToCourse(studentId, courseId));
        assertEquals("Student is not added to the course..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("removeStudentFromCourse method should deleteStudent from Course")
    void removeStudentFromCourse_shoulRemoveStudentFromCourse_whenEnteredDataIsCorrect() {

        int studentId = 1;
        int courseId = 1;
        List<Course> expectedCourse = courseDao.getAllStudentCoursesByStudentID(studentId);

        TableUtility.createTablesAndSchema();
        TableUtility.fillCourseTableDefaultData();
        TableUtility.fillStudentTableDefaultData();
        courseDao.removeStudentFromCourse(studentId, courseId);

        assertTrue(expectedCourse.stream().noneMatch(course -> course.getCourseId() == courseId));
    }

    @Test
    @DisplayName("removeStudentFromCourse method should DataBaseSqlRuntimeException")
    void removeStudentFromCourse_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem()
            throws SQLException {

        int studentId = 1;
        int courseId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockCourseDao.removeStudentFromCourse(studentId, courseId));
        assertEquals("Student is not deleted from the course..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }
}

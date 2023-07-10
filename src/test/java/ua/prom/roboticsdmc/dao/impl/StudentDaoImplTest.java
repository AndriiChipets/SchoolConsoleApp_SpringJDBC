package ua.prom.roboticsdmc.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Student;
import ua.prom.roboticsdmc.tableutility.TableUtility;

@DisplayName("StudentDaoImplTest")
@ExtendWith(MockitoExtension.class)

class StudentDaoImplTest {

    StudentDao studentDao = new StudentDaoImpl(TableUtility.CONNECTOR_DB_TEST);

    @Mock
    private ConnectorDB mockConnectorDB;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private StudentDaoImpl mockStudentDao;

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenConnectionIsProblem() throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockStudentDao.findById(studentId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
    }

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockStudentDao.findById(studentId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenResultSetIsProblem() throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockStudentDao.findById(studentId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenResultSetNextTrueIsProblem() throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockStudentDao.findById(studentId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenSetIntMethodIsIncorrect() throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(SQLException.class).when(mockPreparedStatement).setInt(anyInt(), anyInt());

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockStudentDao.findById(studentId));
        assertEquals("preparedStatement.setInt(1, integer) is failed..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("findById method should DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenGetIntMethodIsIncorrect() throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockStudentDao.findById(studentId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillStudentTableDefaultData();
        studentDao.save(addedStudent);

        assertEquals(expectedStudent, studentDao.findById(expectedStudentId));
    }

    @Test
    @DisplayName("save method should throw DataBaseSqlRuntimeException")
    void save_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int groupId = 4;
        String firstName = "James";
        String lastName = "Garcia";
        Student addedStudent = Student.builder()
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockStudentDao.save(addedStudent));
        assertEquals("Element is not added to the table..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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
                .withGroupId(5)
                .withFirstName("Christopher")
                .withLastName("Thomas")
                .build(),
                Student.builder()
                .withStudentId(2)
                .withGroupId(3)
                .withFirstName("Patricia")
                .withLastName("Wilson")
                .build()));

        TableUtility.createTablesAndSchema();
        studentDao.saveAll(addedStudents);
        assertEquals(expectedStudents, studentDao.findAll());
    }

    @Test
    @DisplayName("saveAll method should throw DataBaseSqlRuntimeException")
    void saveAll_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        List<Student> addedStudents = new ArrayList<Student>(Arrays.asList(
                Student.builder().withGroupId(5).withFirstName("Christopher").withLastName("Thomas").build(),
                Student.builder().withGroupId(3).withFirstName("Patricia").withLastName("Wilson").build()));

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockStudentDao.saveAll(addedStudents));
        assertEquals("Elements are not added to the table..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillStudentTableDefaultData();

        assertEquals(expectedStudent, studentDao.findById(studentId));
    }

    @Test
    @DisplayName("findById method should return empty Optional if Student not exists")
    void findById_shouldReturnEmptyOptional_whenThereIsNotAnyStudentInTableWithEnteredStudentId() {

        int studentId = 100;

        TableUtility.createTablesAndSchema();

        assertEquals(Optional.empty(), studentDao.findById(studentId));
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillStudentTableDefaultData();

        assertEquals(expectedStudents, studentDao.findAll());
    }

    @Test
    @DisplayName("findAll method without pagination should return empty List if Students not exist")
    void findAll_shouldReturnEmptyList_whenThereAreNotAnyStudentInTable() {

        TableUtility.createTablesAndSchema();

        assertEquals(Collections.emptyList(), studentDao.findAll());
    }

    @Test
    @DisplayName("findAll method without pagination should throw DataBaseSqlRuntimeException")
    void findAll_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockStudentDao.findAll());
        assertEquals("Can't get all elements from the table..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillStudentTableDefaultData();

        assertEquals(expectedStudents, studentDao.findAll(rowOffset, rowLimit));
    }

    @Test
    @DisplayName("findAll method with pagination should throw DataBaseSqlRuntimeException")
    void findAll_withPaginationShouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem()
            throws SQLException {

        int rowOffset = 2;
        int rowLimit = 2;
        
        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockStudentDao.findAll(rowOffset, rowLimit));
        assertEquals("Can't get elements from the table..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillStudentTableDefaultData();
        studentDao.update(updatedStudent);

        assertEquals(expectedStudent, studentDao.findById(studentId));
    }

    @Test
    @DisplayName("update method should throw DataBaseSqlRuntimeException")
    void update_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

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

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockStudentDao.update(updatedStudent));
        assertEquals("Element is not updated..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }

    @Test
    @DisplayName("deleteById method should delete Student from the table")
    void deleteById_shouldDeleteCStudent_whenThereIsSomeStudentInTableWithEnteredStudentId() {

        int studentId = 1;

        TableUtility.createTablesAndSchema();
        TableUtility.fillStudentTableDefaultData();
        studentDao.deleteById(studentId);

        assertEquals(Optional.empty(), studentDao.findById(studentId));
    }

    @Test
    @DisplayName("deleteById method should throw DataBaseSqlRuntimeException")
    void deleteById_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int studentId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockStudentDao.deleteById(studentId));
        assertEquals("Element is not deleted from the table..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
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

        TableUtility.createTablesAndSchema();
        TableUtility.fillStudentTableDefaultData();
        TableUtility.fillCourseTableDefaultData();
        TableUtility.fillStudentCourseTableDefaultData();

        assertEquals(expectedStudents, studentDao.findStudentsByCourseName(courseName));
    }

    @Test
    @DisplayName("findStudentsByCourseName method should throw DataBaseSqlRuntimeException")
    void findStudentsByCourseName_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem()
            throws SQLException {

        String courseName = "Biology";

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> mockStudentDao.findStudentsByCourseName(courseName));
        assertEquals("Can't get students who related to the course..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }
}

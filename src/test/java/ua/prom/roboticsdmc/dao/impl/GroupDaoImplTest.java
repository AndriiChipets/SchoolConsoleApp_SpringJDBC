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
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Group;
import ua.prom.roboticsdmc.tableutility.TableUtility;

@DisplayName("GroupDaoImplTest")
@ExtendWith(value = { MockitoExtension.class })

class GroupDaoImplTest {

    GroupDao groupDao = new GroupDaoImpl(TableUtility.CONNECTOR_DB_TEST);
   
    @Mock
    private ConnectorDB mockConnectorDB;

    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    @InjectMocks
    private GroupDaoImpl mockGroupDao;
   
    
    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenConnectionIsProblem() throws SQLException {

        int groupId = 1;
        
            when(mockConnectorDB.getConnection()).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.findById(groupId));
            assertEquals("Can't get element from the table by element ID..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
    }
    
    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldTrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int groupId = 1;
        
            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.findById(groupId));
            assertEquals("Can't get element from the table by element ID..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenResultSetIsProblem() throws SQLException {

        int groupId = 1;
        
            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenThrow(SQLException.class);
            
            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.findById(groupId));
            assertEquals("Can't get element from the table by element ID..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenResultSetNextTrueIsProblem() throws SQLException {
        
        int groupId = 1;
        
        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenThrow(SQLException.class);
        
        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockGroupDao.findById(groupId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());
        
        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("findById method should DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenSetIntMethodIsIncorrect() throws SQLException {

        int groupId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(SQLException.class).when(mockPreparedStatement).setInt(anyInt(), anyInt());

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockGroupDao.findById(groupId));
        assertEquals("preparedStatement.setInt(1, integer) is failed..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("findById method should DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenGetIntMethodIsIncorrect() throws SQLException {

        int groupId = 1;

        when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyString())).thenThrow(SQLException.class);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> mockGroupDao.findById(groupId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

        InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
        inOrder.verify(mockConnectorDB).getConnection();
        inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("save method should add Group to the table")
    void save_shouldAddGroupToTheTable_whenEnteredDataIsCorrect() {
       
        int expectedGroupId = 6;
        String groupName = "AA-00";
        Group addedGroup = new Group(groupName);
        Optional<Group> expectedGroup = Optional.of(new Group(expectedGroupId, groupName));

        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();
        groupDao.save(addedGroup);
        
        assertEquals(expectedGroup, groupDao.findById(expectedGroupId));
    }
    
    @Test
    @DisplayName("save method should throw DataBaseSqlRuntimeException")
    void save_shouldThrowDataBaseSqlRuntimeException_whenEnteredGroupNameIsAlreadyExist() {
        
        String groupName = "YY-58";
        Group addedGroup = new Group(groupName);
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();
        
        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> groupDao.save(addedGroup));
        assertEquals("Element is not added to the table..", exception.getMessage());
    }
    
    @Test
    @DisplayName("saveAll method should add Groups to the table")
    void saveAll_shouldAddGroupsToTable_whenEnteredDataIsCorrect() {
        
        List<Group> addedGroups = new ArrayList<Group>(Arrays.asList(
                new Group("AA-01"),
                new Group("BB-02"))); 
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                new Group(1, "AA-01"),
                new Group(2, "BB-02")
                ));
        
        TableUtility.createTablesAndSchema();
        groupDao.saveAll(addedGroups);
        
        assertEquals(expectedGroups, groupDao.findAll());  
    }
    
    @Test
    @DisplayName("saveAll method should throw DataBaseSqlRuntimeException")
    void saveAll_shouldThrowDataBaseSqlRuntimeException_whenSomeOfEnteredGroupNameIsAlreadyExist() {

        List<Group> addedGroups = new ArrayList<Group>(Arrays.asList(
                new Group("YY-58"),
                new Group("BB-02")));
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                () -> groupDao.saveAll(addedGroups));
        assertEquals("Elements are not added to the table..", exception.getMessage());
    }

    @Test
    @DisplayName("findById method should return Group from the table if Group exists")
    void findById_shouldReturnGroup_whenThereIsSomeGroupInTableWithEnteredGroupId() {

        int groupId = 1;
        Optional<Group> expectedOptional = Optional.of(new Group(1, "YY-58"));
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();

        assertEquals(expectedOptional, groupDao.findById(groupId));
    }

    @Test
    @DisplayName("findById method should return Optional empty from the table if Group not exists")
    void findById_shouldReturnEmptyOptional_whenThereIsNotAnyGroupInTableWithEnteredGroupId() {

        int groupId = 100;
        
        TableUtility.createTablesAndSchema();

        assertEquals(Optional.empty(), groupDao.findById(groupId));
    }

    @Test
    @DisplayName("findAll method without pagination should return all Groups from the table if Groups exist")
    void findAll_shouldReturnListOfGroups_whenThereAreSomeGroupsInTable() {

        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                new Group(1, "YY-58"),
                new Group(2, "VA-90"),
                new Group(3, "VA-52"),
                new Group(4, "FF-49"),
                new Group(5, "SR-71")));
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();

        assertEquals(expectedGroups, groupDao.findAll());
    }
    
    @Test
    @DisplayName("findAll method without pagination should return empty List from the table if Groups not exist")
    void findAll_shouldReturnEmptyList_whenThereAreNotAnyGroupInTable() {

        TableUtility.createTablesAndSchema();

        assertEquals(Collections.emptyList(), groupDao.findAll());
    }
    
    @Test
    @DisplayName("findAll method without pagination should throw DataBaseSqlRuntimeException")
    void findAll_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.findAll());
            assertEquals("Can't get all elements from the table..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("findAll method with pagination should return Groups with defined offset and limit")
    void findAll_withPaginationShouldReturnDefinedListOfGroups_whenThereAreGroupsInTableWithOffsetAndLimit() {

        int rowOffset = 0;
        int rowLimit = 2;
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                new Group(1, "YY-58"),
                new Group(2, "VA-90")));
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();

        assertEquals(expectedGroups, groupDao.findAll(rowOffset, rowLimit));
    }
    
    @Test
    @DisplayName("findAll method with pagination should throw DataBaseSqlRuntimeException")
    void findAll_withPaginationShouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

            int rowOffset = 0;
            int rowLimit = 2;
        
            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.findAll(rowOffset, rowLimit));
            assertEquals("Can't get elements from the table..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("update method should update Group")
    void update_shouldUpdateGroupInTable_whenEnteredDataIsCorrect() {

        int groupId = 1;
        String groupName = "YY-60";
        Group updatedGroup = new Group(groupId, groupName);
        Optional<Group> expectedGroup = Optional.of(new Group(groupId, groupName));
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();
        groupDao.update(updatedGroup);

        assertEquals(expectedGroup, groupDao.findById(groupId));
    }
    
    @Test
    @DisplayName("update method should throw DataBaseSqlRuntimeException")
    void update_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int groupId = 1;
        String groupName = "YY-60";
        Group updatedGroup = new Group(groupId, groupName);
        
            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.update(updatedGroup));
            assertEquals("Element is not updated..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("deleteById method should delete Group from the table")
    void deleteById_shouldDeleteGroup_whenThereIsSomeGroupInTableWithEnteredGroupId() {

        int groupId = 1;
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();
        groupDao.deleteById(groupId);

        assertEquals(Optional.empty(), groupDao.findById(groupId));
    }
    
    @Test
    @DisplayName("deleteById method should throw DataBaseSqlRuntimeException")
    void deleteById_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int groupId = 1;

            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.deleteById(groupId));
            assertEquals("Element is not deleted from the table..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
    }
    
    @Test
    @DisplayName("findGroupWithLessOrEqualsStudentQuantity method should return List of Groups")
    void findGroupWithLessOrEqualsStudentQuantity_shouldReturnListOfGroupsEachContainsDefinedStudentQuantity_whenThereIsSomeGroupWithOrMoreEnteredStudentQuantity() {

        int studentQuantity = 2;
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                new Group(1, "YY-58"),
                new Group(4, "FF-49"),
                new Group(5, "SR-71")));
        
        TableUtility.createTablesAndSchema();
        TableUtility.fillGroupTableDefaultData();
        TableUtility.fillStudentTableDefaultData();
        
        assertEquals(expectedGroups, groupDao.findGroupWithLessOrEqualsStudentQuantity(studentQuantity));
    }
    
    @Test
    @DisplayName("findGroupWithLessOrEqualsStudentQuantity method should throw DataBaseSqlRuntimeException")
    void findGroupWithLessOrEqualsStudentQuantity_shouldThrowDataBaseSqlRuntimeException_whenThereIsSomeConnectionProblem() throws SQLException {

        int studentQuantity = 2;
        
            when(mockConnectorDB.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

            Exception exception = assertThrows(DataBaseSqlRuntimeException.class,
                    () -> mockGroupDao.findGroupWithLessOrEqualsStudentQuantity(studentQuantity));
            assertEquals("Can't get groups with defined or less quantity of students..", exception.getMessage());

            InOrder inOrder = inOrder(mockConnectorDB, mockConnection);
            inOrder.verify(mockConnectorDB).getConnection();
            inOrder.verify(mockConnection).prepareStatement(anyString());
    }
}

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
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Group;

@JdbcTest
@ContextConfiguration(classes=SchoolApplicationConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = { "/sql/schemaH2.sql", "/sql/dataCourse.sql", "/sql/dataGroup.sql", "/sql/dataStudent.sql",
        "/sql/dataStudentCourse.sql" }, 
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@DisplayName("GroupDaoImplTest")

class GroupDaoImplTest {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    GroupDao groupDao;

    @BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("save method should add Group to the table")
    void save_shouldAddGroupToTheTable_whenEnteredDataIsCorrect() {

        int expectedGroupId = 6;
        String groupName = "AA-00";
        Group addedGroup = new Group(groupName);
        Optional<Group> expectedGroup = Optional.of(new Group(expectedGroupId, groupName));

        groupDao.save(addedGroup);

        assertEquals(expectedGroup, groupDao.findById(expectedGroupId));
    }

    @Test
    @DisplayName("saveAll method should add Groups to the table")
    void saveAll_shouldAddGroupsToTable_whenEnteredDataIsCorrect() {

        List<Group> addedGroups = new ArrayList<Group>(Arrays.asList(
                new Group("AA-01"), 
                new Group("BB-02")));
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                new Group(1, "YY-58"),
                new Group(2, "VA-90"),
                new Group(3, "VA-52"),
                new Group(4, "FF-49"),
                new Group(5, "SR-71"),
                new Group(6, "AA-01"),
                new Group(7, "BB-02")));

        groupDao.saveAll(addedGroups);

        assertEquals(expectedGroups, groupDao.findAll());
    }

    @Test
    @DisplayName("findById method should return Group from the table if Group exists")
    void findById_shouldReturnGroup_whenThereIsSomeGroupInTableWithEnteredGroupId() {

        int groupId = 1;
        Optional<Group> expectedOptional = Optional.of(
                new Group(1, "YY-58"));

        assertEquals(expectedOptional, groupDao.findById(groupId));
    }

    @Test
    @DisplayName("findById method should throw DataBaseSqlRuntimeException")
    void findById_shouldThrowDataBaseSqlRuntimeException_whenThereIsNotAnyGroupInTableWithEnteredGroupId() {

        int groupId = 100;

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> groupDao.findById(groupId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());
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

        assertEquals(expectedGroups, groupDao.findAll());
    }

    @Test
    @DisplayName("findAll method with pagination should return Groups with defined offset and limit")
    void findAll_withPaginationShouldReturnDefinedListOfGroups_whenThereAreGroupsInTableWithOffsetAndLimit() {

        int rowOffset = 0;
        int rowLimit = 2;
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                new Group(1, "YY-58"), 
                new Group(2, "VA-90")));

        assertEquals(expectedGroups, groupDao.findAll(rowOffset, rowLimit));
    }

    @Test
    @DisplayName("update method should update Group")
    void update_shouldUpdateGroupInTable_whenEnteredDataIsCorrect() {

        int groupId = 1;
        String groupName = "YY-60";
        Group updatedGroup = new Group(groupId, groupName);
        Optional<Group> expectedGroup = Optional.of(
                new Group(groupId, groupName));

        groupDao.update(updatedGroup);

        assertEquals(expectedGroup, groupDao.findById(groupId));
    }

    @Test
    @DisplayName("deleteById method should delete Group from the table")
    void deleteById_shouldDeleteGroup_whenThereIsSomeGroupInTableWithEnteredGroupId() {

        int groupId = 1;

        groupDao.deleteById(groupId);

        Exception exception = assertThrows(DataBaseSqlRuntimeException.class, 
                () -> groupDao.findById(groupId));
        assertEquals("Can't get element from the table by element ID..", exception.getMessage());

    }

    @Test
    @DisplayName("findGroupWithLessOrEqualsStudentQuantity method should return List of Groups")
    void findGroupWithLessOrEqualsStudentQuantity_shouldReturnListOfGroupsEachContainsDefinedStudentQuantity_whenThereIsSomeGroupWithOrMoreEnteredStudentQuantity() {

        int studentQuantity = 2;
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                new Group(1, "YY-58"), 
                new Group(4, "FF-49"), 
                new Group(5, "SR-71")));

        assertEquals(expectedGroups, groupDao.findGroupWithLessOrEqualsStudentQuantity(studentQuantity));
    }
}

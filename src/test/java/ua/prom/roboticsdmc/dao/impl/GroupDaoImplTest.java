package ua.prom.roboticsdmc.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.domain.Group;
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
                "/sql/dataStudentCourse.sql" }, 
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@DisplayName("GroupDaoImplTest")

class GroupDaoImplTest {
    
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = PostgresqlTestContainer.getInstance();
    
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
        Group addedGroup = Group.builder().withGroupName(groupName).build();
        Optional<Group> expectedGroup = Optional.of(
                Group.builder()
                .withGroupId(expectedGroupId)
                .withGroupName(groupName)
                .build());

        groupDao.save(addedGroup);

        assertEquals(expectedGroup, groupDao.findById(expectedGroupId));
    }

    @Test
    @DisplayName("saveAll method should add Groups to the table")
    void saveAll_shouldAddGroupsToTable_whenEnteredDataIsCorrect() {

        List<Group> addedGroups = new ArrayList<Group>(Arrays.asList(
                Group.builder()
                .withGroupName("AA-01")
                .build(),
                Group.builder()
                .withGroupName("BB-02")
                .build()));
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                Group.builder()
                .withGroupId(1)
                .withGroupName("YY-58")
                .build(),
                Group.builder()
                .withGroupId(2)
                .withGroupName("VA-90")
                .build(),
                Group.builder()
                .withGroupId(3)
                .withGroupName("VA-52")
                .build(),
                Group.builder()
                .withGroupId(4)
                .withGroupName("FF-49")
                .build(),
                Group.builder()
                .withGroupId(5)
                .withGroupName("SR-71")
                .build(),
                Group.builder()
                .withGroupId(6)
                .withGroupName("AA-01")
                .build(),
                Group.builder()
                .withGroupId(7)
                .withGroupName("BB-02")
                .build()));

        groupDao.saveAll(addedGroups);

        assertEquals(expectedGroups, groupDao.findAll());
    }

    @Test
    @DisplayName("findById method should return Group from the table if Group exists")
    void findById_shouldReturnGroup_whenThereIsSomeGroupInTableWithEnteredGroupId() {

        int groupId = 1;
        Optional<Group> expectedOptional = Optional.of(
                Group.builder()
                .withGroupId(1)
                .withGroupName("YY-58")
                .build());

        assertEquals(expectedOptional, groupDao.findById(groupId));
    }

    @Test
    @DisplayName("findById method should return empty Optional if Course not exists")
    void findById_shouldReturnEmptyOptional_whenThereIsNotAnyGroupInTableWithEnteredGroupId() {

        int groupId = 100;

        assertEquals(Optional.empty(), groupDao.findById(groupId));
    }

    @Test
    @DisplayName("findAll method without pagination should return all Groups from the table if Groups exist")
    void findAll_shouldReturnListOfGroups_whenThereAreSomeGroupsInTable() {

        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                Group.builder()
                .withGroupId(1)
                .withGroupName("YY-58")
                .build(),
                Group.builder()
                .withGroupId(2)
                .withGroupName("VA-90")
                .build(),
                Group.builder()
                .withGroupId(3)
                .withGroupName("VA-52")
                .build(),
                Group.builder()
                .withGroupId(4)
                .withGroupName("FF-49")
                .build(),
                Group.builder()
                .withGroupId(5)
                .withGroupName("SR-71")
                .build()));

        assertEquals(expectedGroups, groupDao.findAll());
    }

    @Test
    @DisplayName("findAll method with pagination should return Groups with defined offset and limit")
    void findAll_withPaginationShouldReturnDefinedListOfGroups_whenThereAreGroupsInTableWithOffsetAndLimit() {

        int rowOffset = 0;
        int rowLimit = 2;
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                Group.builder()
                .withGroupId(1)
                .withGroupName("YY-58")
                .build(),
                Group.builder()
                .withGroupId(2)
                .withGroupName("VA-90")
                .build()));

        assertEquals(expectedGroups, groupDao.findAll(rowOffset, rowLimit));
    }

    @Test
    @DisplayName("update method should update Group")
    void update_shouldUpdateGroupInTable_whenEnteredDataIsCorrect() {

        int groupId = 1;
        String groupName = "YY-60";
        Group updatedGroup = 
                Group.builder()
                .withGroupId(groupId)
                .withGroupName(groupName)
                .build();
        Optional<Group> expectedGroup = Optional.of(
                Group.builder()
                .withGroupId(groupId)
                .withGroupName(groupName)
                .build());

        groupDao.update(updatedGroup);

        assertEquals(expectedGroup, groupDao.findById(groupId));
    }

    @Test
    @DisplayName("deleteById method should delete Group from the table")
    void deleteById_shouldDeleteGroup_whenThereIsSomeGroupInTableWithEnteredGroupId() {

        int groupId = 1;

        groupDao.deleteById(groupId);

        assertEquals(Optional.empty(), groupDao.findById(groupId));
    }

    @Test
    @DisplayName("findGroupWithLessOrEqualsStudentQuantity method should return List of Groups")
    void findGroupWithLessOrEqualsStudentQuantity_shouldReturnListOfGroupsEachContainsDefinedStudentQuantity_whenThereIsSomeGroupWithOrMoreEnteredStudentQuantity() {

        int studentQuantity = 2;
        List<Group> expectedGroups = new ArrayList<Group>(Arrays.asList(
                Group.builder()
                .withGroupId(1)
                .withGroupName("YY-58")
                .build(), 
                Group.builder()
                .withGroupId(4)
                .withGroupName("FF-49")
                .build(),
                Group.builder()
                .withGroupId(5)
                .withGroupName("SR-71")
                .build()));

        assertEquals(expectedGroups, groupDao.findGroupWithLessOrEqualsStudentQuantity(studentQuantity));
    }
}

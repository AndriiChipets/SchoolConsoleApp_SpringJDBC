package ua.prom.roboticsdmc.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.domain.Group;

@Repository
public class GroupDaoImpl extends AbstractCrudDaoImpl<Integer, Group> implements GroupDao {

    private static final String SAVE_QUERY = "INSERT INTO school_app_schema.groups (group_name) VALUES (?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school_app_schema.groups WHERE group_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school_app_schema.groups ORDER BY group_id ASC";
    private static final String FIND_ALL_PAGINATION_QUERY = "SELECT * FROM school_app_schema.groups ORDER BY group_id ASC LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE school_app_schema.groups SET group_name=? WHERE group_id=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school_app_schema.groups WHERE group_id=?";
    private static final String FIND_GROUP_WITH_STUDENT_QUANTITY_QUERY = "SELECT school_app_schema.students.group_id, school_app_schema.groups.group_name, COUNT (school_app_schema.students.student_id) "
            + "FROM school_app_schema.students " + "INNER JOIN school_app_schema.groups "
            + "ON school_app_schema.students.group_id = school_app_schema.groups.group_id "
            + "GROUP BY school_app_schema.students.group_id, school_app_schema.groups.group_name "
            + "HAVING COUNT (school_app_schema.students.student_id) <= ? "
            + "ORDER BY school_app_schema.students.group_id;";

    public GroupDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
    }
    
    @Override
    protected RowMapper<Group> createRowMapper() {
        return (rs, rowNum) -> {
            return new Group(rs.getInt("group_id"), rs.getString("group_name"));
        };
    }

    @Override
    protected Object[] getEntityPropertiesToSave(Group group) {
        return new Object[] { group.getGroupName() };
    }

    @Override
    protected Object[] getEntityPropertiesToUpdate(Group group) {
        return new Object[] { group.getGroupName(), group.getGroupId() };
    }

    @Override
    public List<Group> findGroupWithLessOrEqualsStudentQuantity(Integer studentQuantity) {
        return jdbcTemplate.query(FIND_GROUP_WITH_STUDENT_QUANTITY_QUERY, createRowMapper(), studentQuantity);
    }
}

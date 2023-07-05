package ua.prom.roboticsdmc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;
import ua.prom.roboticsdmc.domain.Group;

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

    public GroupDaoImpl(ConnectorDB connectorDB) {
        super(connectorDB, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY, UPDATE_QUERY,
                DELETE_BY_ID_QUERY);
    }
    
    @Override
    protected Group mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Group(resultSet.getInt("group_id"), resultSet.getString("group_name"));
    }

    @Override
    protected void mapEntityToPreparedStatementSave(Group group, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, group.getGroupName());
    }

    @Override
    protected void mapEntityToPreparedStatementUpdate(Group group, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, group.getGroupName());
        preparedStatement.setInt(2, group.getGroupId());
    }

    @Override
    public List<Group> findGroupWithLessOrEqualsStudentQuantity(Integer studentQuantity) {

        List<Group> groups = new ArrayList<>();

        try (Connection connection = connectorDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_GROUP_WITH_STUDENT_QUANTITY_QUERY)) {

            preparedStatement.setInt(1, studentQuantity);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int groupId = resultSet.getInt("group_id");
                String groupName = resultSet.getString("group_name");
                Group group = new Group(groupId, groupName);
                groups.add(group);
            }
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Can't get groups with defined or less quantity of students..", e);
        }
        return groups;
    }
}

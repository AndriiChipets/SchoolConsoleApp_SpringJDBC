package ua.prom.roboticsdmc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.CrudDao;
import ua.prom.roboticsdmc.dao.exception.DataBaseSqlRuntimeException;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    private static final BiConsumer<PreparedStatement, Integer> INT_PARAM_SETTER = (preparedStatement, integer) -> {
        try {
            preparedStatement.setInt(1, integer);
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("preparedStatement.setInt(1, integer) is failed..", e);
        }
    };

    protected final ConnectorDB connectorDB;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String findAllPeginationQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    protected AbstractCrudDaoImpl(ConnectorDB connectorDB, String saveQuery, String findByIdQuery, String findAllQuery,
            String findAllPeginationQuery, String updateQuery, String deleteByIdQuery) {
        this.connectorDB = connectorDB;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.findAllPeginationQuery = findAllPeginationQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(E entity) {
        saveByParam(entity, saveQuery);
    }

    private void saveByParam(E entity, String saveQuery) {

        try (final Connection connection = connectorDB.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            mapEntityToPreparedStatementSave(entity, preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Element is not added to the table..", e);
        }
    }

    @Override
    public void saveAll(List<E> entities) {
        saveAllByParam(entities, saveQuery);
    }

    private void saveAllByParam(List<E> entities, String saveQuery) {

        try (final Connection connection = connectorDB.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {

            for (E entity : entities) {
                mapEntityToPreparedStatementSave(entity, preparedStatement);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Elements are not added to the table..", e);
        }
    }

    @Override
    public Optional<E> findById(Integer id) {
        return findByParam(id, findByIdQuery, INT_PARAM_SETTER);
    }

    private <P> Optional<E> findByParam(P param, String findByParam,
            BiConsumer<PreparedStatement, P> designatedParamSetter) {

        try (final Connection connection = connectorDB.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(findByParam)) {
            designatedParamSetter.accept(preparedStatement, param);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapResultSetToEntity(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Can't get element from the table by element ID..", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        deleteByParam(id, deleteByIdQuery, INT_PARAM_SETTER);
    }

    private <P> void deleteByParam(P param, String deleteByParam,
            BiConsumer<PreparedStatement, P> designatedParamSetter) {

        try (final Connection connection = connectorDB.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(deleteByParam)) {
            designatedParamSetter.accept(preparedStatement, param);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Element is not deleted from the table..", e);
        }
    }

    @Override
    public List<E> findAll() {
        return findAllByParam(findAllQuery);
    }

    private List<E> findAllByParam(String findAllQuery) {

        List<E> elements = new ArrayList<>();
        try (final Connection connection = connectorDB.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    elements.add(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Can't get all elements from the table..", e);
        }
        return elements;
    }

    @Override
    public List<E> findAll(Integer rawOffset, Integer rawLimit) {
        return findAllByParam(rawOffset, rawLimit, findAllPeginationQuery);
    }

    private List<E> findAllByParam(Integer rawOffset, Integer rawLimit, String findAllPeginationQuery) {

        List<E> elements = new ArrayList<>();
        try (final Connection connection = connectorDB.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(findAllPeginationQuery)) {

            preparedStatement.setInt(1, rawLimit);
            preparedStatement.setInt(2, rawOffset);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    elements.add(mapResultSetToEntity(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Can't get elements from the table..", e);
        }
        return elements;
    }

    @Override
    public void update(E entity) {
        updateByParam(entity, updateQuery);
    }

    private void updateByParam(E entity, String updateQuery) {

        try (final Connection connection = connectorDB.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            mapEntityToPreparedStatementUpdate(entity, preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataBaseSqlRuntimeException("Element is not updated..", e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void mapEntityToPreparedStatementSave(E entity, PreparedStatement preparedStatement)
            throws SQLException;

    protected abstract void mapEntityToPreparedStatementUpdate(E entity, PreparedStatement preparedStatement)
            throws SQLException;
}

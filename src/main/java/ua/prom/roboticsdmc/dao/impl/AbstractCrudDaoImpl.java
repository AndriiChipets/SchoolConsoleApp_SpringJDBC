package ua.prom.roboticsdmc.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ua.prom.roboticsdmc.dao.CrudDao;

@AllArgsConstructor(access = AccessLevel.PROTECTED)

public abstract class AbstractCrudDaoImpl<ID, E> implements CrudDao<ID, E> {

    protected JdbcTemplate jdbcTemplate;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String findAllPeginationQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    @Override
    public void save(E entity) {
        jdbcTemplate.update(saveQuery, getEntityPropertiesToSave(entity));
    }

    @Override
    public void saveAll(List<E> entities) {

        List<Object[]> batch = new ArrayList<>();
        for (E entity : entities) {
            Object[] values = getEntityPropertiesToSave(entity);
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(saveQuery, batch);
    }

    @Override
    public Optional<E> findById(ID id) {
        E entity = null;
        try {
            entity = jdbcTemplate.queryForObject(findByIdQuery, createRowMapper(), id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public void deleteById(ID id) {
        jdbcTemplate.update(deleteByIdQuery, id);
    }

    @Override
    public List<E> findAll() {
        return jdbcTemplate.query(findAllQuery, createRowMapper());
    }

    @Override
    public List<E> findAll(Integer rawOffset, Integer rawLimit) {
        return jdbcTemplate.query(findAllPeginationQuery, createRowMapper(), rawLimit, rawOffset);
    }

    @Override
    public void update(E entity) {
        jdbcTemplate.update(updateQuery, getEntityPropertiesToUpdate(entity));
    }

    protected abstract RowMapper<E> createRowMapper();

    protected abstract Object[] getEntityPropertiesToSave(E entity);

    protected abstract Object[] getEntityPropertiesToUpdate(E entity);
}

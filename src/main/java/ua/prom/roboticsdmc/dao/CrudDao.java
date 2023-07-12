package ua.prom.roboticsdmc.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<ID, E> {

    void save(E entity);

    void saveAll(List<E> entities);

    Optional<E> findById(ID id);

    List<E> findAll();
    
    List<E> findAll(Integer rowOffset, Integer rowLimit);

    void update(E entity);

    void deleteById(ID id);
}

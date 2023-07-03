package ua.prom.roboticsdmc.dao;

import java.util.List;

import ua.prom.roboticsdmc.domain.Group;

public interface GroupDao extends CrudDao<Integer, Group> {

    List<Group> findGroupWithLessOrEqualsStudentQuantity(Integer studentsQuantity);

}


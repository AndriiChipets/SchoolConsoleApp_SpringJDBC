package ua.prom.roboticsdmc.dao;

import java.util.List;

import ua.prom.roboticsdmc.domain.Group;

public interface GroupDao extends CrudDao<Group> {

    List<Group> findGroupWithLessOrEqualsStudentQuantity(int studentsQuantity);

}


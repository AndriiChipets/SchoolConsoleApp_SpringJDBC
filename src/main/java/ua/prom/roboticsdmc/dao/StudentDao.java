package ua.prom.roboticsdmc.dao;

import java.util.List;

import ua.prom.roboticsdmc.domain.Student;

public interface StudentDao extends CrudDao<Integer, Student> {

    List<Student> findStudentsByCourseName(String courseName);

    void addGroupIdtoStudents(List<Student> students);
}

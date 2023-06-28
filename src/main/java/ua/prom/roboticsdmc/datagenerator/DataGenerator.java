package ua.prom.roboticsdmc.datagenerator;

import java.util.List;

import ua.prom.roboticsdmc.domain.Course;
import ua.prom.roboticsdmc.domain.Group;
import ua.prom.roboticsdmc.domain.Student;

public interface DataGenerator {

    List<Student> createRandomStudent();

    List<Group> createRandomGroup();

    List<Course> createCourse();

    List<Student> assignStudentToGroup(List<Group> groups, List<Student> students);

    List<List<Integer>> assignStudentToCourses(List<Student> students, List<Course> courses);

}

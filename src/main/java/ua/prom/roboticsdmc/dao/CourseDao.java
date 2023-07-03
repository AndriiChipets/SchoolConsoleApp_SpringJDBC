package ua.prom.roboticsdmc.dao;

import java.util.List;

import ua.prom.roboticsdmc.domain.Course;

public interface CourseDao extends CrudDao<Integer, Course> {

    List<Course> getAllStudentCoursesByStudentID(Integer studentId);
    
    void addStudentToCourse(Integer studentId, Integer courseId);

    void removeStudentFromCourse(Integer studentId, Integer courseId);
    
    void fillRandomStudentCourseTable(List<List<Integer>> studentCourses);

}

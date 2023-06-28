package ua.prom.roboticsdmc.dao;

import java.util.List;

import ua.prom.roboticsdmc.domain.Course;

public interface CourseDao extends CrudDao<Course> {

    List<Course> getAllStudentCoursesByStudentID(int studentId);
    
    void addStudentToCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int courseId);
    
    void fillRandomStudentCourseTable(List<List<Integer>> studentCourses);

}

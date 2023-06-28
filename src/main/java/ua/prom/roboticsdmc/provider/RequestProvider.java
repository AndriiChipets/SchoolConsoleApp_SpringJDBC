package ua.prom.roboticsdmc.provider;

import java.util.ArrayList;
import java.util.List;

import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.datagenerator.DataGenerator;
import ua.prom.roboticsdmc.domain.Course;
import ua.prom.roboticsdmc.domain.Group;
import ua.prom.roboticsdmc.domain.Student;
import ua.prom.roboticsdmc.tablecreator.TableCreator;

public class RequestProvider {

    private final TableCreator tableCreator;
    private final DataGenerator dataGenerator;
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;

    public RequestProvider(TableCreator tableCreator, DataGenerator dataGenerator, StudentDao studentDao,
            CourseDao courseDao, GroupDao groupDao) {
        this.tableCreator = tableCreator;
        this.dataGenerator = dataGenerator;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
    }
    
    public void provideTablesCreating(String schemaFilePath) {

        tableCreator.createTables(schemaFilePath);
        List<Group> groups = dataGenerator.createRandomGroup();
        List<Course> courses = dataGenerator.createCourse();
        List<Student> students = dataGenerator.createRandomStudent();
        groupDao.saveAll(groups);
        courseDao.saveAll(courses);
        studentDao.saveAll(students);
        groups = new ArrayList<>(groupDao.findAll());
        students = new ArrayList<>(studentDao.findAll());
        List <Student> studentsAssignedToGroups = dataGenerator.assignStudentToGroup(groups, students);
        studentDao.addGroupIdtoStudents(studentsAssignedToGroups);
        students = studentDao.findAll();
        courses = courseDao.findAll();
        List <List<Integer>> studentsAssignedToCourses = dataGenerator.assignStudentToCourses(students, courses);
        courseDao.fillRandomStudentCourseTable(studentsAssignedToCourses);
    }

    public void findGroupWithStudentsQuantity(Integer studentNumber) {
        List<Group> groups = groupDao.findGroupWithLessOrEqualsStudentQuantity(studentNumber);
        groups.forEach(System.out::println);
    }

    public void findStudentByCourseName(String courseName) {
        List<Student> courseStudents = studentDao.findStudentsByCourseName(courseName);
        courseStudents.forEach(System.out::println);
    }

    public void addStudentToCourse(Integer studentId, Integer coursesId) {
        courseDao.addStudentToCourse(studentId, coursesId);
    }

    public void getAllStudentCoursesByStudentID(Integer studentId) {
        List<Course> courses = courseDao.getAllStudentCoursesByStudentID(studentId);
        courses.forEach(System.out::println);
    }
    
    public void removeStudentFromCourse(Integer studentId, Integer courseId) {
        courseDao.removeStudentFromCourse(studentId, courseId);
    }
    
    public void addNewStudent(String firstName, String lastName, Integer groupId) {
        Student student = Student.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withGroupId(groupId).build();
        studentDao.save(student);
    }

    public void findStudentById(Integer studentId) {
        System.out.println(studentDao.findById(studentId));
    }

    public void findAllStudents(Integer rowOffset, Integer rowLimit) {
        List<Student> allStudents = studentDao.findAll(rowOffset, rowLimit);
        allStudents.forEach(System.out::println);
    }

    public void updateStudent(Integer studentId, Integer groupId, String firstName, String lastName) {
        Student student = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName).build();
        studentDao.update(student);
    }
    
    public void deleteStudentById(Integer studentId) {
        studentDao.deleteById(studentId);
    }

    public void addNewGroup(String groupName) {
        Group group = new Group(groupName);
        groupDao.save(group);
    }

    public void findGroupById(Integer groupId) {
        System.out.println(groupDao.findById(groupId));
    }

    public void findAllGroups(Integer rowOffset, Integer rowLimit) {
        List<Group> allGroups = groupDao.findAll(rowOffset, rowLimit);
        allGroups.forEach(System.out::println);
    }

    public void updateGroup(Integer groupId, String groupName) {
        Group group = new Group(groupId, groupName);
        groupDao.update(group);
    }

    public void deleteGroupById(Integer groupId) {
        groupDao.deleteById(groupId);
    }
    
    public void addNewCourse(String courseName, String courseDescription) {
        Course course = Course.builder()
                .withCourseName(courseName)
                .withCourseDescription(courseDescription).build();
        courseDao.save(course);
    }
    
    public void findCourseById(Integer courseId) {
        System.out.println(courseDao.findById(courseId));
    }
    
    public void findAllCourses(Integer rowOffset, Integer rowLimit) {
        List<Course> allCourses = courseDao.findAll(rowOffset, rowLimit);
        allCourses.forEach(System.out::println);
    }
    
    public void findAllCourses() {
        List<Course> allCourses = courseDao.findAll();
        allCourses.forEach(System.out::println);
    }
    
    public void updateCourse(Integer courseId, String courseName, String courseDescription) {
        Course course = Course.builder()
                .withCourseId(courseId)
                .withCourseName(courseName)
                .withCourseDescription(courseDescription).build();
        courseDao.update(course);
    }

    public void deleteCoursetById(Integer courseId) {
        courseDao.deleteById(courseId);
    }
}

package ua.prom.roboticsdmc.controller;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.domain.Course;
import ua.prom.roboticsdmc.domain.Group;
import ua.prom.roboticsdmc.domain.Student;
import ua.prom.roboticsdmc.view.ViewProvider;

@Service
public class FrontController {
    
   private static final String MENU = "\n\t ============ Please, choose what do you want to do ============\n"
            + "1 -> Find all groups with less or equal student quantity \n"
            + "2 -> Find all students related to the course with the given name \n"
            + "3 -> Add a student to the course (from a list) \n"
            + "4 -> Remove the student from one of their courses \n" 
            + "5 -> CRUD_STUDENT save new student \n"
            + "6 -> CRUD_STUDENT find student by the STUDENT_ID \n" 
            + "7 -> CRUD_STUDENT find all students \n"
            + "8 -> CRUD_STUDENT update student \n" 
            + "9 -> CRUD_STUDENT delete student by the STUDENT_ID \n"
            + "10 -> CRUD_GROUP save new group \n" 
            + "11 -> CRUD_GROUP find group by the GROUP_ID \n"
            + "12 -> CRUD_GROUP find all groups \n"
            + "13 -> CRUD_GROUP update group \n"
            + "14 -> CRUD_GROUP delete group by the GROUP_ID \n" 
            + "15 -> CRUD_COURSE save new course \n"
            + "16 -> CRUD_COURSE find course by the COURSE_ID \n" 
            + "17 -> CRUD_COURSE find all courses \n"
            + "18 -> CRUD_COURSE update course \n" 
            + "19 -> CRUD_COURSE delete course by the COURSE_ID \n"
            + "0 -> To exit from the program \n";
   private static final String WRONG_CHOICE_MESSAGE = "Please, make right choice from the list or enter \"0\" to exit from the program";
   private final StudentDao studentDao;
   private final CourseDao courseDao;
   private final GroupDao groupDao;
   private final ViewProvider viewProvider;

   public FrontController(StudentDao studentDao, CourseDao courseDao, GroupDao groupDao, ViewProvider viewProvider) {
       this.studentDao = studentDao;
       this.courseDao = courseDao;
       this.groupDao = groupDao;
       this.viewProvider = viewProvider;
   }

   public void run() {
       boolean isWork = true;
       while (isWork) {
           viewProvider.printMessage(MENU);
           int choice = viewProvider.readInt();
           switch (choice) {
           case 0 -> isWork = false;
           case 1 -> findGroupWithStudentsQuantity();
           case 2 -> findStudentByCourseName();
           case 3 -> addStudentToCourse();
           case 4 -> removeStudentFromCourse();
           case 5 -> addNewStudent();
           case 6 -> findStudentById();
           case 7 -> findAllDeterminedStudents();
           case 8 -> updateStudent();
           case 9 -> deleteStudentById();
           case 10 -> addNewGroup();
           case 11 -> findGroupById();
           case 12 -> findAllDeterminedGroups();
           case 13 -> updateGroup();
           case 14 -> deleteGroupById();
           case 15 -> addNewCourse();
           case 16 -> findCourseById();
           case 17 -> findAllDeterminedCourses();
           case 18 -> updateCourse();
           case 19 -> deleteCoursetById();
           default -> viewProvider.printMessage(WRONG_CHOICE_MESSAGE);
           }
       }
   }
    
    private void findGroupWithStudentsQuantity() {
        viewProvider.printMessage("Enter students quantity: ");
        int studentNumber = viewProvider.readInt();
        List<Group> groups = groupDao.findGroupWithLessOrEqualsStudentQuantity(studentNumber);
        groups.forEach(e -> viewProvider.printMessage(e.toString()));
    }

    private void findStudentByCourseName() {
        viewProvider.printMessage("Choose course name from the list: ");
        findAllCourses();
        viewProvider.printMessage("Enter course name: ");
        String courseName = viewProvider.read();
        List<Student> courseStudents = studentDao.findStudentsByCourseName(courseName);        
        courseStudents.forEach(e -> viewProvider.printMessage(e.toString()));
    }

    private void addStudentToCourse() {
        viewProvider.printMessage("Choose course name from the list to assign student to this course by student ID: ");
        findAllCourses();
        viewProvider.printMessage("Enter course ID: ");
        int courseId = viewProvider.readInt();
        viewProvider.printMessage("Enter student ID: ");
        int studentId = viewProvider.readInt();
        courseDao.addStudentToCourse(studentId, courseId);
    }

    private void getAllStudentCoursesByStudentID(Integer studentId) {
        List<Course> courses = courseDao.getAllStudentCoursesByStudentID(studentId);
        courses.forEach(e -> viewProvider.printMessage(e.toString()));
    }

    private void removeStudentFromCourse() {
        viewProvider.printMessage("Enter student ID who you want to remove from the course: ");
        int studentId = viewProvider.readInt();
        viewProvider.printMessage("Choose course from the list to remove student from his course by student ID: ");
        getAllStudentCoursesByStudentID(studentId);
        viewProvider.printMessage("Enter course ID: ");
        int courseId = viewProvider.readInt();
        courseDao.removeStudentFromCourse(studentId, courseId);
    }

    private void addNewStudent() {
        viewProvider.printMessage("Enter student first name: ");
        String firstName = viewProvider.read();
        viewProvider.printMessage("Enter student last name: ");
        String lastName = viewProvider.read();
        viewProvider.printMessage("Enter group ID: ");
        int groupId = viewProvider.readInt();
        Student student = Student.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withGroupId(groupId)
                .build();
        studentDao.save(student);
    }

    private void findStudentById() {
        viewProvider.printMessage("Enter student ID: ");
        int studentId = viewProvider.readInt();
        viewProvider.printMessage(studentDao.findById(studentId).toString());
    }
 
    private void findAllDeterminedStudents() {
        viewProvider.printMessage("Enter student quantity which you want to skip from beginning of the table: ");
        int rowOffset = viewProvider.readInt();
        viewProvider.printMessage("Enter student quantity you want to display: ");
        int rowLimit = viewProvider.readInt();
        List<Student> allStudents = studentDao.findAll(rowOffset, rowLimit);
        viewProvider.printMessage("The list of students by Student ID", rowOffset, rowLimit);
        allStudents.forEach(e -> viewProvider.printMessage(e.toString()));
    }

    private void updateStudent() {
        viewProvider.printMessage("Enter student ID: ");
        int studentId = viewProvider.readInt();
        viewProvider.printMessage("Enter group ID: ");
        int groupId = viewProvider.readInt();
        viewProvider.printMessage("Enter student first name: ");
        String firstName = viewProvider.read();
        viewProvider.printMessage("Enter student last name: ");
        String lastName = viewProvider.read();
        Student student = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
        studentDao.update(student);
    }

    private void deleteStudentById() {
        viewProvider.printMessage("Enter student ID: ");
        int studentId = viewProvider.readInt();
        studentDao.deleteById(studentId);
    }

    private void addNewGroup() {
        viewProvider.printMessage("Enter group name: ");
        String groupName = viewProvider.read();
        Group group = Group.builder().withGroupName(groupName).build();
        groupDao.save(group);
    }

    private void findGroupById() {
        viewProvider.printMessage("Enter group ID: ");
        int groupId = viewProvider.readInt();
        viewProvider.printMessage(groupDao.findById(groupId).toString());
    }

    private void findAllDeterminedGroups() {
        viewProvider.printMessage("Enter group quantity which you want to skip from beginning of the table: ");
        int rowOffset = viewProvider.readInt();
        viewProvider.printMessage("Enter group quantity you want to display: ");
        int rowLimit = viewProvider.readInt();
        List<Group> allGroups = groupDao.findAll(rowOffset, rowLimit);
        viewProvider.printMessage("The list of groups by Group ID", rowOffset, rowLimit);
        allGroups.forEach(e -> viewProvider.printMessage(e.toString()));
    }

    private void updateGroup() {
        viewProvider.printMessage("Enter group ID: ");
        int groupId = viewProvider.readInt();
        viewProvider.printMessage("Enter group name: ");
        String groupName = viewProvider.read();
        Group group = Group.builder().withGroupId(groupId).withGroupName(groupName).build();
        groupDao.update(group);
    }

    private void deleteGroupById() {
        viewProvider.printMessage("To delete group, enter its group ID: ");
        int grouptId = viewProvider.readInt();
        groupDao.deleteById(grouptId);
    }

    private void addNewCourse() {
        viewProvider.printMessage("Enter course name: ");
        String courseName = viewProvider.read();
        viewProvider.printMessage("Enter course description: ");
        String courseDescription = viewProvider.read();
        Course course = Course.builder()
                .withCourseName(courseName)
                .withCourseDescription(courseDescription)
                .build();
        courseDao.save(course);
    }

    private void findCourseById() {
        viewProvider.printMessage("Enter course ID: ");
        int courseId = viewProvider.readInt();
        viewProvider.printMessage(courseDao.findById(courseId).toString());
    }

    private void findAllDeterminedCourses() {
        viewProvider.printMessage("Enter course quantity which you want to skip from beginning of the table: ");
        int rowOffset = viewProvider.readInt();
        viewProvider.printMessage("Enter course quantity you want to display: ");
        int rowLimit = viewProvider.readInt();
        List<Course> allCourses = courseDao.findAll(rowOffset, rowLimit);
        viewProvider.printMessage("The list of courses by Course ID", rowOffset, rowLimit);
        allCourses.forEach(e -> viewProvider.printMessage(e.toString()));
    }

    private void findAllCourses() {
        List<Course> allCourses = courseDao.findAll();
        allCourses.forEach(e -> viewProvider.printMessage(e.toString()));
    }

    private void updateCourse() {
        viewProvider.printMessage("Enter course ID: ");
        int courseId = viewProvider.readInt();
        viewProvider.printMessage("Enter course name: ");
        String courseName = viewProvider.read();
        viewProvider.printMessage("Enter course description: ");
        String courseDescription = viewProvider.read();
        Course course = Course.builder()
                .withCourseId(courseId)
                .withCourseName(courseName)
                .withCourseDescription(courseDescription)
                .build();
        courseDao.update(course);
    }

    private void deleteCoursetById() {
        viewProvider.printMessage("To delete course, enter its course ID: ");
        int courseId = viewProvider.readInt();
        courseDao.deleteById(courseId);
    }
}

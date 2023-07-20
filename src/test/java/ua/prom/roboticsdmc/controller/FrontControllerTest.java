package ua.prom.roboticsdmc.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.domain.Course;
import ua.prom.roboticsdmc.domain.Group;
import ua.prom.roboticsdmc.domain.Student;
import ua.prom.roboticsdmc.view.ViewProvider;

@DisplayName("FrontControllerTest")
@ExtendWith(MockitoExtension.class)

class FrontControllerTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private CourseDao courseDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private ViewProvider viewProvider;

    @InjectMocks
    private FrontController frontController;

    @Test
    @DisplayName("run() should invoke findGroupWithStudentsQuantity()")
    void run_shouldInvokeMethodFindGroupWithStudentsQuantity_whenChoiceIs1() {

        int studentNumber = 15;
        List<Group> groups = new ArrayList<Group>(Arrays.asList(
                Group.builder()
                .withGroupName("AA-01")
                .build(),
                Group.builder()
                .withGroupName("BB-02")
                .build()));
        
        when(viewProvider.readInt()).thenReturn(1).thenReturn(studentNumber).thenReturn(0);
        when(groupDao.findGroupWithLessOrEqualsStudentQuantity(anyInt())).thenReturn(groups);
        frontController.run();

        verify(groupDao, times(1)).findGroupWithLessOrEqualsStudentQuantity(studentNumber);
    }

    @Test
    @DisplayName("run() should invoke findStudentByCourseName()")
    void run_shouldInvokeMethodFindStudentByCourseName_whenChoiceIs2() {

        String courseName = "Ukranian";      
        List<Course> courses = new ArrayList<Course>(Arrays.asList(
                Course.builder()
                .withCourseId(1)
                .withCourseName("Ukranian")
                .build(),
                Course.builder()
                .withCourseId(2)
                .withCourseName("Physics")
                .build()));       
        List<Student> courseStudents = new ArrayList<Student>(Arrays.asList(
                Student.builder().withStudentId(1)
                                 .withGroupId(5)
                                 .withFirstName("Christopher")
                                 .withLastName("Thomas")
                                 .build(),
                Student.builder().withStudentId(2)
                                 .withGroupId(3)
                                 .withFirstName("Patricia")
                                 .withLastName("Wilson")
                                 .build()));

        when(viewProvider.readInt()).thenReturn(2).thenReturn(0);
        when(courseDao.findAll()).thenReturn(courses);
        when(viewProvider.read()).thenReturn(courseName);
        when(studentDao.findStudentsByCourseName(anyString())).thenReturn(courseStudents);
        frontController.run();

        verify(studentDao, times(1)).findStudentsByCourseName(courseName);
    }

    @Test
    @DisplayName("run() should invoke addStudentToCourse()")
    void run_shouldInvokeMethodAddStudentToCourse_whenChoiceIs3() {

        int studentId = 10;
        int courseId = 1;
        List<Course> courses = new ArrayList<Course>(Arrays.asList(
                Course.builder()
                .withCourseId(1)
                .withCourseName("Ukranian")
                .build(),
                Course.builder()
                .withCourseId(2)
                .withCourseName("Physics")
                .build()));

        when(viewProvider.readInt()).thenReturn(3).thenReturn(courseId).thenReturn(studentId).thenReturn(0);
        when(courseDao.findAll()).thenReturn(courses);
        frontController.run();

        verify(courseDao, times(1)).addStudentToCourse(studentId, courseId);
    }

    @Test
    @DisplayName("run() should invoke removeStudentFromCourse()")
    void run_shouldInvokeMethodRemoveStudentFromCourse_whenChoiceIs4() {

        int studentId = 10;
        int courseId = 1;
        List<Course> courses = new ArrayList<Course>(Arrays.asList(
                Course.builder()
                .withCourseId(1)
                .withCourseName("Ukranian")
                .build(),
                Course.builder()
                .withCourseId(2)
                .withCourseName("Physics")
                .build()));

        when(viewProvider.readInt()).thenReturn(4).thenReturn(studentId).thenReturn(courseId).thenReturn(0);
        when(courseDao.getAllStudentCoursesByStudentID(anyInt())).thenReturn(courses);
        frontController.run();

        verify(courseDao, times(1)).removeStudentFromCourse(studentId, courseId);
    }

    @Test
    @DisplayName("run() should invoke addNewStudent()")
    void run_shouldInvokeMethodAddNewStudent_whenChoiceIs5() {

        int groupId = 4;
        String firstName = "James";
        String lastName = "Garcia";
        Student student = Student.builder()
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        when(viewProvider.readInt()).thenReturn(5).thenReturn(groupId).thenReturn(0);
        when(viewProvider.read()).thenReturn(firstName).thenReturn(lastName);
        frontController.run();

        verify(studentDao, times(1)).save(student);
    }

    @Test
    @DisplayName("run() should invoke findStudentById()")
    void run_shouldInvokeMethodFindStudentById_whenChoiceIs6() {

        int studentId = 10;

        when(viewProvider.readInt()).thenReturn(6).thenReturn(studentId).thenReturn(0);
        frontController.run();

        verify(studentDao, times(1)).findById(studentId);
    }

    @Test
    @DisplayName("run() should invoke findAllDeterminedStudents()")
    void run_shouldInvokeMethodFindAllDeterminedStudents_whenChoiceIs7() {

        int rowOffset = 10;
        int rowLimit = 20;
        List<Student> students = new ArrayList<Student>(Arrays.asList(
                Student.builder().withStudentId(1)
                                 .withGroupId(5)
                                 .withFirstName("Christopher")
                                 .withLastName("Thomas").build(),
                Student.builder().withStudentId(2)
                                 .withGroupId(3)
                                 .withFirstName("Patricia")
                                 .withLastName("Wilson").build()));

        when(viewProvider.readInt()).thenReturn(7).thenReturn(rowOffset).thenReturn(rowLimit).thenReturn(0);
        when(studentDao.findAll(anyInt(), anyInt())).thenReturn(students);
        frontController.run();

        verify(studentDao, times(1)).findAll(rowOffset, rowLimit);
    }

    @Test
    @DisplayName("run() should invoke updateStudent()")
    void run_shouldInvokeMethodUpdateStudent_whenChoiceIs8() {

        int studentId = 10;
        int groupId = 4;
        String firstName = "James";
        String lastName = "Garcia";
        Student student = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        when(viewProvider.readInt()).thenReturn(8).thenReturn(studentId).thenReturn(groupId).thenReturn(0);
        when(viewProvider.read()).thenReturn(firstName).thenReturn(lastName);
        frontController.run();
        
        verify(studentDao, times(1)).update(student);
    }

    @Test
    @DisplayName("run() should invoke deleteStudentById()")
    void run_shouldInvokeMethodDeleteStudentById_whenChoiceIs9() {

        int studentId = 10;

        when(viewProvider.readInt()).thenReturn(9).thenReturn(studentId).thenReturn(0);
        frontController.run();
        
        verify(studentDao, times(1)).deleteById(studentId);
    }

    @Test
    @DisplayName("run() should invoke addNewGroup()")
    void run_shouldInvokeMethodAddNewGroup_whenChoiceIs10() {

        String groupName = "AA-00";
        Group group =Group.builder()
                .withGroupName(groupName)
                .build();

        when(viewProvider.readInt()).thenReturn(10).thenReturn(0);
        when(viewProvider.read()).thenReturn(groupName);
        frontController.run();
        
        verify(groupDao, times(1)).save(group);
    }

    @Test
    @DisplayName("run() should invoke findGroupById()")
    void run_shouldInvokeMethodFindGroupById_whenChoiceIs11() {

        int groupId = 6;

        when(viewProvider.readInt()).thenReturn(11).thenReturn(groupId).thenReturn(0);
        frontController.run();
        
        verify(groupDao, times(1)).findById(groupId);
    }

    @Test
    @DisplayName("run() should invoke findAllDeterminedGroups()")
    void run_shouldInvokeMethodFindAllDeterminedGroups_whenChoiceIs12() {

        int rowOffset = 0;
        int rowLimit = 5;
        List<Group> groups = new ArrayList<Group>(Arrays.asList(
                Group.builder()
                .withGroupName("AA-01")
                .build(),
                Group.builder()
                .withGroupName("BB-02")
                .build()));

        when(viewProvider.readInt()).thenReturn(12).thenReturn(rowOffset).thenReturn(rowLimit).thenReturn(0);
        when(groupDao.findAll(anyInt(), anyInt())).thenReturn(groups);
        frontController.run();

        verify(groupDao, times(1)).findAll(rowOffset, rowLimit);
    }

    @Test
    @DisplayName("run() should invoke updateGroup()")
    void run_shouldInvokeMethodUpdateGroup_whenChoiceIs13() {

        int groupId = 6;
        String groupName = "AA-00";
        Group group = Group.builder()
                .withGroupId(groupId)
                .withGroupName(groupName)
                .build();

        when(viewProvider.readInt()).thenReturn(13).thenReturn(groupId).thenReturn(0);
        when(viewProvider.read()).thenReturn(groupName);
        frontController.run();

        verify(groupDao, times(1)).update(group);
    }

    @Test
    @DisplayName("run() should invoke deleteGroupById()")
    void run_shouldInvokeMethodDeleteGroupById_whenChoiceIs14() {

        int groupId = 6;

        when(viewProvider.readInt()).thenReturn(14).thenReturn(groupId).thenReturn(0);
        frontController.run();

        verify(groupDao, times(1)).deleteById(groupId);
    }

    @Test
    @DisplayName("run() should invoke addNewCourse()")
    void run_shouldInvokeMethodAddNewCourse_whenChoiceIs15() {

        String courseName = "Math";
        String courseDescription = "New course";
        Course course = Course.builder()
                .withCourseName(courseName)
                .withCourseDescription(courseDescription)
                .build();

        when(viewProvider.readInt()).thenReturn(15).thenReturn(0);
        when(viewProvider.read()).thenReturn(courseName).thenReturn(courseDescription);
        frontController.run();

        verify(courseDao, times(1)).save(course);
    }

    @Test
    @DisplayName("run() should invoke findCourseById()")
    void run_shouldInvokeMethodFindCourseById_whenChoiceIs16() {

        int courseId = 1;

        when(viewProvider.readInt()).thenReturn(16).thenReturn(courseId).thenReturn(0);
        frontController.run();

        verify(courseDao, times(1)).findById(courseId);
    }

    @Test
    @DisplayName("run() should invoke findAllDeterminedCourses()")
    void run_shouldInvokeMethodFindAllDeterminedCourses_whenChoiceIs17() {

        int rowOffset = 4;
        int rowLimit = 5;
        List<Course> courses = new ArrayList<Course>(Arrays.asList(
                Course.builder()
                .withCourseId(1)
                .withCourseName("Ukranian")
                .withCourseDescription("Any description")
                .build(),
                Course.builder()
                .withCourseId(2)
                .withCourseName("Physics")
                .withCourseDescription("Any description")
                .build()));

        when(viewProvider.readInt()).thenReturn(17).thenReturn(rowOffset).thenReturn(rowLimit).thenReturn(0);
        when(courseDao.findAll(anyInt(), anyInt())).thenReturn(courses);
        frontController.run();

        verify(courseDao, times(1)).findAll(rowOffset, rowLimit);
    }

    @Test
    @DisplayName("run() should invoke updateCourse()")
    void run_shouldInvokeMethodUpdateCourse_whenChoiceIs18() {

        int courseId = 6;
        String courseName = "Math";
        String courseDescription = "New course";
        Course course = Course.builder()
                .withCourseId(courseId)
                .withCourseName(courseName)
                .withCourseDescription(courseDescription)
                .build();

        when(viewProvider.readInt()).thenReturn(18).thenReturn(courseId).thenReturn(0);
        when(viewProvider.read()).thenReturn(courseName).thenReturn(courseDescription);
        frontController.run();

        verify(courseDao, times(1)).update(course);
    }

    @Test
    @DisplayName("run() should invoke deleteCoursetById()")
    void run_shouldInvokeMethodDeleteCoursetById_whenChoiceIs19() {

        int courseId = 1;

        when(viewProvider.readInt()).thenReturn(19).thenReturn(courseId).thenReturn(0);
        frontController.run();

        verify(courseDao, times(1)).deleteById(courseId);
    }

    @Test
    @DisplayName("run() should print message when choice is incorrect")
    void run_shouldPrintMessage_whenChoiceIsIncorrect() {

        int choise = 1000;
        String wrongChoiceMessage = "Please, make right choice from the list or enter \"0\" to exit from the program";

        when(viewProvider.readInt()).thenReturn(choise).thenReturn(0);
        frontController.run();

        verify(viewProvider, times(1)).printMessage(wrongChoiceMessage);
    }
}

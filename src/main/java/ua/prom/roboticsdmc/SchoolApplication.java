package ua.prom.roboticsdmc;

import java.util.Scanner;

import ua.prom.roboticsdmc.dao.ConnectorDB;
import ua.prom.roboticsdmc.dao.CourseDao;
import ua.prom.roboticsdmc.dao.GroupDao;
import ua.prom.roboticsdmc.dao.StudentDao;
import ua.prom.roboticsdmc.dao.impl.CourseDaoImpl;
import ua.prom.roboticsdmc.dao.impl.GroupDaoImpl;
import ua.prom.roboticsdmc.dao.impl.StudentDaoImpl;
import ua.prom.roboticsdmc.datagenerator.DataGenerator;
import ua.prom.roboticsdmc.datagenerator.DataGeneratorImpl;
import ua.prom.roboticsdmc.provider.RequestProvider;
import ua.prom.roboticsdmc.tablecreator.TableCreator;
import ua.prom.roboticsdmc.tablecreator.TableCreatorImpl;

public class SchoolApplication {
    
    public static void main(String[] args) {

        String stopWord = "-";
        String schemaFilePath = "src/main/resources/sgl/schema.sql";
        String menu = "\n\t ============ Please, choose what do you want to do ============\n"
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
                + "\"-\" -> To exit from the program \n";

        ConnectorDB connectorDB = new ConnectorDB("database");
        TableCreator tableCreator = new TableCreatorImpl(connectorDB);
        DataGenerator dataGenerator = new DataGeneratorImpl();
        StudentDao studentDao = new StudentDaoImpl(connectorDB);
        CourseDao courseDao = new CourseDaoImpl(connectorDB);
        GroupDao groupDao = new GroupDaoImpl(connectorDB);

        try (Scanner scanner = new Scanner(System.in)) {

            RequestProvider requestProvider = new RequestProvider(tableCreator, dataGenerator, studentDao,
                    courseDao, groupDao);

            requestProvider.provideTablesCreating(schemaFilePath);

            System.out.println(menu);

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                if (command.equals(stopWord)) {
                    break;
                }
                if (command.equals("1")) {
                    System.out.print("Enter students quantity: ");
                    int studentNumber = Integer.parseInt(scanner.nextLine());
                    requestProvider.findGroupWithStudentsQuantity(studentNumber);
                } else if (command.equals("2")) {
                    System.out.println("Choose course name from the list: ");
                    requestProvider.findAllCourses();
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    requestProvider.findStudentByCourseName(courseName);
                } else if (command.equals("3")) {
                    System.out.println("Choose course name from the list to assign student to this course by student ID: ");
                    requestProvider.findAllCourses();
                    System.out.print("Enter course ID: ");
                    int courseId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter student ID: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    requestProvider.addStudentToCourse(studentId, courseId);
                } else if (command.equals("4")) {
                    System.out.print("Enter student ID who you want to remove from the course: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Choose course from the list to remove student from his course by student ID: ");
                    requestProvider.getAllStudentCoursesByStudentID(studentId);
                    System.out.print("Enter course ID: ");
                    int courseId = Integer.parseInt(scanner.nextLine());
                    requestProvider.removeStudentFromCourse(studentId, courseId);
                } else if (command.equals("5")) {
                    System.out.print("Enter student first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter student last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter group ID: ");
                    int groupId = Integer.parseInt(scanner.nextLine());
                    requestProvider.addNewStudent(firstName, lastName, groupId);
                } else if (command.equals("6")) {
                    System.out.print("Enter student ID: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    requestProvider.findStudentById(studentId);
                } else if (command.equals("7")) {
                    System.out.print("Enter student quantity which you want to skip from beginning of the table: ");
                    int rowOffset = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter student quantity you want to display: ");
                    int rowLimit = Integer.parseInt(scanner.nextLine());
                    System.out.printf("The list of students by Student ID (%d - %d): \n", rowOffset + 1,
                            rowOffset + rowLimit);
                    requestProvider.findAllStudents(rowOffset, rowLimit);
                } else if (command.equals("8")) {
                    System.out.print("Enter student ID: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter group ID: ");
                    int groupId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter student first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter student last name: ");
                    String lastName = scanner.nextLine();
                    requestProvider.updateStudent(studentId, groupId, firstName, lastName);
                } else if (command.equals("9")) {
                    System.out.print("To delete student, enter its student ID: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    requestProvider.deleteStudentById(studentId);
                } else if (command.equals("10")) {
                    System.out.print("Enter group name: ");
                    String groupName = scanner.nextLine();
                    requestProvider.addNewGroup(groupName);
                } else if (command.equals("11")) {
                    System.out.print("Enter group ID: ");
                    int groupId = Integer.parseInt(scanner.nextLine());
                    requestProvider.findGroupById(groupId);
                } else if (command.equals("12")) {
                    System.out.print("Enter group quantity which you want to skip from beginning of the table: ");
                    int rowOffset = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter group quantity you want to display: ");
                    int rowLimit = Integer.parseInt(scanner.nextLine());
                    System.out.printf("The list of groups by Group ID (%d - %d): \n", rowOffset + 1,
                            rowOffset + rowLimit);
                    requestProvider.findAllGroups(rowOffset, rowLimit);
                } else if (command.equals("13")) {
                    System.out.print("Enter group ID: ");
                    int groupId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter group name: ");
                    String groupName = scanner.nextLine();
                    requestProvider.updateGroup(groupId, groupName);
                } else if (command.equals("14")) {
                    System.out.print("To delete group, enter its group ID: ");
                    int grouptId = Integer.parseInt(scanner.nextLine());
                    requestProvider.deleteGroupById(grouptId);
                } else if (command.equals("15")) {
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Enter course description: ");
                    String courseDescription = scanner.nextLine();
                    requestProvider.addNewCourse(courseName, courseDescription);
                } else if (command.equals("16")) {
                    System.out.print("Enter course ID: ");
                    int courseId = Integer.parseInt(scanner.nextLine());
                    requestProvider.findCourseById(courseId);
                } else if (command.equals("17")) {
                    System.out.print("Enter course quantity which you want to skip from beginning of the table: ");
                    int rowOffset = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter course quantity you want to display: ");
                    int rowLimit = Integer.parseInt(scanner.nextLine());
                    System.out.printf("The list of courses by Course ID (%d - %d): \n", rowOffset + 1,
                            rowOffset + rowLimit);
                    requestProvider.findAllCourses(rowOffset, rowLimit);
                } else if (command.equals("18")) {
                    System.out.print("Enter course ID: ");
                    int courseId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Enter course description: ");
                    String courseDescription = scanner.nextLine();
                    requestProvider.updateCourse(courseId, courseName, courseDescription);
                } else if (command.equals("19")) {
                    System.out.print("To delete course, enter its course ID: ");
                    int coursetId = Integer.parseInt(scanner.nextLine());
                    requestProvider.deleteCoursetById(coursetId);
                } else {
                    System.out.print("Please, make right choise from the list or enter \"-\" to exit from the program");
                }
                System.out.print(menu);
            }
        }
        System.out.println("The program is stopped");
    }
}

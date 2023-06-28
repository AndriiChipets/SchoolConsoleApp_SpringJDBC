package ua.prom.roboticsdmc.datagenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ua.prom.roboticsdmc.domain.Course;
import ua.prom.roboticsdmc.domain.Group;
import ua.prom.roboticsdmc.domain.Student;

public class DataGeneratorImpl implements DataGenerator {

    private List<Course> courses;
    private List<Student> students;
    private List<Group> groups;

    public DataGeneratorImpl() {
        this.courses = createCourse();
        this.students = createRandomStudent();
        this.groups = createRandomGroup();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public List<Student> createRandomStudent() {
        
        final List<String> firstNames = new ArrayList<>(Arrays.asList("James", "Robert", "John", "Michael", "David",
                "William", "Richard", "Joseph", "Thomas", "Christopher", "Mary", "Patricia", "Jennifer", "Linda",
                "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Karen"));
        final List<String> lastNames = new ArrayList<>(Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones",
                "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson",
                "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"));
        List<Student> studentNames = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < 200; i++) {
            Student student = Student.builder()
                    .withFirstName(firstNames.get(random.nextInt(firstNames.size())))
                    .withLastName(lastNames.get(random.nextInt(lastNames.size()))).build();
            studentNames.add(student);
        }
        return studentNames;
    }

    @Override
    public List<Group> createRandomGroup() {
        
        final char[] enAlphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        final char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        List<Group> newGroups = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            StringBuilder lettersBldr = new StringBuilder();
            StringBuilder numbersBldr = new StringBuilder();
            for (int j = 0; j < 2; j++) {
                lettersBldr.append(enAlphabet[random.nextInt(enAlphabet.length)]);
                numbersBldr.append(numbers[random.nextInt(numbers.length)]);
            }
            newGroups.add(new Group(0, String.format("%s-%s", lettersBldr, numbersBldr)));
        }
        return newGroups;
    }

    @Override
    public List<Course> createCourse() {
        
        final List<String> courseNames = new ArrayList<>(Arrays.asList("Math", "Biology", "Philosophy", "Literature",
                "Science of law", "Physics", "Chemistry", "Ukrainian", "English", "Astronomy"));
        List<Course> newCourses = new ArrayList<>();
        
        for (int i = 0; i < courseNames.size(); i++) {
            Course course = Course.builder().withCourseName(courseNames.get(i)).build();
            newCourses.add(course);
        }
        return newCourses;
    }

    @Override
    public List<Student> assignStudentToGroup(List<Group> groups, List<Student> students) {
        
        int studentsQuantity = students.size();
        boolean isFreeStudent = studentsQuantity > 0;

        for (int i = 1; i <= groups.size(); i++) {
            if (!isFreeStudent) {
                break;
            }
            int studentsInGroup = setRandomInterval(10, 31);
            for (int j = 0; j < studentsInGroup; j++) {
                Student studentWithoutGroup = students.get(studentsQuantity - 1);
                Student studentWithGroup = Student.builder()
                        .withStudentId(studentWithoutGroup.getStudentId())
                        .withGroupId(i)
                        .withFirstName(studentWithoutGroup.getFirstName())
                        .withLastName(studentWithoutGroup.getLastName())
                        .build();
                students.set(studentsQuantity - 1, studentWithGroup);
                studentsQuantity--;
                if (studentsQuantity <= 0) {
                    isFreeStudent = false;
                    break;
                }
            }
        }
        return students;
    }

    @Override
    public List<List<Integer>> assignStudentToCourses(List<Student> students, List<Course> courses) {
        
        List<List<Integer>> studentCourses = new ArrayList<>();
        List<Integer> coursesId = null;
        Random random = new Random();

        for (int i = 1; i <= students.size(); i++) {
            int numCourcesPerStudent = random.nextInt(1, 4);
            coursesId = new ArrayList<>();
            for (int j = 0; j < numCourcesPerStudent; j++) {
                int randomCourseId = courses.get(random.nextInt(courses.size()-1)).getCourseId();
                coursesId.add(randomCourseId);
            }
            studentCourses.add(coursesId);
        }
        return studentCourses;
    }

    public int setRandomInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}

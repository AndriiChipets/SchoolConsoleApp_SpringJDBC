An application that inserts/updates/deletes data in the database using Spring boot Jdbc. PostgreSQL DB is used.

1. Created a service layer on the top of DAO to implement the following requirements:
- Find all groups with less or equals student count;
- Find all students related to the course with the given name;
- Add a new student;
- Delete a student by STUDENT_ID;
- Add a student to the course;
- Remove the student from one of their courses;

2. Created a generator service that will be called if the database is empty:
- Create 10 groups with randomly generated names. The name should contain 2 characters, hyphen, 2 numbers;
- Create 10 courses (math, biology, etc.);
- Create 200 students. Take 20 first names and 20 last names and randomly combine them to generate students;
- Randomly assign students to the groups. Each group can contain from 10 to 30 students. It is possible that some groups are without students or students without groups;
- Create the relation MANY-TO-MANY between the tables STUDENTS and COURSES. Randomly assign from 1 to 3 courses for each student;

3. Used ApplicationRunner interface as an entry point for triggering generator
4. Covered services with tests using a mocked DAO layer
![image-1](https://github.com/AndriiChipets/SchoolConsoleApp_SpringJDBC/assets/137887124/74bce8ad-fe39-43fc-9597-decb6d08ea39)
![image-2](https://github.com/AndriiChipets/SchoolConsoleApp_SpringJDBC/assets/137887124/a157ade9-8bec-4c50-997e-05829dca6445)
![image-3](https://github.com/AndriiChipets/SchoolConsoleApp_SpringJDBC/assets/137887124/fc8d59f5-26d0-46c4-b73b-c5d26444ba26)
![image-4](https://github.com/AndriiChipets/SchoolConsoleApp_SpringJDBC/assets/137887124/250389a6-0a0a-4af9-b8d2-721b3b54f6f7)
![image-5](https://github.com/AndriiChipets/SchoolConsoleApp_SpringJDBC/assets/137887124/5cbdd125-e332-4404-89f6-da0ac420f1d9)


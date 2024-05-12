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
![image](/uploads/e27ae11fd4c7040c321aa354e5e18c2f/image.png)
![image](/uploads/de8cc87b9f86dc51cf11d8582dcfdcd1/image.png)
![image](/uploads/b275af0203804ce7a249a7e7a5ec7643/image.png)

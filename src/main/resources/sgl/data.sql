-- In my application this file have not used, it is just for program testing

--Enter data to the "groups" table
INSERT INTO school_app_schema.groups(group_name) VALUES ('TestGroup1');
INSERT INTO school_app_schema.groups(group_name) VALUES ('TestGroup2');
INSERT INTO school_app_schema.groups(group_name) VALUES ('TestGroup3');
INSERT INTO school_app_schema.groups(group_name) VALUES ('TestGroup4');
INSERT INTO school_app_schema.groups(group_name) VALUES ('TestGroup5');

--Enter data to the "students" table
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (1, 'first_name1', 'last_name1');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (2, 'first_name2', 'last_name2');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (3, 'first_name3', 'last_name3');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (4, 'first_name4', 'last_name4');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (5, 'first_name5', 'last_name5');

--Enter data to the "courses" table
INSERT INTO school_app_schema.courses(course_name, course_description) VALUES ('course_name1', 'course_description1');
INSERT INTO school_app_schema.courses(course_name, course_description) VALUES ('course_name2', 'course_description2');
INSERT INTO school_app_schema.courses(course_name, course_description) VALUES ('course_name3', 'course_description3');
INSERT INTO school_app_schema.courses(course_name, course_description) VALUES ('course_name4', 'course_description4');
INSERT INTO school_app_schema.courses(course_name, course_description) VALUES ('course_name5', 'course_description5');

--Enter data to the "students_courses" table
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (1, 2);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (1, 3);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (1, 4);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (2, 2);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (2, 4);

--SELECT * FROM school_app_schema.groups
--SELECT * FROM school_app_schema.students
--SELECT * FROM school_app_schema.courses
--SELECT * FROM school_app_schema.students_courses
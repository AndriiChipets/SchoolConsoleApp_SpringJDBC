--Enter data to the "groups" table
INSERT INTO school_app_schema.groups(group_name) VALUES ('YY-58');
INSERT INTO school_app_schema.groups(group_name) VALUES ('VA-90');
INSERT INTO school_app_schema.groups(group_name) VALUES ('VA-52');
INSERT INTO school_app_schema.groups(group_name) VALUES ('FF-49');
INSERT INTO school_app_schema.groups(group_name) VALUES ('SR-71');

--Enter data to the "students" table
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (1, 'Michael', 'Thomas');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (2, 'Christopher', 'Garcia');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (2, 'Patricia', 'Garcia');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (4, 'Patricia', 'Jackson');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (5, 'William', 'Wilson');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (4, 'James', 'Williams');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (2, 'Robert', 'Rodriguez');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (1, 'John', 'Martinez');
INSERT INTO school_app_schema.students(group_id, first_name, last_name) VALUES (5, 'Karen', 'Garcia');

--Enter data to the "courses" table
INSERT INTO school_app_schema.courses(course_name) VALUES ('Math');
INSERT INTO school_app_schema.courses(course_name) VALUES ('Biology');
INSERT INTO school_app_schema.courses(course_name) VALUES ('Philosophy');
INSERT INTO school_app_schema.courses(course_name) VALUES ('Literature');
INSERT INTO school_app_schema.courses(course_name) VALUES ('English');
INSERT INTO school_app_schema.courses(course_name) VALUES ('Chemistry');

--Enter data to the "students_courses" table
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (1, 1);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (2, 3);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (3, 4);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (4, 2);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (5, 4);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (6, 2);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (6, 3);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (3, 5);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (7, 2);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (8, 4);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (9, 2);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (1, 3);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (9, 4);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (3, 2);
INSERT INTO school_app_schema.students_courses(student_id, course_id) VALUES (2, 6);

--SELECT * FROM school_app_schema.groups ORDER BY group_id ASC
--SELECT * FROM school_app_schema.students ORDER BY student_id ASC
--SELECT * FROM school_app_schema.courses ORDER BY course_id ASC
--SELECT * FROM school_app_schema.students_courses
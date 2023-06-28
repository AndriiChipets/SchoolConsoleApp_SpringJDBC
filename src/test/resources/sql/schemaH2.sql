-- SCHEMA: school_app_schema
-- DROP SCHEMA IF EXISTS school_app_schema; 
CREATE SCHEMA IF NOT EXISTS school_app_schema
AUTHORIZATION school_app_user;

-- Drop all tables
DROP TABLE IF EXISTS school_app_schema.groups CASCADE;
DROP TABLE IF EXISTS school_app_schema.students CASCADE;
DROP TABLE IF EXISTS school_app_schema.courses CASCADE;
DROP TABLE IF EXISTS school_app_schema.students_courses CASCADE;

-- TABLE: groups
CREATE TABLE IF NOT EXISTS school_app_schema.groups (
    group_id SERIAL PRIMARY KEY,
    group_name character(40) UNIQUE
);
    
-- TABLE: students
CREATE TABLE IF NOT EXISTS school_app_schema.students (
    student_id SERIAL PRIMARY KEY,
    group_id int,
    first_name character(30) NOT NULL,
    last_name character(30) NOT NULL
);
    
--TABLE: courses
CREATE TABLE IF NOT EXISTS school_app_schema.courses (
    course_id SERIAL PRIMARY KEY,
    course_name character(30) UNIQUE,
    course_description character(255)
);
    
-- TABLE: students_courses - this is a table for implementation MANY TO MANY relationship
CREATE TABLE IF NOT EXISTS school_app_schema.students_courses (
    student_id int,
    course_id int,
    FOREIGN KEY (student_id) 
    REFERENCES school_app_schema.students(student_id)
    ON DELETE CASCADE,
    FOREIGN KEY (course_id) 
    REFERENCES school_app_schema.courses(course_id)
    ON DELETE CASCADE
);

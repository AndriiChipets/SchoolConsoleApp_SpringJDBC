package ua.prom.roboticsdmc.domain;

import java.util.Objects;

public class Course {

    private final int courseId;
    private final String courseName;
    private final String courseDescription;

    public Course(Builder builder) {
        this.courseId = builder.courseId;
        this.courseName = builder.courseName != null ? builder.courseName.trim() : null;
        this.courseDescription = builder.courseDescription;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, courseDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null||getClass()!=o.getClass()) {
            return false;
        }
        Course course =(Course) o;
        return Objects.equals(courseId, course.courseId)
                && Objects.equals(courseName, course.courseName)
                && Objects.equals(courseDescription, course.courseDescription);
    }

    @Override
    public String toString() {
        return "CourseID = " + courseId + "\t" + "CourseName = " + courseName + "\t" + "CourseDescription = "
                + courseDescription;
    }

    public static class Builder {

        private int courseId;
        private String courseName;
        private String courseDescription;

        private Builder() {
        }

        public Builder withCourseId(int courseId) {
            this.courseId = courseId;
            return this;
        }

        public Builder withCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder withCourseDescription(String courseDescription) {
            this.courseDescription = courseDescription;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }
}

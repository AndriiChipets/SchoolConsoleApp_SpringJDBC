package ua.prom.roboticsdmc.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class Course {

    private final int courseId;
    @ToString.Include(name = "\t" + "CourseName")
    private final String courseName;
    @ToString.Include(name = "\t" + "CourseDescription")
    private final String courseDescription;

    public static class CourseBuilder {

        public CourseBuilder withCourseName(String courseName) {
            this.courseName = courseName != null ? courseName.trim() : null;
            return this;
        }
    }
}

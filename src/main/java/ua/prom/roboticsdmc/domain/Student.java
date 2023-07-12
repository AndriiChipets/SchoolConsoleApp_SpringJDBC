package ua.prom.roboticsdmc.domain;

import java.util.Objects;

public class Student {

    private final int studentId;
    private final int groupId;
    private final String firstName;
    private final String lastName;

    public Student(Builder builder) {
        this.studentId = builder.studentId;
        this.groupId = builder.groupId;
        this.firstName = builder.firstName != null ? builder.firstName.trim() : null;
        this.lastName = builder.lastName != null ? builder.lastName.trim() : null;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public int getGroupId() {
        return groupId;
    }
    
    public int getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, groupId, firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null|| getClass()!=o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals (studentId, student.studentId)
                && Objects.equals (groupId, student.groupId)
                && Objects.equals (firstName, student.firstName)
                && Objects.equals (lastName, student.lastName);
    }

    @Override
    public String toString() {
        return "StudentID = " + studentId + "\t" + "GroupID = " + groupId + "\t" + firstName.trim() + " " + lastName.trim();
    }
    
    public static class Builder {

        private int studentId;
        private int groupId;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder withStudentId(int studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder withGroupId(int groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }    
}

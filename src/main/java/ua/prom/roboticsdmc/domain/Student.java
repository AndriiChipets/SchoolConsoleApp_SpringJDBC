package ua.prom.roboticsdmc.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class Student {

    private final int studentId;
    @ToString.Include(name = "\t" + "GroupID")
    private final int groupId;
    @ToString.Include(name = "\t" + "FirstName")
    private final String firstName;
    @ToString.Include(name = "\t" + "LastName")
    private final String lastName;

    public static class StudentBuilder {

        public StudentBuilder withFirstName(String firstName) {
            this.firstName = firstName != null ? firstName.trim() : null;
            return this;
        }

        public StudentBuilder withLastName(String lastName) {
            this.lastName = lastName != null ? lastName.trim() : null;
            return this;
        }
    }
}

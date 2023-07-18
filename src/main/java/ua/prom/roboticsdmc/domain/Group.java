package ua.prom.roboticsdmc.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class Group {

    private final int groupId;
    @ToString.Include(name = "\t" + "GroupName")
    private final String groupName;

    public static class GroupBuilder {

        public GroupBuilder withGroupName(String groupName) {
            this.groupName = groupName != null ? groupName.trim() : null;
            return this;
        }
    }
}

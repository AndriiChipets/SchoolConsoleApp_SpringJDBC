package ua.prom.roboticsdmc.domain;

import java.util.Objects;

public class Group {

    private int groupId;
    private String groupName;

    public Group(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName != null ? groupName.trim() : null;
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return Objects.equals(groupId, group.groupId) 
                && Objects.equals(groupName, group.groupName);
    }

    @Override
    public String toString() {
        return "GroupID = " + groupId + "\t" + "GroupName = " + groupName;
    }
}

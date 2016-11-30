package com.example.andy.api_model;

import java.util.ArrayList;

/**
 * POJO representing an array of groups from the REST API
 * @author Andrew Jarombek
 * @since 11/29/2016
 */

public class GroupArray {

    private ArrayList<Group> groups;

    @Override
    public String toString() {
        String groupsString = "Groups: [";
        for (Group group: groups) {
            groupsString += group.toString();
        }
        groupsString += "]";
        return groupsString;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
}

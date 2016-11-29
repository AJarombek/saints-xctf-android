package com.example.andy.api_model;

import java.util.ArrayList;

/**
 * POJO representing an array of groups from the REST API
 * @author Andrew Jarombek
 * @since 11/29/2016
 */

public class GroupArray {

    private ArrayList<Group> groups;

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
}

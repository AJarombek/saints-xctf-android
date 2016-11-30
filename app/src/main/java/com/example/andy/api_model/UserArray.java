package com.example.andy.api_model;

import java.util.ArrayList;

/**
 * POJO representing an array of users from the REST API
 * @author Andrew Jarombek
 * @since 11/29/2016
 */

public class UserArray {

    private ArrayList<User> users;

    @Override
    public String toString() {
        String usersString = "Users: [";
        for (User user: users) {
            usersString += user.toString();
        }
        usersString += "]";
        return usersString;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}

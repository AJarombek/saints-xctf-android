package com.example.andy.api_model;

/**
 * POJO representing a users group information from the REST API
 * @author Andrew Jarombek
 * @since 3/1/2017
 */

public class GroupInfo {

    private String group_name;
    private String group_title;
    private String newest_log;
    private String newest_message;

    @Override
    public String toString() {
        return "GroupInfo: [ group_name: " + group_name + ", group_title: " + group_title +
                ", newest_log: " + newest_log + ", newest_message: " + newest_message + "]";
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }

    public String getNewest_log() {
        return newest_log;
    }

    public void setNewest_log(String newest_log) {
        this.newest_log = newest_log;
    }

    public String getNewest_message() {
        return newest_message;
    }

    public void setNewest_message(String newest_message) {
        this.newest_message = newest_message;
    }
}

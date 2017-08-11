package com.example.andy.api_model.pojos;

/**
 * POJO representing a users group information from the REST API
 * @author Andrew Jarombek
 * @since 3/1/2017
 */

public class GroupInfo {

    private String group_name;
    private String group_title;
    private String status;
    private String user;
    private String newest_log;
    private String newest_message;

    @Override
    public String toString() {
        return "GroupInfo: [ group_name: " + group_name + ", group_title: " + group_title +
                ", status: " + status + ", user: " + user + ", newest_log: " + newest_log +
                ", newest_message: " + newest_message + "]";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

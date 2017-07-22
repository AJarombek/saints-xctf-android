package com.example.andy.api_model;

import java.util.Date;

/**
 * POJO representing a groups member information from the REST API
 * @author Andrew Jarombek
 * @since 3/1/2017
 */

public class GroupMember {

    private String username;
    private String first;
    private String last;
    private Date member_since;
    private String status;
    private String user;

    @Override
    public String toString() {
        return "GroupMember: [ username: " + username + ", first: " + first +
                ", last: " + last + ", member_since: " + member_since.toString() + ", status: " +
                status + ", user: " + user + "]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Date getMember_since() {
        return member_since;
    }

    public void setMember_since(Date member_since) {
        this.member_since = member_since;
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


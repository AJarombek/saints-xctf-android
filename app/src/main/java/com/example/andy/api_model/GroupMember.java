package com.example.andy.api_model;

/**
 * POJO representing a groups member information from the REST API
 * @author Andrew Jarombek
 * @since 3/1/2017
 */

public class GroupMember {

    private String username;
    private String first;
    private String last;
    private String member_since;

    @Override
    public String toString() {
        return "GroupMember: [ username: " + username + ", first: " + first +
                ", last: " + last + ", member_since: " + member_since + "]";
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

    public String getMember_since() {
        return member_since;
    }

    public void setMember_since(String member_since) {
        this.member_since = member_since;
    }
}


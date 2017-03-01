package com.example.andy.api_model;

/**
 * POJO representing a leaderboard users information from the REST API
 * @author Andrew Jarombek
 * @since 3/1/2017
 */

public class LeaderboardItem {

    private String username;
    private String first;
    private String last;
    private Double miles;

    @Override
    public String toString() {
        return "GroupMember: [ username: " + username + ", first: " + first +
                ", last: " + last + ", miles: " + miles + "]";
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

    public Double getMiles() {
        return miles;
    }

    public void setMiles(Double miles) {
        this.miles = miles;
    }
}


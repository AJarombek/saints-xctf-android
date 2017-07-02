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
    private Double milesrun;
    private Double milesbiked;
    private Double milesswam;
    private Double milesother;

    @Override
    public String toString() {
        return "GroupMember: [ username: " + username + ", first: " + first +
                ", last: " + last + ", miles: " + miles + ", miles: " + milesrun + ", miles: " +
                milesbiked + ", miles: " + milesswam + ", miles: " + milesother + "]";
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

    public Double getMilesrun() {
        return milesrun;
    }

    public void setMilesrun(Double milesrun) {
        this.milesrun = milesrun;
    }

    public Double getMilesbiked() {
        return milesbiked;
    }

    public void setMilesbiked(Double milesbiked) {
        this.milesbiked = milesbiked;
    }

    public Double getMilesswam() {
        return milesswam;
    }

    public void setMilesswam(Double milesswam) {
        this.milesswam = milesswam;
    }

    public Double getMilesother() {
        return milesother;
    }

    public void setMilesother(Double milesother) {
        this.milesother = milesother;
    }
}


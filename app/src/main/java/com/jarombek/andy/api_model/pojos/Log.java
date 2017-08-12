package com.jarombek.andy.api_model.pojos;

import java.util.ArrayList;

/**
 * POJO representing a exercise log from the REST API
 * @author Andrew Jarombek
 * @since 11/8/2016
 */

public class Log {

    private Integer log_id;
    private String username;
    private String first;
    private String last;
    private String name;
    private String location;
    private String date;
    private String type;
    private Double distance;
    private String metric;
    private Double miles;
    private String time;
    private String pace;
    private Integer feel;
    private String description;
    private String time_created;
    private ArrayList<Comment> comments;

    @Override
    public String toString() {
        return "Log: [ log_id: " + log_id + "username: " + username + ", first: " + first + ", last: " + last +
                ", name: " + name + ", location: " + location + ", date: " + date +
                ", type: " + type + ", distance: " + distance.toString() + ", metric: " + metric +
                ", miles: " + miles.toString() + ", time: " + time + ", pace:" + pace +
                ", feel: " + feel.toString() + ", comments: " + comments.toString() + ", time_created: "
                + time_created + ", description: " + description + "]";
    }

    public Integer getLog_id() {
        return log_id;
    }

    public void setLog_id(Integer log_id) {
        this.log_id = log_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Double getMiles() {
        return miles;
    }

    public void setMiles(Double miles) {
        this.miles = miles;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getFeel() {
        return feel;
    }

    public void setFeel(Integer feel) {
        this.feel = feel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }
}

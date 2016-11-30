package com.example.andy.api_model;

import java.sql.Time;
import java.util.Date;

/**
 * POJO representing a exercise log from the REST API
 * @author Andrew Jarombek
 * @since 11/8/2016
 */

public class Log {

    private String username;
    private String name;
    private String location;
    private Date date;
    private String type;
    private Double distance;
    private String metric;
    private Double miles;
    private Time time;
    private Integer feel;
    private String description;

    @Override
    public String toString() {
        return "Log: [ username: " + username + ", name: " + name + ", location: " + location +
                ", date: " + date.toString() + ", type: " + type + ", distance: "
                + distance.toString() + ", metric: " + metric + ", miles: " + miles.toString() +
                ", time: " + time.toString() + ", feel: " + feel.toString() + ", description: " +
                description + "]";
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
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
}

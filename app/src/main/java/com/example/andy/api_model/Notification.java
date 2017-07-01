package com.example.andy.api_model;

import java.util.Date;

/**
 * POJO representing a notification for a user from the REST API
 * @author Andrew Jarombek
 * @since 6/10/2017
 */

public class Notification {

    private String notification_id;
    private String username;
    private Date time;
    private String link;
    private String description;
    private String viewed;

    @Override
    public String toString() {
        return "Notification: [ notification_id: " + notification_id + ", username: " +
                username + ", time: " + time + ", link: " + link + ", viewed: " + viewed +
                ", description: " + description + "]";
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }
}
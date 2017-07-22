package com.example.andy.api_model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * POJO representing a message in a group from the REST API
 * @author Andrew Jarombek
 * @since 6/10/2017
 */

public class Message {

    private int message_id;
    private String username;
    private String first;
    private String last;
    private String group_name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private String content;

    @Override
    public String toString() {
        return "Message: [ message_id: " + message_id + ", username: " + username +
                ", first: " + first + ", last: " + last + ", group_name: " + group_name +
                ", time: " + time + ", content: " + content + "]";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

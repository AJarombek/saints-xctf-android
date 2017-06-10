package com.example.andy.api_model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * POJO representing a comment on a log from the REST API
 * @author Andrew Jarombek
 * @since 12/11/2016
 */

public class Comment {

    private int comment_id;
    private int log_id;
    private String username;
    private String first;
    private String last;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private String content;

    @Override
    public String toString() {
        return "Comment: [ comment_id: " + comment_id + ",log_id: " + log_id + ",username: " +
                username + ",first: " + first + ",last: " + last + ",time: " + time.toString() +
                ",content: " + content + "]";
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

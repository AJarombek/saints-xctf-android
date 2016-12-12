package com.example.andy.api_model;

import java.util.ArrayList;

/**
 * POJO representing an array of comments from the REST API
 * @author Andrew Jarombek
 * @since 12/11/2016
 */

public class CommentArray {

    private ArrayList<Comment> comments;

    @Override
    public String toString() {
        String commentsString = "Comments: [";
        for (Comment comment: comments) {
            commentsString += comment.toString();
        }
        commentsString += "]";
        return commentsString;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}

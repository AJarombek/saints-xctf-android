package com.example.andy.api_model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Andrew Jarombek
 * @since 11/9/2016
 */
public class JSONConverter {

    private final static String LOG_TAG = JSONConverter.class.getName();

    public static User toUser(String JSON) throws IOException {
        android.util.Log.d(LOG_TAG, JSON);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        User user = mapper.readValue(JSON, User.class);
        return user;
    }

    public static Log toLog(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Log log = mapper.readValue(JSON, Log.class);
        return log;
    }

    public static Group toGroup(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Group group = mapper.readValue(JSON, Group.class);
        return group;
    }

    public static Comment toComment(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Comment comment = mapper.readValue(JSON, Comment.class);
        return comment;
    }

    public static List<User> toUserList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<User> userArray = mapper.readValue(JSON, new TypeReference<List<User>>(){});
        return userArray;
    }

    public static List<Log> toLogList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        List<Log> logsArray = mapper.readValue(JSON, new TypeReference<List<Log>>(){});
        return logsArray;
    }

    public static List<Group> toGroupList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Group> groupArray = mapper.readValue(JSON, new TypeReference<List<Group>>(){});
        return groupArray;
    }

    public static List<Comment> toCommentList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Comment> commentArray = mapper.readValue(JSON, new TypeReference<List<Comment>>(){});
        return commentArray;
    }

    public static String fromUser(User user) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        String userJsonString = mapper.writeValueAsString(user);
        android.util.Log.d(LOG_TAG, "The User JSON String\n" + userJsonString);
        return userJsonString;
    }

    public static String fromLog(Log log) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String logJsonString = mapper.writeValueAsString(log);
        android.util.Log.d(LOG_TAG, "The Log JSON String\n" + logJsonString);
        return logJsonString;
    }

    public static String fromGroup(Group group) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String groupJsonString = mapper.writeValueAsString(group);
        android.util.Log.d(LOG_TAG, "The Group JSON String\n" + groupJsonString);
        return groupJsonString;
    }

    public static String fromComment(Comment comment) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String commentJsonString = mapper.writeValueAsString(comment);
        android.util.Log.d(LOG_TAG, "The Comment JSON String\n" + commentJsonString);
        return commentJsonString;
    }

    public static String fromUserList(List<User> users) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String usersJsonString = mapper.writeValueAsString(users);
        android.util.Log.d(LOG_TAG, "The Users JSON String\n" + usersJsonString);
        return usersJsonString;
    }

    public static String fromLogList(List<Log> logs) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String logsJsonString = mapper.writeValueAsString(logs);
        android.util.Log.d(LOG_TAG, "The Logs JSON String\n" + logsJsonString);
        return logsJsonString;
    }

    public static String fromGroupList(List<Group> groups) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String groupsJsonString = mapper.writeValueAsString(groups);
        android.util.Log.d(LOG_TAG, "The Groups JSON String\n" + groupsJsonString);
        return groupsJsonString;
    }

    public static String fromCommentList(List<Comment> logs) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String commentsJsonString = mapper.writeValueAsString(logs);
        android.util.Log.d(LOG_TAG, "The Comments JSON String\n" + commentsJsonString);
        return commentsJsonString;
    }
}

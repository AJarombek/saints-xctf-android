package com.example.andy.api_model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
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
        JSON = getUserJSON(JSON);
        android.util.Log.d(LOG_TAG, JSON);
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(JSON, User.class);
        return user;
    }

    private static String getUserJSON(String JSON) {
        for (int i=0; i < JSON.length(); i++) {
            char c = JSON.charAt(i);
            if (JSON.charAt(i) == ':' && JSON.substring(i+1,i+12).equals("{\"username\"")) {
                return JSON.substring(i+1,JSON.length() - 1);
            }
        }
        return JSON;
    }

    public static Log toLog(String JSON) throws IOException {
        JSON = getLogJSON(JSON);
        ObjectMapper mapper = new ObjectMapper();
        Log log = mapper.readValue(JSON, Log.class);
        return log;
    }

    private static String getLogJSON(String JSON) {
        for (int i=0; i < JSON.length(); i++) {
            char c = JSON.charAt(i);
            if (JSON.charAt(i) == ':' && JSON.substring(i+1,i+10).equals("{\"log_id\"")) {
                return JSON.substring(i+1,JSON.length() - 1);
            }
        }
        return JSON;
    }

    public static Group toGroup(String JSON) throws IOException {
        JSON = getGroupJSON(JSON);
        ObjectMapper mapper = new ObjectMapper();
        Group group = mapper.readValue(JSON, Group.class);
        return group;
    }

    private static String getGroupJSON(String JSON) {
        for (int i=0; i < JSON.length(); i++) {
            char c = JSON.charAt(i);
            if (JSON.charAt(i) == ':' && JSON.substring(i+1,i+14).equals("{\"group_name\"")) {
                return JSON.substring(i+1,JSON.length() - 1);
            }
        }
        return JSON;
    }

    public static Comment toComment(String JSON) throws IOException {
        JSON = getCommentJSON(JSON);
        ObjectMapper mapper = new ObjectMapper();
        Comment comment = mapper.readValue(JSON, Comment.class);
        return comment;
    }

    private static String getCommentJSON(String JSON) {
        for (int i=0; i < JSON.length(); i++) {
            char c = JSON.charAt(i);
            if (JSON.charAt(i) == ':' && JSON.substring(i+1,i+14).equals("{\"comment_id\"")) {
                return JSON.substring(i+1,JSON.length() - 1);
            }
        }
        return JSON;
    }

    public static List<User> toUserList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UserArray userArray = mapper.readValue(JSON, UserArray.class);
        return userArray.getUsers();
    }

    public static TreeMap<Integer,Log> toLogList(String JSON) throws IOException, ParseException {
        JSON = JSON.substring(10, JSON.length()-1);
        ObjectMapper mapper = new ObjectMapper();
        TreeMap<String,Map<String,String>> logsMap = mapper.readValue(JSON, TreeMap.class);

        TreeMap<Integer, Log> logList = new TreeMap<>();
        for (TreeMap.Entry<String, Map<String,String>> entry : logsMap.entrySet()) {
            Map<String,String> logMap = entry.getValue();
            Log tempLog = new Log();
            tempLog.setUsername(logMap.get("username"));
            tempLog.setFirst(logMap.get("first"));
            tempLog.setLast(logMap.get("last"));
            tempLog.setName(logMap.get("name"));
            tempLog.setLocation(logMap.get("location"));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            tempLog.setDate(formatter.parse(logMap.get("date")));
            tempLog.setType(logMap.get("type"));
            tempLog.setDistance(Double.parseDouble(logMap.get("distance")));
            tempLog.setMetric(logMap.get("metric"));
            tempLog.setMiles(Double.parseDouble(logMap.get("miles")));
            tempLog.setTime(Time.valueOf(logMap.get("time")));
            tempLog.setPace(Time.valueOf(logMap.get("pace")));
            tempLog.setFeel(Integer.parseInt(logMap.get("feel")));
            tempLog.setDescription(logMap.get("description"));
            //tempLog.setComments(logMap.get("comments"));
            logList.put(Integer.parseInt(logMap.get("log_id")), tempLog);
        }
        return logList;
    }

    public static List<Group> toGroupList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupArray groupArray = mapper.readValue(JSON, GroupArray.class);
        return groupArray.getGroups();
    }

    public static List<Comment> toCommentList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CommentArray commentArray = mapper.readValue(JSON, CommentArray.class);
        return commentArray.getComments();
    }

    public static String fromUser(User user) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
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

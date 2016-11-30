package com.example.andy.api_model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @author Andrew Jarombek
 * @since 11/9/2016
 */
public class JSONConverter {

    private final static String LOG_TAG = JSONConverter.class.getName();

    public static User toUser(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
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

    public static List<User> toUserList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UserArray userArray = mapper.readValue(JSON, UserArray.class);
        return userArray.getUsers();
    }

    public static List<Log> toLogList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LogArray logArray = mapper.readValue(JSON, LogArray.class);
        return logArray.getLogs();
    }

    public static List<Group> toGroupList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupArray groupArray = mapper.readValue(JSON, GroupArray.class);
        return groupArray.getGroups();
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
}

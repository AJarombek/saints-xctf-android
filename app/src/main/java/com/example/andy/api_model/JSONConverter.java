package com.example.andy.api_model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Jarombek
 * @since 11/9/2016
 */
public class JSONConverter {

    public static User toUser(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(JSON, User.class);
    }

    public static Log toLog(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(JSON, Log.class);
    }

    public static Group toGroup(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(JSON, Group.class);
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

    public static String fromUser(User user) {
        return null;
    }

    public static String fromLog(Log log) {
        return null;
    }

    public static String fromGroup(Group group) {
        return null;
    }

    public static String fromUserList(List<User> user) {
        return null;
    }

    public static String fromLogList(List<Log> log) {
        return null;
    }

    public static String fromGroupList(List<Group> group) {
        return null;
    }
}

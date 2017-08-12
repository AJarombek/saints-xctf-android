package com.jarombek.andy.api_model.services;

import com.jarombek.andy.api_model.pojos.Comment;
import com.jarombek.andy.api_model.pojos.Group;
import com.jarombek.andy.api_model.pojos.Log;
import com.jarombek.andy.api_model.pojos.Mail;
import com.jarombek.andy.api_model.pojos.Message;
import com.jarombek.andy.api_model.pojos.Notification;
import com.jarombek.andy.api_model.pojos.RangeView;
import com.jarombek.andy.api_model.pojos.User;
import com.jarombek.andy.api_model.pojos.ActivationCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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

    public static Message toMessage(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(JSON, Message.class);
        return message;
    }

    public static ActivationCode toActivationCode(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ActivationCode code = mapper.readValue(JSON, ActivationCode.class);
        return code;
    }

    public static Notification toNotification(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Notification notification = mapper.readValue(JSON, Notification.class);
        return notification;
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

    public static List<Message> toMessageList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messageArray = mapper.readValue(JSON, new TypeReference<List<Message>>(){});
        return messageArray;
    }

    public static List<RangeView> toRangeViewList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<RangeView> rangeViewArray = mapper.readValue(JSON, new TypeReference<List<RangeView>>(){});
        return rangeViewArray;
    }

    public static List<Notification> toNotificationList(String JSON) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Notification> notificationArray = mapper.readValue(JSON, new TypeReference<List<Notification>>(){});
        return notificationArray;
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

    public static String fromMessage(Message message) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String messageJsonString = mapper.writeValueAsString(message);
        android.util.Log.d(LOG_TAG, "The Message JSON String\n" + messageJsonString);
        return messageJsonString;
    }

    public static String fromActivationCode(ActivationCode code) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String codeJsonString = mapper.writeValueAsString(code);
        android.util.Log.d(LOG_TAG, "The ActivationCode JSON String\n" + codeJsonString);
        return codeJsonString;
    }

    public static String fromNotification(Notification notification) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String notificationJsonString = mapper.writeValueAsString(notification);
        android.util.Log.d(LOG_TAG, "The Notification JSON String\n" + notificationJsonString);
        return notificationJsonString;
    }

    public static String fromMail(Mail mail) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String mailJsonString = mapper.writeValueAsString(mail);
        android.util.Log.d(LOG_TAG, "The JSON String\n" + mailJsonString);
        return mailJsonString;
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

    public static String fromMessageList(List<Message> messages) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String messagesJsonString = mapper.writeValueAsString(messages);
        android.util.Log.d(LOG_TAG, "The Messages JSON String\n" + messagesJsonString);
        return messagesJsonString;
    }

    public static String fromRangeViewList(List<RangeView> rangeView) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String rangeViewJsonString = mapper.writeValueAsString(rangeView);
        android.util.Log.d(LOG_TAG, "The Range View JSON String\n" + rangeViewJsonString);
        return rangeViewJsonString;
    }

    public static String fromNotificationList(List<Notification> notifications) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String notificationsJsonString = mapper.writeValueAsString(notifications);
        android.util.Log.d(LOG_TAG, "The Notifications JSON String\n" + notificationsJsonString);
        return notificationsJsonString;
    }
}

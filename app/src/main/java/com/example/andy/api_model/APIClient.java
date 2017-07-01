package com.example.andy.api_model;

import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.TreeMap;

/**
 * Class used to hit endpoints on the REST API
 * @author Andrew Jarombek
 * @since 11/7/2016
 */
public class APIClient {

    private static final String TAG = "APIRequest: ";

    /*
     * GET REQUESTS
     */

    public static List<User> usersGetRequest() throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/users");
        if (response.equals("false")) return null;
        return JSONConverter.toUserList(response);
    }

    public static User userGetRequest(String username) throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/users/" + username);
        if (response.equals("false")) return null;
        return JSONConverter.toUser(response);
    }

    public static List<com.example.andy.api_model.Log> logsGetRequest() throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/logs");
        if (response.equals("false")) return null;
        return JSONConverter.toLogList(response);
    }

    public static com.example.andy.api_model.Log logGetRequest(int logno) throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/logs/" + logno);
        if (response.equals("false")) return null;
        return JSONConverter.toLog(response);
    }

    public static List<Comment> commentsGetRequest() throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/comments");
        if (response.equals("false")) return null;
        return JSONConverter.toCommentList(response);
    }

    public static Comment commentsGetRequest(int commentno) throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/comments/" + commentno);
        if (response.equals("false")) return null;
        return JSONConverter.toComment(response);
    }

    public static List<Group> groupsGetRequest() throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/groups");
        if (response.equals("false")) return null;
        return JSONConverter.toGroupList(response);
    }

    public static Group groupGetRequest(String groupname) throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/groups/" + groupname);
        if (response.equals("false")) return null;
        return JSONConverter.toGroup(response);
    }

    public static List<Message> messagesGetRequest() throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/messages");
        if (response.equals("false")) return null;
        return JSONConverter.toMessageList(response);
    }

    public static Message messageGetRequest(String messageno) throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/message/" + messageno);
        if (response.equals("false")) return null;
        return JSONConverter.toMessage(response);
    }

    public static ActivationCode activationcodeGetRequest(String code) throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/activationcode/" + code);
        if (response.equals("false")) return null;
        return JSONConverter.toActivationCode(response);
    }

    public static List<Notification> notificationsGetRequest() throws IOException {
        String response = getRequest("https://www.saintsxctf.com/api/api.php/notifications");
        if (response.equals("false")) return null;
        return JSONConverter.toNotificationList(response);
    }

    public static List<com.example.andy.api_model.Log> logfeedGetRequest(String... params)
            throws IOException {
        String paramtype = params[0];
        String sortparam = params[1];
        String limit = params[2];
        String offset = params[3];

        String response = getRequest("https://www.saintsxctf.com/api/api.php/logfeed/" + paramtype + "/"
                + sortparam + "/" + limit + "/" + offset);
        if (response.equals("false")) return null;
        return JSONConverter.toLogList(response);
    }

    public static List<Message> messagefeedGetRequest(String... params)
            throws IOException {
        String paramtype = params[0];
        String sortparam = params[1];
        String limit = params[2];
        String offset = params[3];

        String response = getRequest("https://www.saintsxctf.com/api/api.php/messagefeed/" + paramtype + "/"
                + sortparam + "/" + limit + "/" + offset);
        if (response.equals("false")) return null;
        return JSONConverter.toMessageList(response);
    }

    public static List<RangeView> rangeviewGetRequest(String... params)
            throws IOException {
        String paramtype = params[0];
        String sortparam = params[1];
        String start = params[2];
        String end = params[3];

        String response = getRequest("https://www.saintsxctf.com/api/api.php/rangeview/" + paramtype +
                "/" + sortparam + "/" + start + "/" + end);
        if (response.equals("false")) return null;
        return JSONConverter.toRangeViewList(response);
    }

    /*
     * POST REQUESTS
     */

    public static User userPostRequest(String user) throws IOException {
        String response = postRequest("https://www.saintsxctf.com/api/api.php/user/", user);
        if (response.equals("false")) return null;
        return JSONConverter.toUser(response);
    }

    public static com.example.andy.api_model.Log logPostRequest(String log) throws IOException {
        String response = postRequest("https://www.saintsxctf.com/api/api.php/log/", log);
        if (response.equals("false")) return null;
        return JSONConverter.toLog(response);
    }

    public static Comment commentPostRequest(String comment) throws IOException {
        String response = postRequest("https://www.saintsxctf.com/api/api.php/comment/", comment);
        if (response.equals("false")) return null;
        return JSONConverter.toComment(response);
    }

    public static Message messagePostRequest(String message) throws IOException {
        String response = postRequest("https://www.saintsxctf.com/api/api.php/message/", message);
        if (response.equals("false")) return null;
        return JSONConverter.toMessage(response);
    }

    public static ActivationCode activationcodePostRequest(String code) throws IOException {
        String response = postRequest("https://www.saintsxctf.com/api/api.php/activationcode/", code);
        if (response.equals("false")) return null;
        return JSONConverter.toActivationCode(response);
    }

    public static Notification notificationPostRequest(String notification) throws IOException {
        String response = postRequest("https://www.saintsxctf.com/api/api.php/notification/", notification);
        if (response.equals("false")) return null;
        return JSONConverter.toNotification(response);
    }

    public static void mailPostRequest(String mail) throws IOException {
        postRequest("https://www.saintsxctf.com/api/api.php/mail/", mail);
    }

    /*
     * PUT REQUESTS
     */

    public static User userPutRequest(String username, String user) throws IOException {
        String response = putRequest("https://www.saintsxctf.com/api/api.php/users/" + username, user);
        if (response.equals("false")) return null;
        return JSONConverter.toUser(response);
    }

    public static com.example.andy.api_model.Log logPutRequest(int logno, String log)
            throws IOException {
        String response = putRequest("https://www.saintsxctf.com/api/api.php/logs/" + logno, log);
        if (response.equals("false")) return null;
        return JSONConverter.toLog(response);
    }

    public static Comment commentPutRequest(int commentno, String comment) throws IOException {
        String response = putRequest("https://www.saintsxctf.com/api/api.php/logs/" +
                commentno, comment);
        if (response.equals("false")) return null;
        return JSONConverter.toComment(response);
    }

    public static Group groupPutRequest(String groupname, String group) throws IOException {
        String response = putRequest("https://www.saintsxctf.com/api/api.php/groups/" + groupname, group);
        if (response.equals("false")) return null;
        return JSONConverter.toGroup(response);
    }

    public static Message messagePutRequest(String messageno, String message) throws IOException {
        String response = putRequest("https://www.saintsxctf.com/api/api.php/message/" + messageno, message);
        if (response.equals("false")) return null;
        return JSONConverter.toMessage(response);
    }

    /*
     * DELETE REQUESTS
     */

    public static boolean userDeleteRequest(String username) {
        return deleteRequest("https://www.saintsxctf.com/api/api.php/users/" + username);
    }

    public static boolean logDeleteRequest(int logno) {
        return deleteRequest("https://www.saintsxctf.com/api/api.php/logs/" + logno);
    }

    public static boolean commentDeleteRequest(int commentno) {
        return deleteRequest("https://www.saintsxctf.com/api/api.php/comments/" + commentno);
    }

    public static boolean messageDeleteRequest(int messageno) {
        return deleteRequest("https://www.saintsxctf.com/api/api.php/message/" + messageno);
    }

    public static boolean activationcodeDeleteRequest(int code) {
        return deleteRequest("https://www.saintsxctf.com/api/api.php/activationcode/" + code);
    }

    public static boolean notificationDeleteRequest(int notificationno) {
        return deleteRequest("https://www.saintsxctf.com/api/api.php/notification/" + notificationno);
    }

    /*
     * API VERB REQUESTS
     */

    private static String getRequest(String url) {
        String response = "";
        try {
            response = APIRequest.get(url);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with GET Request.");
            throwable.printStackTrace();
            return "false";
        }
        return response;
    }

    private static String postRequest(String url, String json) {
        String response = "";
        try {
            response = APIRequest.post(url, json);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with POST Request.");
            throwable.printStackTrace();
            return "false";
        }
        return response;
    }

    private static String putRequest(String url, String json) {
        String response = "";
        try {
            response = APIRequest.put(url, json);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with PUT Request.");
            throwable.printStackTrace();
            return "false";
        }
        return response;
    }

    private static boolean deleteRequest(String url) {
        boolean response = false;
        try {
            response = APIRequest.delete(url);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with DELETE Request.");
            throwable.printStackTrace();
            return false;
        }
        return response;
    }

}
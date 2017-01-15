package com.example.andy.api_model;

import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Class used to hit endpoints on the REST API
 * @author Andrew Jarombek
 * @since 11/7/2016
 */
public class APIClient {

    private static final String TAG = "APIRequest: ";

    public static List<User> usersGetRequest() throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/users");
        return JSONConverter.toUserList(response);
    }

    public static User userGetRequest(String username) throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/users/" + username);
        return JSONConverter.toUser(response);
    }

    public static List<com.example.andy.api_model.Log> logsGetRequest() throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/logs");
        return JSONConverter.toLogList(response);
    }

    public static com.example.andy.api_model.Log logGetRequest(int logno) throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/logs/" + logno);
        return JSONConverter.toLog(response);
    }

    public static List<Comment> commentsGetRequest() throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/comments");
        return JSONConverter.toCommentList(response);
    }

    public static Comment commentsGetRequest(int commentno) throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/comments/" + commentno);
        return JSONConverter.toComment(response);
    }

    public static List<Group> groupsGetRequest() throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/groups");
        return JSONConverter.toGroupList(response);
    }

    public static Group groupGetRequest(String groupname) throws IOException {
        String response = getRequest("www.saintsxctf.com/api/api.php/groups/" + groupname);
        return JSONConverter.toGroup(response);
    }

    public static List<com.example.andy.api_model.Log> logfeedGetRequest(String[] params)
            throws IOException {
        String paramtype = params[0];
        String sortparam = params[1];
        String limit = params[2];
        String offset = params[3];

        String response = getRequest("www.saintsxctf.com/api/api.php/logfeed/" + paramtype + "/"
                            + sortparam + "/" + limit + "/" + offset);
        return JSONConverter.toLogList(response);
    }

    public static User userPostRequest(String user) throws IOException {
        String response = postRequest("www.saintsxctf.com/api/api.php/user/", user);
        return JSONConverter.toUser(response);
    }

    public static com.example.andy.api_model.Log logPostRequest(String log) throws IOException {
        String response = postRequest("www.saintsxctf.com/api/api.php/log/", log);
        return JSONConverter.toLog(response);
    }

    public static Comment commentPostRequest(String comment) throws IOException {
        String response = postRequest("www.saintsxctf.com/api/api.php/comment/", comment);
        return JSONConverter.toComment(response);
    }

    public static User userPutRequest(String username, String user) throws IOException {
        String response = putRequest("www.saintsxctf.com/api/api.php/users/" + username, user);
        return JSONConverter.toUser(response);
    }

    public static com.example.andy.api_model.Log logPutRequest(int logno, String log)
            throws IOException {
        String response = putRequest("www.saintsxctf.com/api/api.php/logs/" + logno, log);
        return JSONConverter.toLog(response);
    }

    public static Comment commentPutRequest(int commentno, String comment) throws IOException {
        String response = putRequest("www.saintsxctf.com/api/api.php/logs/" +
                commentno, comment);
        return JSONConverter.toComment(response);
    }

    public static Group groupPutRequest(String groupname, String group) throws IOException {
        String response = putRequest("www.saintsxctf.com/api/api.php/groups/" + groupname, group);
        return JSONConverter.toGroup(response);
    }

    public static boolean userDeleteRequest(String username) {
        return deleteRequest("www.saintsxctf.com/api/api.php/users/" + username);
    }

    public static boolean logDeleteRequest(int logno) {
        return deleteRequest("www.saintsxctf.com/api/api.php/logs/" + logno);
    }

    public static boolean commentDeleteRequest(int commentno) {
        return deleteRequest("www.saintsxctf.com/api/api.php/comments/" + commentno);
    }

    private static String getRequest(String url) {
        String response = "";
        try {
            response = APIRequest.get(url);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with GET Request.");
            throwable.printStackTrace();
        }
        return response;
    }

    private static String postRequest(String url, String json) {
        String response = "";
        try {
            response = APIRequest.post(url, json);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with GET Request.");
            throwable.printStackTrace();
        }
        return response;
    }

    private static String putRequest(String url, String json) {
        String response = "";
        try {
            response = APIRequest.put(url, json);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with GET Request.");
            throwable.printStackTrace();
        }
        return response;
    }

    private static boolean deleteRequest(String url) {
        boolean response = false;
        try {
            response = APIRequest.delete(url);
        } catch (Throwable throwable) {
            Log.e(TAG, "Error Connecting to API with GET Request.");
            throwable.printStackTrace();
        }
        return response;
    }

}

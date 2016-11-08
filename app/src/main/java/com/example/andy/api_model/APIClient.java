package com.example.andy.api_model;

import android.util.Log;

/**
 * Class used to hit endpoints on the REST API
 * @author Andrew Jarombek
 * @since 11/7/2016
 */
public class APIClient {

    private static final String TAG = "APIRequest: ";

    public static String usersGetRequest() {
        return getRequest("localhost/saints-xctf/api/api.php/users");
    }

    public static String userGetRequest(String username) {
        return getRequest("localhost/saints-xctf/api/api.php/users/" + username);
    }

    public static String logsGetRequest() {
        return getRequest("localhost/saints-xctf/api/api.php/logs");
    }

    public static String logGetRequest(int logno) {
        return getRequest("localhost/saints-xctf/api/api.php/logs/" + logno);
    }

    public static String groupsGetRequest() {
        return getRequest("localhost/saints-xctf/api/api.php/groups");
    }

    public static String groupGetRequest(String groupname) {
        return getRequest("localhost/saints-xctf/api/api.php/groups/" + groupname);
    }

    public static String logfeedGetRequest(String[] params) {
        String paramtype = params[0];
        String sortparam = params[1];
        String limit = params[2];
        String offset = params[3];

        return getRequest("localhost/saints-xctf/api/api.php/logfeed/" + paramtype + "/"
                            + sortparam + "/" + limit + "/" + offset);
    }

    public static String userPostRequest(String user) {
        return postRequest("localhost/saints-xctf/api/api.php/user", user);
    }

    public static String logPostRequest(String log) {
        return postRequest("localhost/saints-xctf/api/api.php/log", log);
    }

    public static String userPutRequest(String username, String user) {
        return putRequest("localhost/saints-xctf/api/api.php/users/" + username, user);
    }

    public static String logPutRequest(int logno, String log) {
        return putRequest("localhost/saints-xctf/api/api.php/logs/" + logno, log);
    }

    public static String groupPutRequest(String groupname, String group) {
        return putRequest("localhost/saints-xctf/api/api.php/groups/" + groupname, group);
    }

    public static boolean userDeleteRequest(String username) {
        return deleteRequest("localhost/saints-xctf/api/api.php/users/" + username);
    }

    public static boolean logDeleteRequest(int logno) {
        return deleteRequest("localhost/saints-xctf/api/api.php/logs/" + logno);
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

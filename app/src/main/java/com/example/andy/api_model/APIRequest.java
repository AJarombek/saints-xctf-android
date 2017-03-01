package com.example.andy.api_model;

import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Andrew Jarombek
 * @since 11/7/2016
 */
public class APIRequest {

    private static final String TAG = "APIRequest: ";

    public static String get(String url) throws Throwable {

        // Add credentials to the connection
        Credentials credentials = new Credentials();

        ObjectMapper mapper = new ObjectMapper();
        String credentialsJsonString = mapper.writeValueAsString(credentials);
        Log.d(TAG, credentialsJsonString);

        byte[] cred = Base64.encode(credentialsJsonString.getBytes(), Base64.DEFAULT);
        String credString = new String(cred);
        credString = credString.replace("\n", "");
        Log.d(TAG, credString);

        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Authorization", credString);
        connection.setRequestMethod("GET");

        Log.d(TAG, Arrays.toString(connection.getRequestProperties().entrySet().toArray()));

        if (connection.getResponseCode() != HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new RuntimeException("API Response Failed: Error Code = "
                    + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

        StringBuilder responseBody = new StringBuilder();
        String r;
        int loop = 0;
        while ((r = br.readLine()) != null) {
            responseBody.append(r);
            Log.d(TAG, "" + loop++);
        }

        connection.disconnect();

        Log.d(TAG, "API GET Response Body: " + responseBody);
        return responseBody.toString();
    }

    public static String post(String url, String input) throws Throwable {
        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");

        // Add credentials to the connection
        Credentials credentials = new Credentials();

        ObjectMapper mapper = new ObjectMapper();
        String credentialsJsonString = mapper.writeValueAsString(credentials);

        byte[] cred = Base64.encode(credentialsJsonString.getBytes(), Base64.DEFAULT);
        connection.addRequestProperty("Authorization", new String(cred));

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(input.getBytes());
        outputStream.flush();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new RuntimeException("API Response Failed: Error Code = "
                    + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

        StringBuilder responseBody = new StringBuilder();
        String r;
        int loop = 0;
        while ((r = br.readLine()) != null) {
            responseBody.append(r);
            Log.d(TAG, "" + loop++);
        }

        connection.disconnect();

        Log.d(TAG, "API POST Response Body: " + responseBody);
        return responseBody.toString();
    }

    public static String put(String url, String input) throws Throwable {
        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Accept", "application/json");

        // Add credentials to the connection
        Credentials credentials = new Credentials();

        ObjectMapper mapper = new ObjectMapper();
        String credentialsJsonString = mapper.writeValueAsString(credentials);

        byte[] cred = Base64.encode(credentialsJsonString.getBytes(), Base64.DEFAULT);
        connection.addRequestProperty("Authorization", new String(cred));

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(input.getBytes());
        outputStream.flush();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new RuntimeException("API Response Failed: Error Code = "
                    + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

        StringBuilder responseBody = new StringBuilder();
        String r;
        int loop = 0;
        while ((r = br.readLine()) != null) {
            responseBody.append(r);
            Log.d(TAG, "" + loop++);
        }

        connection.disconnect();

        Log.d(TAG, "API PUT Response Body: " + responseBody);
        return responseBody.toString();
    }

    public static boolean delete(String url) throws Throwable {
        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");

        // Add credentials to the connection
        Credentials credentials = new Credentials();

        ObjectMapper mapper = new ObjectMapper();
        String credentialsJsonString = mapper.writeValueAsString(credentials);

        byte[] cred = Base64.encode(credentialsJsonString.getBytes(), Base64.DEFAULT);
        connection.addRequestProperty("Authorization", new String(cred));

        Log.d(TAG, "API DELETE Response Code: " + connection.getResponseCode());

        connection.disconnect();
        return (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT);
    }
}

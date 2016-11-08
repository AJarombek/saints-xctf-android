package com.example.andy.api_model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Andrew Jarombek
 * @since 11/7/2016
 */
public class APIRequest {

    private static final String TAG = "APIRequest: ";

    public static String get(String url) throws Throwable {
        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API Response Failed: Error Code = "
                    + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String responseBody = "";
        String r;
        while ((r = br.readLine()) != null) {
            responseBody = responseBody + r;
        }

        connection.disconnect();

        Log.d(TAG, "API GET Response Body: " + responseBody);
        return responseBody;
    }

    public static String post(String url, String input) throws Throwable {
        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(input.getBytes());
        outputStream.flush();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("API Response Failed: Error Code = "
                    + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String responseBody = "";
        String r;
        while ((r = br.readLine()) != null) {
            responseBody = responseBody + r;
        }

        connection.disconnect();

        Log.d(TAG, "API POST Response Body: " + responseBody);
        return responseBody;
    }

    public static String put(String url, String input) throws Throwable {
        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Accept", "application/json");

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(input.getBytes());
        outputStream.flush();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("API Response Failed: Error Code = "
                    + connection.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String responseBody = "";
        String r;
        while ((r = br.readLine()) != null) {
            responseBody = responseBody + r;
        }

        connection.disconnect();

        Log.d(TAG, "API PUT Response Body: " + responseBody);
        return responseBody;
    }

    public static boolean delete(String url) throws Throwable {
        URL api_url = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) api_url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");

        Log.d(TAG, "API DELETE Response Code: " + connection.getResponseCode());

        connection.disconnect();
        return (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT);
    }
}

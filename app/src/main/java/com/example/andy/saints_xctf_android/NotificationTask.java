package com.example.andy.saints_xctf_android;

import android.os.AsyncTask;
import android.util.Log;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.Notification;

/**
 * NotificationTask is an asynchronous job for sending notifications for various reasons
 * @author Andrew Jarombek
 * @since 7/30/2017
 */

public class NotificationTask extends AsyncTask<Notification, Void, Object> {

    private static final String TAG = NotificationTask.class.getName();

    /**
     * @param params [0] -> notification
     * @return null
     */
    @Override
    protected Object doInBackground(Notification... params) {
        Notification notification = params[0];
        try {

            String notificationJSON = JSONConverter.fromNotification(notification);
            APIClient.notificationPostRequest(notificationJSON);
            return null;

        } catch (Throwable e) {
            Log.e(TAG, "Send Notification Failed.");
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
    }
}

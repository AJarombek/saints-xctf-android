package com.jarombek.andy.saints_xctf_android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.jarombek.andy.api_model.services.APIClient;
import com.jarombek.andy.api_model.services.JSONConverter;
import com.jarombek.andy.api_model.pojos.Notification;

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

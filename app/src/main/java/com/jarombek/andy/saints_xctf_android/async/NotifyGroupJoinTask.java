package com.jarombek.andy.saints_xctf_android.async;

import android.os.AsyncTask;
import android.util.Log;

import com.jarombek.andy.api_model.services.APIClient;
import com.jarombek.andy.api_model.pojos.Group;
import com.jarombek.andy.api_model.pojos.GroupMember;
import com.jarombek.andy.api_model.services.JSONConverter;
import com.jarombek.andy.api_model.pojos.Notification;

/**
 * NotifyGroupJoinTask is an asynchronous job for sending notifications to group admins
 * saying that the user has requested to join their group
 * @author Andrew Jarombek
 * @since 7/30/2017
 */

public class NotifyGroupJoinTask extends AsyncTask<Object, Void, Object> {

    private static final String TAG = NotifyGroupJoinTask.class.getName();

    /**
     * @param params [0] -> groupname, [1] -> notification
     * @return null
     */
    @Override
    protected Object doInBackground(Object... params) {
        Group group;
        try {
            group = APIClient.groupGetRequest((String) params[0]);

            if (group == null) {
                return null;
            } else {
                Notification notification = (Notification) params[1];

                // Send the notification to every admin in the group
                for (GroupMember groupMember : group.getMembers()) {
                    if (groupMember.getUser().equals("admin")) {

                        notification.setUsername(groupMember.getUsername());

                        String notificationJSON = JSONConverter.fromNotification(notification);

                        APIClient.notificationPostRequest(notificationJSON);
                    }
                }
            }

            return null;

        } catch (Throwable e) {
            Log.e(TAG, "Pick Group Send Notification Failed.");
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

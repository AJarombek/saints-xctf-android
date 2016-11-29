package com.example.andy.saints_xctf_android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Log;
import com.example.andy.api_model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * Class for the Login DialogFragment which is used as a form to login users
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class LoginDialogFragment extends DialogFragment {

    private static final String LOG_TAG = LoginDialogFragment.class.getName();

    // Views in the fragment
    private EditText login_username;
    private EditText login_password;
    private User user;

    /**
     * Create and Run an AlertDialog for Log Submitting
     * @param bundle --
     * @return --
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View loginDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_login, null);
        builder.setView(loginDialogView);

        // set the AlertDialog's message
        builder.setTitle(R.string.title_log_dialog);

        // Initialize View variables
        login_username = (EditText) loginDialogView.findViewById(R.id.login_username);
        login_password = (EditText) loginDialogView.findViewById(R.id.login_password);

        // Attempt to Log In the user
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = login_username.getText().toString();
                String password = login_password.getText().toString();

                LoginTask loginTask = new LoginTask();
                loginTask.execute(username, password);
            }
        });

        return builder.create();
    }

    class LoginTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... params) {
            User user = null;
            try {
                user = APIClient.userGetRequest(params[0]);
            } catch (IOException e) {
                android.util.Log.e(LOG_TAG, "User object JSON conversion failed.");
                return null;
            }
            String hashedPassword = user.getPassword();
            if (BCrypt.checkpw(params[1], hashedPassword)) {
                return user;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            LoginDialogFragment.this.user = user;
        }
    }
}

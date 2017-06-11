package com.example.andy.saints_xctf_android;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * Class for the Login DialogFragment which is used as a form to login users
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class LoginDialogFragment extends DialogFragment {

    private static final String LOG_TAG = LoginDialogFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    // Views in the fragment
    private View v;
    private EditText login_username;
    private EditText login_password;
    private TextView error_message;
    private Button forgot_password;
    private User user;
    private String username;
    private AlertDialog d;
    private View progress;
    private GridLayout login_forms;

    /**
     * Create and Run an AlertDialog for Log Submitting
     * @param bundle --
     * @return --
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        android.util.Log.d(LOG_TAG, "Inside onCreateDialog.");
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View loginDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_login, null);
        v = loginDialogView;
        builder.setView(loginDialogView);

        // set the AlertDialog's message
        builder.setTitle("Log In");

        // Initialize View variables
        login_username = (EditText) loginDialogView.findViewById(R.id.login_username);
        login_password = (EditText) loginDialogView.findViewById(R.id.login_password);
        error_message = (TextView) loginDialogView.findViewById(R.id.error_message);
        forgot_password = (Button) loginDialogView.findViewById(R.id.forgot_password_button);

        // Attempt to Log In the user
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        // Create a click listener on the forgot_password button
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss the login dialog and show the forgot password dialog
                d.dismiss();
                ForgotPasswordDialogFragment fpDialogFragment = new ForgotPasswordDialogFragment();
                fpDialogFragment.show(getFragmentManager(), "forgot password dialog");
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        d = (AlertDialog) getDialog();
        d.setCanceledOnTouchOutside(false);
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    username = login_username.getText().toString();
                    String password = login_password.getText().toString();

                    boolean formError = false;

                    // First Do Validation for the form inputs
                    if (password.length() == 0) {
                        error_message.setText(R.string.no_password);
                        login_password.requestFocus();
                        formError = true;
                    }

                    if (username.length() == 0) {
                        error_message.setText(R.string.no_username);
                        login_username.requestFocus();
                        formError = true;
                    }

                    // Second try to login the user
                    if (!formError) {
                        LoginTask loginTask = new LoginTask();
                        loginTask.execute(username, password);
                    }
                }
            });
        }
    }

    class LoginTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            User user = null;
            try {
                user = APIClient.userGetRequest(params[0]);

                // If null is returned, there is no internet connection
                if (user == null) {
                    return "no_internet";
                }
            } catch (IOException e) {
                android.util.Log.e(LOG_TAG, "User object JSON conversion failed.");
                android.util.Log.e(LOG_TAG, e.getMessage());
                return "invalid_user";
            }
            String hashedPassword = user.getPassword();

            // The hashed password must be altered from the PHP implementation in order to match
            hashedPassword = "$2a$" + hashedPassword.substring(4,hashedPassword.length());

            if (BCrypt.checkpw(params[1], hashedPassword)) {
                return user;
            } else {
                return "invalid password";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = v.findViewById(R.id.progress_overlay);
            login_forms = (GridLayout) v.findViewById(R.id.login_forms);
            login_forms.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response.equals("no_internet")) {
                ((MainActivity) getActivity()).noInternet();
                d.dismiss();
            } else if (response.equals("invalid_user")) {
                error_message.setText(R.string.invalid_user);
                login_username.requestFocus();
            } else if (response.equals("invalid_password")) {
                error_message.setText(R.string.invalid_password);
                login_username.requestFocus();
            } else if (response instanceof User) {

                LoginDialogFragment.this.user = (User) response;
                android.util.Log.d(LOG_TAG, "The User Object Received: " + response.toString());

                if (username.equals(user.getUsername())) {

                    ObjectMapper mapper = new ObjectMapper();
                    String userJsonString = "";
                    try {
                        userJsonString = mapper.writeValueAsString(user);
                    } catch (JsonProcessingException e) {
                        android.util.Log.e(LOG_TAG, "Unable to store user data in preferences.");
                        android.util.Log.e(LOG_TAG, e.getMessage());
                    }

                    SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("user", userJsonString);
                    editor.putString("username", username);
                    editor.putString("first", user.getFirst());
                    editor.putString("last", user.getLast());
                    editor.apply();

                    // Exit the dialog fragment
                    d.dismiss();

                    // Sign In the User and Display the new Fragment
                    ((MainActivity) getActivity()).signIn();
                }
            }
            progress.setVisibility(View.GONE);
            login_forms.setVisibility(View.VISIBLE);
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }
}

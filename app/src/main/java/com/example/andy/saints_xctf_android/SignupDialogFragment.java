package com.example.andy.saints_xctf_android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.andy.api_model.User;
import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for the SignUp DialogFragment which is used as a form to sign up potential users
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class SignupDialogFragment extends DialogFragment {

    // Views in the fragment
    private EditText signup_username;
    private EditText signup_first_name;
    private EditText signup_last_name;
    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_confirm_password;
    private EditText signup_activation_code;
    private TextView signup_error_message;
    private AlertDialog d;
    private View v;
    private View progress;
    private GridLayout signup_forms;

    private static final String LOG_TAG = SignupDialogFragment.class.getName();
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String NAME_REGEX = "^[a-zA-Z\\-']+$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]+$";
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

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
                R.layout.fragment_signup, null);
        v = loginDialogView;
        builder.setView(loginDialogView);

        // set the AlertDialog's message
        builder.setTitle("Sign Up");

        // Initialize View variables
        signup_username = (EditText) loginDialogView.findViewById(R.id.signup_username);
        signup_first_name = (EditText) loginDialogView.findViewById(R.id.signup_first_name);
        signup_last_name = (EditText) loginDialogView.findViewById(R.id.signup_last_name);
        signup_email = (EditText) loginDialogView.findViewById(R.id.signup_email);
        signup_password = (EditText) loginDialogView.findViewById(R.id.signup_password);
        signup_confirm_password = (EditText) loginDialogView.findViewById(R.id.signup_confirm_password);
        signup_activation_code = (EditText) loginDialogView.findViewById(R.id.signup_activation_code);
        signup_error_message = (TextView) loginDialogView.findViewById(R.id.signup_error_message);

        // Attempt to Sign Up a user
        builder.setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
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

                    String username = signup_username.getText().toString();
                    String first = signup_first_name.getText().toString();
                    String last = signup_last_name.getText().toString();
                    String email = signup_email.getText().toString();
                    String password = signup_password.getText().toString();
                    String confirm_password = signup_confirm_password.getText().toString();
                    String activation_code = signup_activation_code.getText().toString();

                    boolean formError = false;

                    // First Do Validation for the form inputs
                    if (activation_code.length() == 0) {
                        signup_error_message.setText(R.string.no_activation_code);
                        signup_activation_code.requestFocus();
                        formError = true;
                    }

                    if (password.length() == 0) {
                        signup_error_message.setText(R.string.no_password);
                        signup_password.requestFocus();
                        formError = true;
                    } else if (password.length() < 6) {
                        signup_error_message.setText(R.string.password_length);
                        signup_password.requestFocus();
                        formError = true;
                    }

                    if (confirm_password.length() == 0) {
                        signup_error_message.setText(R.string.no_cpassword);
                        signup_confirm_password.requestFocus();
                        formError = true;
                    } else if (confirm_password.length() < 6 || !confirm_password.equals(password)) {
                        signup_error_message.setText(R.string.no_password_match);
                        signup_password.requestFocus();
                        formError = true;
                    }

                    Pattern pattern = Pattern.compile(EMAIL_REGEX);
                    Matcher matcher = pattern.matcher(email);

                    if (email.length() == 0) {
                        signup_error_message.setText(R.string.no_email);
                        signup_email.requestFocus();
                        formError = true;
                    } else if (!matcher.matches()) {
                        signup_error_message.setText(R.string.invalid_email);
                        signup_email.requestFocus();
                        formError = true;
                    }

                    Pattern name_pattern = Pattern.compile(NAME_REGEX);
                    Matcher last_matcher = name_pattern.matcher(last);

                    if (last.length() == 0) {
                        signup_error_message.setText(R.string.no_last_name);
                        signup_last_name.requestFocus();
                        formError = true;
                    } else if (!last_matcher.matches()) {
                        signup_error_message.setText(R.string.invalid_last_name);
                        signup_last_name.requestFocus();
                        formError = true;
                    }

                    Matcher first_matcher = name_pattern.matcher(first);

                    if (first.length() == 0) {
                        signup_error_message.setText(R.string.no_first_name);
                        signup_first_name.requestFocus();
                        formError = true;
                    } else if (!first_matcher.matches()) {
                        signup_error_message.setText(R.string.invalid_first_name);
                        signup_first_name.requestFocus();
                        formError = true;
                    }

                    Pattern username_pattern = Pattern.compile(USERNAME_REGEX);
                    Matcher username_matcher = username_pattern.matcher(username);

                    if (username.length() == 0) {
                        signup_error_message.setText(R.string.no_username);
                        signup_username.requestFocus();
                        formError = true;
                    } else if (!username_matcher.matches()) {
                        signup_error_message.setText(R.string.invalid_username);
                        signup_username.requestFocus();
                        formError = true;
                    }

                    // Second try to login the user
                    if (!formError) {
                        SignUpTask signupTask = new SignUpTask();
                        signupTask.execute(username,first,last,email,password,activation_code);
                    }
                }
            });
        }
    }
    class SignUpTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            User user;
            try {
                // First see if there is already a user with this username
                user = APIClient.userGetRequest(params[0]);

                if (user == null) {
                    return "no_internet";
                } else {
                    return "invalid_username";
                }

            } catch (IOException e) {
                // The username is not already used
                Log.d(LOG_TAG, "There is no user with this username.");

                String hash = BCrypt.hashpw(params[4], BCrypt.gensalt());

                // Create the new user object
                User newUser = new User();
                newUser.setUsername(params[0]);
                newUser.setFirst(params[1]);
                newUser.setLast(params[2]);
                newUser.setEmail(params[3]);
                newUser.setPassword(hash);
                newUser.setActivation_code(params[5]);

                // Convert the new user to JSON
                String userJSON;
                try {
                    userJSON = JSONConverter.fromUser(newUser);
                } catch (Throwable t) {
                    android.util.Log.d(LOG_TAG, "Failed to Convert from User to JSON.");
                    Log.d(LOG_TAG, t.getMessage());
                    return "failed";
                }

                try {
                    // Second try to add this user to the database
                    user = APIClient.userPostRequest(userJSON);

                    if (user == null) return "no_internet";

                } catch (IOException e1) {
                    android.util.Log.d(LOG_TAG, "The activation code is invalid.");
                    Log.d(LOG_TAG, e1.getMessage());
                    return "bad_activation_code";
                }

            }
            return user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = v.findViewById(R.id.progress_overlay);
            signup_forms = (GridLayout) v.findViewById(R.id.signup_forms);
            signup_forms.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response.equals("no_internet")) {
                signup_error_message.setText(R.string.no_internet);
            } else if (response.equals("invalid_username")) {
                signup_error_message.setText(R.string.invalid_username);
                signup_username.requestFocus();
            } else if (response.equals("bad_activation_code")) {
                signup_error_message.setText(R.string.invalid_activation_code);
                signup_username.requestFocus();
            } else if (response.equals("failed")) {
                signup_error_message.setText(R.string.server_error);
            } else if (response instanceof User) {

                User user = (User) response;
                android.util.Log.d(LOG_TAG, "The User Object Received: " + user.toString());

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
                editor.putString("username", user.getUsername());
                editor.putString("first", user.getFirst());
                editor.putString("last", user.getLast());
                editor.apply();

                // Exit the dialog fragment
                d.dismiss();

                // Sign In the User and Display the new Fragment
                ((MainActivity) getActivity()).signUp();
            }
            progress.setVisibility(View.GONE);
            signup_forms.setVisibility(View.VISIBLE);
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

}

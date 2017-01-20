package com.example.andy.saints_xctf_android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.User;

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

    private static final String LOG_TAG = SignupDialogFragment.class.getName();
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String NAME_REGEX = "^[a-zA-Z\\-']+$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]+$";

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

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        d = (AlertDialog) getDialog();
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
    class SignUpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            User user;
            try {
                user = APIClient.userGetRequest(params[0]);
            } catch (IOException e) {
                android.util.Log.e(LOG_TAG, "User object JSON conversion failed.");
                android.util.Log.e(LOG_TAG, e.getMessage());
                return null;
            }
            String hashedPassword = user.getPassword();
            if (BCrypt.checkpw(params[1], hashedPassword)) {
                return 0;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);

            if (response == 2) {
                d.dismiss();
            }
        }
    }

}

package com.example.andy.saints_xctf_android;

import android.app.Dialog;
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

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.Mail;
import com.example.andy.api_model.User;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for the Login DialogFragment which is used as a form to login users
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class ForgotPasswordDialogFragment extends DialogFragment {

    private static final String LOG_TAG = ForgotPasswordDialogFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    // Views in the fragment
    private View v;
    private AlertDialog d;
    private EditText enterEmail;
    private Button submitEmail;
    private TextView submitEmailError;
    private View progress;
    private GridLayout forgotPasswordEnterEmail;
    private GridLayout forgotPasswordCreateNew;
    private GridLayout forgotPasswordChanged;
    private TextView changePasswordError;

    private TextView newPassword;
    private TextView confirmNewPassword;
    private TextView verificationCode;
    private Button submitNewPassword;

    private User user;

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
        View forgotPasswordDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_forgot_password, null);
        v = forgotPasswordDialogView;
        builder.setView(forgotPasswordDialogView);

        // set the AlertDialog's message
        builder.setTitle("Forgot Password");

        // Initialize View variables
        forgotPasswordEnterEmail = (GridLayout) v.findViewById(R.id.forgot_password_enter_email);
        forgotPasswordCreateNew = (GridLayout) v.findViewById(R.id.forgot_password_create_new);
        forgotPasswordChanged = (GridLayout) v.findViewById(R.id.forgot_password_changed);

        enterEmail = (EditText) forgotPasswordDialogView.findViewById(R.id.enter_email);
        submitEmail = (Button) forgotPasswordDialogView.findViewById(R.id.submit_email_button);
        submitEmailError = (TextView) forgotPasswordDialogView.findViewById(R.id.forgot_password_enter_email_error_message);

        newPassword = (TextView) v.findViewById(R.id.new_password);
        confirmNewPassword = (TextView) v.findViewById(R.id.confirm_new_password);
        verificationCode = (TextView) v.findViewById(R.id.verification_code);
        submitNewPassword = (Button) v.findViewById(R.id.submit_new_password_button);
        changePasswordError = (TextView) forgotPasswordDialogView.findViewById(R.id.change_password_error_message);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        d = (AlertDialog) getDialog();
        d.setCanceledOnTouchOutside(false);
        if (d != null) {

            forgotPasswordEnterEmail.setVisibility(View.VISIBLE);

            // At First, the Forgot Password - Enter Email Fragment is Visible

            // Triggered when the Submit Email Button is Clicked
            submitEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = enterEmail.getText().toString();

                    Pattern pattern = Pattern.compile(EMAIL_REGEX);
                    Matcher matcher = pattern.matcher(email);

                    if (email.length() == 0) {
                        submitEmailError.setText(R.string.no_email);
                        enterEmail.requestFocus();
                    } else if (!matcher.matches()) {
                        submitEmailError.setText(R.string.invalid_email);
                        enterEmail.requestFocus();
                    } else {
                        // Valid Email
                        ForgotPasswordEmailTask fpwEmailTask = new ForgotPasswordEmailTask();
                        fpwEmailTask.execute(email);
                    }
                }
            });

            // Once a Valid Email has been Entered, the Forgot Password - Create New Fragment is Visible

            submitNewPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean formError = false;

                    // Validate the Inputs
                    if (verificationCode.length() == 0) {
                        changePasswordError.setText(R.string.no_activation_code);
                        verificationCode.requestFocus();
                        formError = true;
                    }
                }
            });
        }
    }

    class ForgotPasswordEmailTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            User user;
            try {
                // See if there is already a user with this email
                user = APIClient.userGetRequest(params[0]);

                if (user == null) {
                    return "no_internet";
                } else {
                    // Send an email to the user with forgot password code
                    String code = "";

                    Mail mail = new Mail();
                    mail.setEmailAddress(params[0]);
                    mail.setSubject("Saintsxctf.com Forgot Password");
                    mail.setBody("<html>" +
                            "<head>" +
                            "<title>HTML email</title>" +
                            "</head>" +
                            "<body>" +
                                "<h3>Forgot Password</h3>" +
                                "<br><p>You Forgot Your Password!  Your password is one-way encrypted and salted in our database" +
                                " (AKA There is currently no known way for anyone to hack it).  So make it simple!</p>" +
                                "<br><br><p>Use the following confirmation code to reset your password:</p><br>" +
                                "<p><b>Code: </b> " + code + "</p>" +
                                "<p><b>Username: </b> " + user.getUsername() + "</p>" +
                                "</body>" +
                            "</html>");

                    String mailString = "";
                    try {
                        mailString = JSONConverter.fromMail(mail);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    APIClient.mailPostRequest(mailString);

                    user.setFpw_code(code);

                    String userString = "";

                    try {
                        userString = JSONConverter.fromUser(user);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    APIClient.userPutRequest(user.getUsername(), userString);

                    return user;
                }

            } catch (IOException e) {
                Log.d(LOG_TAG, "There is no user with this email.");
                e.printStackTrace();
                return "invalid_email";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = v.findViewById(R.id.progress_overlay);
            forgotPasswordEnterEmail.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response.equals("no_internet")) {
                submitEmailError.setText(R.string.no_internet);
            } else if (response.equals("invalid_email")) {
                submitEmailError.setText(R.string.no_user_email);
            } else if (response instanceof User) {

                user = (User) response;
                android.util.Log.d(LOG_TAG, "The User Object Received: " + user.toString());
            }

            progress.setVisibility(View.GONE);
            forgotPasswordCreateNew.setVisibility(View.VISIBLE);
        }
    }
}

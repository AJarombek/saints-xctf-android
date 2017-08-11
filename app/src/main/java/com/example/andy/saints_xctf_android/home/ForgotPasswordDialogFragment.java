package com.example.andy.saints_xctf_android.home;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.andy.api_model.services.APIClient;
import com.example.andy.api_model.services.JSONConverter;
import com.example.andy.api_model.pojos.Mail;
import com.example.andy.api_model.pojos.User;
import com.example.andy.saints_xctf_android.R;
import com.example.andy.saints_xctf_android.utils.ControllerUtils;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Arrays;
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

    private String currentView = "forgotPasswordEnterEmail";

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

        enterEmail = (EditText) v.findViewById(R.id.enter_email);
        submitEmail = (Button) v.findViewById(R.id.submit_email_button);
        submitEmailError = (TextView) v.findViewById(R.id.forgot_password_enter_email_error_message);

        newPassword = (TextView) v.findViewById(R.id.new_password);
        confirmNewPassword = (TextView) v.findViewById(R.id.confirm_new_password);
        verificationCode = (TextView) v.findViewById(R.id.verification_code);
        submitNewPassword = (Button) v.findViewById(R.id.submit_new_password_button);
        changePasswordError = (TextView) v.findViewById(R.id.change_password_error_message);

        // Check the saved state on rotations - see which view should be visible
        if (bundle != null && bundle.containsKey("forgotPasswordCreateNew")) {
            currentView = "forgotPasswordCreateNew";
            forgotPasswordEnterEmail.setVisibility(View.GONE);
            forgotPasswordCreateNew.setVisibility(View.VISIBLE);
            forgotPasswordChanged.setVisibility(View.GONE);
        } else if (bundle != null && bundle.containsKey("forgotPasswordChanged")) {
            currentView = "forgotPasswordChanged";
            forgotPasswordEnterEmail.setVisibility(View.GONE);
            forgotPasswordCreateNew.setVisibility(View.GONE);
            forgotPasswordChanged.setVisibility(View.VISIBLE);
        }

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        d = (AlertDialog) getDialog();
        d.setCanceledOnTouchOutside(true);
        if (d != null) {

            // At First, the Forgot Password - Enter Email Fragment is Visible

            // Triggered when the Submit Email Button is Clicked
            submitEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = enterEmail.getText().toString();

                    Pattern pattern = Pattern.compile(EMAIL_REGEX);
                    Matcher matcher = pattern.matcher(email);

                    // Validate the Email Input
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

                    String verCode = verificationCode.getText().toString();
                    String pw = newPassword.getText().toString();
                    String cpw = confirmNewPassword.getText().toString();

                    // Validate the Inputs
                    if (verCode.length() == 0) {
                        changePasswordError.setText(R.string.no_activation_code);
                        verificationCode.requestFocus();
                        formError = true;
                    }

                    if (cpw.length() == 0) {
                        changePasswordError.setText(R.string.no_cpassword);
                        confirmNewPassword.requestFocus();
                        formError = true;
                    } else if (cpw.length() < 6 || !cpw.equals(pw)) {
                        changePasswordError.setText(R.string.no_password_match);
                        confirmNewPassword.requestFocus();
                        formError = true;
                    }

                    if (pw.length() == 0) {
                        changePasswordError.setText(R.string.no_password);
                        newPassword.requestFocus();
                        formError = true;
                    } else if (pw.length() < 6) {
                        changePasswordError.setText(R.string.password_length);
                        newPassword.requestFocus();
                        formError = true;
                    }

                    if (!formError) {
                        // If no errors, try to set the new password
                        NewPasswordTask newPasswordTask = new NewPasswordTask();
                        newPasswordTask.execute(pw, verCode);
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(currentView, "true");
    }

    class ForgotPasswordEmailTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            User user;
            try {
                // See if there is already a user with this email
                user = APIClient.userGetRequest(params[0]);

                if (user == null) {
                    return "invalid_email";
                } else {
                    // Send an email to the user with forgot password code
                    String code = ControllerUtils.generateCode(8);

                    // Build the Mail Object
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

                    // Convert the mail object to JSON
                    String mailString = "";
                    try {
                        mailString = JSONConverter.fromMail(mail);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return "server error";
                    }

                    // Send the Mail
                    APIClient.mailPostRequest(mailString);

                    // Set the forgot password code for the user
                    user.setFpw_code(code);

                    String userString = "";

                    try {
                        userString = JSONConverter.fromUser(user);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return "server error";
                    }

                    // PUT request to add forgot password code on user
                    user = APIClient.userPutRequest(user.getUsername(), userString);

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

            // Display the progress spinner
            progress = v.findViewById(R.id.progress_overlay);
            forgotPasswordEnterEmail.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            // display error messages if operation failed
            if (response.equals("no_internet")) {
                submitEmailError.setText(R.string.no_internet);
            } else if (response.equals("invalid_email")) {
                submitEmailError.setText(R.string.no_user_email);
            } else if (response.equals("server error")) {
                submitEmailError.setText(R.string.server_errors);
            } else if (response instanceof User) {

                user = (User) response;
                android.util.Log.d(LOG_TAG, "The User Object Received: " + user.toString());
            }

            // Move to the forgot password - create new view
            progress.setVisibility(View.GONE);
            forgotPasswordCreateNew.setVisibility(View.VISIBLE);
            currentView = "forgotPasswordCreateNew";
        }
    }

    class NewPasswordTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {

            // If the verification code matches one on the user object, change the password
            if (Arrays.asList(user.getForgotpassword()).contains(params[1])) {
                android.util.Log.d(LOG_TAG, "The Forgot Password Code Is Valid.");

                // Hash & Salt New Password
                String hash = BCrypt.hashpw(params[0], BCrypt.gensalt());

                user.setFpw_delete_code(params[1]);
                user.setFpw_password(hash);

                String userString;
                try {
                    userString = JSONConverter.fromUser(user);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    return "server error";
                }

                // PUT request to alter user with the delete verification code and new password
                User newuser;
                try {
                    newuser = APIClient.userPutRequest(user.getUsername(), userString);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "no internet";
                }

                return newuser;

            } else {
                return "invalidVerificationCode";
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Display the progress spinner
            progress = v.findViewById(R.id.progress_overlay);
            forgotPasswordCreateNew.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            // Display errors if operation failed
            if (response.equals("no_internet")) {
                submitEmailError.setText(R.string.no_internet);
            } else if (response.equals("server error")) {
                submitEmailError.setText(R.string.server_errors);
            } else if (response instanceof User) {

                user = (User) response;
                android.util.Log.d(LOG_TAG, "The User Object Received: " + user.toString());
            }

            // Change the view to forgot password changed
            progress.setVisibility(View.GONE);
            forgotPasswordChanged.setVisibility(View.VISIBLE);
            currentView = "forgotPasswordChanged";

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Dismiss the Dialog Fragment After 3 Seconds
                    d.dismiss();
                }
            }, 3000);
        }
    }
}

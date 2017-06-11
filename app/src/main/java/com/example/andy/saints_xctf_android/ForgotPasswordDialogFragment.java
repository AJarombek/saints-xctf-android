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
public class ForgotPasswordDialogFragment extends DialogFragment {

    private static final String LOG_TAG = ForgotPasswordDialogFragment.class.getName();
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
        builder.setTitle("Forgot Password");

        // Initialize View variables

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        d = (AlertDialog) getDialog();
        d.setCanceledOnTouchOutside(false);
        if (d != null) {

        }
    }
}

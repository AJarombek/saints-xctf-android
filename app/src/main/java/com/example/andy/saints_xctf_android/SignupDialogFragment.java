package com.example.andy.saints_xctf_android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

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
    private EditText signup_password;
    private EditText signup_confirm_password;

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
        signup_password = (EditText) loginDialogView.findViewById(R.id.signup_password);
        signup_confirm_password = (EditText) loginDialogView.findViewById(R.id.signup_confirm_password);

        // Attempt to Sign Up a user
        builder.setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}

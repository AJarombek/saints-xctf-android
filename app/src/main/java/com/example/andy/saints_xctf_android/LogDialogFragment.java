package com.example.andy.saints_xctf_android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

// class for the Select Color dialog

/**
 * Class for the Log DialogFragment which is used for creating new exercise logs
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class LogDialogFragment extends DialogFragment {

    // Views in the fragment
    private EditText log_run_name;
    private EditText log_location;
    private TextView log_date;
    private Button log_date_button;
    private Spinner log_type;
    private EditText log_distance;
    private Spinner log_metric;
    private EditText log_time_hours;
    private EditText log_time_minutes;
    private EditText log_time_seconds;
    private SeekBar log_feel;
    private EditText log_description;

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
        View logDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_log, null);
        builder.setView(logDialogView); // add GUI to dialog

        // set the AlertDialog's message
        builder.setTitle(R.string.title_log_dialog);

        // Initialize View variables
        log_run_name = (EditText) logDialogView.findViewById(R.id.log_run_name);
        log_location = (EditText) logDialogView.findViewById(R.id.log_location);

        log_date = (TextView) logDialogView.findViewById(R.id.log_date);
        log_date_button = (Button) logDialogView.findViewById(R.id.log_date_button);
        log_type = (Spinner) logDialogView.findViewById(R.id.log_type);

        log_distance = (EditText) logDialogView.findViewById(R.id.log_distance);

        log_metric = (Spinner) logDialogView.findViewById(R.id.log_metric);
        log_feel = (SeekBar) logDialogView.findViewById(R.id.log_feel);

        log_time_hours = (EditText) logDialogView.findViewById(R.id.log_time_hours);
        log_time_minutes = (EditText) logDialogView.findViewById(R.id.log_time_minutes);
        log_time_seconds = (EditText) logDialogView.findViewById(R.id.log_time_seconds);
        log_description = (EditText) logDialogView.findViewById(R.id.log_description);

        // Cancel the Log
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Post the Log
        builder.setPositiveButton("Post Log", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create(); // return dialog
    }
}

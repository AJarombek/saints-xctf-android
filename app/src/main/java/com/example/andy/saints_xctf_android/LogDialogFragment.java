package com.example.andy.saints_xctf_android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for the Log DialogFragment which is used for creating new exercise logs
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class LogDialogFragment extends DialogFragment {

    private static final String[] COLOR_DESCRIPTION = {MainActivity.DESCRIPT_TERRIBLE,
            MainActivity.DESCRIPT_VERYBAD, MainActivity.DESCRIPT_BAD, MainActivity.DESCRIPT_PRETTYBAD,
            MainActivity.DESCRIPT_MEDIOCRE, MainActivity.DESCRIPT_AVERAGE,
            MainActivity.DESCRIPT_FAIRLYGOOD, MainActivity.DESCRIPT_GOOD,
            MainActivity.DESCRIPT_GREAT, MainActivity.DESCRIPT_FANTASTIC};
    private static final String DISTANCE_REGEX = "^[0-9]{0,3}(\\.[0-9]{1,2})?$";
    private static final String MINUTE_REGEX = "^[0-9]{1,5}$";
    private static final String SECOND_REGEX = "^[0-9]{1,2}$";

    // Views in the fragment
    private TextView log_feel_view;
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
    private TextView log_error_message;
    private Calendar calendar;
    private AlertDialog d;

    /**
     * Create and Run an AlertDialog for Log Submitting
     * @param bundle --
     * @return --
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialog = super.onCreateDialog(bundle);
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View logDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_log, null);
        builder.setView(logDialogView); // add GUI to dialog

        // Make this dialog fragment have no title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Initialize View variables
        log_feel_view = (TextView) logDialogView.findViewById(R.id.log_feel_view);
        log_run_name = (EditText) logDialogView.findViewById(R.id.log_run_name);
        log_location = (EditText) logDialogView.findViewById(R.id.log_location);

        log_date = (TextView) logDialogView.findViewById(R.id.log_date);
        log_date_button = (Button) logDialogView.findViewById(R.id.log_date_button);
        log_type = (Spinner) logDialogView.findViewById(R.id.log_type);

        log_distance = (EditText) logDialogView.findViewById(R.id.log_distance);

        log_metric = (Spinner) logDialogView.findViewById(R.id.log_metric);
        log_feel = (SeekBar) logDialogView.findViewById(R.id.log_feel);

        log_time_minutes = (EditText) logDialogView.findViewById(R.id.log_time_minutes);
        log_time_seconds = (EditText) logDialogView.findViewById(R.id.log_time_seconds);
        log_description = (EditText) logDialogView.findViewById(R.id.log_description);
        log_error_message = (TextView) logDialogView.findViewById(R.id.log_error_message);

        // Populate the spinner for the workout type
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.log_type_array,
                        android.R.layout.simple_spinner_item);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        log_type.setAdapter(typeAdapter);

        // Populate the spinner for the distance metric
        ArrayAdapter<CharSequence> metricAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.log_metric_array,
                        android.R.layout.simple_spinner_item);

        metricAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        log_metric.setAdapter(metricAdapter);

        // Get the current date and set the log date text
        calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("EST"));
        log_date.setText((calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR));

        // Create a click listener for the log date button.  It will start the DatePickerDialog
        log_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), dateOnDateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        // As the user slides the seekbar, make changes
        log_feel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Change the text value for the logs feel
                log_feel_view.setText(COLOR_DESCRIPTION[progress].toUpperCase());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Cancel the Log
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        // Post the Log
        builder.setPositiveButton("Post Log", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        return builder.create(); // return dialog
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

                    String name = log_run_name.getText().toString();
                    String location = log_location.getText().toString();
                    String type = log_type.getSelectedItem().toString();
                    String distance = log_distance.getText().toString();
                    String metric = log_metric.getSelectedItem().toString();
                    String minutes = log_time_minutes.getText().toString();
                    String seconds = log_time_seconds.getText().toString();
                    int feel = log_feel.getProgress() + 1;
                    String description = log_location.getText().toString();

                    boolean formError = false;

                    if (distance.equals("") && seconds.equals("") && minutes.equals("") ||
                            seconds.equals("") && minutes.equals("") && distance.equals("")) {
                        log_error_message.setText("Either Distance or Time Must Be Entered");
                        log_date_button.requestFocus();
                        formError = true;
                    }

                    Pattern distance_pattern = Pattern.compile(DISTANCE_REGEX);
                    Matcher distance_matcher = distance_pattern.matcher(distance);

                    if (!distance_matcher.matches()) {
                        log_error_message.setText("Invalid Distance Entered");
                        log_distance.requestFocus();
                        formError = true;
                    }

                    Pattern minute_pattern = Pattern.compile(MINUTE_REGEX);
                    Matcher minute_matcher = minute_pattern.matcher(minutes);

                    if (!minute_matcher.matches()) {
                        log_error_message.setText("Invalid Minutes Entered");
                        log_time_minutes.requestFocus();
                        formError = true;
                    }

                    Pattern second_pattern = Pattern.compile(SECOND_REGEX);
                    Matcher second_matcher = second_pattern.matcher(seconds);

                    if (!second_matcher.matches() || Integer.parseInt(seconds) > 59) {
                        log_error_message.setText("Invalid Seconds Entered");
                        log_time_seconds.requestFocus();
                        formError = true;
                    }

                    Calendar today = GregorianCalendar.getInstance(TimeZone.getTimeZone("EST"));
                    Calendar calendar2016 = GregorianCalendar.getInstance();
                    calendar2016.set(2016,0,1);

                    if (calendar.compareTo(today) > 0) {
                        log_error_message.setText("Invalid Date. The Date Selected Is In The Future");
                        log_date_button.requestFocus();
                        formError = true;
                    } else if (calendar.compareTo(calendar2016) < 0) {
                        log_error_message.setText("Invalid Date. The Date Must Be After Jan 1, 2016");
                        log_date_button.requestFocus();
                        formError = true;
                    }

                    // First Do Validation for the form inputs
                    if (name.length() == 0) {
                        log_error_message.setText("No Log Name Entered");
                        log_run_name.requestFocus();
                        formError = true;
                    }

                    // Second try to submit the log
                    if (!formError) {

                    }
                }
            });
        }
    }

    // Listener for when the user picks a date
    private DatePickerDialog.OnDateSetListener dateOnDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            log_date.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
            calendar.set(year, monthOfYear, dayOfMonth);
        }

    };
}

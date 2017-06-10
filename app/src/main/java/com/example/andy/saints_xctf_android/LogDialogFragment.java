package com.example.andy.saints_xctf_android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.Log;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.andy.saints_xctf_android.R.id.signup_error_message;

/**
 * Class for the Log DialogFragment which is used for creating new exercise logs
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class LogDialogFragment extends DialogFragment {

    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    private static final String LOG_TAG = LogDialogFragment.class.getName();
    public static final int REQUEST_CODE = 0;
    public static final String NEW_LOG_KEY = "new_log";

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
    private EditText log_time_minutes;
    private EditText log_time_seconds;
    private SeekBar log_feel;
    private EditText log_description;
    private TextView log_error_message;
    private Calendar calendar;
    private AlertDialog d;
    private int feel;
    private String name,location,type,distance,metric,minutes,seconds,description,date;
    private View v;
    private View progress;
    private LinearLayout log_forms;

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
        v = logDialogView;
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
        d.setCanceledOnTouchOutside(false);
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    name = log_run_name.getText().toString();
                    location = log_location.getText().toString();
                    type = log_type.getSelectedItem().toString();
                    distance = log_distance.getText().toString();
                    metric = log_metric.getSelectedItem().toString();
                    minutes = log_time_minutes.getText().toString();
                    seconds = log_time_seconds.getText().toString();
                    feel = log_feel.getProgress() + 1;
                    description = log_description.getText().toString();
                    date = log_date.getText().toString();

                    String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

                    if (month.length() == 1)
                        month = "0" + month;

                    date = year + "-" + month + "-" + day;

                    // Second try to submit the log
                    if (!validateForms()) {
                        SharedPreferences prefs = getContext().getSharedPreferences(
                                PREFS_NAME, Context.MODE_PRIVATE);
                        String username = prefs.getString("username", "");
                        String first = prefs.getString("first", "");
                        String last = prefs.getString("last", "");

                        // Get the time in the correct format
                        String time = null;
                        if (!minutes.equals("")) {
                            int hours = Integer.parseInt(minutes) / 60;
                            minutes = String.valueOf(Integer.parseInt(minutes) % 60);
                            time = hours + ":" + minutes + ":" + seconds;
                        }

                        if (distance.equals(""))
                            distance = "0";

                        LogTask logTask = new LogTask();
                        logTask.execute(username,first,last,name,location,type,distance,
                                metric,time,minutes,seconds,
                                String.valueOf(feel),description,date);
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

    private boolean validateForms() {

        // First Do Validation for the form inputs
        if (name.length() == 0) {
            log_error_message.setText(R.string.no_log_name);
            log_run_name.requestFocus();
            return true;
        }

        Calendar today = GregorianCalendar.getInstance(TimeZone.getTimeZone("EST"));
        Calendar calendar2016 = GregorianCalendar.getInstance();
        calendar2016.set(2016,0,1);

        if (calendar.compareTo(today) > 0) {
            log_error_message.setText(R.string.invalid_date_future);
            log_date_button.requestFocus();
            return true;
        } else if (calendar.compareTo(calendar2016) < 0) {
            log_error_message.setText(R.string.invalid_date_past);
            log_date_button.requestFocus();
            return true;
        }

        if (distance.equals("") && seconds.equals("") && minutes.equals("")) {
            log_error_message.setText(R.string.no_distance_or_time);
            log_date_button.requestFocus();
            return true;
        }

        Pattern distance_pattern = Pattern.compile(DISTANCE_REGEX);
        Matcher distance_matcher = distance_pattern.matcher(distance);

        Pattern minute_pattern = Pattern.compile(MINUTE_REGEX);
        Matcher minute_matcher = minute_pattern.matcher(minutes);

        Pattern second_pattern = Pattern.compile(SECOND_REGEX);
        Matcher second_matcher = second_pattern.matcher(seconds);

        if (!distance_matcher.matches()) {
            if (distance.equals("") && minute_matcher.matches() && second_matcher.matches()) {
                return false;
            }
            log_error_message.setText(R.string.invalid_distance);
            log_distance.requestFocus();
            return true;
        }

        if (minutes.equals("") && seconds.equals(""))
            return false;

        if (!minute_matcher.matches()) {
            if (minutes.equals("") && (second_matcher.matches() && Integer.parseInt(seconds) <= 59)) {
                return false;
            }
            log_error_message.setText(R.string.invalid_minutes);
            log_time_minutes.requestFocus();
            return true;
        }

        if (!second_matcher.matches() || Integer.parseInt(seconds) > 59) {
            log_error_message.setText(R.string.invalid_seconds);
            log_time_seconds.requestFocus();
            return true;
        }

        return false;
    }

    class LogTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            Log log;

            // Create the new log object
            log = new Log();
            log.setUsername(params[0]);
            log.setFirst(params[1]);
            log.setLast(params[2]);
            log.setName(params[3]);
            log.setLocation(params[4]);
            log.setType(params[5]);

            double distance = Double.parseDouble(params[6]);
            log.setDistance(distance);
            log.setMiles(ControllerUtils.convertToMiles(distance, params[7]));
            log.setMetric(params[7]);

            String time = params[8];
            if (time == null || time.equals(":"))
                time = "00:00:00";

            log.setTime(time);
            log.setPace(ControllerUtils.milePace(distance, params[9], params[10]));
            log.setFeel(Integer.parseInt(params[11]));
            log.setDescription(params[12]);
            log.setDate(params[13]);

            // Convert the new log to JSON
            String logJSON;
            try {
                logJSON = JSONConverter.fromLog(log);
            } catch (Throwable t) {
                android.util.Log.d(LOG_TAG, "Failed to Convert from Log to JSON.");
                android.util.Log.d(LOG_TAG, t.getMessage());
                return "internal_error";
            }

            try {
                // add log to the database
                android.util.Log.d(LOG_TAG, logJSON);
                log = APIClient.logPostRequest(logJSON);

                if (log == null) return "no_internet";

            } catch (IOException e) {
                android.util.Log.d(LOG_TAG, "The log failed to be uploaded.");
                android.util.Log.d(LOG_TAG, e.getMessage());
                return "internal_error";
            }

            return log;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = v.findViewById(R.id.progress_overlay);
            log_forms = (LinearLayout) v.findViewById(R.id.log_forms);
            log_forms.setVisibility(View.GONE);
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
            } else if (response.equals("internal_error")) {
                log_error_message.setText(R.string.internal_error);
            }  else if (response instanceof Log) {

                Log log = (Log) response;
                android.util.Log.d(LOG_TAG, "The Log Object Received: " + log.toString());

                String logJSON = "";
                try {
                    logJSON = JSONConverter.fromLog(log);
                } catch (Throwable t) {
                    android.util.Log.d(LOG_TAG, "Failed to Convert from Log to JSON.");
                    android.util.Log.d(LOG_TAG, t.getMessage());
                }

                sendResult(logJSON, REQUEST_CODE);
                d.dismiss();
            }
            progress.setVisibility(View.GONE);
            log_forms.setVisibility(View.VISIBLE);
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    private void sendResult(String logJSON, int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.putExtra(NEW_LOG_KEY, logJSON);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}

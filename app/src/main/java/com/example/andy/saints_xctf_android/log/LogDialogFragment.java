package com.example.andy.saints_xctf_android.log;

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

import com.example.andy.api_model.services.APIClient;
import com.example.andy.api_model.services.JSONConverter;
import com.example.andy.api_model.pojos.Log;
import com.example.andy.api_model.pojos.Notification;
import com.example.andy.saints_xctf_android.MainActivity;
import com.example.andy.saints_xctf_android.async.NotificationTask;
import com.example.andy.saints_xctf_android.R;
import com.example.andy.saints_xctf_android.utils.ControllerUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    private static final String LOG_TAG = LogDialogFragment.class.getName();
    public static final int REQUEST_CODE_NEW_LOG = 0;
    public static final int REQUEST_CODE_UPDATED_LOG = 1;
    public static final String NEW_LOG_KEY = "new_log";
    public static final String UPDATED_LOG_INDEX = "updated_log_index";

    private static final String[] COLOR_DESCRIPTION = {MainActivity.DESCRIPT_TERRIBLE,
            MainActivity.DESCRIPT_VERYBAD, MainActivity.DESCRIPT_BAD, MainActivity.DESCRIPT_PRETTYBAD,
            MainActivity.DESCRIPT_MEDIOCRE, MainActivity.DESCRIPT_AVERAGE,
            MainActivity.DESCRIPT_FAIRLYGOOD, MainActivity.DESCRIPT_GOOD,
            MainActivity.DESCRIPT_GREAT, MainActivity.DESCRIPT_FANTASTIC};
    private static final String DISTANCE_REGEX = "^[0-9]{0,3}(\\.[0-9]{1,2})?$";
    private static final String MINUTE_REGEX = "^[0-9]{1,5}$";
    private static final String SECOND_REGEX = "^[0-9]{1,2}$";
    private static final String USER_TAG_REGEX = "@[a-zA-Z0-9]+";

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
    private boolean isNew;
    private String log_id;
    private int log_array_index;

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

        // If the log already exists, populate the fields with the existing values
        Bundle args = getArguments();
        if (args != null && args.containsKey("logString")) {
            isNew = false;
            android.util.Log.i(LOG_TAG, "Existing Log");

            try {
                Log log = JSONConverter.toLog(getArguments().getString("logString"));
                log_array_index = getArguments().getInt("logIndex");

                android.util.Log.i(LOG_TAG, "Editing Existing Log");

                log_id = String.valueOf(log.getLog_id());

                log_run_name.setText(log.getName());
                log_location.setText(log.getLocation());

                int typePosition = typeAdapter.getPosition(log.getType());
                log_type.setSelection(typePosition);

                log_distance.setText(String.valueOf(log.getDistance()));

                int metricPosition = metricAdapter.getPosition(log.getMetric());
                log_metric.setSelection(metricPosition);

                String time = log.getTime();

                int minutes = (Integer.parseInt(time.substring(0, 2)) * 60) +
                                Integer.parseInt(time.substring(3, 5));
                int seconds = Integer.parseInt(time.substring(6, 8));

                if (minutes != 0) {
                    if (minutes < 10) {
                        log_time_minutes.setText("0" + String.valueOf(minutes));
                    } else {
                        log_time_minutes.setText(String.valueOf(minutes));
                    }
                }

                if (seconds != 0) {
                    if (seconds < 10) {
                        log_time_seconds.setText("0" + String.valueOf(seconds));
                    } else {
                        log_time_seconds.setText(String.valueOf(seconds));
                    }
                }

                log_feel.setProgress(log.getFeel() - 1);
                log_description.setText(log.getDescription());

                String dateString = log.getDate();
                int year = Integer.parseInt(dateString.substring(0,4));
                int month = Integer.parseInt(dateString.substring(5,7));
                int day = Integer.parseInt(dateString.substring(8,10));

                calendar.set(year,month-1,day);

                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                df.setCalendar(calendar);
                String date = df.format(calendar.getTime());
                log_date.setText(date);

            } catch (IOException e) {
                android.util.Log.e(LOG_TAG, "Failed to Convert Existing Log Bundle to Log");
                e.printStackTrace();
            }
        } else {
            isNew = true;
            android.util.Log.i(LOG_TAG, "New Log");
        }

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
                                String.valueOf(feel),description,date,log_id);
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

    /**
     * LogTask is an asynchronous job for creating new logs and updating existing logs
     */
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
            double miles = ControllerUtils.convertToMiles(distance, params[7]);
            log.setMiles(miles);
            log.setMetric(params[7]);

            String time = params[8];
            if (time == null || time.equals(":"))
                time = "00:00:00";

            log.setTime(time);
            log.setPace(ControllerUtils.milePace(miles, params[9], params[10]));
            log.setFeel(Integer.parseInt(params[11]));
            log.setDescription(params[12]);
            log.setDate(params[13]);

            if (params[14] != null)
                log.setLog_id(Integer.parseInt(params[14]));

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
                android.util.Log.d(LOG_TAG, logJSON);

                // add log to the database or update log in database
                if (params[14] == null) {
                    log = APIClient.logPostRequest(logJSON);
                } else {
                    android.util.Log.i(LOG_TAG, "Updating Log #" + log_id);
                    log = APIClient.logPutRequest(Integer.parseInt(params[14]), logJSON);
                }

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

                // Send a notification to each user tagged in the log
                Pattern pattern = Pattern.compile(USER_TAG_REGEX);
                Matcher matcher = pattern.matcher(log.getDescription());

                while (matcher.find()) {
                    String tag = matcher.group().substring(1);

                    // You can't get a notification if you tag yourself
                    if (!tag.equals(log.getUsername())) {
                        NotificationTask notificationTask = new NotificationTask();

                        Notification notification = new Notification();
                        notification.setUsername(tag);
                        notification.setViewed("N");
                        notification.setLink("https://www.saintsxctf.com/log.php?logno=" + log.getLog_id());
                        notification.setDescription(log.getFirst() + " " + log.getLast() + " Mentioned You in a Log.");

                        notificationTask.execute(notification);
                    }

                }

                String logJSON = "";
                try {
                    logJSON = JSONConverter.fromLog(log);
                } catch (Throwable t) {
                    android.util.Log.d(LOG_TAG, "Failed to Convert from Log to JSON.");
                    android.util.Log.d(LOG_TAG, t.getMessage());
                }

                // Send an intent to the recycler adapter saying that the log has been created/updated
                if (isNew) {
                    sendNewResult(logJSON, REQUEST_CODE_NEW_LOG);
                } else {
                    sendUpdateResult(logJSON, log_array_index, REQUEST_CODE_UPDATED_LOG);
                }

                d.dismiss();
            }
            progress.setVisibility(View.GONE);
            log_forms.setVisibility(View.VISIBLE);
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    /**
     * Send an intent of the newly created log
     * @param logJSON the JSON String for the log
     * @param REQUEST_CODE code for the recycler adapter
     */
    private void sendNewResult(String logJSON, int REQUEST_CODE) {
        android.util.Log.i(LOG_TAG, "Sending New Log Intent");
        Intent intent = new Intent();
        intent.putExtra(NEW_LOG_KEY, logJSON);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }

    /**
     * Send an intent of the updated log
     * @param logJSON the JSON string for the log
     * @param index the index of the log in the recycler adapter
     * @param REQUEST_CODE code for the recycler adapter
     */
    private void sendUpdateResult(String logJSON, int index, int REQUEST_CODE) {
        android.util.Log.i(LOG_TAG, "Sending Update Log Intent");
        Intent intent = new Intent();
        intent.putExtra(NEW_LOG_KEY, logJSON);
        intent.putExtra(UPDATED_LOG_INDEX, index);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}

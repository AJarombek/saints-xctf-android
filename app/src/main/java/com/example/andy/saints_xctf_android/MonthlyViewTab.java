package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.RangeView;
import com.example.andy.api_model.User;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class MonthlyViewTab extends Fragment {

    private static final String LOG_TAG = MonthlyViewTab.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    private static final DecimalFormat decimalFormat = new DecimalFormat(".##");

    private View v;
    private boolean startsSunday;
    private DateTime start_date, end_date, month_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.monthly_view_tab, container, false);
        v = view;

        // Loop through each row in the calendar and populate the table row
        for (int i = 1; i <= 6; i++) {

            TableRow tableRow = (TableRow) v.findViewById(CalendarArrays.CALENDAR_ROW_IDS[i-1]);

            ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.temp_progress);
            progressBar.setVisibility(View.GONE);

            // Create a table cell for each day and programmatically give it unique ids
            for (int j = 1; j <= 8; j++) {
                int index = (((i - 1) * 8) + j) - 1;

                GridLayout calendarCell = (GridLayout) inflater.inflate(R.layout.calendar_cell, null);
                TextView calday = (TextView) calendarCell.findViewById(R.id.calday);
                TextView calmiles = (TextView) calendarCell.findViewById(R.id.calmiles);

                calendarCell.setId(CalendarArrays.CALENDAR_CELL_IDS[index]);
                calday.setId(CalendarArrays.CALENDAR_DAY_IDS[index]);
                calmiles.setId(CalendarArrays.CALENDAR_MILES_IDS[index]);

                tableRow.addView(calendarCell);
            }
        }

        Bundle bundle = getArguments();
        String userJSON = bundle.getString("user", "");
        String username = bundle.getString("username", "");

        User user;
        try {
            user = JSONConverter.toUser(userJSON);

            String week_start = user.getWeek_start();
            startsSunday = week_start.equals("sunday");

        } catch (IOException e) {
            Log.e(LOG_TAG, "User object JSON conversion failed.");
            Log.e(LOG_TAG, e.getMessage());
        }

        // Value that will always hold the first day of the shown month
        month_date = new DateTime().dayOfMonth().withMinimumValue();

        // First set start date as the first day of the month
        start_date = new DateTime().dayOfMonth().withMinimumValue();

        // Then get first sunday/monday of that week
        if (startsSunday) {
            start_date = start_date.withDayOfWeek(DateTimeConstants.SUNDAY);
        } else {
            start_date = start_date.withDayOfWeek(DateTimeConstants.MONDAY);
        }

        String start_date_string = start_date.toString("yyyy-MM-dd");

        // First set end date as the last day of the month
        end_date = new DateTime().dayOfMonth().withMaximumValue();

        // Then get last saturday/sunday of that week
        if (startsSunday) {
            end_date = end_date.withDayOfWeek(DateTimeConstants.SATURDAY);
        } else {
            end_date = end_date.withDayOfWeek(DateTimeConstants.SUNDAY);
        }

        String end_date_string = end_date.toString("yyyy-MM-dd");

        if (startsSunday) {
            for (int i = 0; i < 7; i++) {
                TextView weekday = (TextView) v.findViewById(CalendarArrays.CALENDAR_WEEKDAY_IDS[i]);
                weekday.setText(CalendarArrays.CALENDAR_WEEKDAYS_SUNDAY_START[i]);
            }
        }

        Log.i(LOG_TAG, "Monthly View From: " + start_date_string + " to " + end_date_string);

        MonthlyRangeViewTask monthlyRangeViewTask = new MonthlyRangeViewTask();
        monthlyRangeViewTask.execute("user", username, "r", start_date_string, end_date_string);

        return view;
    }

    /**
     * MonthlyRangeViewTask is an asynchronous job for getting a range view and populating the monthly calendar
     */
    class MonthlyRangeViewTask extends AsyncTask<String, Void, List<RangeView>> {

        /**
         * @param params [0] -> paramtype, [1] -> sortparam, [2] -> filter, [3] -> start, [4] -> end
         * @return an ArrayList of RangeView items
         */
        @Override
        protected List<RangeView> doInBackground(String... params) {
            List<RangeView> rangeViews;
            try {
                rangeViews = APIClient.rangeviewGetRequest(params[0], params[1], params[2], params[3], params[4]);

                return rangeViews;

            } catch (IOException e) {
                android.util.Log.d(LOG_TAG, "The rangeview failed to be loaded.");
                android.util.Log.d(LOG_TAG, e.getMessage());
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(List<RangeView> rangeView) {
            super.onPostExecute(rangeView);

            if (rangeView == null) {
                rangeView = new ArrayList<>();
            }

            DateTime start = new DateTime(start_date);
            DateTime end = new DateTime(end_date);

            TextView month_view = (TextView) v.findViewById(R.id.cal_month);
            month_view.setText(month_date.toString("MMMM YYYY"));

            double weekly_miles = 0.0;

            for (int i = 0; i < 48; i++) {
                int day = start.getDayOfMonth();

                GridLayout cell_view = (GridLayout) v.findViewById(CalendarArrays.CALENDAR_CELL_IDS[i]);

                TextView miles_view = (TextView) v.findViewById(CalendarArrays.CALENDAR_MILES_IDS[i]);
                miles_view.setText("");

                if (i % 8 != 7) {
                    TextView day_view = (TextView) v.findViewById(CalendarArrays.CALENDAR_DAY_IDS[i]);
                    day_view.setText(String.valueOf(day));

                    DateTime startminus1 = start.minusDays(1);
                    String currentDate = startminus1.toString("yyyy-MM-dd");
                    Log.i(LOG_TAG, currentDate);

                    if (rangeView.size() > 0 && new DateTime(rangeView.get(0).getDate())
                            .toString("yyyy-MM-dd").equals(currentDate)) {

                        double miles = rangeView.get(0).getMiles();
                        weekly_miles += miles;

                        miles_view.setText(String.format("%.2f",miles) + "\nmiles");

                        // Set the background color according to the log feel
                        GradientDrawable bgShape = (GradientDrawable)cell_view.getBackground();
                        int color = Color.parseColor(CalendarArrays.COLOR_VALUE[rangeView.get(0).getFeel()-1]);
                        bgShape.setColor(color);

                        rangeView.remove(0);
                    } else {
                        // Set the background color according to the log feel
                        GradientDrawable bgShape = (GradientDrawable)cell_view.getBackground();
                        int color = Color.parseColor(CalendarArrays.COLOR_VALUE[10]);
                        bgShape.setColor(color);
                    }

                    start = start.plusDays(1);
                } else {
                    miles_view.setText(String.format("%.2f",weekly_miles) + "\nmiles");
                    weekly_miles = 0.0;

                    // Set the background color according to the log feel
                    GradientDrawable bgShape = (GradientDrawable)cell_view.getBackground();
                    int color = Color.parseColor(CalendarArrays.COLOR_VALUE[10]);
                    bgShape.setColor(color);
                }
            }
        }
    }
}

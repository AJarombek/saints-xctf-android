package com.example.andy.saints_xctf_android.profile;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.andy.api_model.services.APIClient;
import com.example.andy.api_model.services.JSONConverter;
import com.example.andy.api_model.pojos.RangeView;
import com.example.andy.api_model.pojos.User;
import com.example.andy.saints_xctf_android.R;
import com.example.andy.saints_xctf_android.model.CalendarData;
import com.example.andy.saints_xctf_android.utils.CalendarArrays;
import com.example.andy.saints_xctf_android.utils.ControllerUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private static final String SELECTED_COLOR = "#990000";
    private static final String DESELECTED_COLOR = "#EEEEEE";

    private View v;
    private boolean startsSunday;
    private Button previous_month, next_month;
    private Button filter_run, filter_bike, filter_swim, filter_other;
    private CalendarData calendarData;
    private String username;
    private boolean run, bike, swim, other;
    private String filter = "r";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.monthly_view_tab, container, false);
        v = view;

        previous_month = (Button) v.findViewById(R.id.previous_month);
        next_month = (Button) v.findViewById(R.id.next_month);
        filter_run = (Button) v.findViewById(R.id.filter_run);
        filter_bike = (Button) v.findViewById(R.id.filter_bike);
        filter_swim = (Button) v.findViewById(R.id.filter_swim);
        filter_other = (Button) v.findViewById(R.id.filter_other);

        // Loop through each row in the calendar and populate the table row
        for (int i = 1; i <= 6; i++) {

            TableRow tableRow = (TableRow) v.findViewById(CalendarArrays.CALENDAR_ROW_IDS[i-1]);

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
        username = bundle.getString("username", "");

        User user;
        try {
            user = JSONConverter.toUser(userJSON);

            String week_start = user.getWeek_start();
            startsSunday = week_start.equals("sunday");

        } catch (IOException e) {
            Log.e(LOG_TAG, "User object JSON conversion failed.");
            Log.e(LOG_TAG, e.getMessage());
        }

        // Used to keep track of calendar state
        calendarData = new CalendarData();

        run = true;

        // Filter the calendar by activity types

        filter_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run) {
                    filter_run.setTextColor(Color.BLACK);
                    filter_run.setBackgroundColor(Color.parseColor(DESELECTED_COLOR));
                } else {
                    filter_run.setTextColor(Color.WHITE);
                    filter_run.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
                }

                run = !run;
                filter = ControllerUtils.getFilter(run, bike, swim, other);
                Log.i(LOG_TAG, filter);
                initCalendar(calendarData.getMonth_date());
            }
        });

        filter_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bike) {
                    filter_bike.setTextColor(Color.BLACK);
                    filter_bike.setBackgroundColor(Color.parseColor(DESELECTED_COLOR));
                } else {
                    filter_bike.setTextColor(Color.WHITE);
                    filter_bike.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
                }

                bike = !bike;
                filter = ControllerUtils.getFilter(run, bike, swim, other);
                Log.i(LOG_TAG, filter);
                initCalendar(calendarData.getMonth_date());
            }
        });

        filter_swim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swim) {
                    filter_swim.setTextColor(Color.BLACK);
                    filter_swim.setBackgroundColor(Color.parseColor(DESELECTED_COLOR));
                } else {
                    filter_swim.setTextColor(Color.WHITE);
                    filter_swim.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
                }

                swim = !swim;
                filter = ControllerUtils.getFilter(run, bike, swim, other);
                Log.i(LOG_TAG, filter);
                initCalendar(calendarData.getMonth_date());
            }
        });

        filter_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (other) {
                    filter_other.setTextColor(Color.BLACK);
                    filter_other.setBackgroundColor(Color.parseColor(DESELECTED_COLOR));
                } else {
                    filter_other.setTextColor(Color.WHITE);
                    filter_other.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
                }

                other = !other;
                filter = ControllerUtils.getFilter(run, bike, swim, other);
                Log.i(LOG_TAG, filter);
                initCalendar(calendarData.getMonth_date());
            }
        });

        // Value that will always hold the first day of the shown month
        DateTime month_date = new DateTime().dayOfMonth().withMinimumValue();

        initCalendar(month_date);

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

            buildCalendar(calendarData.getStart_date(), calendarData.getEnd_date(),
                    calendarData.getMonth_date(), rangeView);
        }
    }

    /**
     * Initialize the calendar creation process by setting the start and end dates plus
     * making the API call to get the rangeview data
     * @param month_date the first day of the calendar month to be displayed
     */
    private void initCalendar(DateTime month_date) {
        calendarData.setMonth_date(month_date);

        // First set start date as the first day of the month
        DateTime start_date = new DateTime(month_date);
        start_date = start_date.dayOfMonth().withMinimumValue();

        // Then get first sunday/monday of that week
        if (startsSunday) {
            start_date = start_date.withDayOfWeek(DateTimeConstants.SUNDAY).minusDays(7);
        } else {
            start_date = start_date.withDayOfWeek(DateTimeConstants.MONDAY);
        }

        calendarData.setStart_date(start_date);
        String start_date_string = start_date.toString("yyyy-MM-dd");

        // First set end date as the last day of the month
        DateTime end_date = new DateTime(month_date);
        end_date = end_date.dayOfMonth().withMaximumValue();

        // Then get last saturday/sunday of that week
        if (startsSunday) {
            end_date = end_date.withDayOfWeek(DateTimeConstants.SATURDAY);
        } else {
            end_date = end_date.withDayOfWeek(DateTimeConstants.SUNDAY);
        }

        // Stop the calendar early if the entire row is in the next month
        if (end_date.getDayOfMonth() >= 7 && end_date.getDayOfMonth() < 20) {
            end_date = end_date.minusDays(7);
        }
        calendarData.setEnd_date(end_date);
        String end_date_string = end_date.toString("yyyy-MM-dd");

        // Change the weekday textviews based on what day starts the week
        if (startsSunday) {
            for (int i = 0; i < 7; i++) {
                TextView weekday = (TextView) v.findViewById(CalendarArrays.CALENDAR_WEEKDAY_IDS[i]);
                weekday.setText(CalendarArrays.CALENDAR_WEEKDAYS_SUNDAY_START[i]);
            }
        }

        Log.i(LOG_TAG, "Monthly View From: " + start_date_string + " to " + end_date_string);

        MonthlyRangeViewTask monthlyRangeViewTask = new MonthlyRangeViewTask();
        monthlyRangeViewTask.execute("user", username, filter, start_date_string, end_date_string);
    }

    /**
     * Build the calendar with the rangeview data retrieved
     * @param start_date the first day on the calendar
     * @param end_date the last day on the calendar
     * @param month_date the first day of the month being displayed on the calendar
     * @param rangeView the rangeview of log data from the start_date to end_date
     */
    private void buildCalendar(DateTime start_date, DateTime end_date, DateTime month_date, List<RangeView> rangeView) {

        boolean done = false;

        DateTime start = new DateTime(start_date);
        DateTime end = new DateTime(end_date);

        TextView month_view = (TextView) v.findViewById(R.id.cal_month);
        month_view.setText(month_date.toString("MMMM YYYY"));

        double weekly_miles = 0.0;

        for (int i = 0; i < 48; i++) {

            int day = start.getDayOfMonth();

            // If the final row exists entirely in the next month, stop populating the calendar
            if (i == 40 && day < 10) {
                done = true;
            }

            GridLayout cell_view = (GridLayout) v.findViewById(CalendarArrays.CALENDAR_CELL_IDS[i]);

            TextView miles_view = (TextView) v.findViewById(CalendarArrays.CALENDAR_MILES_IDS[i]);
            miles_view.setText("");

            GradientDrawable bgShape = (GradientDrawable)cell_view.getBackground();

            if (i % 8 != 7) {
                TextView day_view = (TextView) v.findViewById(CalendarArrays.CALENDAR_DAY_IDS[i]);

                if (done) {
                    day_view.setText("");

                    int color = Color.parseColor(CalendarArrays.COLOR_VALUE[10]);
                    bgShape.setColor(color);
                } else {

                    day_view.setText(String.valueOf(day));

                    DateTime startminus1 = start.minusDays(1);
                    String currentDate = startminus1.toString("yyyy-MM-dd");

                    if (rangeView.size() > 0 && new DateTime(rangeView.get(0).getDate())
                            .toString("yyyy-MM-dd").equals(currentDate)) {

                        double miles = rangeView.get(0).getMiles();
                        weekly_miles += miles;

                        miles_view.setText(String.format("%.2f", miles) + "\nmiles");

                        // Set the background color according to the log feel
                        int color = Color.parseColor(CalendarArrays.COLOR_VALUE[rangeView.get(0).getFeel() - 1]);
                        bgShape.setColor(color);

                        rangeView.remove(0);
                    } else {

                        // Set the background color according to if the cell is the current month
                        if ((i < 8 && day > 10) || (i > 30 && day < 10)) {
                            // Previous or Next Month
                            int color = Color.parseColor(CalendarArrays.COLOR_VALUE[11]);
                            bgShape.setColor(color);
                        } else {
                            // Current Month
                            int color = Color.parseColor(CalendarArrays.COLOR_VALUE[10]);
                            bgShape.setColor(color);
                        }
                    }
                    start = start.plusDays(1);
                }
            } else {
                if (!done)
                    miles_view.setText(String.format("%.2f",weekly_miles) + "\nmiles");

                weekly_miles = 0.0;

                // Set the background color to the default background
                int color = Color.parseColor(CalendarArrays.COLOR_VALUE[10]);
                bgShape.setColor(color);
            }
        }

        // Start the initCalendar() for the next month
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime month_date = calendarData.getMonth_date();
                month_date = month_date.plusMonths(1);
                initCalendar(month_date);
            }
        });

        // Start the initCalendar() for the previous month
        previous_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime month_date = calendarData.getMonth_date();
                Log.i(LOG_TAG, month_date.toString("yyyy-MM-dd"));
                month_date = month_date.minusMonths(1);
                initCalendar(month_date);
            }
        });
    }
}

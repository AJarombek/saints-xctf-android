package com.example.andy.saints_xctf_android;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.RangeView;
import com.example.andy.api_model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/27/2017 -
 */
public class WeeklyViewTab extends Fragment {

    private static final String LOG_TAG = WeeklyViewTab.class.getName();

    private static final String SELECTED_COLOR = "#990000";
    private static final String DESELECTED_COLOR = "#EEEEEE";

    private View v;
    private BarChart weeklychart;
    private BarDataSet weeklychart_dataset;
    private Button filter_run, filter_bike, filter_swim, filter_other;
    private String weekstart;
    private String username;
    private boolean startsSunday;
    private boolean run, bike, swim, other;
    private DateTime start_date, end_date;
    private String start_date_string, end_date_string;
    private DateTime[] weeklyStartDate, weeklyEndDate;
    private String filter = "r";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.weekly_view_tab, container, false);
        v = view;

        weeklychart = (BarChart) v.findViewById(R.id.weekly_chart);
        filter_run = (Button) v.findViewById(R.id.filter_run);
        filter_bike = (Button) v.findViewById(R.id.filter_bike);
        filter_swim = (Button) v.findViewById(R.id.filter_swim);
        filter_other = (Button) v.findViewById(R.id.filter_other);

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

        run = true;

        initWeeklyView();

        // Filter the weekly view by activity types

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
                populateWeeklyView(start_date_string, end_date_string);
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
                populateWeeklyView(start_date_string, end_date_string);
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
                populateWeeklyView(start_date_string, end_date_string);
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
                populateWeeklyView(start_date_string, end_date_string);
            }
        });

        return view;
    }

    /**
     * WeeklyRangeViewTask is an asynchronous job for getting a range view and populating the weekly chart
     */
    class WeeklyRangeViewTask extends AsyncTask<String, Void, List<RangeView>> {

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

            buildWeeklyView(rangeView);
        }
    }

    private void initWeeklyView() {
        end_date = new DateTime();

        // If the week starts sunday, the last day is saturday.  Otherwise, sunday.
        if (startsSunday) {
            end_date = end_date.withDayOfWeek(DateTimeConstants.SATURDAY);
        } else {
            end_date = end_date.withDayOfWeek(DateTimeConstants.SUNDAY);
        }

        start_date = new DateTime(end_date);

        start_date = start_date.minusDays(70);

        start_date_string = start_date.toString("yyyy-MM-dd");


        end_date_string = end_date.toString("yyyy-MM-dd");

        weeklyStartDate = new DateTime[10];
        weeklyEndDate = new DateTime[10];

        weeklyStartDate[0] = new DateTime(start_date);
        weeklyEndDate[9] = new DateTime(end_date);

        DateTime start = new DateTime(start_date);

        // Set two arrays containing the start and end of each week
        for (int i = 0; i < 9; i++) {
            start = start.plusDays(7);

            DateTime weekStartEnd = new DateTime(start);
            weeklyStartDate[i+1] = weekStartEnd;
            weeklyEndDate[i] = weekStartEnd;
        }

        Log.i(LOG_TAG, "Weekly View From: " + start_date_string + " to " + end_date_string);

        populateWeeklyView(start_date_string, end_date_string);
    }

    private void populateWeeklyView(String start_date_string, String end_date_string) {
        WeeklyRangeViewTask weeklyRangeViewTask = new WeeklyRangeViewTask();
        weeklyRangeViewTask.execute("user", username, filter, start_date_string, end_date_string);
    }

    private void buildWeeklyView(List<RangeView> rangeView) {

        double[] weeklyMileage = new double[10];
        int[] weeklyFeel = new int[10];
        int[] weeklyItems = new int[10];

        // Go through the range views and find which week the rangeview belongs to
        for (RangeView rangeViewItem : rangeView) {
            DateTime date = new DateTime(rangeViewItem.getDate());

            //Must add a day to any date from the JDK Date API  - Unknown Bug
            date = date.plusDays(1);

            for (int i = 0; i < 10; i++) {
                if (!date.isBefore(weeklyStartDate[i]) && !date.isAfter(weeklyEndDate[i])) {

                    // Update the weekly mileage, feel, and item counter
                    weeklyMileage[i] = weeklyMileage[i] + rangeViewItem.getMiles();
                    weeklyFeel[i] = weeklyFeel[i] + rangeViewItem.getFeel();
                    weeklyItems[i] = weeklyItems[i] + 1;
                    break;
                }
            }
        }

        // Log out all the data structures created
        Log.i(LOG_TAG, Arrays.toString(weeklyStartDate));
        Log.i(LOG_TAG, Arrays.toString(weeklyEndDate));

        Log.i(LOG_TAG, Arrays.toString(weeklyMileage));
        Log.i(LOG_TAG, Arrays.toString(weeklyFeel));
        Log.i(LOG_TAG, Arrays.toString(weeklyItems));

        // Create a list of barentries containing the mileage for each week
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            entries.add(new BarEntry(i, (float) weeklyMileage[i]));
        }

        // Put these entries into a data set
        BarDataSet barDataSet = new BarDataSet(entries, "");

        List<Integer> colors = new ArrayList<>();

        // Create a list of colors for the bar chart
        // the color correlates to the average feel for the week
        for (int i = 0; i < 10; i++) {
            int feel = 1;
            if (weeklyItems[i] != 0) {
                feel = weeklyFeel[i] / weeklyItems[i];
            }

            colors.add(Color.parseColor(CalendarArrays.COLOR_VALUE[feel - 1]));
        }

        // Set the colors to the data set
        barDataSet.setColors(colors);

        // Create labels for the chart which have the dates for the start of each week
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            labels.add(i, weeklyStartDate[i].toString("MMM. dd"));
        }

        BarData barData = new BarData(barDataSet);

        // Set the minimum Y-Axis value as zero
        YAxis yAxis = weeklychart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawGridLines(true);

        YAxis yAxisR = weeklychart.getAxisRight();
        yAxisR.setDrawGridLines(false);

        // Set the labels and data to the bar chart
        weeklychart.setData(barData);
        weeklychart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        Description description = new Description();
        description.setText("");

        weeklychart.setDescription(description);
        weeklychart.getLegend().setEnabled(false);
        weeklychart.setScaleEnabled(false);
        weeklychart.invalidate();
    }
}


package com.example.andy.saints_xctf_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.RangeView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/27/2017 -
 */
public class WeeklyViewTab extends Fragment {

    private static final String LOG_TAG = WeeklyViewTab.class.getName();

    private static final String MONDAY = "monday";
    private static final String SUNDAY = "sunday";

    private View v;
    private BarChart weeklychart;
    private BarDataSet weeklychart_dataset;
    private Button filter_run, filter_bike, filter_swim, filter_other;
    private Calendar start_date, end_date;
    private String weekstart;

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

        start_date = Calendar.getInstance();

        // First set the start date to nine weeks ago
        start_date.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        start_date.add(Calendar.DATE, 7);

        end_date = Calendar.getInstance();

        // Then set the start date to this coming sunday
        end_date.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        end_date.add(Calendar.DATE, 7);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String start_date_string = df.format(start_date);
        String end_date_string = df.format(end_date);
        // weeklychart_dataset = new BarDataSet(entries, "WeeklyChart");

        WeeklyRangeViewTask weeklyRangeViewTask = new WeeklyRangeViewTask();
        weeklyRangeViewTask.execute(start_date_string, end_date_string);

        return view;
    }

    /**
     * WeeklyRangeViewTask is an asynchronous job for getting a range view and populating the weekly chart
     */
    class WeeklyRangeViewTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... dates) {
            RangeView rangeView;

            try {
                APIClient.rangeviewGetRequest("", "", "", "");

            } catch (IOException e) {
                android.util.Log.d(LOG_TAG, "The message failed to be uploaded.");
                android.util.Log.d(LOG_TAG, e.getMessage());
                return "internal_error";
            }

            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);
        }
    }
}


package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class MonthlyViewTab extends Fragment {

    private static final String TAG = MonthlyViewTab.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    private View v;
    private boolean startsSunday;
    private Calendar start_date, end_date;

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
                int index = (i * j) - 1;

                GridLayout calendarCell = (GridLayout) inflater.inflate(R.layout.calendar_cell, null);
                TextView calday = (TextView) calendarCell.findViewById(R.id.calday);
                TextView calmiles = (TextView) calendarCell.findViewById(R.id.calmiles);

                calendarCell.setId(CalendarArrays.CALENDAR_CELL_IDS[index]);
                calday.setId(CalendarArrays.CALENDAR_DAY_IDS[index]);
                calmiles.setId(CalendarArrays.CALENDAR_MILES_IDS[index]);

                tableRow.addView(calendarCell);
            }
        }

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        String userJSON = prefs.getString("user", "");

        User user;
        try {
            user = JSONConverter.toUser(userJSON);

            String week_start = user.getWeek_start();
            startsSunday = week_start.equals("sunday");

        } catch (IOException e) {
            Log.e(TAG, "User object JSON conversion failed.");
            Log.e(TAG, e.getMessage());
        }

        start_date = Calendar.getInstance();

        // First get the first day of the month, then first sunday/monday of that week
        start_date.set(Calendar.DAY_OF_MONTH, 1);

        if (startsSunday)
            start_date.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        else
            start_date.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

        end_date = Calendar.getInstance();

        // Then get the last day of the month, then last saturday/sunday of that week
        end_date.set(Calendar.DAY_OF_MONTH, end_date.getActualMaximum(Calendar.DAY_OF_MONTH));

        if (startsSunday) {
            for (int i = 0; i < 7; i++) {
                TextView weekday = (TextView) v.findViewById(CalendarArrays.CALENDAR_WEEKDAY_IDS[i]);
                weekday.setText(CalendarArrays.CALENDAR_WEEKDAYS_SUNDAY_START[i]);
            }
        }

        return view;
    }
}

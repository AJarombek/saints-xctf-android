package com.example.andy.saints_xctf_android;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class MonthlyViewTab extends Fragment {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.monthly_view_tab, container, false);
        v = view;

        for (int i = 1; i <= 6; i++) {

            TableRow tableRow = (TableRow) v.findViewById(CalendarArrays.CALENDAR_ROW_IDS[i-1]);

            ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.temp_progress);
            progressBar.setVisibility(View.GONE);

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

        // monthly_view = (WebView) v.findViewById(R.id.monthly_web_view);

        return view;
    }
}

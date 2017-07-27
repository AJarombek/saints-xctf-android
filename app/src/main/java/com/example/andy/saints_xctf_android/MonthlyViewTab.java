package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class MonthlyViewTab extends Fragment {

    private View v;
    private ArrayList<String> calIds;
    private ArrayList<String> caldayIds;
    private ArrayList<String> calmilesIds;

    private ArrayList<GridLayout> calendar_grids;
    private ArrayList<TextView> calendar_days;
    private ArrayList<TextView> calendar_miles;

    private ArrayList<TextView> calendar_total_miles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.monthly_view_tab, container, false);
        v = view;

        calIds = new ArrayList<>();

        // Generate lists of all the calendar index ids
        for (int i = 0; i < 48; i++) {
            int row = i / 8;
            int col = i % 8;
            calIds.add("cal_" + row + "_" + col);
            caldayIds.add("calday_" + row + "_" + col);
            calmilesIds.add("calmiles_" + row + "_" + col);
        }

        // Bundle bundle = getArguments();
        // username = bundle.getString("username", "");

        // monthly_view = (WebView) v.findViewById(R.id.monthly_web_view);

        return view;
    }
}

package com.example.andy.saints_xctf_android;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 1/27/2017 -
 */
public class StatisticsTab extends Fragment {

    private View v;

    private TextView statistics_mileage_career;
    private TextView statistics_mileage_year;
    private TextView statistics_mileage_month;
    private TextView statistics_mileage_week;
    private TextView statistics_running_career;
    private TextView statistics_running_year;
    private TextView statistics_running_month;
    private TextView statistics_running_week;
    private TextView statistics_feel_career;
    private TextView statistics_feel_year;
    private TextView statistics_feel_month;
    private TextView statistics_feel_week;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.statistics_tab, container, false);
        v = view;

        statistics_mileage_career = (TextView) v.findViewById(R.id.statistics_mileage_career);
        statistics_mileage_year = (TextView) v.findViewById(R.id.statistics_mileage_year);
        statistics_mileage_month = (TextView) v.findViewById(R.id.statistics_mileage_month);
        statistics_mileage_week = (TextView) v.findViewById(R.id.statistics_mileage_week);
        statistics_running_career = (TextView) v.findViewById(R.id.statistics_running_career);
        statistics_running_year = (TextView) v.findViewById(R.id.statistics_running_year);
        statistics_running_month = (TextView) v.findViewById(R.id.statistics_running_month);
        statistics_running_week = (TextView) v.findViewById(R.id.statistics_running_week);
        statistics_feel_career = (TextView) v.findViewById(R.id.statistics_feel_career);
        statistics_feel_year = (TextView) v.findViewById(R.id.statistics_feel_year);
        statistics_feel_month = (TextView) v.findViewById(R.id.statistics_feel_month);
        statistics_feel_week = (TextView) v.findViewById(R.id.statistics_feel_week);

        Bundle bundle = getArguments();
        statistics_mileage_career.setText(bundle.getString("workout_career", ""));
        statistics_mileage_year.setText(bundle.getString("workout_year", ""));
        statistics_mileage_month.setText(bundle.getString("workout_month", ""));
        statistics_mileage_week.setText(bundle.getString("workout_week", ""));

        statistics_running_career.setText(bundle.getString("running_career", ""));
        statistics_running_year.setText(bundle.getString("running_year", ""));
        statistics_running_month.setText(bundle.getString("running_month", ""));
        statistics_running_week.setText(bundle.getString("running_week", ""));

        statistics_feel_career.setText(bundle.getString("feel_career", ""));
        statistics_feel_year.setText(bundle.getString("feel_year", ""));
        statistics_feel_month.setText(bundle.getString("feel_month", ""));
        statistics_feel_week.setText(bundle.getString("feel_week", ""));

        return view;
    }

}

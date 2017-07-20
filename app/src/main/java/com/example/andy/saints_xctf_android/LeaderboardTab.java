package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.andy.api_model.Group;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.LeaderboardItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/18/2017 -
 */
public class LeaderboardTab extends Fragment {

    private static final String[] LEADERBOARD_TIME_FILTERS = {MainActivity.MILES_ALL_TIME,
            MainActivity.MILES_PAST_YEAR, MainActivity.MILES_PAST_MONTH, MainActivity.MILES_PAST_WEEK};

    private View v;
    private Group group;
    private Spinner filter_time;
    private RecyclerView recyclerView;
    private Button filter_run, filter_bike, filter_swim, filter_other;
    private LinearLayoutManager linearLayoutManager;
    private LeaderboardRecyclerAdapter adapter;
    private ArrayList<LeaderboardItem> leaderboardItems;
    private Map<String, List<LeaderboardItem>> leaderboards;

    private boolean run, bike, swim, other;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.leaderboard_tab, container, false);
        v = view;

        filter_time = (Spinner) v.findViewById(R.id.filter_time);
        filter_run = (Button) v.findViewById(R.id.filter_run);
        filter_bike = (Button) v.findViewById(R.id.filter_bike);
        filter_swim = (Button) v.findViewById(R.id.filter_swim);
        filter_other = (Button) v.findViewById(R.id.filter_other);

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate the spinner for the workout type
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.leaderboard_filters,
                        android.R.layout.simple_spinner_item);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_time.setAdapter(typeAdapter);

        leaderboardItems = new ArrayList<>();

        leaderboards = group.getLeaderboards();

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerProfileView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LeaderboardRecyclerAdapter(leaderboardItems);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        filter_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filter_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        filter_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        filter_swim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        filter_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void sortLeaderboard() {

    }
}

package com.example.andy.saints_xctf_android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/18/2017 -
 */
public class LeaderboardTab extends Fragment {

    private static final String LOG_TAG = LeaderboardTab.class.getName();

    private static final String[] LEADERBOARD_TIME_FILTERS = {MainActivity.MILES_ALL_TIME,
            MainActivity.MILES_PAST_YEAR, MainActivity.MILES_PAST_MONTH, MainActivity.MILES_PAST_WEEK};
    private static final String SELECTED_COLOR = "#990000";
    private static final String DESELECTED_COLOR = "#EEEEEE";

    private View v;
    private Group group;
    private Spinner filter_time;
    private RecyclerView recyclerView;
    private Button filter_run, filter_bike, filter_swim, filter_other;
    private LinearLayoutManager linearLayoutManager;
    private LeaderboardRecyclerAdapter adapter;
    private ArrayList<LeaderboardItem> leaderboardItems;
    private Map<String, ArrayList<LeaderboardItem>> leaderboards;

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

        // Initialize the leaderboard data and filters
        run = true;
        leaderboards = group.getLeaderboards();
        leaderboardItems = leaderboards.get(LEADERBOARD_TIME_FILTERS[0]);

        sortLeaderboard(run, bike, swim, other);

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerLeaderboardView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LeaderboardRecyclerAdapter(leaderboardItems);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // All the filters for the leaderboard

        filter_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_TAG, "Leaderboard Filter - " + LEADERBOARD_TIME_FILTERS[position]);
                leaderboardItems = leaderboards.get(LEADERBOARD_TIME_FILTERS[position]);
                sortLeaderboard(run, bike, swim, other);
                adapter = new LeaderboardRecyclerAdapter(leaderboardItems);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
                sortLeaderboard(run, bike, swim, other);
                adapter.notifyDataSetChanged();
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
                sortLeaderboard(run, bike, swim, other);
                adapter.notifyDataSetChanged();
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
                sortLeaderboard(run, bike, swim, other);
                adapter.notifyDataSetChanged();
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
                sortLeaderboard(run, bike, swim, other);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Sort the array of leaderboard items based on the filters added
     * @param run -
     * @param bike -
     * @param swim -
     * @param other -
     */
    private void sortLeaderboard(boolean run, boolean bike, boolean swim, boolean other) {
        if (run) {
            if (bike) {
                if (swim) {
                    if (other) {
                        // [Run, Bike, Swim, Other]
                        for (LeaderboardItem leaderboardItem : leaderboardItems) {
                            leaderboardItem.setFilteredmiles(leaderboardItem.getMiles());
                        }

                    } else {
                        // [Run, Bike, Swim]
                        for (LeaderboardItem leaderboardItem : leaderboardItems) {
                            leaderboardItem.setFilteredmiles(leaderboardItem.getMilesrun() +
                                    leaderboardItem.getMilesbiked() + leaderboardItem.getMilesswam());
                        }
                    }
                } else {
                    if (other) {
                        // [Run, Bike, Other]
                        for (LeaderboardItem leaderboardItem : leaderboardItems) {
                            leaderboardItem.setFilteredmiles(leaderboardItem.getMilesrun() +
                                    leaderboardItem.getMilesbiked() + leaderboardItem.getMilesother());
                        }
                    } else {
                        // [Run, Bike]
                        for (LeaderboardItem leaderboardItem : leaderboardItems) {
                            leaderboardItem.setFilteredmiles(leaderboardItem.getMilesrun() +
                                    leaderboardItem.getMilesbiked());
                        }
                    }
                }

            } else if (swim) {
                if (other) {
                    // [Run, Swim, Other]
                    for (LeaderboardItem leaderboardItem : leaderboardItems) {
                        leaderboardItem.setFilteredmiles(leaderboardItem.getMilesrun() +
                                leaderboardItem.getMilesswam() + leaderboardItem.getMilesother());
                    }

                } else {
                    // [Run, Swim]
                    for (LeaderboardItem leaderboardItem : leaderboardItems) {
                        leaderboardItem.setFilteredmiles(leaderboardItem.getMilesrun() +
                                leaderboardItem.getMilesswam());
                    }
                }
            } else if (other) {
                // [Run, Other]
                for (LeaderboardItem leaderboardItem : leaderboardItems) {
                    leaderboardItem.setFilteredmiles(leaderboardItem.getMilesrun() +
                            leaderboardItem.getMilesother());
                }
            } else {
                // [Run]
                for (LeaderboardItem leaderboardItem : leaderboardItems) {
                    leaderboardItem.setFilteredmiles(leaderboardItem.getMilesrun());
                }
            }

        } else if (bike) {
            if (swim) {
                if (other) {
                    // [Bike, Swim, Other]
                    for (LeaderboardItem leaderboardItem : leaderboardItems) {
                        leaderboardItem.setFilteredmiles(leaderboardItem.getMilesbiked() +
                                leaderboardItem.getMilesswam() + leaderboardItem.getMilesother());
                    }

                } else {
                    // [Bike, Swim]
                    for (LeaderboardItem leaderboardItem : leaderboardItems) {
                        leaderboardItem.setFilteredmiles(leaderboardItem.getMilesbiked() +
                                leaderboardItem.getMilesswam());
                    }
                }
            } else if (other) {
                // [Bike, Other]
                for (LeaderboardItem leaderboardItem : leaderboardItems) {
                    leaderboardItem.setFilteredmiles(leaderboardItem.getMilesbiked() +
                            leaderboardItem.getMilesother());
                }
            } else {
                // [Bike]
                for (LeaderboardItem leaderboardItem : leaderboardItems) {
                    leaderboardItem.setFilteredmiles(leaderboardItem.getMilesbiked());
                }
            }

        } else if (swim) {
            if (other) {
                // [Swim, Other]
                for (LeaderboardItem leaderboardItem : leaderboardItems) {
                    leaderboardItem.setFilteredmiles(leaderboardItem.getMilesswam() +
                            leaderboardItem.getMilesother());
                }
            } else {
                // [Swim]
                for (LeaderboardItem leaderboardItem : leaderboardItems) {
                    leaderboardItem.setFilteredmiles(leaderboardItem.getMilesswam());
                }
            }

        } else if (other) {
            // [Other]
            for (LeaderboardItem leaderboardItem : leaderboardItems) {
                leaderboardItem.setFilteredmiles(leaderboardItem.getMilesother());
            }
        } else {
            for (LeaderboardItem leaderboardItem : leaderboardItems) {
                leaderboardItem.setFilteredmiles(0.0);
            }
        }

        // Sort the leaderboards based on the filtered miles
        Collections.sort(leaderboardItems, new Comparator<LeaderboardItem>() {
            @Override
            public int compare(LeaderboardItem lhs, LeaderboardItem rhs) {
                return lhs.getFilteredmiles() > rhs.getFilteredmiles() ? -1 :
                        (lhs.getFilteredmiles() < rhs.getFilteredmiles() ) ? 1 : 0;
            }
        });
    }
}

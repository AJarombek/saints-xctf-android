package com.example.andy.saints_xctf_android;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 1/15/2017 -
 */
public class MainFragment extends Fragment {

    private View v;
    private static final String TAG = "MainFragment: ";
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    public static final int REQUEST_CODE = 0;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter adapter;
    private ArrayList<com.example.andy.api_model.Log> logs;
    private String username,first,last;
    private LoadLogTask loadLogTask;

    /**
     * Android onCreateView method
     * @param inflater --
     * @param container --
     * @param savedInstanceState --
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.main, container, false);
        Log.d(TAG, "Inside onCreateView.");

        setHasOptionsMenu(true);
        this.v = view;

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        username = prefs.getString("username", "");
        first = prefs.getString("first", "");
        last = prefs.getString("last", "");

        logs = new ArrayList<>();
        loadLogTask = new LoadLogTask();

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(getContext(), this, logs, recyclerView);
        recyclerView.setAdapter(adapter);

        // Initiate the progress bar
        logs.add(null);
        adapter.notifyItemInserted(logs.size() - 1);

        adapter.setOnLoadMoreListener(new RecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                int itemsLoaded = adapter.getItemCount();
                if ((itemsLoaded % 10) == 0) {
                    logs.add(null);
                    adapter.notifyItemInserted(logs.size() - 1);
                    LoadLogTask loadLogTask = new LoadLogTask();
                    loadLogTask.execute("all", "all", String.valueOf(itemsLoaded));
                }
            }
        });

        // Load the first ten logs
        loadLogTask.execute("all","all","0");

        return v;
    }

    /**
     * Android onCreate method
     * @param savedInstanceState --
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Inside onCreate.");
        // Save on rotation
        setRetainInstance(true);
    }

    /**
     * Android Activity method for taking an action when an item is clicked in the
     * app bar
     * @param item menu item selected
     * @return --
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log:
                LogDialogFragment logDialog = new LogDialogFragment();
                logDialog.setTargetFragment(this, REQUEST_CODE);
                logDialog.show(getFragmentManager(), "log dialog");
                return true;
            case R.id.action_home:
                return true;
            case R.id.action_profile:
                ((MainActivity) getActivity()).viewProfile(username);
                return true;
            case R.id.action_group:
                GroupDialogFragment groupDialog = new GroupDialogFragment();
                groupDialog.setTargetFragment(this, REQUEST_CODE);
                groupDialog.show(getFragmentManager(), "group dialog");
                return true;
            case R.id.action_exit:
                getContext().getSharedPreferences(PREFS_NAME, 0).edit().clear().apply();
                ((MainActivity) getActivity()).signOut();
                return true;
            default:
                // The user's action was not recognized, invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        if (requestCode == LogDialogFragment.REQUEST_CODE) {
            String newlog = data.getStringExtra(
                    LogDialogFragment.NEW_LOG_KEY);

            com.example.andy.api_model.Log log = null;
            try {
                log = JSONConverter.toLog(newlog);
            } catch (IOException e) {
                Log.e(TAG, "Log object JSON conversion failed.");
                Log.e(TAG, e.getMessage());
            }

            logs.add(0,log);
            adapter.notifyItemInserted(0);
        }
    }

    class LoadLogTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            List<com.example.andy.api_model.Log> logs = null;
            try {
                logs = APIClient.logfeedGetRequest(strings[0], strings[1], "10", strings[2]);

                if (logs == null)
                    return "no_internet";

            } catch (Exception e) {
                Log.e(TAG, "Log object JSON conversion failed.");
                Log.e(TAG, e.getMessage());
                return "no_internet";
            }

            return logs;
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response != null) {
                if (response.equals("no_internet")) {
                    ((MainActivity) getActivity()).noInternet();
                } else if (response instanceof List) {
                    ArrayList<com.example.andy.api_model.Log> addedlogs
                            = (ArrayList<com.example.andy.api_model.Log>) response;

                    // Remove the progress bar and add the new items to the recycler view
                    logs.remove(logs.size() - 1);
                    adapter.notifyItemRemoved(logs.size());
                    logs.addAll(addedlogs);
                    adapter.notifyItemInserted(logs.size() - 1);
                    adapter.setLoaded();
                }
            }
        }
    }
}

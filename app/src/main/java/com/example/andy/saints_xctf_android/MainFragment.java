package com.example.andy.saints_xctf_android;


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
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter adapter;

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

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        LoadLogTask loadLogTask = new LoadLogTask();
        loadLogTask.execute();

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
                logDialog.show(getFragmentManager(), "log dialog");
                return true;
            case R.id.action_home:
                return true;
            case R.id.action_profile:
                return true;
            case R.id.action_group:
                return true;
            case R.id.action_exit:
                return true;
            default:
                // The user's action was not recognized, invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    class LoadLogTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... voids) {
            List<com.example.andy.api_model.Log> logArray = null;
            try {
                logArray = APIClient.logsGetRequest();

                // If null is returned, there is no internet connection
                if (logArray == null) {
                    return "no_internet";
                }
            } catch (Exception e) {
                android.util.Log.e(TAG, "Log object JSON conversion failed.");
                android.util.Log.e(TAG, e.getMessage());
                return "no_internet";
            }

            return logArray;
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response.equals("no_internet")) {
                ((MainActivity) getActivity()).noInternet();
            } else if (response instanceof List) {
                ArrayList<com.example.andy.api_model.Log> logs =
                        (ArrayList<com.example.andy.api_model.Log>) response;
                adapter = new RecyclerAdapter(logs);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}

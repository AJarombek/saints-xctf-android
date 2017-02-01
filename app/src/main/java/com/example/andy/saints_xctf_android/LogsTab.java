package com.example.andy.saints_xctf_android;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 1/27/2017 -
 */
public class LogsTab extends Fragment {

    private static final String TAG = LogsTab.class.getName();

    private View v;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter adapter;
    private ArrayList<com.example.andy.api_model.Log> logs;
    private String username;
    private LoadLogTask loadLogTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.logs_tab, container, false);
        v = view;

        Bundle bundle = getArguments();
        username = bundle.getString("username", "");

        logs = new ArrayList<>();
        loadLogTask = new LoadLogTask();

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerProfileView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(getContext(), logs, recyclerView);
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
                    loadLogTask.execute("user", username, String.valueOf(itemsLoaded));
                }
            }
        });

        // Load the first ten logs
        loadLogTask.execute("user",username,"0");

        return view;
    }

    class LoadLogTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            List<Log> logs = null;
            try {
                logs = APIClient.logfeedGetRequest(strings[0], strings[1], "10", strings[2]);

                if (logs == null)
                    return "no_internet";

            } catch (Exception e) {
                android.util.Log.e(TAG, "Log object JSON conversion failed.");
                android.util.Log.e(TAG, e.getMessage());
                return null;
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
            } else {
                // The user has no logs
                logs.remove(logs.size() - 1);
                adapter.notifyItemRemoved(logs.size());
            }
        }
    }

}

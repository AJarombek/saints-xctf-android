package com.example.andy.saints_xctf_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Group;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.Log;
import com.example.andy.api_model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/18/2017 -
 */
public class MessagesTab extends Fragment {

    private static final String LOG_TAG = MessagesTab.class.getName();

    private View v;
    private Group group;
    private RecyclerView recyclerView;
    private Button submit_message;
    private LinearLayoutManager linearLayoutManager;
    private MessageRecyclerAdapter adapter;
    private ArrayList<Message> messages;
    private LoadMessagesTask loadMessagesTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.messages_tab, container, false);
        v = view;

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        messages = new ArrayList<>();
        loadMessagesTask = new LoadMessagesTask();

        // monthly_view = (WebView) v.findViewById(R.id.monthly_web_view);

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerMessageView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageRecyclerAdapter(messages);
        recyclerView.setAdapter(adapter);

        loadMessagesTask.execute("group",group.getGroup_name(),"0");

        return view;
    }

    class LoadMessagesTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            List<Message> messages = null;
            try {
                messages = APIClient.messagefeedGetRequest(strings[0], strings[1], "10", strings[2]);

                if (messages == null)
                    return "no_internet";

            } catch (Exception e) {
                android.util.Log.e(LOG_TAG, "Log object JSON conversion failed.");
                android.util.Log.e(LOG_TAG, e.getMessage());
                return null;
            }

            return messages;
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response != null) {
                if (response.equals("no_internet")) {
                    ((MainActivity) getActivity()).noInternet();
                } else if (response instanceof List) {
                    ArrayList<Message> loadedmessages = (ArrayList<Message>) response;

                    messages.addAll(loadedmessages);
                    adapter.notifyItemInserted(messages.size() - 1);
                }
            } else {
                // The user has no logs
                messages.remove(messages.size() - 1);
                adapter.notifyItemRemoved(messages.size());
            }
        }
    }
}

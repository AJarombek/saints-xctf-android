package com.example.andy.saints_xctf_android.group;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.andy.api_model.services.APIClient;
import com.example.andy.api_model.pojos.Group;
import com.example.andy.api_model.pojos.GroupMember;
import com.example.andy.api_model.services.JSONConverter;
import com.example.andy.api_model.pojos.Message;
import com.example.andy.api_model.pojos.Notification;
import com.example.andy.saints_xctf_android.MainActivity;
import com.example.andy.saints_xctf_android.async.NotificationTask;
import com.example.andy.saints_xctf_android.R;

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
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    private View v;
    private Group group;
    private String username, first, last;
    private RecyclerView recyclerView;
    private Button submit_message;
    private TextView message_input;
    private LinearLayoutManager linearLayoutManager;
    private MessageRecyclerAdapter adapter;
    private ArrayList<Message> messages;
    private LoadMessagesTask loadMessagesTask;
    private MessageTask messageTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.messages_tab, container, false);
        v = view;

        submit_message = (Button) v.findViewById(R.id.message_submit);
        message_input = (TextView) v.findViewById(R.id.message_input);

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        messages = new ArrayList<>();
        loadMessagesTask = new LoadMessagesTask();

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerMessageView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageRecyclerAdapter(messages);
        recyclerView.setAdapter(adapter);

        loadMessagesTask.execute("group",group.getGroup_name(),"0");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set on Click Listener for the Submit Message Button
        submit_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getContext().getSharedPreferences(
                        PREFS_NAME, Context.MODE_PRIVATE);
                username = prefs.getString("username", "");
                first = prefs.getString("first", "");
                last = prefs.getString("last", "");

                Message message = new Message();
                message.setContent(message_input.getText().toString());
                message.setGroup_name(group.getGroup_name());
                message.setFirst(first);
                message.setLast(last);
                message.setUsername(username);

                android.util.Log.i(LOG_TAG, message.toString());

                messageTask = new MessageTask();
                messageTask.execute(message);

                // Send a notification of the new message to all group members except for the
                // current user

                for (GroupMember groupMember : group.getMembers()) {
                    if (!groupMember.getUsername().equals(username)) {
                        NotificationTask notificationTask = new NotificationTask();

                        Notification notification = new Notification();
                        notification.setUsername(groupMember.getUsername());
                        notification.setViewed("N");
                        notification.setLink("https://www.saintsxctf.com/group.php?name=" + group.getGroup_name());
                        notification.setDescription(first + " " + last + " Sent a Message in " + group.getGroup_title());

                        notificationTask.execute(notification);
                    }
                }

            }
        });
    }

    /**
     * LoadMessagesTask is an asynchronous job for getting a feed of messages
     */
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
                if (messages.size() > 0) {
                    messages.remove(messages.size() - 1);
                    adapter.notifyItemRemoved(messages.size());
                }
            }
        }
    }

    /**
     * MessageTask is an asynchronous job for creating new messages and updating existing messages
     */
    class MessageTask extends AsyncTask<Message, Void, Object> {

        @Override
        protected Object doInBackground(Message... messages) {
            Message message;

            // Convert the new message to JSON
            String messageJSON;
            try {
                messageJSON = JSONConverter.fromMessage(messages[0]);
            } catch (Throwable t) {
                android.util.Log.d(LOG_TAG, "Failed to Convert from Message to JSON.");
                android.util.Log.d(LOG_TAG, t.getMessage());
                return "internal_error";
            }

            try {
                android.util.Log.d(LOG_TAG, messageJSON);

                // add log to the database or update log in database
                message = APIClient.messagePostRequest(messageJSON);

                if (message == null) return "no_internet";

            } catch (IOException e) {
                android.util.Log.d(LOG_TAG, "The message failed to be uploaded.");
                android.util.Log.d(LOG_TAG, e.getMessage());
                return "internal_error";
            }

            return message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            submit_message.setEnabled(false);
        }


        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response.equals("no_internet")) {
                ((MainActivity) getActivity()).noInternet();
            } else if (response.equals("internal_error")) {
                message_input.requestFocus();
            }  else if (response instanceof Message) {

                Message message = (Message) response;
                android.util.Log.d(LOG_TAG, "The Message Object Received: " + message.toString());

                messages.add(0, message);
                adapter.notifyItemChanged(0);

                message_input.setText("");

            }
            submit_message.setEnabled(true);
        }
    }
}

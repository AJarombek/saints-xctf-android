package com.example.andy.saints_xctf_android.group;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andy.api_model.services.APIClient;
import com.example.andy.api_model.pojos.ActivationCode;
import com.example.andy.api_model.pojos.Group;
import com.example.andy.api_model.pojos.GroupInfo;
import com.example.andy.api_model.pojos.GroupMember;
import com.example.andy.api_model.services.JSONConverter;
import com.example.andy.api_model.pojos.Mail;
import com.example.andy.api_model.pojos.Notification;
import com.example.andy.api_model.pojos.User;
import com.example.andy.saints_xctf_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class AdminTab extends Fragment {

    private static final String LOG_TAG = AdminTab.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private View v;
    private boolean isAdmin;
    private Group group;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AddUserRecyclerAdapter adapter;
    private ArrayList<GroupMember> members;
    private Spinner group_users_dropdown;
    private EditText email_request_input, flair_input, notification_input;
    private Button email_request_send, flair_submit, notification_submit;
    private ArrayList<String> member_names;
    private HashMap<String, String> member_map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find out if this user is an admin for this group and if they are accepted
        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        String userJSON = prefs.getString("user", "");
        User user;

        try {
            user = JSONConverter.toUser(userJSON);
            for (GroupInfo groupInfo : user.getGroups()) {
                if (groupInfo.getGroup_name().equals(group.getGroup_name()) && groupInfo.getUser().equals("admin")) {
                    isAdmin = true;
                }
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "User object JSON conversion failed.");
            Log.e(LOG_TAG, e.getMessage());
        }

        View view;

        if (isAdmin) {

            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.admin_tab, container, false);
            v = view;

            group_users_dropdown = (Spinner) v.findViewById(R.id.group_users_dropdown);
            email_request_input = (EditText) v.findViewById(R.id.email_request_input);
            flair_input = (EditText) v.findViewById(R.id.flair_input);
            notification_input = (EditText) v.findViewById(R.id.notification_input);
            email_request_send = (Button) v.findViewById(R.id.email_request_send);
            flair_submit = (Button) v.findViewById(R.id.flair_submit);
            notification_submit = (Button) v.findViewById(R.id.notification_submit);

            member_names = new ArrayList<>();
            member_map = new HashMap<>();
            ArrayList<GroupMember> groupMembers = group.getMembers();

            for (GroupMember member : groupMembers) {
                String name = member.getFirst() + " " + member.getLast();
                member_names.add(name);
                member_map.put(name, member.getUsername());
            }

            // Populate the spinner with all the group members names
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, member_names);

            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            group_users_dropdown.setAdapter(typeAdapter);

            members = new ArrayList<>();

            ArrayList<GroupMember> allMembers = group.getMembers();

            // Get only the members with a pending status to add to the adduser recycler adapter
            for (GroupMember member : allMembers) {
                if (member.getStatus().equals("pending")) {
                    members.add(member);
                }
            }

            // Set up the recycler view and layout manager
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerAddUsersView);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new AddUserRecyclerAdapter(members, group, this);
            recyclerView.setAdapter(adapter);
        } else {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.locked_page, container, false);
            v = view;
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isAdmin) {

            email_request_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = email_request_input.getText().toString();

                    Pattern pattern = Pattern.compile(EMAIL_REGEX);
                    Matcher matcher = pattern.matcher(email);

                    // Validate the Email Input
                    if (email.length() == 0) {
                        Toast.makeText(getContext(), "Must Enter Email", Toast.LENGTH_SHORT).show();
                        email_request_input.requestFocus();
                    } else if (!matcher.matches()) {
                        Toast.makeText(getContext(), "Invalid Email Entered", Toast.LENGTH_SHORT).show();
                        email_request_input.requestFocus();
                    } else {
                        // Valid Email
                        EmailTask emailTask = new EmailTask();
                        emailTask.execute(email);
                    }
                }
            });

            flair_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String username = group_users_dropdown.getSelectedItem().toString();
                    username = member_map.get(username);

                    String flair = flair_input.getText().toString();

                    FlairTask flairTask = new FlairTask();
                    flairTask.execute(username, flair);
                }
            });

            notification_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String notification = "A Message From " + group.getGroup_title() + ": " +
                            notification_input.getText().toString();

                    AdminNotificationTask adminNotificationTask = new AdminNotificationTask();
                    adminNotificationTask.execute(notification);
                }
            });
        }
    }

    /**
     * Remove an item from the AddUser array after it has been accepted or rejected
     * @param position the index in the array to be removed
     */
    public void removeAddUser(int position) {
        members.remove(position);
        adapter.notifyDataSetChanged();
    }

    class EmailTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ActivationCode activationCode = APIClient.activationcodePostRequest("");

                if (activationCode == null) {
                    return "Email Failed";
                }

                // Build the Mail Object
                Mail mail = new Mail();
                mail.setEmailAddress(params[0]);
                mail.setSubject("SaintsXCTF.com Invite");
                mail.setBody("<html>" +
                        "<head>" +
                        "<title>HTML email</title>" +
                        "</head>" +
                        "<body>" +
                        "<h3>SaintsXCTF.com Invite</h3>" +
                        "<br><p>You Have Been Invited to SaintsXCTF.com!</p>" +
                        "<br><br><p>Use the following confirmation code to sign up:</p><br>" +
                        "<p><b>Code: </b>" + activationCode.getActivation_code() + "</p>" +
                        "</body>" +
                        "</html>");

                // Convert the mail object to JSON
                String mailString;
                try {
                    mailString = JSONConverter.fromMail(mail);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    return "Email Failed";
                }

                // Send the Mail
                APIClient.mailPostRequest(mailString);

                return "Email Sent!";

            } catch (IOException e) {
                e.printStackTrace();
                return "Email Failed";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email_request_send.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response.equals("Email Failed")) {
                email_request_input.requestFocus();
            }

            // display error/success toast
            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            email_request_send.setEnabled(true);
            email_request_input.setText("");
        }
    }

    class FlairTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            User user;
            try {
                user = APIClient.userPostRequest(params[0]);

                // If null is returned, there is no internet connection
                if (user == null) {
                    return "No Internet";
                }

                // Add the one new flair item
                user.setGive_flair(params[1]);

                // Convert the user object to JSON
                String userString;
                try {
                    userString = JSONConverter.fromUser(user);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    return "Server Error";
                }

                APIClient.userPutRequest(user.getUsername(), userString);
                return "Flair Successfully Given";

            } catch (IOException e) {
                android.util.Log.e(LOG_TAG, "Add Flair failed.");
                android.util.Log.e(LOG_TAG, e.getMessage());
                return "Server Error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flair_submit.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response.equals("Server Error") || response.equals("No Internet")) {
                email_request_input.requestFocus();
            }

            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            flair_submit.setEnabled(true);
            flair_input.setText("");
        }
    }

    class AdminNotificationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Notification notification = new Notification();
                notification.setDescription(params[0]);
                notification.setLink("https://www.saintsxctf.com/group.php?name=" + group.getGroup_name());
                notification.setViewed("N");

                for (GroupMember member : group.getMembers()) {
                    notification.setUsername(member.getUsername());

                    // Convert the notification object to JSON
                    String notificationString;
                    try {
                        notificationString = JSONConverter.fromNotification(notification);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return "Server Error";
                    }

                    APIClient.notificationPostRequest(notificationString);
                }

                return "Notifications Sent";

            } catch (IOException e) {
                android.util.Log.e(LOG_TAG, "Notifications Failed to Send.");
                android.util.Log.e(LOG_TAG, e.getMessage());
                return "Server Error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notification_submit.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response.equals("Notifications Sent")) {
                notification_input.requestFocus();
            }

            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            notification_submit.setEnabled(true);
            notification_input.setText("");
        }
    }
}


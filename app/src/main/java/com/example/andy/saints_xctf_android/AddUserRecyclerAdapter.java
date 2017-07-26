package com.example.andy.saints_xctf_android;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Group;
import com.example.andy.api_model.GroupInfo;
import com.example.andy.api_model.GroupMember;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Adapter for the RecycleView (which shows add user to group items)
 * @author Andrew Jarombek
 * @since 7/26/2017 -
 */

public class AddUserRecyclerAdapter extends RecyclerView.Adapter<AddUserRecyclerAdapter.MemberItemHolder> {

    private ArrayList<GroupMember> groupMembers;
    private AdminTab adminTab;
    private Group group;

    public AddUserRecyclerAdapter(ArrayList<GroupMember> groupMembers, Group group, AdminTab adminTab) {
        this.groupMembers = groupMembers;
        this.group = group;
        this.adminTab = adminTab;
    }

    @Override
    public AddUserRecyclerAdapter.MemberItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adduser_item, parent, false);
        return new MemberItemHolder(inflatedView, group, adminTab);
    }

    @Override
    public void onBindViewHolder(AddUserRecyclerAdapter.MemberItemHolder holder, int position) {
        GroupMember groupMember = groupMembers.get(position);
        holder.bindMemberItem(groupMember, position);
    }

    @Override
    public int getItemCount() {
        return groupMembers.size();
    }

    public static class MemberItemHolder extends RecyclerView.ViewHolder {

        private static final String LOG_TAG = MemberRecyclerAdapter.MemberItemHolder.class.getName();

        private View v;
        private Group group;
        private GroupMember groupMember;
        private LinearLayout adduserview;
        private TextView adduser_name;
        private Button accept_user;
        private Button reject_user;
        private AcceptRejectUserTask arUserTask;
        private AdminTab adminTab;
        private int position;

        public MemberItemHolder(View v, Group group, AdminTab adminTab) {
            super(v);
            adduserview = (LinearLayout) v.findViewById(R.id.adduserview);
            adduser_name = (TextView) v.findViewById(R.id.adduser_name);
            accept_user = (Button) v.findViewById(R.id.accept_user);
            reject_user = (Button) v.findViewById(R.id.reject_user);
            arUserTask = new AcceptRejectUserTask();
            this.v = v;
            this.group = group;
            this.adminTab = adminTab;
        }

        public void bindMemberItem(final GroupMember groupMember, int position) {
            this.groupMember = groupMember;
            this.position = position;

            adduser_name.setText(groupMember.getFirst() + " " + groupMember.getLast());

            // Set click listeners to accept and reject users into the group
            accept_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arUserTask.execute(groupMember.getUsername(), group.getGroup_name(),
                            AcceptRejectUserTask.ACCEPT);
                }
            });

            reject_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arUserTask.execute(groupMember.getUsername(), group.getGroup_name(),
                            AcceptRejectUserTask.REJECT);
                }
            });
        }

        class AcceptRejectUserTask extends AsyncTask<String, Void, String> {

            // Strings for determining if we are accepting or rejecting a user
            private static final String ACCEPT = "ACCEPT";
            private static final String REJECT = "REJECT";

            /**
             * @param strings [0] -> username, [1] -> groupname, [2] -> accept/reject
             * @return a message to be displayed in a Toast
             */
            @Override
            protected String doInBackground(String... strings) {
                try {
                    User user = APIClient.userGetRequest(strings[0]);

                    if (user == null)
                        return "Server Error";

                    List<GroupInfo> groupInfos = user.getGroups();

                    for (GroupInfo groupInfo : groupInfos) {
                        if (groupInfo.getGroup_name().equals(strings[1])) {

                            // Take action based on if we are accepting or rejecting
                            if (strings[2].equals(ACCEPT)) {
                                groupInfo.setStatus("accepted");
                            } else if (strings[2].equals(REJECT)) {
                                groupInfos.remove(groupInfo);
                            }
                        }
                    }

                    String jsonUser = "";
                    try {
                        jsonUser = JSONConverter.fromUser(user);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    APIClient.userPutRequest(user.getUsername(), jsonUser);

                    if (strings[2].equals(ACCEPT)) {
                        return "User Accepted";
                    } else if (strings[2].equals(REJECT)) {
                        return "User Rejected";
                    } else {
                        return "Andy Messed Up";
                    }

                } catch (Exception e) {
                    Log.e(LOG_TAG, "User object server JSON conversion failed.");
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                    return "Server Error";
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                reject_user.setEnabled(false);
                accept_user.setEnabled(false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);

                if (response.equals("User Accepted") || response.equals("User Rejected")) {
                    adminTab.removeAddUser(position);
                }

                // display error/success toast
                Toast.makeText(v.getContext(), response, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

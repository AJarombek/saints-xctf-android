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

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Group;
import com.example.andy.api_model.GroupInfo;
import com.example.andy.api_model.GroupMember;
import com.example.andy.api_model.User;

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
    private Group group;

    public AddUserRecyclerAdapter(ArrayList<GroupMember> groupMembers, Group group) {
        this.groupMembers = groupMembers;
        this.group = group;
    }

    @Override
    public AddUserRecyclerAdapter.MemberItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adduser_item, parent, false);
        return new MemberItemHolder(inflatedView, group);
    }

    @Override
    public void onBindViewHolder(AddUserRecyclerAdapter.MemberItemHolder holder, int position) {
        GroupMember groupMember = groupMembers.get(position);
        holder.bindMemberItem(groupMember);
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
        private AcceptUserTask acceptUserTask;

        public MemberItemHolder(View v, Group group) {
            super(v);
            adduserview = (LinearLayout) v.findViewById(R.id.adduserview);
            adduser_name = (TextView) v.findViewById(R.id.adduser_name);
            accept_user = (Button) v.findViewById(R.id.accept_user);
            reject_user = (Button) v.findViewById(R.id.reject_user);
            acceptUserTask = new AcceptUserTask();
            this.v = v;
            this.group = group;
        }

        public void bindMemberItem(final GroupMember groupMember) {
            this.groupMember = groupMember;

            adduser_name.setText(groupMember.getFirst() + " " + groupMember.getLast());

            accept_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptUserTask.execute(groupMember.getUsername(), group.getGroup_name());
                }
            });

            reject_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        class AcceptUserTask extends AsyncTask<String, Void, Object> {

            @Override
            protected Object doInBackground(String... strings) {
                User user = null;
                try {
                    user = APIClient.userGetRequest(strings[0]);

                    if (user == null)
                        return "no_internet";

                    List<GroupInfo> groupInfos = user.getGroups();

                } catch (Exception e) {
                    Log.e(LOG_TAG, "User object server JSON conversion failed.");
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                    return "no_internet";
                }

                return user;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                reject_user.setEnabled(false);
                accept_user.setEnabled(false);
            }

            @Override
            protected void onPostExecute(Object response) {
                super.onPostExecute(response);
            }
        }
    }
}

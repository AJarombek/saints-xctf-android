package com.example.andy.saints_xctf_android;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andy.api_model.Group;
import com.example.andy.api_model.GroupMember;
import com.example.andy.api_model.LeaderboardItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Adapter for the RecycleView (which shows group member items)
 * @author Andrew Jarombek
 * @since 7/22/2017 -
 */

public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRecyclerAdapter.MemberItemHolder> {

    private ArrayList<GroupMember> groupMembers;

    public MemberRecyclerAdapter(ArrayList<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    @Override
    public MemberRecyclerAdapter.MemberItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_member_item, parent, false);
        return new MemberItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MemberRecyclerAdapter.MemberItemHolder holder, int position) {
        GroupMember groupMember = groupMembers.get(position);
        holder.bindMemberItem(groupMember);
    }

    @Override
    public int getItemCount() {
        return groupMembers.size();
    }

    public static class MemberItemHolder extends RecyclerView.ViewHolder {

        private View v;
        private GroupMember groupMember;
        private LinearLayout memberview;
        private TextView member_name;
        private TextView member_joined;

        public MemberItemHolder(View v) {
            super(v);
            memberview = (LinearLayout) v.findViewById(R.id.memberitemview);
            member_name = (TextView) v.findViewById(R.id.member_name);
            member_joined = (TextView) v.findViewById(R.id.member_joined);
            this.v = v;
        }

        public void bindMemberItem(final GroupMember groupMember) {
            this.groupMember = groupMember;

            member_name.setText(groupMember.getFirst() + " " + groupMember.getLast());

            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = df.format(groupMember.getMember_since());
            member_joined.setText("Member Since: " + date);

            memberview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) v.getContext()).viewProfile(groupMember.getUsername());
                }
            });
        }
    }
}

package com.example.andy.saints_xctf_android.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andy.api_model.pojos.Group;
import com.example.andy.api_model.pojos.GroupMember;
import com.example.andy.api_model.services.JSONConverter;
import com.example.andy.saints_xctf_android.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/22/2017 -
 */
public class MembersTab extends Fragment {

    private static final String LOG_TAG = MembersTab.class.getName();

    private View v;
    private Group group;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MemberRecyclerAdapter adapter;
    private ArrayList<GroupMember> members;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.members_tab, container, false);
        v = view;

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the members data
        members = new ArrayList<>();

        // Only display accepted members
        for (GroupMember member : group.getMembers()) {
            if (member.getStatus().equals("accepted")) {
                members.add(member);
            }
        }

        // Set up the recycler view and layout manager
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerMembersView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemberRecyclerAdapter(members);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

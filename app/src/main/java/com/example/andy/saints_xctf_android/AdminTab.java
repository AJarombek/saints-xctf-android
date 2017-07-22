package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.andy.api_model.Group;
import com.example.andy.api_model.GroupMember;
import com.example.andy.api_model.JSONConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class AdminTab extends Fragment {

    private View v;
    private Group group;
    private Spinner group_users_dropdown;
    private ArrayList<String> member_names;
    private HashMap<String, String> member_map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_tab, container, false);
        v = view;

        group_users_dropdown = (Spinner) v.findViewById(R.id.group_users_dropdown);

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        return view;
    }
}


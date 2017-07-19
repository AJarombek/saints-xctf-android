package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andy.api_model.Group;
import com.example.andy.api_model.JSONConverter;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/18/2017 -
 */
public class LeaderboardTab extends Fragment {

    private View v;
    private Group group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.leaderboard_tab, container, false);
        v = view;

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // monthly_view = (WebView) v.findViewById(R.id.monthly_web_view);

        return view;
    }
}

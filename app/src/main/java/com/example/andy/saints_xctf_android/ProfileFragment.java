package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 1/26/2017 -
 */
public class ProfileFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private static final String TAG = ProfileFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView profile_name;
    private TextView profile_username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        v = view;

        profile_name = (TextView) v.findViewById(R.id.profile_name);
        profile_username = (TextView) v.findViewById(R.id.profile_username);

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        Bundle bundle = getArguments();
        String username = bundle.getString("username", "");
        String viewer = prefs.getString("username", "");

        if (username.equals(viewer)) {
            // This is the users profile page
            String userJSON = prefs.getString("user", "");
            User user = new User();
            try {
                user = JSONConverter.toUser(userJSON);
            } catch (IOException e) {
                Log.e(TAG, "User object JSON conversion failed.");
                Log.e(TAG, e.getMessage());
            }
            profile_name.setText(user.getFirst() + " " + user.getLast());
            profile_username.setText(getString(R.string.at) + user.getUsername());
        } else {
            // This is someone else's profile page
        }

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) v.findViewById(R.id.pager);
        ProfilePager adapter = new ProfilePager(getActivity().getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

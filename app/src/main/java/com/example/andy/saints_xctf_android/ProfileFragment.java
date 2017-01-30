package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

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
    private ImageView profile_picture;
    private TextView profile_name;
    private TextView profile_username;
    private TextView profile_member_since;
    private TextView profile_groups;
    private TextView profile_class_year;
    private TextView profile_location;
    private TextView profile_favorite_event;
    private TextView profile_description;
    private TextView profile_class_year_view;
    private TextView profile_location_view;
    private TextView profile_favorite_event_view;
    private TextView profile_description_view;
    private Button edit_profile_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        v = view;

        profile_picture = (ImageView) v.findViewById(R.id.profile_picture);
        profile_name = (TextView) v.findViewById(R.id.profile_name);
        profile_username = (TextView) v.findViewById(R.id.profile_username);
        profile_member_since = (TextView) v.findViewById(R.id.profile_member_since);
        profile_groups = (TextView) v.findViewById(R.id.profile_groups);
        profile_class_year = (TextView) v.findViewById(R.id.profile_class_year);
        profile_location = (TextView) v.findViewById(R.id.profile_location);
        profile_favorite_event = (TextView) v.findViewById(R.id.profile_favorite_event);
        profile_description = (TextView) v.findViewById(R.id.profile_description);
        edit_profile_button = (Button) v.findViewById(R.id.edit_profile_button);

        profile_class_year_view = (TextView) v.findViewById(R.id.profile_class_year_view);
        profile_location_view = (TextView) v.findViewById(R.id.profile_location_view);
        profile_favorite_event_view = (TextView) v.findViewById(R.id.profile_favorite_event_view);
        profile_description_view = (TextView) v.findViewById(R.id.profile_description_view);

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        Bundle bundle = getArguments();
        String username = bundle.getString("username", "");
        String viewer = prefs.getString("username", "");

        User user = new User();
        if (username.equals(viewer)) {
            // This is the users profile page
            String userJSON = prefs.getString("user", "");

            try {
                user = JSONConverter.toUser(userJSON);
            } catch (IOException e) {
                Log.e(TAG, "User object JSON conversion failed.");
                Log.e(TAG, e.getMessage());
            }
            populateProfileInfo(user);

            edit_profile_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).editProfile();
                }
            });

        } else {
            // This is someone else's profile page
            edit_profile_button.setVisibility(View.GONE);
        }

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) v.findViewById(R.id.pager);
        ProfilePager adapter = new ProfilePager(getActivity().getSupportFragmentManager(),
                tabLayout.getTabCount(), user);
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);

        return view;
    }

    private void populateProfileInfo(User user) {
        profile_name.setText(user.getFirst() + " " + user.getLast());
        profile_username.setText(getString(R.string.at) + user.getUsername());

        // Display the profile picture
        String base64encoding = user.getProfilepic();
        if (base64encoding != null) {
            base64encoding = base64encoding.replace("data:image/jpeg;base64,", "");

            byte[] decodedString = Base64.decode(base64encoding, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profile_picture.setImageBitmap(decodedByte);
        }

        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        String date = df.format(user.getMember_since());
        profile_member_since.setText(date);

        StringBuilder groupString = new StringBuilder();
        Map<String,String> groups = user.getGroups();
        for (Map.Entry<String,String> entry : groups.entrySet()) {
            groupString.append(entry.getValue());
            groupString.append("\n");
        }
        profile_groups.setText(groupString.substring(0, groupString.length()-1));

        String class_year = String.valueOf(user.getClass_year());
        String location = user.getLocation();
        String favorite_event = user.getFavorite_event();
        String description = user.getDescription();

        if (class_year.equals("")) {
            profile_class_year.setVisibility(View.GONE);
            profile_class_year_view.setVisibility(View.GONE);
        } else {
            profile_class_year.setText(String.valueOf(user.getClass_year()));
        }

        if (location == null) {
            profile_location.setVisibility(View.GONE);
            profile_location_view.setVisibility(View.GONE);
        } else {
            profile_location.setText(user.getLocation());
        }

        if (favorite_event == null) {
            profile_favorite_event.setVisibility(View.GONE);
            profile_favorite_event_view.setVisibility(View.GONE);
        } else {
            profile_favorite_event.setText(user.getFavorite_event());
        }

        if (description == null) {
            profile_description.setVisibility(View.GONE);
            profile_description_view.setVisibility(View.GONE);
        } else {
            profile_description.setText(user.getDescription());
        }

        // Populate the statistics
        Map<String,Double> statistics = user.getStatistics();
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

package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
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
    public static final int REQUEST_CODE = 0;

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
    private LinearLayout profile_picture_view;
    private LinearLayout profile_info_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        v = view;
        setHasOptionsMenu(true);

        profile_picture_view = (LinearLayout) v.findViewById(R.id.profile_picture_view);
        profile_info_view = (LinearLayout) v.findViewById(R.id.profile_info_view);

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

            tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            viewPager = (ViewPager) v.findViewById(R.id.pager);
            ProfilePager adapter = new ProfilePager(getChildFragmentManager(),
                    tabLayout.getTabCount(), user);
            viewPager.setAdapter(adapter);
            tabLayout.setOnTabSelectedListener(this);

        } else {
            // This is someone else's profile page
            edit_profile_button.setVisibility(View.GONE);
            profile_picture_view.setVisibility(View.INVISIBLE);
            profile_info_view.setVisibility(View.INVISIBLE);

            LoadUserTask loadUserTask = new LoadUserTask();
            loadUserTask.execute(username);
        }

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

        if (groupString.length() > 0)
            profile_groups.setText(groupString.substring(0, groupString.length()-1));
        else {
            profile_groups.setVisibility(View.GONE);
        }


        String class_year = String.valueOf(user.getClass_year());
        String location = user.getLocation();
        String favorite_event = user.getFavorite_event();
        String description = user.getDescription();

        if (class_year.equals("0") || class_year.equals("null")) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log:
                LogDialogFragment logDialog = new LogDialogFragment();
                logDialog.setTargetFragment(this, REQUEST_CODE);
                logDialog.show(getFragmentManager(), "log dialog");
                return true;
            case R.id.action_home:
                ((MainActivity) getActivity()).viewMainPage();
            case R.id.action_profile:
                return true;
            case R.id.action_group:
                return true;
            case R.id.action_exit:
                getContext().getSharedPreferences(PREFS_NAME, 0).edit().clear().apply();
                ((MainActivity) getActivity()).signOut();
                return true;
            default:
                // The user's action was not recognized, invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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

    class LoadUserTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            User user = null;
            try {
                user = APIClient.userGetRequest(strings[0]);

                if (user == null)
                    return "no_internet";

            } catch (Exception e) {
                Log.e(TAG, "User object server JSON conversion failed.");
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return "no_internet";
            }

            return user;
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            User user;
            if (response != null) {
                if (response.equals("no_internet")) {
                    ((MainActivity) getActivity()).noInternet();
                } else if (response instanceof User) {
                    user = (User) response;

                    populateProfileInfo(user);
                    profile_info_view.setVisibility(View.VISIBLE);
                    profile_picture_view.setVisibility(View.VISIBLE);

                    tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
                    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                    viewPager = (ViewPager) v.findViewById(R.id.pager);
                    ProfilePager adapter = new ProfilePager(getChildFragmentManager(),
                            tabLayout.getTabCount(), user);
                    viewPager.setAdapter(adapter);
                    tabLayout.setOnTabSelectedListener(ProfileFragment.this);
                }
            }
        }
    }
}

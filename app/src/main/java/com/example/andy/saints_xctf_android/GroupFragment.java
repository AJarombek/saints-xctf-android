package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Group;
import com.example.andy.api_model.GroupMember;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 2/1/2017 -
 */
public class GroupFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private static final String LOG_TAG = GroupFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    public static final int REQUEST_CODE = 0;

    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView group_picture;
    private TextView group_name;
    private TextView group_members;
    private TextView group_description_view;
    private TextView group_description;
    private View group_info_progress;
    private View group_picture_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_group, container, false);
        setHasOptionsMenu(true);

        group_picture = (ImageView) v.findViewById(R.id.group_picture);
        group_name = (TextView) v.findViewById(R.id.group_name);
        group_members = (TextView) v.findViewById(R.id.group_members);
        group_description_view = (TextView) v.findViewById(R.id.group_description_view);
        group_description = (TextView) v.findViewById(R.id.group_description);

        group_info_progress = v.findViewById(R.id.group_info_progress);
        group_picture_progress = v.findViewById(R.id.group_picture_progress);

        Bundle bundle = getArguments();
        String groupname = bundle.getString("group", "");

        group_info_progress.setVisibility(View.VISIBLE);
        group_picture_progress.setVisibility(View.VISIBLE);

        group_picture.setVisibility(View.GONE);
        group_name.setVisibility(View.GONE);
        group_members.setVisibility(View.GONE);
        group_description_view.setVisibility(View.GONE);
        group_description.setVisibility(View.GONE);

        LoadGroupTask loadGroupTask = new LoadGroupTask();
        loadGroupTask.execute(groupname);

        return v;
    }

    private void populateGroupInfo(Group group) {
        group_name.setText(group.getGroup_title());

        List<GroupMember> members = group.getMembers();
        int memberCount = members.size();
        group_members.setText(String.valueOf(memberCount));

        // Display the profile picture
        String base64encoding = group.getGrouppic();
        if (base64encoding != null) {
            base64encoding = base64encoding.replace("data:image/jpeg;base64,", "");

            byte[] decodedString = Base64.decode(base64encoding, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            group_picture.setImageBitmap(decodedByte);
        }

        String description = group.getDescription();
        if (description == null) {
            group_description.setVisibility(View.GONE);
            group_description_view.setVisibility(View.GONE);
        } else {
            group_description.setText(description);
            group_description.setVisibility(View.VISIBLE);
            group_description_view.setVisibility(View.VISIBLE);
        }

        group_info_progress.setVisibility(View.GONE);
        group_picture_progress.setVisibility(View.GONE);
        group_picture.setVisibility(View.VISIBLE);
        group_name.setVisibility(View.VISIBLE);
        group_members.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

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
                return true;
            case R.id.action_profile:
                SharedPreferences prefs = getContext().getSharedPreferences(
                        PREFS_NAME, Context.MODE_PRIVATE);
                String username = prefs.getString("username", "");
                ((MainActivity) getActivity()).viewProfile(username);
                return true;
            case R.id.action_group:
                GroupDialogFragment groupDialog = new GroupDialogFragment();
                groupDialog.setTargetFragment(this, REQUEST_CODE);
                groupDialog.show(getFragmentManager(), "log dialog");
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

    class LoadGroupTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            Group group = null;
            try {
                group = APIClient.groupGetRequest(strings[0]);

                if (group == null)
                    return "no_internet";

            } catch (Exception e) {
                Log.e(LOG_TAG, "Group object server JSON conversion failed.");
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
                return "no_internet";
            }

            return group;
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            Group group;
            if (response != null) {
                if (response.equals("no_internet")) {
                    ((MainActivity) getActivity()).noInternet();
                } else if (response instanceof Group) {
                    group = (Group) response;

                    populateGroupInfo(group);

                    tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
                    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                    viewPager = (ViewPager) v.findViewById(R.id.pager);
                    GroupPager adapter = new GroupPager(getChildFragmentManager(),
                            tabLayout.getTabCount(), group);
                    viewPager.setAdapter(adapter);
                    tabLayout.setOnTabSelectedListener(GroupFragment.this);
                }
            }
        }
    }
}

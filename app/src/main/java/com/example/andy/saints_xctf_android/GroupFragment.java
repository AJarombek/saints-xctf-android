package com.example.andy.saints_xctf_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Group;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 2/1/2017 -
 */
public class GroupFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private static final String LOG_TAG = GroupFragment.class.getName();

    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView group_picture;
    private TextView group_name;
    private TextView group_members;
    private TextView group_description_view;
    private TextView group_description;

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

        Bundle bundle = getArguments();
        String groupname = bundle.getString("group", "");

        LoadGroupTask loadUserTask = new LoadGroupTask();
        loadUserTask.execute(groupname);

        return v;
    }

    private void populateGroupInfo(Group group) {
        group_name.setText(group.getGroup_title());

        List<String> members = group.getMembers();
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
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

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
                    ProfilePager adapter = new ProfilePager(getChildFragmentManager(),
                            tabLayout.getTabCount(), group);
                    viewPager.setAdapter(adapter);
                    tabLayout.setOnTabSelectedListener(GroupFragment.this);
                }
            }
        }
    }
}

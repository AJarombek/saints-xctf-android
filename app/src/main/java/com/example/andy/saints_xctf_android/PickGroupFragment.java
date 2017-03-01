package com.example.andy.saints_xctf_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.GroupInfo;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 1/31/2017 -
 */
public class PickGroupFragment extends Fragment {

    private static final String TAG = PickGroupFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    private View v;
    private Button pick_group_mensxc;
    private Button pick_group_wmensxc;
    private Button pick_group_menstf;
    private Button pick_group_wmenstf;
    private Button pick_group_alumni;
    private Button pick_group;
    private boolean mensxc, wmensxc, menstf, wmenstf, alumni;
    private User user;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_pick_group, container, false);

        pick_group_mensxc = (Button) v.findViewById(R.id.pick_group_mensxc);
        pick_group_wmensxc = (Button) v.findViewById(R.id.pick_group_wmensxc);
        pick_group_menstf = (Button) v.findViewById(R.id.pick_group_menstf);
        pick_group_wmenstf = (Button) v.findViewById(R.id.pick_group_wmenstf);
        pick_group_alumni = (Button) v.findViewById(R.id.pick_group_alumni);
        pick_group = (Button) v.findViewById(R.id.pick_group);

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);

        String userJSON = prefs.getString("user", "");
        user = null;

        try {
            user = JSONConverter.toUser(userJSON);
        } catch (IOException e) {
            Log.e(TAG, "User object JSON conversion failed.");
            Log.e(TAG, e.getMessage());
        }

        username = user.getUsername();

        pick_group_mensxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mensxc) {
                    mensxc = false;
                    pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!menstf) {
                        // Enable Women's XC
                        pick_group_wmensxc.setClickable(true);
                        pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Women's TF
                        pick_group_wmenstf.setClickable(true);
                        pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        pick_group_alumni.setClickable(true);
                        pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    mensxc = true;
                    pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    pick_group_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Women's XC
                    pick_group_wmensxc.setClickable(false);
                    pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's TF
                    pick_group_wmenstf.setClickable(false);
                    pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    pick_group_alumni.setClickable(false);
                    pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        pick_group_menstf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menstf) {
                    menstf = false;
                    pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!mensxc) {
                        // Enable Women's XC
                        pick_group_wmensxc.setClickable(true);
                        pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Women's TF
                        pick_group_wmenstf.setClickable(true);
                        pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        pick_group_alumni.setClickable(true);
                        pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    menstf = true;
                    pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    pick_group_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Women's XC
                    pick_group_wmensxc.setClickable(false);
                    pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's TF
                    pick_group_wmenstf.setClickable(false);
                    pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    pick_group_alumni.setClickable(false);
                    pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        pick_group_wmensxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wmensxc) {
                    wmensxc = false;
                    pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!wmenstf) {
                        // Enable Men's XC
                        pick_group_mensxc.setClickable(true);
                        pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_mensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Men's TF
                        pick_group_menstf.setClickable(true);
                        pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_menstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        pick_group_alumni.setClickable(true);
                        pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    wmensxc = true;
                    pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Men's XC
                    pick_group_mensxc.setClickable(false);
                    pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Men's TF
                    pick_group_menstf.setClickable(false);
                    pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    pick_group_alumni.setClickable(false);
                    pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        pick_group_wmenstf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wmenstf) {
                    wmenstf = false;
                    pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!wmensxc) {
                        // Enable Men's XC
                        pick_group_mensxc.setClickable(true);
                        pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_mensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Men's TF
                        pick_group_menstf.setClickable(true);
                        pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_menstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        pick_group_alumni.setClickable(true);
                        pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        pick_group_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    wmenstf = true;
                    pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Men's XC
                    pick_group_mensxc.setClickable(false);
                    pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Men's TF
                    pick_group_menstf.setClickable(false);
                    pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    pick_group_alumni.setClickable(false);
                    pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        pick_group_alumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alumni) {
                    alumni = false;
                    pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Men's XC
                    pick_group_mensxc.setClickable(true);
                    pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Men's TF
                    pick_group_menstf.setClickable(true);
                    pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Women's XC
                    pick_group_wmensxc.setClickable(true);
                    pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Women's TF
                    pick_group_wmenstf.setClickable(true);
                    pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));
                } else {
                    alumni = true;
                    pick_group_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    pick_group_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Men's XC
                    pick_group_mensxc.setClickable(false);
                    pick_group_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Men's TF
                    pick_group_menstf.setClickable(false);
                    pick_group_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's XC
                    pick_group_wmensxc.setClickable(false);
                    pick_group_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's TF
                    pick_group_wmenstf.setClickable(false);
                    pick_group_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    pick_group_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        pick_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GroupInfo> groups = new ArrayList<>();
                if (mensxc) {
                    GroupInfo mensxc_info = new GroupInfo();
                    mensxc_info.setGroup_name("mensxc");
                    mensxc_info.setGroup_title("Men's Cross Country");
                    groups.add(mensxc_info);
                }
                if (wmensxc) {
                    GroupInfo wmensxc_info = new GroupInfo();
                    wmensxc_info.setGroup_name("wmensxc");
                    wmensxc_info.setGroup_title("Women's Cross Country");
                    groups.add(wmensxc_info);
                }
                if (menstf) {
                    GroupInfo menstf_info = new GroupInfo();
                    menstf_info.setGroup_name("menstf");
                    menstf_info.setGroup_title("Men's Track & Field");
                    groups.add(menstf_info);
                }
                if (wmenstf) {
                    GroupInfo wmenstf_info = new GroupInfo();
                    wmenstf_info.setGroup_name("wmenstf");
                    wmenstf_info.setGroup_title("Women's Track & Field");
                    groups.add(wmenstf_info);
                }
                if (alumni) {
                    GroupInfo alumni_info = new GroupInfo();
                    alumni_info.setGroup_name("alumni");
                    alumni_info.setGroup_title("Alumni");
                    groups.add(alumni_info);
                }
                user.setGroups(groups);

                String userString = "";
                try {
                    userString = JSONConverter.fromUser(user);
                } catch (Throwable throwable) {
                    Log.e(TAG, "User object JSON conversion failed.");
                    Log.e(TAG, throwable.getMessage());
                    throwable.printStackTrace();
                    return;
                }

                PickGroupTask pickGroupTask = new PickGroupTask();
                pickGroupTask.execute(username, userString);
            }
        });

        return v;
    }

    class PickGroupTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... strings) {
            User user;
            try {
                user = APIClient.userPutRequest(strings[0], strings[1]);

                if (user == null)
                    return null;

            } catch (Exception e) {
                Log.e(TAG, "User object server JSON conversion failed.");
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return null;
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            String userString = "";
            try {
                userString = JSONConverter.fromUser(user);
            } catch (Throwable throwable) {
                Log.e(TAG, "User object received JSON conversion failed.");
                Log.e(TAG, throwable.getMessage());
            }

            SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("user", userString);
            editor.apply();

            ((MainActivity) getActivity()).viewMainPage();
        }
    }
}

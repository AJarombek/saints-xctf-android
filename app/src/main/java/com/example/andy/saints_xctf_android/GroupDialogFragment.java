package com.example.andy.saints_xctf_android;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.andy.api_model.GroupInfo;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for the Group Select Dialog Fragment
 * @author Andrew Jarombek
 * @since 2/1/2017 -
 */
public class GroupDialogFragment extends DialogFragment {

    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    private static final String LOG_TAG = GroupDialogFragment.class.getName();

    private Button view_group_mensxc;
    private Button view_group_wmensxc;
    private Button view_group_menstf;
    private Button view_group_wmenstf;
    private Button view_group_alumni;
    private AlertDialog d;

    /**
     * Create and Run an AlertDialog for Log Submitting
     * @param bundle --
     * @return --
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialog = super.onCreateDialog(bundle);
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View groupDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_group_dialog, null);
        builder.setView(groupDialogView); // add GUI to dialog

        // Make this dialog fragment have no title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        view_group_mensxc = (Button) groupDialogView.findViewById(R.id.view_group_mensxc);
        view_group_wmensxc = (Button) groupDialogView.findViewById(R.id.view_group_wmensxc);
        view_group_menstf = (Button) groupDialogView.findViewById(R.id.view_group_menstf);
        view_group_wmenstf = (Button) groupDialogView.findViewById(R.id.view_group_wmenstf);
        view_group_alumni = (Button) groupDialogView.findViewById(R.id.view_group_alumni);

        return builder.create(); // return dialog
    }

    @Override
    public void onStart() {
        super.onStart();
        d = (AlertDialog) getDialog();
        if (d != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(
                    PREFS_NAME, Context.MODE_PRIVATE);
            String userJSON = prefs.getString("user", "");

            User user = new User();
            try {
                user = JSONConverter.toUser(userJSON);
            } catch (IOException e) {
                android.util.Log.e(LOG_TAG, "User object JSON conversion failed.");
                android.util.Log.e(LOG_TAG, e.getMessage());
            }

            List<GroupInfo> groups = user.getGroups();
            List<String> group_names = new ArrayList<>();
            for (GroupInfo group : groups) {
                group_names.add(group.getGroup_name());
            }

            if (!group_names.contains("mensxc")) {
                view_group_mensxc.setVisibility(View.GONE);
            } else {
                view_group_mensxc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).viewGroup("mensxc");
                        d.dismiss();
                    }
                });
            }

            if (!group_names.contains("wmensxc")) {
                view_group_wmensxc.setVisibility(View.GONE);
            } else {
                view_group_wmensxc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).viewGroup("wmensxc");
                        d.dismiss();
                    }
                });
            }

            if (!group_names.contains("menstf")) {
                view_group_menstf.setVisibility(View.GONE);
            } else {
                view_group_menstf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).viewGroup("menstf");
                        d.dismiss();
                    }
                });
            }

            if (!group_names.contains("wmenstf")) {
                view_group_wmenstf.setVisibility(View.GONE);
            } else {
                view_group_wmenstf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).viewGroup("wmenstf");
                        d.dismiss();
                    }
                });
            }

            if (!group_names.contains("alumni")) {
                view_group_alumni.setVisibility(View.GONE);
            } else {
                view_group_alumni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).viewGroup("alumni");
                        d.dismiss();
                    }
                });
            }
        }
    }
}


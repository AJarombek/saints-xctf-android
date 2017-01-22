package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andy.api_model.Log;

import java.util.ArrayList;

/**
 * Adapter for the RecycleView (which shows workout logs)
 * @author Andrew Jarombek
 * @since 1/21/2017 -
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LogHolder> {

    private ArrayList<Log> logs;

    public RecyclerAdapter(ArrayList<Log> logs) {
        this.logs = logs;
    }

    @Override
    public RecyclerAdapter.LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        return new LogHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.LogHolder holder, int position) {
        Log itemlog = logs.get(position);
        holder.bindLog(itemlog);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public static class LogHolder extends RecyclerView.ViewHolder {

        private TextView logview_username;
        private TextView logview_name;
        private TextView logview_date;
        private TextView logview_type;
        private TextView logview_location;
        private TextView logview_distance;
        private TextView logview_time;
        private TextView logview_description;
        private EditText logview_add_comment;
        private Log log;

        private static final String LOG_KEY = "LOGVIEW";

        public LogHolder(View v) {
            super(v);

            logview_username = (TextView) v.findViewById(R.id.logview_username);
            logview_name = (TextView) v.findViewById(R.id.logview_name);
            logview_date = (TextView) v.findViewById(R.id.logview_date);
            logview_type = (TextView) v.findViewById(R.id.logview_type);
            logview_location = (TextView) v.findViewById(R.id.logview_location);
            logview_distance = (TextView) v.findViewById(R.id.logview_distance);
            logview_time = (TextView) v.findViewById(R.id.logview_time);
            logview_description = (TextView) v.findViewById(R.id.logview_description);
            logview_add_comment = (EditText) v.findViewById(R.id.logview_add_comment);

            // Go to the users profile when you click on their name
            logview_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bindLog(Log log) {
            this.log = log;
            logview_username.setText(log.getFirst() + " " + log.getLast());
            logview_name.setText(log.getName());
            logview_date.setText(log.getDate().toString());
            logview_type.setText(log.getType());
            logview_location.setText(log.getLocation());
            logview_distance.setText(String.valueOf(log.getDistance()));
            logview_time.setText(log.getTime().toString());
            logview_description.setText(log.getDescription());
        }
    }
}

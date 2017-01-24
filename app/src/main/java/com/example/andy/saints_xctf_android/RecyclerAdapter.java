package com.example.andy.saints_xctf_android;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andy.api_model.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

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

        private View v;
        private RecyclerView recyclerCommentView;
        private LinearLayoutManager linearLayoutManager;
        private RecyclerCommentAdapter adapter;

        private LinearLayout logview;
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
        private static final String[] COLOR_VALUE = {MainActivity.COLOR_TERRIBLE,
                MainActivity.COLOR_VERYBAD, MainActivity.COLOR_BAD, MainActivity.COLOR_PRETTYBAD,
                MainActivity.COLOR_MEDIOCRE, MainActivity.COLOR_AVERAGE,
                MainActivity.COLOR_FAIRLYGOOD, MainActivity.COLOR_GOOD,
                MainActivity.COLOR_GREAT, MainActivity.COLOR_FANTASTIC};

        public LogHolder(View v) {
            super(v);

            this.v = v;
            logview = (LinearLayout) v.findViewById(R.id.logview);
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

            // Add the comment to the view and call API when user hits enter
            logview_add_comment.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return false;
                }
            });
        }

        public void bindLog(Log log) {
            this.log = log;

            // Set the background color according to the log feel
            GradientDrawable bgShape = (GradientDrawable)logview.getBackground();
            int color = Color.parseColor(COLOR_VALUE[log.getFeel()-1]);
            bgShape.setColor(color);

            // Set all of the log values
            logview_username.setText(log.getFirst() + " " + log.getLast());
            logview_name.setText(log.getName());

            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            String date = df.format(log.getDate());
            logview_date.setText(date);
            logview_type.setText(log.getType().toUpperCase());
            logview_location.setText(log.getLocation());

            String distance = String.valueOf(log.getDistance());
            if (distance.equals("0.0")) {
                logview_distance.setVisibility(View.GONE);
            }
            logview_distance.setText(distance + " " + log.getMetric());

            String time = log.getTime().toString();
            if (time.equals("00:00:00")) {
                logview_time.setVisibility(View.GONE);
            }
            logview_time.setText(log.getTime().toString());
            logview_description.setText(log.getDescription());

            // Add the comment recycler view
            // Set up the recycler view and layout manager
            recyclerCommentView = (RecyclerView) v.findViewById(R.id.recyclerViewComments);
            linearLayoutManager = new LinearLayoutManager(v.getContext());
            recyclerCommentView.setLayoutManager(linearLayoutManager);

            Collections.reverse(log.getComments());
            adapter = new RecyclerCommentAdapter(log.getComments());
            recyclerCommentView.setAdapter(adapter);
        }
    }
}

package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
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

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Comment;
import com.example.andy.api_model.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Adapter for the RecycleView (which shows workout logs)
 * @author Andrew Jarombek
 * @since 1/21/2017 -
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LogHolder> {

    private static final String LOG_TAG = RecyclerAdapter.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
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
        private ArrayList<Comment> comments;

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
                    // If the event is a key down on the enter button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        SharedPreferences prefs = v.getContext().getSharedPreferences(
                                PREFS_NAME, Context.MODE_PRIVATE);
                        String username = prefs.getString("username", "");
                        String first = prefs.getString("first", "");
                        String last = prefs.getString("last", "");

                        CommentTask commentTask = new CommentTask();
                        commentTask.execute(logview_add_comment.getText().toString(),
                                String.valueOf(log.getLog_id()), username, first, last);
                        return true;
                    }
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
            logview_date.setText(log.getDate());
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

            comments = log.getComments();
            Collections.reverse(comments);
            adapter = new RecyclerCommentAdapter(comments);
            recyclerCommentView.setAdapter(adapter);
        }

        class CommentTask extends AsyncTask<String, Void, Comment> {

            @Override
            protected Comment doInBackground(String... params) {
                Comment comment;
                String commentJSON;
                try {
                    comment = new Comment();
                    comment.setContent(params[0]);
                    comment.setLog_id(Integer.parseInt(params[1]));
                    comment.setUsername(params[2]);
                    comment.setFirst(params[3]);
                    comment.setLast(params[4]);

                    ObjectMapper mapper = new ObjectMapper();
                    commentJSON = mapper.writeValueAsString(comment);

                    comment = APIClient.commentPostRequest(commentJSON);

                    // If null is returned, there is no internet connection
                    if (comment == null) {
                        return null;
                    }
                } catch (IOException e) {
                    android.util.Log.e(LOG_TAG, "Comment object JSON conversion failed.");
                    android.util.Log.e(LOG_TAG, e.getMessage());
                    return null;
                }
                return comment;
            }

            @Override
            protected void onPostExecute(Comment response) {
                super.onPostExecute(response);

                if (response != null) {
                    android.util.Log.d(LOG_TAG, "The Comment Object Received: " + response.toString());
                    logview_add_comment.setText("");
                    comments.add(response);
                    adapter.notifyItemInserted(comments.size() - 1);
                }
            }
        }
    }
}

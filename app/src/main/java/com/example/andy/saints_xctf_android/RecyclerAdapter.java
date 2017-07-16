package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.Comment;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * Adapter for the RecycleView (which shows workout logs)
 * @author Andrew Jarombek
 * @since 1/21/2017 -
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LogHolder> {

    private static final String LOG_TAG = RecyclerAdapter.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    public static final int REQUEST_CODE = 0;

    private ArrayList<Log> logs;
    private Context context;
    private Fragment fragment;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerAdapter(Context context, Fragment fragment, ArrayList<Log> logs,
                           RecyclerView recyclerView) {
        this.logs = logs;
        this.context = context;
        this.fragment = fragment;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager =
                    (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        loading = true;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return logs.get(position) != null ? 1 : 0;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogHolder logHolder;
        View layoutView;
        if (viewType == 1) {
            layoutView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recyclerview_item_row, parent, false);
            logHolder = new LogHolder(layoutView, this);
        } else {
            layoutView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.progress_item, parent, false);
            logHolder = new ProgressHolder(layoutView, this);
        }
        return logHolder;
    }

    @Override
    public void onBindViewHolder(LogHolder holder, int position) {
        if (holder instanceof ProgressHolder) {
            ((ProgressHolder)holder).progressBar.setIndeterminate(true);
        } else {
            Log itemlog = logs.get(position);
            holder.bindLog(itemlog);
        }
    }

    public void setLoad() {
        loading = true;
    }

    public void setLoaded() {
        loading = false;
    }

    public boolean getLoading() {
        return loading;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * Remove a single log from the recyclerAdapter
     * @param position the index of the log to be removed
     */
    public void removeAt(int position) {
        logs.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, logs.size());
    }

    /**
     * Edit a single log in the recyclerAdapter
     * @param position the index of the log that needs to be edited
     */
    public void edit(int position) {
        try {
            String log = JSONConverter.fromLog(logs.get(position));

            // Pass the log dialog the current logs information
            Bundle args = new Bundle();
            args.putString("logString", log);

            LogDialogFragment logDialog = new LogDialogFragment();
            logDialog.setTargetFragment(fragment, REQUEST_CODE);
            logDialog.setArguments(args);
            logDialog.show(fragment.getFragmentManager(), "log dialog");

        } catch (Throwable throwable) {
            android.util.Log.e(LOG_TAG, "Failed to Convert Log to String for Editing");
            throwable.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    /**
     * Inner Class to Hold each Individual Log and its Corresponding logic in the RecyclerAdapter
     */
    public static class LogHolder extends RecyclerView.ViewHolder {

        private RecyclerAdapter recyclerAdapter;
        private View v;
        private RecyclerView recyclerCommentView;
        private LinearLayoutManager linearLayoutManager;
        private RecyclerCommentAdapter adapter;

        private LinearLayout logview;
        private FloatingActionButton edit_fab;
        private FloatingActionButton delete_fab;
        private TextView logview_un;
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
        private String username;
        private boolean isFabVisible = false;
        private ArrayList<Comment> comments;

        private static final String LOG_KEY = "LOGVIEW";
        private static final String[] COLOR_VALUE = {MainActivity.COLOR_TERRIBLE,
                MainActivity.COLOR_VERYBAD, MainActivity.COLOR_BAD, MainActivity.COLOR_PRETTYBAD,
                MainActivity.COLOR_MEDIOCRE, MainActivity.COLOR_AVERAGE,
                MainActivity.COLOR_FAIRLYGOOD, MainActivity.COLOR_GOOD,
                MainActivity.COLOR_GREAT, MainActivity.COLOR_FANTASTIC};

        public LogHolder(View v, RecyclerAdapter recyclerAdapter) {
            super(v);

            this.v = v;
            this.recyclerAdapter = recyclerAdapter;
            logview = (LinearLayout) v.findViewById(R.id.logview);
            edit_fab = (FloatingActionButton) v.findViewById(R.id.edit_fab);
            delete_fab = (FloatingActionButton) v.findViewById(R.id.delete_fab);
            logview_un = (TextView) v.findViewById(R.id.logview_un);
            logview_username = (TextView) v.findViewById(R.id.logview_username);
            logview_name = (TextView) v.findViewById(R.id.logview_name);
            logview_date = (TextView) v.findViewById(R.id.logview_date);
            logview_type = (TextView) v.findViewById(R.id.logview_type);
            logview_location = (TextView) v.findViewById(R.id.logview_location);
            logview_distance = (TextView) v.findViewById(R.id.logview_distance);
            logview_time = (TextView) v.findViewById(R.id.logview_time);
            logview_description = (TextView) v.findViewById(R.id.logview_description);
            logview_add_comment = (EditText) v.findViewById(R.id.logview_add_comment);
        }

        public void bindLog(Log logObject) {
            this.log = logObject;
            logview_un.setText(log.getUsername());
            username = logview_un.getText().toString();

            // Get the users preferences (username, first, last)
            SharedPreferences prefs = v.getContext().getSharedPreferences(
                    PREFS_NAME, Context.MODE_PRIVATE);
            final String my_username = prefs.getString("username", "");
            final String my_first = prefs.getString("first", "");
            final String my_last = prefs.getString("last", "");


            // If this is the users log, display the edit and delete action buttons on click
            logview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (username.equals(my_username)) {
                        if (isFabVisible) {
                            edit_fab.setVisibility(View.GONE);
                            delete_fab.setVisibility(View.GONE);
                            isFabVisible = false;
                        } else {
                            edit_fab.setVisibility(View.VISIBLE);
                            delete_fab.setVisibility(View.VISIBLE);
                            isFabVisible = true;
                        }

                    }
                }
            });

            edit_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerAdapter.edit(getAdapterPosition());
                }
            });

            // When clicking the delete action button, make an async call to delete
            delete_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteLogTask deleteLogTask = new DeleteLogTask();
                    deleteLogTask.execute(log.getLog_id());
                }
            });

            // Go to the users profile when you click on their name
            logview_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.util.Log.d(LOG_TAG, username);
                    ((MainActivity) recyclerAdapter.context).viewProfile(username);
                }
            });

            // Add the comment to the view and call API when user hits enter
            logview_add_comment.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    String newcomment = logview_add_comment.getText().toString().trim();

                    // If the event is a key down on the enter button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        // Make sure the comment contains content
                        if (!newcomment.equals("")) {

                            CommentTask commentTask = new CommentTask();
                            commentTask.execute(newcomment,
                                    String.valueOf(log.getLog_id()), my_username, my_first, my_last);
                            return true;
                        } else {
                            // Hide the keyboard
                            InputMethodManager imm = (InputMethodManager)
                                    v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                    return false;
                }
            });

            // Set the background color according to the log feel
            GradientDrawable bgShape = (GradientDrawable)logview.getBackground();
            int color = Color.parseColor(COLOR_VALUE[log.getFeel()-1]);
            bgShape.setColor(color);

            // Set all of the log values
            logview_username.setText(log.getFirst() + " " + log.getLast());
            logview_name.setText(log.getName());

            String dateString = log.getDate();
            int year = Integer.parseInt(dateString.substring(0,4));
            int month = Integer.parseInt(dateString.substring(5,7));
            int day = Integer.parseInt(dateString.substring(8,10));

            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(year,month-1,day);

            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            df.setCalendar(calendar);
            String date = df.format(calendar.getTime());
            logview_date.setText(date);
            logview_type.setText(log.getType().toUpperCase());
            logview_location.setText(log.getLocation());

            String distance = String.valueOf(log.getDistance());
            if (distance.equals("0.0")) {
                logview_distance.setVisibility(View.GONE);
            }
            logview_distance.setText(distance + " " + log.getMetric());

            String time = log.getTime();
            if (time == null || time.equals("00:00:00")) {
                logview_time.setVisibility(View.GONE);
            } else {
                // Format the log time correctly removing zeros
                for (int i = 0; i < time.length(); i++) {
                    char c = time.charAt(i);
                    if (c != '0' && c != ':') {
                        time = time.substring(i);
                        break;
                    }
                }

                String pace = log.getPace();
                if (distance.equals("0.0")) {
                    pace = "";
                } else {
                    // Format the log pace correctly removing zeros
                    for (int i = 0; i < pace.length(); i++) {
                        char c = pace.charAt(i);
                        if (c != '0' && c != ':') {
                            pace = "(" + pace.substring(i) + "/mi)";
                            break;
                        }
                    }
                }
                logview_time.setText(time + " " + pace);
            }

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

        /**
         * Async Task for Commenting on a Log and Adding the Comment to the Database
         */
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

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager)
                            v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    comments.add(response);
                    adapter.notifyItemInserted(comments.size() - 1);
                }
            }
        }

        /**
         * Async Task for Deleting a Log from the RecyclerView and Database
         */
        class DeleteLogTask extends AsyncTask<Integer, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Integer... params) {
                try {

                    Integer log_id = params[0];

                    return APIClient.logDeleteRequest(log_id);

                } catch (Throwable e) {
                    android.util.Log.e(LOG_TAG, "Log Delete Failed.");
                    android.util.Log.e(LOG_TAG, e.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean response) {
                super.onPostExecute(response);

                if (response) {
                    recyclerAdapter.removeAt(getAdapterPosition());
                }
            }
        }
    }

    public static class ProgressHolder extends LogHolder {

        private ProgressBar progressBar;

        public ProgressHolder(View v, RecyclerAdapter recyclerAdapter) {
            super(v, recyclerAdapter);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}

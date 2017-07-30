package com.example.andy.saints_xctf_android;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andy.api_model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adapter for the RecycleView (which shows workout logs)
 * @author Andrew Jarombek
 * @since 1/21/2017 -
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder> {

    private static final String LOG_TAG = CommentRecyclerAdapter.class.getName();
    private static final String USER_TAG_REGEX = "@[a-zA-Z0-9]+";

    private Context context;
    private ArrayList<Comment> comments;

    public CommentRecyclerAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public CommentRecyclerAdapter.CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_comments_item, parent, false);
        return new CommentHolder(this, inflatedView);
    }

    @Override
    public void onBindViewHolder(CommentRecyclerAdapter.CommentHolder holder, int position) {
        Comment itemcomment = comments.get(position);
        holder.bindComment(itemcomment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        private View v;
        private CommentRecyclerAdapter commentRecyclerAdapter;
        private Comment comment;
        private LinearLayout commentview;
        private TextView commentview_username;
        private TextView commentview_date;
        private TextView commentview_content;
        private String commenter_username;

        public CommentHolder(CommentRecyclerAdapter commentRecyclerAdapter, View v) {
            super(v);
            this.commentRecyclerAdapter = commentRecyclerAdapter;
            commentview = (LinearLayout) v.findViewById(R.id.commentview);
            commentview_username = (TextView) v.findViewById(R.id.commentview_username);
            commentview_date = (TextView) v.findViewById(R.id.commentview_date);
            commentview_content = (TextView) v.findViewById(R.id.commentview_content);
            this.v = v;
        }

        public void bindComment(Comment comment) {
            this.comment = comment;

            commentview_username.setText(comment.getFirst() + " " + comment.getLast());

            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = df.format(comment.getTime());
            commentview_date.setText(date);

            String content = comment.getContent();
            SpannableString ss = new SpannableString(content);

            Pattern pattern = Pattern.compile(USER_TAG_REGEX);
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                final String match = matcher.group();

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        ((MainActivity) commentRecyclerAdapter.context).viewProfile(match.substring(1));
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.DKGRAY);
                        ds.setTypeface(Typeface.DEFAULT_BOLD);
                        ds.setUnderlineText(false);
                    }
                };

                ss.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            commentview_content.setText(ss);
            commentview_content.setMovementMethod(LinkMovementMethod.getInstance());
            commentview_content.setHighlightColor(Color.TRANSPARENT);

            commenter_username = comment.getUsername();

            // Go to the commenters profile when you click on their name
            commentview_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.util.Log.d(LOG_TAG, commenter_username);
                    ((MainActivity) commentRecyclerAdapter.context).viewProfile(commenter_username);
                }
            });
        }
    }
}

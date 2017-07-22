package com.example.andy.saints_xctf_android;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andy.api_model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Adapter for the RecycleView (which shows workout logs)
 * @author Andrew Jarombek
 * @since 1/21/2017 -
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder> {

    private ArrayList<Comment> comments;

    public CommentRecyclerAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentRecyclerAdapter.CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_comments_item, parent, false);
        return new CommentHolder(inflatedView);
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

    public static class CommentHolder extends RecyclerView.ViewHolder {

        private View v;
        private Comment comment;
        private LinearLayout commentview;
        private TextView commentview_username;
        private TextView commentview_date;
        private TextView commentview_content;

        public CommentHolder(View v) {
            super(v);
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

            commentview_content.setText(comment.getContent());
        }
    }
}

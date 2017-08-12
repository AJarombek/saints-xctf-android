package com.jarombek.andy.saints_xctf_android.group;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarombek.andy.api_model.pojos.Message;
import com.jarombek.andy.saints_xctf_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Adapter for the RecycleView (which shows message items)
 * @author Andrew Jarombek
 * @since 7/22/2017 -
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageItemHolder> {

    private ArrayList<Message> messageItems;

    public MessageRecyclerAdapter(ArrayList<Message> messageItems) {
        this.messageItems = messageItems;
    }

    @Override
    public MessageRecyclerAdapter.MessageItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_message_item, parent, false);
        return new MessageItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MessageRecyclerAdapter.MessageItemHolder holder, int position) {
        Message messageItem = messageItems.get(position);
        holder.bindMessage(messageItem);
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    public static class MessageItemHolder extends RecyclerView.ViewHolder {

        private View v;
        private Message message;
        private LinearLayout messageview;
        private TextView messageview_username;
        private TextView messageview_date;
        private TextView messageview_content;

        public MessageItemHolder(View v) {
            super(v);
            messageview = (LinearLayout) v.findViewById(R.id.messageitemview);
            messageview_username = (TextView) v.findViewById(R.id.messageview_username);
            messageview_date = (TextView) v.findViewById(R.id.messageview_date);
            messageview_content = (TextView) v.findViewById(R.id.messageview_content);
            this.v = v;
        }

        public void bindMessage(Message message) {
            this.message = message;

            messageview_username.setText(message.getFirst() + " " + message.getLast());

            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = df.format(message.getTime());
            messageview_date.setText(date);

            messageview_content.setText(message.getContent());
        }
    }
}

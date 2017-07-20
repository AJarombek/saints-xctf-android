package com.example.andy.saints_xctf_android;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andy.api_model.Comment;
import com.example.andy.api_model.LeaderboardItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Adapter for the RecycleView (which shows leaderboard items)
 * @author Andrew Jarombek
 * @since 7/19/2017 -
 */

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerAdapter.LeaderboardItemHolder> {

    private ArrayList<LeaderboardItem> leaderboardItems;

    public LeaderboardRecyclerAdapter(ArrayList<LeaderboardItem> leaderboardItems) {
        this.leaderboardItems = leaderboardItems;
    }

    @Override
    public LeaderboardRecyclerAdapter.LeaderboardItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_leaderboard_item_row, parent, false);
        return new LeaderboardItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(LeaderboardRecyclerAdapter.LeaderboardItemHolder holder, int position) {
        LeaderboardItem leaderboardItem = leaderboardItems.get(position);
        holder.bindLeaderboardItem(leaderboardItem, position);
    }

    @Override
    public int getItemCount() {
        return leaderboardItems.size();
    }

    public static class LeaderboardItemHolder extends RecyclerView.ViewHolder {

        private View v;
        private LeaderboardItem leaderboardItem;
        private LinearLayout leaderboarditemview;
        private TextView leaderboard_rank;
        private TextView leaderboard_name;
        private TextView leaderboard_mileage;

        public LeaderboardItemHolder(View v) {
            super(v);
            leaderboarditemview = (LinearLayout) v.findViewById(R.id.leaderboarditemview);
            leaderboard_rank = (TextView) v.findViewById(R.id.leaderboard_rank);
            leaderboard_name = (TextView) v.findViewById(R.id.leaderboard_name);
            leaderboard_mileage = (TextView) v.findViewById(R.id.leaderboard_mileage);
            this.v = v;
        }

        public void bindLeaderboardItem(LeaderboardItem leaderboardItem, int position) {
            this.leaderboardItem = leaderboardItem;

            leaderboard_rank.setText((position + 1) + ")");
            leaderboard_name.setText(leaderboardItem.getFirst() + " " + leaderboardItem.getLast());
            double miles = BigDecimal.valueOf(leaderboardItem.getFilteredmiles())
                    .setScale(1, RoundingMode.HALF_UP).doubleValue();

            leaderboard_mileage.setText(String.valueOf(miles));
        }
    }
}

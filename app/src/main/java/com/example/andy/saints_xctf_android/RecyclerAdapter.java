package com.example.andy.saints_xctf_android;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.andy.api_model.Log;

import java.util.ArrayList;

/**
 * Recycler for the RecycleView (which shows workout logs)
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
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.LogHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return logs.size();
    }
}

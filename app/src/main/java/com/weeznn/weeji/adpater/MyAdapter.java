package com.weeznn.weeji.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.weeznn.weeji.entry.MeetingModul;

import java.util.ArrayList;

/**
 * Created by weeznn on 2018/3/14.
 */

public class MyAdapter extends RecyclerView.Adapter {
    private static final String TAG="meetingAdapter";

    private ArrayList<MeetingModul> data=new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: 2018/3/14
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static final class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }


    }
}

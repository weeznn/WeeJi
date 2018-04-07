package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weeznn.weeji.R;
import com.weeznn.weeji.util.db.entry.People;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by weeznn on 2018/4/6.
 */

public class PeopleListShowAdapter extends RecyclerView.Adapter<PeopleListShowAdapter.PeopleShowViewHolder> {
    private static final String TAG = "PeopleListShowAdapter";

    private List<People> data = new LinkedList<>();
    private LayoutInflater inflater;

    public PeopleListShowAdapter(Context contex, List<People> list) {
        Log.i(TAG,"PeopleListShowAdapter  data size "+list.size());
        this.inflater = LayoutInflater.from(contex);
        this.data = list;
    }

    @Override
    public PeopleShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_people_list_show,parent,false);
        return new PeopleShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeopleShowViewHolder holder, int position) {

        People people = data.get(position);
        Log.i(TAG,"bindShowViewHolder");

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/4/3 跳转联系人详情
            }
        });

        holder.job.setText(people.getCompany() + " | " + people.getJob());
        holder.name.setText(people.getName());

        Glide.with(holder.imageView)
                .load(people.getPhoto())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * show ViewHolder
     */
    public class PeopleShowViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name;
        public TextView job;
        public ConstraintLayout layout;

        public PeopleShowViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            job = itemView.findViewById(R.id.job);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}



package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weeznn.weeji.R;
import com.weeznn.weeji.util.db.entry.Diary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weeznn on 2018/3/22.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>{
    private static final String TAG=DiaryAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater inflater;
    private List<Diary> data=new ArrayList<>();

    public DiaryAdapter(Context context, List<Diary> list){
        this.context=context;
        this.data=list;
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.card_dialy,parent,false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        Diary modul=  data.get(position);
        holder.mood.setText(modul.getMood());
        holder.addr.setText(modul.getAddress());
        holder.time.setText(modul.getDate());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DiaryViewHolder extends RecyclerView.ViewHolder{
        public TextView time;
        public TextView addr;
        public TextView mood;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            addr=itemView.findViewById(R.id.address);
            mood=itemView.findViewById(R.id.mood);
        }
    }
}

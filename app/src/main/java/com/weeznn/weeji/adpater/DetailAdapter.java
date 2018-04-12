package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.weeji.R;
import com.weeznn.weeji.interfaces.ItemClickListener;
import com.weeznn.weeji.util.db.entry.Diary;
import com.weeznn.weeji.util.db.entry.Meeting;
import com.weeznn.weeji.util.db.entry.Note;

import java.util.List;

/**
 * Created by weeznn on 2018/4/7.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder>
                            implements Constant{

    private static final String TAG=DetailAdapter.class.getSimpleName();
    private List<Object> data;
    private LayoutInflater inflater;
    private int type;
    private ItemClickListener listener;

    public DetailAdapter(Context context,int type,List<Object> list){
        Log.i(TAG,"DetailAdapter  data size: "+list.size()+"    type："+type);
        this.data=list;
        this.type=type;
        this.inflater=LayoutInflater.from(context);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_detail,parent,false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, final int position) {
        switch (type){
            case CODE_DAI:
                holder.textView.setText(((Diary)(data.get(position))).getDate());
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(position);
                    }
                });
            case CODE_NOT:
                holder.textView.setText(((Note)(data.get(position))).getSource());
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(position);
                    }
                });
            case CODE_MRT:
                holder.textView.setText(((Meeting)(data.get(position))).getTitle());
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(position);
                    }
                });
            break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ConstraintLayout layout;
        public DetailViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
            layout=itemView.findViewById(R.id.container);
        }
    }

}

package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.weeznn.weeji.R;
import com.weeznn.weeji.interfaces.ItemClickListener;

import java.util.List;

/**
 * Created by weeznn on 2018/4/14.
 */

public class PopupItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG=PopupItemAdapter.class.getSimpleName();
    
    public enum TYPE{
        RecyclerV,
        RecyclerH
    }
    
    private TYPE type;
     
    
    private List<String> data;
    private LayoutInflater inflater;
    private ItemClickListener listener;

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }
    public PopupItemAdapter(List<String> list, Context context, TYPE type){
        Log.i(TAG,"Create data size :"+ list.size());
        this.data=list;
        this.type=type;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==1){
            View view=inflater.inflate(R.layout.view_holder_keyword,parent,false);
            return new KeyWordViewHolder(view);
        }else {
            View view=inflater.inflate(R.layout.view_holder_imter_point,parent,false);
            return new InterPointViewHolder(view);
        }
      
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(TAG,"onBindViewHolder");
        if (holder instanceof KeyWordViewHolder){
            ((KeyWordViewHolder) holder).textView.setText(data.get(position));
            ((KeyWordViewHolder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
            ((KeyWordViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  listener.onItemClick(position);
                }
            });
        }
        if (holder instanceof InterPointViewHolder){
            ((InterPointViewHolder) holder).textView.setText(data.get(position));
            ((InterPointViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   listener.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG,"getItemViewType");
        if (type== TYPE.RecyclerH){
            return 1;
        }else {
            return 2;
        }
    }

    public class KeyWordViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public CheckBox checkBox;
        public CardView layout;
        public KeyWordViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);
            checkBox =itemView.findViewById(R.id.checkbox);
            layout=itemView.findViewById(R.id.layout);
        }
    }

    public class InterPointViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ConstraintLayout layout;
        public InterPointViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);
            layout=itemView.findViewById(R.id.layout);
        }
    }
}

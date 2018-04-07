package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weeznn.weeji.R;
import com.weeznn.weeji.util.db.entry.Meeting;

import java.util.List;

/**
 * Created by weeznn on 2018/4/7.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private static final String TAG=DetailAdapter.class.getSimpleName();
    private List<Object> data;
    private LayoutInflater inflater;
    private int type;
    private UpdateFragmentDetail listener;

    public DetailAdapter(Context context,int type,List<Object> list){
        Log.i(TAG,"DetailAdapter  data size: "+list.size());
        this.data=list;
        this.type=type;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_detail,parent,false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, final int position) {
        switch (type){
            case R.integer.CODE_DAI:
            case R.integer.CODE_NOT:
            case R.integer.CODE_MRT:
                holder.textView.setText(((Meeting)(data.get(position))).getTitle());
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.update(""+((Meeting)data.get(position)).get_metID());
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
        private TextView textView;
        public DetailViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
        }
    }

    /**
     * 通知fragment点击的item 的_id
     */
    public interface UpdateFragmentDetail {
        void update(String code);
    }
}

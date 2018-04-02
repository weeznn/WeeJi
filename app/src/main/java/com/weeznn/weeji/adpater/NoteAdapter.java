package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weeznn.weeji.R;
import com.weeznn.weeji.util.db.entry.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weeznn on 2018/3/22.
 */

public class NoteAdapter extends RecyclerView.Adapter {
    private List<Note> data;
    private LayoutInflater inflater;

    public NoteAdapter(Context context,List<Note> list){
        this.data=list;
        this.inflater=LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.card_note,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Note item= data.get(position);
        NoteViewHolder viewHolder= (NoteViewHolder) holder;
        viewHolder.count.setText((item.get_noteID()+1)+"条");
        viewHolder.subtext.setText(item.getSub());
        viewHolder.title.setText(item.getSource()+"/");

        // TODO: 2018/3/30 去判断是否缓存了图片，如果缓存就从本地读，不缓存就从链接读
        Glide.with(viewHolder.imageView)
                .load(item.getCache())
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private static class NoteViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG=NoteViewHolder.class.getSimpleName();

        private ImageView imageView;
        private TextView subtext;
        private TextView count;
        private TextView title;

        public NoteViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            subtext=itemView.findViewById(R.id.subtext);
            count=itemView.findViewById(R.id.num);
            title=itemView.findViewById(R.id.source);
        }
    }

}

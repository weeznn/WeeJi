package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weeznn.weeji.R;
import com.weeznn.weeji.fragment.NoteFragment;
import com.weeznn.weeji.interfaces.ItemClickListener;
import com.weeznn.weeji.util.db.entry.Note;

import java.util.List;

/**
 * Created by weeznn on 2018/3/22.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> data;
    private LayoutInflater inflater;
    private ItemClickListener listener;
    public NoteAdapter(Context context, List<Note> list) {
        this.data = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, final int position) {
        Note item = data.get(position);
        holder.count.setText((item.get_noteID() + 1) + "条");
        holder.subtext.setText(item.getSub());
        holder.title.setText(item.getSource() + "/");

        // TODO: 2018/3/30 去判断是否缓存了图片，如果缓存就从本地读，不缓存就从链接读
        Glide.with(holder.imageView)
                .load(item.getImage())
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private  final String TAG = NoteViewHolder.class.getSimpleName();

        public ImageView imageView;
        public TextView subtext;
        public TextView count;
        public TextView title;
        private CardView cardView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            subtext = itemView.findViewById(R.id.subtext);
            count = itemView.findViewById(R.id.num);
            title = itemView.findViewById(R.id.source);
            cardView=itemView.findViewById(R.id.card);
        }
    }

}

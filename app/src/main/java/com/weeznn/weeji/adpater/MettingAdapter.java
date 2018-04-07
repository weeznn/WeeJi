package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weeznn.weeji.R;
import com.weeznn.weeji.util.db.entry.Meeting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weeznn on 2018/3/14.
 */

public class MettingAdapter extends RecyclerView.Adapter <MettingAdapter.MeetingViewHolder>{
    private static final String TAG="meetingAdapter";

    private List<Meeting> data=new ArrayList<>();
    private LayoutInflater inflater;

    public MettingAdapter(Context context, List<Meeting> moduls){
        this.data= moduls;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.card_meeting,parent,false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        Meeting meetingModul= data.get(position);
        holder.title.setText(meetingModul.getTitle());
        holder.sub.setText(meetingModul.getSub());
        holder.keyWord3.setText(meetingModul.getKeyword3());
        holder.keyWord2.setText(meetingModul.getKeyword2());
        holder.keyWord1.setText(meetingModul.getKeyword1());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MeetingViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView sub;
        public TextView keyWord1;
        public TextView keyWord2;
        public TextView keyWord3;
        public ConstraintLayout layout;
        public MeetingViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            sub=itemView.findViewById(R.id.subtext);
            keyWord1=itemView.findViewById(R.id.key_word_1);
            keyWord2=itemView.findViewById(R.id.key_word_2);
            keyWord3=itemView.findViewById(R.id.key_word_3);
            layout=itemView.findViewById(R.id.layout);
        }

    }
}

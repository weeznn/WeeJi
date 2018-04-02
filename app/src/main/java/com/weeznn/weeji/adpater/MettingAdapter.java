package com.weeznn.weeji.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class MettingAdapter extends RecyclerView.Adapter {
    private static final String TAG="meetingAdapter";

    private List<Meeting> data=new ArrayList<>();
    private LayoutInflater inflater;

    public MettingAdapter(Context context, List<Meeting> moduls){
        this.data= moduls;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.card_meeting,parent,false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Meeting meetingModul= data.get(position);
        MeetingViewHolder meetingViewHolder= ((MeetingViewHolder)holder);
        meetingViewHolder.title.setText(meetingModul.getTitle());
        meetingViewHolder.sub.setText(meetingModul.getSub());
        meetingViewHolder.keyWord3.setText(meetingModul.getKeyword3());
        meetingViewHolder.keyWord2.setText(meetingModul.getKeyword2());
        meetingViewHolder.keyWord1.setText(meetingModul.getKeyword1());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private static final class MeetingViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView sub;
        private TextView keyWord1;
        private TextView keyWord2;
        private TextView keyWord3;
        public MeetingViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            sub=itemView.findViewById(R.id.subtext);
            keyWord1=itemView.findViewById(R.id.key_word_1);
            keyWord2=itemView.findViewById(R.id.key_word_2);
            keyWord3=itemView.findViewById(R.id.key_word_3);
        }

    }
}

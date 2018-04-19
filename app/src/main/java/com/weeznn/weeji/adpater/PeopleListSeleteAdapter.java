package com.weeznn.weeji.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.weeznn.weeji.R;
import com.weeznn.weeji.interfaces.ItemClickListener;
import com.weeznn.weeji.util.SimplePeople;
import com.weeznn.weeji.util.db.entry.People;

import org.w3c.dom.ls.LSException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by weeznn on 2018/4/6.
 */

public class PeopleListSeleteAdapter extends RecyclerView.Adapter<PeopleListSeleteAdapter.PeopleSeleteViewHolder> {

    private static final String TAG = "PeopleListSeleteAdapter";

    private List<People> data;
    private List<SimplePeople> simplePeopleList;
    private LayoutInflater inflater;
    PeopleListChangeInterface listener;


    public void setPeopleListChangeListener(PeopleListChangeInterface listener) {
        this.listener = listener;
    }

    public PeopleListSeleteAdapter(Context context, List<People> peopleList, List<SimplePeople> simplePeople) {
        Log.i(TAG,"PeopleListSeleteAdapter  list size:"+peopleList.size()+"  simplePeople size "+simplePeople.size());
        this.data = peopleList;
        this.inflater = LayoutInflater.from(context);
        this.simplePeopleList = simplePeople;
    }

    @Override
    public PeopleSeleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_people_list_select, parent, false);
        return new PeopleSeleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PeopleSeleteViewHolder holder, final int position) {
        final People people = data.get(position);

        RequestOptions options = new RequestOptions();
        options.circleCrop()
                .error(R.drawable.ic_user_black)
                .placeholder(R.drawable.ic_user_black);
        Glide.with(holder.imageView)
                .load(people.getPhoto())
                .apply(options)
                .into(holder.imageView);

        SimplePeople simplePeople=new SimplePeople(people.getName(),people.getPhoto(),people.getJob(),people.getCompany());
        if (simplePeopleList.contains(simplePeople)){
            holder.chrck.setChecked(true);
        }

        holder.chrck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listener.add(position);
                }else {
                    listener.remove(position);
                }
            }
        });


        holder.name.setText(people.getName());
        holder.job.setText(people.getJob());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                CheckBox view1 = v.findViewById(R.id.check);
                if (view1.isChecked()){
                    view1.setChecked(false);
                    listener.remove(position);
                }else {
                    view1.setChecked(true);
                    listener.add(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Selete  ViewHolder
     */
    public class PeopleSeleteViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name;
        public TextView job;
        public ConstraintLayout layout;
        public CheckBox chrck;

        public PeopleSeleteViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            job = itemView.findViewById(R.id.job);
            layout = itemView.findViewById(R.id.layout);
            chrck = itemView.findViewById(R.id.check);
        }
    }

    public interface PeopleListChangeInterface{
        void add(int position);
        void remove(int position);
    }
}

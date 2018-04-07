package com.weeznn.weeji.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CheckableImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.weeznn.weeji.R;
import com.weeznn.weeji.util.SimplePeople;
import com.weeznn.weeji.util.db.entry.People;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by weeznn on 2018/4/6.
 */

public class PeopleListSeleteAdapter extends RecyclerView.Adapter<PeopleListSeleteAdapter.PeopleSeleteViewHolder>{

    private static final String TAG="PeopleListSeleteAdapter";

    private List<People> data=new LinkedList<>();
    private List<SimplePeople> simplePeopleList=new LinkedList<>();
    private LayoutInflater inflater;

    public PeopleListSeleteAdapter(Context context,List<People> peopleList,List<SimplePeople>simplePeople){
        Log.i(TAG,"PeopleListSeleteAdapter  people List Size : "+peopleList.size()+"   simple people list size "+simplePeople.size());
        this.data=peopleList;
        this.inflater=LayoutInflater.from(context);
        this.simplePeopleList=simplePeople;
    }

    @Override
    public PeopleSeleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i(TAG,"onCreateViewHolder");
            View view=inflater.inflate(R.layout.item_people_list_select,parent,false);
            return new PeopleSeleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeopleSeleteViewHolder holder, int position) {
        final People people = data.get(position);

        Log.i(TAG,"bindSeleteViewHolder");
        RequestOptions options=new RequestOptions();
        options.circleCrop()
                .error(R.drawable.ic_user_black)
                .placeholder(R.drawable.ic_user_black);
        Glide.with(holder.imageView)
                .load(people.getPhoto())
                .apply(options)
                .into(holder.imageView);

        holder.chrckBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                CheckableImageButton view1 = v.findViewById(R.id.check);
                cllick(view1.isChecked(),people);
                view1.setChecked(!view1.isChecked());

            }
        });

        holder.name.setText(people.getName());
        holder.job.setText(people.getJob());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                CheckableImageButton view1 = v.findViewById(R.id.check);
               cllick(view1.isChecked(),people);
               view1.setChecked(!view1.isChecked());
            }
        });
    }

    private void cllick(boolean b, People people){
        if (b) {
            Log.i(TAG, "被选中，添加");
            //添加到simpledata中
            simplePeopleList.add(new SimplePeople(people.getName(), people.getPhoto(), people.getJob(), people.getCompany()));
        } else {
            Log.i(TAG, "被删除");
            //删除simpledata中
            simplePeopleList.remove(new SimplePeople(people.getName(), people.getPhoto(), people.getJob(), people.getCompany()));
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG,"getItemCount"+data.size());
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
        public CheckableImageButton chrckBtn;

        public PeopleSeleteViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            job = itemView.findViewById(R.id.job);
            layout = itemView.findViewById(R.id.layout);
            chrckBtn = itemView.findViewById(R.id.check);
        }
    }
}

package com.weeznn.weeji.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weeznn.weeji.R;
import com.weeznn.weeji.util.SimplePeople;
import com.weeznn.weeji.util.db.entry.People;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingPreEditFragment extends Fragment {
    private static final String TAG = MeetingPreEditFragment.class.getSimpleName();
    public static final String TAG_BACK="MeetingPreEdit";
    public static final String FLAG_PEOPLES="peoples";

    //View
    private FloatingActionButton fab;
    private TextInputEditText title;
    private TextInputEditText sub;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    //逻辑
    private List<SimplePeople> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       String json=getArguments().getString(FLAG_PEOPLES);
       list=SimplePeople.getListFromJson(json);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_pre_edit, container, false);
        fab = view.findViewById(R.id.fab);
        title = view.findViewById(R.id.editText_title);
        sub = view.findViewById(R.id.editText_sub);
        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/4/3 添加动画
               FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
               transaction.add(new PeopleListFragment(),PeopleListFragment.TAG_BACK);
               transaction.addToBackStack(MeetingPreEditFragment.TAG_BACK);
               transaction.commit();
            }
        });
        //textview

        //recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(new PreEditAdapter(getContext(),list));
        //toolbar
    }

    private class PreEditAdapter extends RecyclerView.Adapter{

        private List<SimplePeople> data;
        private LayoutInflater inflater;
        public PreEditAdapter(Context context, List<SimplePeople> list){
            this.data=list;
            inflater=LayoutInflater.from(context);
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.item_meeting_people,parent,false);
            return new PreEditViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
          final PreEditViewHolder viewHolder= (PreEditViewHolder) holder;
            SimplePeople people=data.get(position);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.btn.setVisibility(View.VISIBLE);
                }
            });

            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });

            Glide.with(viewHolder.imageView)
                    .load(people.getPhoto())
                    .into(viewHolder.imageView);

            viewHolder.name.setText(people.getName());
        }

        @Override
        public int getItemCount() {
            return data.isEmpty()?0:data.size();
        }
    }

    private class PreEditViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private Button btn;
        private ImageView imageView;
        private CardView cardView;

        public PreEditViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            btn=itemView.findViewById(R.id.btn);
            imageView=itemView.findViewById(R.id.image);
            cardView=itemView.findViewById(R.id.card);
        }
    }

}

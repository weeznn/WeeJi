package com.weeznn.weeji.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.adpater.MettingAdapter;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.entry.Meeting;

import java.util.ArrayList;
import java.util.List;

public class MettingFragment extends Fragment {
    private static final String TAG=MettingFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private List<Meeting> data=new ArrayList<>();
    private MettingAdapter mettingAdapter;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
           if (msg.what==1){
               refreshLayout.setRefreshing(false);
           }
           return true;
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"oncreat");
        mettingAdapter=new MettingAdapter(getContext(),data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"oncreatView");
        View view=inflater.inflate(R.layout.fragment_metting, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.freshLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewcreat");
        //freshlayout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyApplication.getInstant().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        MeetingDao meetingDao=MyApplication.getInstant().getMeetingDao();
                        data=meetingDao.queryBuilder()
                                .limit(10)
                                .list();
                        Log.i(TAG,"onRefresh data size ="+data.size());

                        mettingAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                        Log.i(TAG,"refreshLayout is refreshing "+refreshLayout.isRefreshing());
                    }
                });
            }
        });


        //recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mettingAdapter);
    }



}

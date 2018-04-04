package com.weeznn.weeji.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.adpater.MettingAdapter;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.entry.Meeting;

import java.util.ArrayList;
import java.util.List;

public class MettingFragment extends Fragment {
    private static final String TAG=MettingFragment.class.getSimpleName();
    public static final String TAG_BACK="Metting";

    //View
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ActionBar actionBar;

    //逻辑
    private List<Meeting> data=new ArrayList<>();
    private MettingAdapter mettingAdapter;
    private Fragment fragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreat");
        mettingAdapter=new MettingAdapter(getContext(),data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"oncreatView");
        View view=inflater.inflate(R.layout.fragment_metting, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.freshLayout);
        fab=view.findViewById(R.id.fab);
        toolbar=view.findViewById(R.id.toolbar);
        toolbarTitle=view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewcreat");
        //freshlayout
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
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

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment meetingPreEditfragment=new MeetingPreEditFragment();
                Bundle bundle=new Bundle();
                String self=getActivity().getSharedPreferences(getString(R.string.SharedPreferences_name),0)
                        .getString(getString(R.string.pref_sim_self_json),getString(R.string.pref_self_def_sim_json));
                bundle.putString(MeetingPreEditFragment.FLAG_PEOPLES,self);
                meetingPreEditfragment.setArguments(bundle);

                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.fragment_enter_from_bottom,R.animator.fragment_exit_to_bottom);
                transaction.add(R.id.frameLayout,meetingPreEditfragment,MeetingPreEditFragment.TAG_BACK);
                transaction.addToBackStack(MettingFragment.TAG_BACK);
                //transaction.hide(fragment);
                transaction.commit();
            }
        });

        //toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbarTitle.setText(R.string.nav_skill_meeting);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWright));

    }



}

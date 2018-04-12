package com.weeznn.weeji.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.DetailActivity;
import com.weeznn.weeji.adpater.MettingAdapter;
import com.weeznn.weeji.interfaces.ItemClickListener;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.entry.Meeting;
import com.weeznn.weeji.util.db.entry.People;

import java.util.ArrayList;
import java.util.List;

public class MettingFragment extends Fragment
        implements
        Constant,
        ItemClickListener {
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
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            refreshLayout.setRefreshing(false);
            return true;
        }
    });


    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreat");
        hasOptionsMenu();
        fragment=this;

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
                updata();
                refreshLayout.setRefreshing(false);
            }
        });

        //recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mettingAdapter=new MettingAdapter(getContext(),data);
        mettingAdapter.setItemClickListener(this);
        recyclerView.setAdapter(mettingAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));



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
                transaction.addToBackStack(MettingFragment.TAG_BACK);
                transaction.hide(fragment);

                transaction.setCustomAnimations(R.animator.fragment_enter_from_bottom,R.animator.fragment_exit_to_left);
                transaction.add(R.id.frameLayout,meetingPreEditfragment,MeetingPreEditFragment.TAG_BACK);
                transaction.commit();
            }
        });

        //toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbarTitle.setText(R.string.nav_skill_meeting);

    }

    private void updata(){
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                MeetingDao meetingDao=MyApplication.getInstant().getMeetingDao();
                List<Meeting>list=meetingDao.queryBuilder()
                        .limit(10)
                        .list();
                Log.i(TAG,"DB query size:"+list.size());
                data.addAll(list);
                Log.i(TAG,"onRefresh data size ="+data.size());
                mettingAdapter.notifyDataSetChanged();
                handler.sendMessage(new Message());
                Log.i(TAG,"refreshLayout is refreshing "+refreshLayout.isRefreshing());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //第一次添加数据
        refreshLayout.setRefreshing(true);
        while (refreshLayout.isRefreshing()){
            updata();
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    /**
     * 实现该接口，当item 被点击时跳转至DetailActivity；
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(getString(R.string.LEFT_TYPE),CODE_MRT);
        intent.putExtra(getString(R.string.LEFT_CODE),data.get(position).get_metID());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {

    }
}

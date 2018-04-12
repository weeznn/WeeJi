package com.weeznn.weeji.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.DetailActivity;
import com.weeznn.weeji.adpater.DiaryAdapter;
import com.weeznn.weeji.adpater.MettingAdapter;
import com.weeznn.weeji.interfaces.ItemClickListener;
import com.weeznn.weeji.util.db.DiaryDao;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.entry.Diary;
import com.weeznn.weeji.util.db.entry.Meeting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weeznn on 2018/4/2.
 */

public class DiaryFragment extends Fragment implements
        Constant,
        ItemClickListener{
    private static final String TAG= DiaryFragment.class.getSimpleName();


    //view
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    //逻辑
    private List<Diary> data=new ArrayList<>();
    private DiaryAdapter adapter;
    private Fragment fragment;

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasOptionsMenu();
        fragment=this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_metting, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.freshLayout);
        toolbar=view.findViewById(R.id.toolbar);
        fab=view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //freshlayout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               updata();
            }
        });


        //recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new DiaryAdapter(getContext(),data);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        //toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/4/4 添加
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

    private void updata(){
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                DiaryDao diaryDao=MyApplication.getInstant().getDiaryDao();
                List<Diary> result=diaryDao.queryBuilder()
                        .limit(10)
                        .list();
                data.addAll(result);
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(getString(R.string.LEFT_TYPE),CODE_DAI);
        intent.putExtra(getString(R.string.LEFT_CODE),data.get(position).get_DAIID());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {

    }
}

package com.weeznn.weeji.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.weeznn.weeji.R;
import com.weeznn.weeji.adpater.MyAdapter;

import java.util.Locale;

public class ListFragment extends Fragment {
    private static final String TAG="ListFragment";

    //View

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;
    private MyAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;


    //逻辑相关
    private String toolbarTitle;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreat");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.refreshLayout);

        //开始新的
        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getActivity().getPackageManager()
                        .getLaunchIntentForPackage("com.weeznn.baidu_speech");
               if (intent!=null){
                   DialogFragment fragment=new DialogFragment();
                   fragment.show(getActivity().getSupportFragmentManager(),"dialog");
               }else {
                   Toast.makeText(getActivity(),"未安装百度语音服务！",Toast.LENGTH_SHORT);
               }
            }
        });
        Log.i(TAG,"onCreatView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewCreate");

        recyclerView.setAdapter(myAdapter);
        if (layoutManager==null){
            layoutManager=new LinearLayoutManager(getActivity());
        }
        recyclerView.setLayoutManager(layoutManager);

    }

    public void update(@NonNull MenuItem item){
        // TODO: 2018/3/14 更改内容
        toolbarTitle=item.getTitle().toString();
    }

    public void setLayoutManagerType(MenuItem item){
        // TODO: 2018/3/14 更改布局方式
    }
}
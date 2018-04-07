package com.weeznn.weeji.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ImageView;

import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.adpater.DetailAdapter;
import com.weeznn.weeji.adpater.DiaryAdapter;
import com.weeznn.weeji.fragment.MeetingDetailFragment;
import com.weeznn.weeji.fragment.MettingFragment;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.entry.Meeting;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements MeetingDetailFragment.OnFragmentInteractionListener{
    private static final String TAG=DetailActivity.class.getSimpleName();

    private String code;
    private int type;

    private Context context;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private List<Object> list=new ArrayList<>();
    private DetailAdapter adapter=new DetailAdapter(context,type,list);

    //view
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        context=this;

        Intent intent=getIntent();
        type=intent.getIntExtra(getResources().getString(R.string.LEFT_TYPE),R.integer.CODE_MRT);
        code=intent.getStringExtra(getResources().getString(R.string.LEFT_CODE));

        Log.i(TAG,"onCreate type:"+type+"   code:"+code);

        fragmentManager=getSupportFragmentManager();

        initdata();
        initView();

    }

    private void initdata() {
        final int[] offset=new int[]{1};
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case R.integer.CODE_DAI:
                    case R.integer.CODE_NOT:
                    case R.integer.CODE_MRT:
                        MeetingDao dao=MyApplication.getInstant().getMeetingDao();
                        List<Meeting> resout=dao.queryBuilder()
                                .limit(15)
                                .offset(offset[0]++)
                                .list();
                        list.addAll(resout);
                        adapter.notifyDataSetChanged();
                        Log.i(TAG,"updata size "+list.size());
                        break;
                }
            }
        });
    }

    private void initView() {
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        recyclerView=findViewById(R.id.recyclerView);
        imageView=findViewById(R.id.imageView);

        switch (type){
            // TODO: 2018/4/7 哈哈哈哈哈哈，现在只有这一个
            case R.integer.CODE_DAI:
            case R.integer.CODE_NOT:
            case R.integer.CODE_MRT:
                fragment= MeetingDetailFragment.newInstance(code);
                imageView.setBackground(getResources().getDrawable(R.drawable.ic_meeting));
                break;
        }
        fragmentManager.beginTransaction().add(R.id.frameLayout,fragment);

        //侧边栏
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}

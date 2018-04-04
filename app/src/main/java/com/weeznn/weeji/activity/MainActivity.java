package com.weeznn.weeji.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weeznn.weeji.R;
import com.weeznn.weeji.fragment.DiaryFragment;
import com.weeznn.weeji.fragment.MettingFragment;
import com.weeznn.weeji.fragment.NoteFragment;
import com.weeznn.weeji.fragment.PeopleDetailFragment;
import com.weeznn.weeji.fragment.StartFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    //View
    private TextView nav_header_text;
    private ImageView nav_header_back;
    private ImageView nav_header_imag;
    private TextView title;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Fragment defFragment;
    private FragmentManager fragmentManager;

    //逻辑相关
    private String[] toolbarTitles;
    //handler 用于计时等待开启动画结束将fragment置换
    private static final int ISANIMATIONSTOP = 1;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == ISANIMATIONSTOP) {
                Log.i(TAG, "开场动画结束");
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MettingFragment(), "meeting")
                        .commit();
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        Log.i(TAG, "onCreat");

        toolbarTitles = getResources().getStringArray(R.array.toolbar_title);
        initView();


        fragmentManager = getSupportFragmentManager();

        if (getSharedPreferences(getString(R.string.SharedPreferences_name), 0)
                .getBoolean("isFirst", true)) {
            fragmentManager.beginTransaction().add(R.id.frameLayout, new StartFragment(), "start").commit();

            getSharedPreferences(getString(R.string.SharedPreferences_name), 0).edit()
                    .putBoolean("isFirst", false).commit();
            //为第一次启动动画计时
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = ISANIMATIONSTOP;
                    handler.sendMessageDelayed(message, 6000);
                }
            }).start();
        } else {
            defFragment = new MettingFragment();
            fragmentManager.beginTransaction().add(R.id.frameLayout, defFragment, "meeting").commit();
        }

    }


    private void initView() {
        //drawLayout
        //侧边栏
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.nav_open, R.string.nav_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //navigationView
        navigationView = findViewById(R.id.nav_view);
//        为header中的view设置背景
//        View view=navigationView.getHeaderView(0);
//        if (view!=null){
//            nav_header_back=view.findViewById(R.id.nav_header_background);
//            nav_header_imag=view.findViewById(R.id.nav_header_image);
//            nav_header_text=view.findViewById(R.id.nav_header_text);
//        }
        navigationView.setNavigationItemSelectedListener(this);

    }


    /**
     * 点击了侧边栏的menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "点击了 " + item.getTitle());
        switch (item.getItemId()) {
            case R.id.nav_seeting:
                //去设置界面
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_collection:
            case R.id.nav_menu_meeting:
                //更新Fragment 的内容
                if (defFragment.getTag() != "meeting") {
                    title.setText(item.getTitle());
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, new MettingFragment(), "meeting").commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_menu_dairy:
                //更新Fragment 的内容
                if (defFragment.getTag() != "dairy") {
                    title.setText(item.getTitle());
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, new DiaryFragment(), "dairy").commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_menu_note:
                //笔记
                if (defFragment.getTag() != "note") {
                    title.setText(item.getTitle());
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, new NoteFragment(), "note").commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_self:
                //自我信息
                Fragment fragment=new PeopleDetailFragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.add(fragment,PeopleDetailFragment.FLAG_BACK);
                transaction.addToBackStack(defFragment.getTag());
                transaction.commit();
                break;
        }
        return true;
    }

    /**
     * 点击返回按钮
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

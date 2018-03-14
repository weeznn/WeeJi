package com.weeznn.weeji.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.weeznn.weeji.R;
import com.weeznn.weeji.fragment.ListFragment;




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG=MainActivity.class.getSimpleName();

    //View
    private TextView nav_header_text;
    private ImageView nav_header_back;
    private ImageView nav_header_imag;
    private Toolbar toolbar;
    private TextView title;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout frameLayout;

    private ListFragment fragment;
    private FragmentManager fragmentManager;

    //逻辑相关
    private String[] toolbarTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        Log.i(TAG,"onCreat");
        toolbarTitles=getResources().getStringArray(R.array.toolbar_title);

        initView();

        fragment=new ListFragment();
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frameLayout,fragment,"frag").commit();

    }


    private void initView() {
        //toolbar
        toolbar=findViewById(R.id.toolbar);
        title=findViewById(R.id.title_text_view);
        setSupportActionBar(toolbar);
        title.setText(toolbarTitles[0]);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragment.setLayoutManagerType(item);
                return true;
            }
        });


        //drawLayout
        drawerLayout=findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,R.string.nav_open,R.string.nav_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //navigationView
        navigationView=findViewById(R.id.nav_view);
//        为header中的view设置背景
//        View view=navigationView.getHeaderView(0);
//        if (view!=null){
//            nav_header_back=view.findViewById(R.id.nav_header_background);
//            nav_header_imag=view.findViewById(R.id.nav_header_image);
//            nav_header_text=view.findViewById(R.id.nav_header_text);
//        }
        navigationView.setNavigationItemSelectedListener(this);

        //framelayout
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }


    /**
     * 点击了侧边栏的menu
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i(TAG,"点击了 "+item.getTitle());
        if (item.getItemId()==R.id.nav_seeting){
            //去设置界面
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }else {
            //更新listFragment 的内容
            drawerLayout.closeDrawer(GravityCompat.START);
            title.setText(item.getTitle());
            fragment.update(item);
        }
        return true;
    }

    /**
     * 点击返回按钮
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}

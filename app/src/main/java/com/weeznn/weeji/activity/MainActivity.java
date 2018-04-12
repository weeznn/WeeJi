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
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.fragment.DiaryFragment;
import com.weeznn.weeji.fragment.MettingFragment;
import com.weeznn.weeji.fragment.NoteFragment;
import com.weeznn.weeji.fragment.PeopleDetailFragment;
import com.weeznn.weeji.fragment.StartFragment;
import com.weeznn.weeji.util.db.DiaryDao;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.NoteDao;
import com.weeznn.weeji.util.db.entry.Diary;
import com.weeznn.weeji.util.db.entry.Meeting;
import com.weeznn.weeji.util.db.entry.Note;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        Constant{
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


    //handler 用于计时等待开启动画结束将fragment置换
    private static final int ISANIMATIONSTOP = 1;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == ISANIMATIONSTOP) {
                Log.i(TAG, "开场动画结束");
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MettingFragment(), MettingFragment.TAG_BACK)
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
            fragmentManager.beginTransaction().add(R.id.frameLayout, defFragment, MettingFragment.TAG_BACK).commit();
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

//        nav_header_text=findViewById(R.id.nav_header_text);
//        nav_header_text.setText(getSharedPreferences(getString(R.string.SharedPreferences_name),0).getString(getString(R.string.pref_self_name),"你").toCharArray()[0]);

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
                Fragment fragment = new PeopleDetailFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout,fragment, PeopleDetailFragment.FLAG_BACK);
                transaction.addToBackStack(defFragment.getTag());
                transaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
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
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"OnActivityResult  requestCode :"+requestCode+"   resultCode:"+resultCode);
        if (data!=null){
            final Bundle bundle = data.getBundleExtra("result");
            if (null!=bundle){
                switch (requestCode) {
                    case REQUEST_CODE_MET:
                        if (resultCode == RESOULT_CODE_DOWN) {
                            MyApplication.getInstant().runInTx(new Runnable() {
                                @Override
                                public void run() {
                                    MeetingDao dao = MyApplication.getInstant().getMeetingDao();
                                    dao.insert(
                                            new Meeting(
                                                    Long.decode(bundle.getString(getResources().getString(R.string.TABLE_MET_metID),"未命名".hashCode()+"")),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_time),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_title),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_sub),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_keyword1),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_keyword2),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_keyword3),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_address),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_MET_modetator),"")
                                            ));
                                }
                            });
                            Log.i(TAG,"meeting insert");
                        }
                        break;
                    case REQUEST_CODE_DIA:
                        if (resultCode == RESOULT_CODE_DOWN) {
                            MyApplication.getInstant().runInTx(new Runnable() {
                                @Override
                                public void run() {
                                    DiaryDao dao = MyApplication.getInstant().getDiaryDao();
                                    String date=bundle.getString(getResources().getString(R.string.TABLE_DAI_date),"");
                                    String addr=bundle.getString(getResources().getString(R.string.TABLE_DAI_address),"");
                                    dao.insert(
                                            new Diary(
                                                    (date+addr).hashCode(),
                                                    date,
                                                    addr,
                                                    bundle.getInt(getResources().getString(R.string.TABLE_DAI_mood),0),
                                                    bundle.getString(getString(R.string.TABLE_DAI_image),"")
                                            ));
                                }
                            });
                            Log.i(TAG,"dairy insert");
                        }
                        break;
                    case REQUEST_CODE_NOT:
                        if (resultCode == RESOULT_CODE_DOWN) {
                            MyApplication.getInstant().runInTx(new Runnable() {
                                @Override
                                public void run() {
                                    NoteDao dao = MyApplication.getInstant().getNoteDao();
                                    dao.insert(
                                            new Note(
                                                    Long.decode(bundle.getString(getResources().getString(R.string.TABLE_NOT_noteID),"未命名".hashCode()+"")),
                                                    bundle.getString(getResources().getString(R.string.TABLE_NOT_time),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_NOT_cache),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_NOT_sub),""),
                                                    bundle.getString(getResources().getString(R.string.TABLE_NOT_source),"")
                                            ));
                                }
                            });
                            Log.i(TAG,"note insert");
                        }
                        break;
                }
            }
        }

        Fragment fragment=getSupportFragmentManager().findFragmentByTag(MettingFragment.TAG_BACK);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().show(fragment).commit();

    }
}

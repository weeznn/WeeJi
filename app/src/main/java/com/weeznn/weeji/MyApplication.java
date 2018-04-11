package com.weeznn.weeji;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.weeznn.weeji.util.db.DaoMaster;
import com.weeznn.weeji.util.db.DaoSession;

/**
 * Created by weeznn on 2018/3/30.
 */

public class MyApplication extends Application {
    private static final String TAG=MyApplication.class.getSimpleName();

    private static DaoSession daoSession;
    private static Context context;
    private static MyApplication myApplication;
    //设备信息  屏幕
    public static  float scale;
    public static  float scaleDensity;



    public static MyApplication getApplication() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        myApplication=this;
        Log.i(TAG,"oncreat");
        scale=getContext().getResources().getDisplayMetrics().density;
        scaleDensity=getContext().getResources().getDisplayMetrics().scaledDensity;
        //配置数据库
        setupDataBase();
    }

    private  void setupDataBase() {
        Log.i(TAG,"setupDataBase");
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"WeeJi.db");
        SQLiteDatabase db=helper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();
    }

    public static DaoSession getInstant(){
        return daoSession;
    }

    public static Context getContext(){return context;}
}

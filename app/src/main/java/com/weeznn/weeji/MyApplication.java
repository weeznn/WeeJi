package com.weeznn.weeji;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.weeznn.weeji.util.db.DaoMaster;
import com.weeznn.weeji.util.db.DaoSession;

/**
 * Created by weeznn on 2018/3/30.
 */

public class MyApplication extends Application {

    private static final String TAG=MyApplication.class.getSimpleName();
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        //配置数据库
        setupDataBase();
    }

    private void setupDataBase() {
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,getString(R.string.app_name));
        SQLiteDatabase db=helper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();
    }

    public static DaoSession getInstant(){
        return daoSession;
    }
}

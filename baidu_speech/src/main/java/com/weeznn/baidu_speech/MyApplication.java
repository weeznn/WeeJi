package com.weeznn.baidu_speech;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by weeznn on 2018/3/9.
 */

public class MyApplication extends Application {
    private static final String TAG="MyApplication";
    private static final String APPSHAREDPREFERENCENAME="baidu_appech";
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    /**
     * ApplicationContext;
     * @return
     */
    public static Context getContext() {
        return context;
    }
}

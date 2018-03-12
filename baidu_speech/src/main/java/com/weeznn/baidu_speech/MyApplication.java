package com.weeznn.baidu_speech;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by weeznn on 2018/3/9.
 */

public class MyApplication extends Application {
    private static final String TAG="MyApplication";
    //设备信息  屏幕
    public static  float scale;
    public static  float scaleDensity;

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        scale=getContext().getResources().getDisplayMetrics().density;
        scaleDensity=getContext().getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * ApplicationContext;
     * @return
     */
    public static Context getContext() {
        return context;
    }
}

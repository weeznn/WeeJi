package com.weeznn.mylibrary.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by weeznn on 2018/4/2.
 */

public class DataUtil {
    public static final String TAG=DataUtil.class.getSimpleName();

    public static String[] formatstr;
    public static String[] datas;
    public static String[] times;

    public DataUtil(){
        java.util.Date date=new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat format=new SimpleDateFormat();
        String  string= format.format(date);
        Log.i(TAG,"时间格式化的结果为："+string);

        formatstr=string.split(" ");
        datas=formatstr[0].split("/");
    }

    public static String getDate() {
        StringBuilder data=new StringBuilder();
        for (int i=0;i<data.length();i++){
            data.append(datas[i]);
        }

        Log.i(TAG,"返回的日期值为："+data.toString());
        return data.toString();
    }
}

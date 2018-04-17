package com.weeznn.mylibrary.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by weeznn on 2018/4/2.
 */

public class DataUtil {
    public static final String TAG = DataUtil.class.getSimpleName();
    public static String string;

    public DataUtil() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        string = format.format(new Date());
        Log.i(TAG, "时间格式化的结果为：" + string);
    }

    public static String getString() {
        return string;
    }

}

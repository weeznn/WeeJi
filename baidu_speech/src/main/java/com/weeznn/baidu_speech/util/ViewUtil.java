package com.weeznn.baidu_speech.util;


import android.app.Application;

import com.weeznn.baidu_speech.MyApplication;

import javax.xml.transform.sax.SAXTransformerFactory;

/**
 * Created by weeznn on 2018/3/11.
 */

public class ViewUtil {
    private static  final float scaledDensity= MyApplication.scaleDensity;
    private static final float scale=MyApplication.scale;

    public static final int CHINESE=0;
    public static final int NUMBER_OR_CHARACTER=1;

    /**
     * dp->px
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue){
        return (int) (dipValue*scale+0.5f);
    }

    /**
     * px->dip
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue){
        return (int)(pxValue/scale+0.5f);
    }

    public static float sp2px(float spValue,int type){
        switch (type){
            case CHINESE:
                return spValue*scaledDensity;
            case NUMBER_OR_CHARACTER:
                return spValue*scaledDensity*10.0f/18.0f;
                default:
                    return spValue*scaledDensity;
        }
    }
}

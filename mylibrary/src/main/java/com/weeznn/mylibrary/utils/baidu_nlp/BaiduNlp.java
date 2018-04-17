package com.weeznn.mylibrary.utils.baidu_nlp;

import android.util.Log;

import com.baidu.aip.nlp.AipNlp;
import com.weeznn.mylibrary.utils.baidu_nlp.res_entry.Lexer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by weeznn on 2018/4/13.
 */

public class BaiduNlp {
    private static final String TAG=BaiduNlp.class.getSimpleName();


    public static final String APP_ID = "10910164";
    public static final String API_KEY = "l3zcIgcZ2220emGmtyHbseTG";
    public static final String SECRET_KEY = "0mzIOLDsds4Gz60a1Pqboe5GLIhbHZ10";

    private static AipNlp client;

    public BaiduNlp(){
        getInstance();
    }
    public static AipNlp getInstance(){
        if (client==null){
            synchronized (BaiduNlp.class){
                if (client==null){
                    client=new AipNlp(APP_ID,API_KEY,SECRET_KEY);
                    client.setConnectionTimeoutInMillis(2000);
                    client.setSocketTimeoutInMillis(6000);
                }
            }
        }
        return client;
    }

    /**
     * 对句子进行分词处理，
     * @param text
     * @return
     */
    public static List<String> lexer(String text){
        Log.i(TAG,"词法分析:  text:"+text);
        List<String> proWordList=new ArrayList<>();

        HashMap<String,Object>options=new HashMap<>();
        JSONObject res=BaiduNlp.getInstance().lexer(text,options);
        Log.i(TAG,"词法分析结果："+res.toString());

        try {
            JSONArray jsonArray=res.getJSONObject("results").getJSONArray("items");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                if (object.getString("uri")!=null){
                    //命名实体
                   proWordList.add(object.getString("item")+"|"+object.getString("uri"));
                }
                if (object.getString("ne")!=null){
                    //专名
                    proWordList.add(object.getString("item")+"#"+object.getString("ne"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return proWordList;
    }


    public static void depParser(String text){
        Log.i(TAG,"依存句法分析  text："+text);
        HashMap<String,Object> options=new HashMap<>();
        options.put("mode",1);
        JSONObject res=BaiduNlp.getInstance().depParser(text,options);

        Log.i(TAG,"依存句法分析结果: "+res.toString());
        // TODO: 2018/4/13 口语转书面 去查paper
    }


    public static List<String> keyWord(String title,String content){
        Log.i(TAG,"keyWord");
        HashMap<String,Object> options=new HashMap<>();

        List<String> list=new ArrayList<>();
        JSONObject res=BaiduNlp.getInstance().keyword(title,content,options);
        Log.i(TAG,"keyWord res "+res.toString());
        try {
            int count=res.getJSONArray("items").length();
            for (int i=0;i<(count<5?count:5);i++){
                list.add(res.getJSONArray("items").getJSONObject(i).getString("tag"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"key word "+list.toString());
       return list;
    }


    public static List<String> topic(String title,String txt){
        HashMap<String,Object> options=new HashMap<>();
        List<String>list=new ArrayList<>();
        JSONObject res=getInstance().topic(title,txt,options);
        try {
            list.add(res.getJSONObject("lv1_tag_list").getString("tag"));
            JSONArray jsonArray=res.getJSONArray( "lv2_tag_list");
            for (int i=0;i<jsonArray.length();i++){
                list.add(jsonArray.getJSONObject(i).getString("tag"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}

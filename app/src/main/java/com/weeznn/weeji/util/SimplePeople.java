package com.weeznn.weeji.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by weeznn on 2018/4/3.
 */

public class SimplePeople {
    private static final String TAG= SimplePeople.class.getSimpleName();
    private String name;
    private String photo;
    private static List<SimplePeople> list=new LinkedList<>();
    public SimplePeople(String name,String photo){
        this.name=name;
        this.photo=photo;
    }

    public static List<SimplePeople> getListFromJson(String json){
        if (json!=null&&!"".equals(json)) {
            try {
                JSONObject object = new JSONObject(json);
                JSONArray peoples = (JSONArray) object.get("people");
                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject peo = peoples.getJSONObject(i);
                    SimplePeople people=new SimplePeople(peo.getString("name"),peo.getString("photo"));
                    list.add(people);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            list=null;
        }
        if (list!=null){
            Log.i(TAG,"list  size "+list.size());
        }

        return list;
    }

    public static String list2String(List<SimplePeople> list){
        StringBuilder builder=new StringBuilder();
        builder.append("{"+
                            "\"people\":[");
        if (list!=null){
            for (SimplePeople people:list){
                builder.append("{"+
                        "\"name\":\""+people.name+"\","+
                        "\"photo\":\""+people.photo+"\""+
                        "}");
                if (!(people==list.get(list.size()-1))){
                    builder.append(",");
                }
            }
        }
        builder.append("]}");
        Log.i(TAG,builder.toString());
        return builder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

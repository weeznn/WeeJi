package com.weeznn.weeji.util;

import com.weeznn.mylibrary.utils.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by weeznn on 2018/4/13.
 */

public class NoteTxt {
    public static String JSON_ENTY_NOTE = "note";
    public static String JSON_ENTY_DATE = "date";
    public static String JSON_ENTY_TXT = "txt";
    public static String JSON_ENTY_IDEA = "idea";


    public String txt;
    public String idea;
    public String date;

    public NoteTxt(String txt, String idea) {
        this.txt = txt;
        this.idea = idea;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        this.date = dateFormat.format(new Date());
    }

    public NoteTxt() {
    }

    public static String object2json(NoteTxt noteTxt) {
        String json = "{" +
                        "\"note\": {" +
                                "\"date\": \"" + noteTxt.date + "\"," +
                                "\"txt\": \"" + noteTxt.txt + "\"," +
                                "\"idea\": \"" + noteTxt.idea + "\"" +
                                "}" +
                        "}";
        return json;
    }

    public static NoteTxt json2object(String json){
        NoteTxt noteTxt=new NoteTxt();
        try {
            JSONObject jsonObject=new JSONObject(json);
            noteTxt.date=jsonObject.getString(NoteTxt.JSON_ENTY_DATE);
            noteTxt.idea=jsonObject.getString(NoteTxt.JSON_ENTY_IDEA);
            noteTxt.txt=jsonObject.getString(NoteTxt.JSON_ENTY_TXT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noteTxt;
    }

    public static List<NoteTxt> read(File path){
        List<NoteTxt> noteTxtList=new LinkedList<>();
        if (path.exists()&&path.isDirectory()){
            String[] list=path.list();
            for (int i=0;i<list.length;i++){
                String s= FileUtil.ReadText(list[i]);
                noteTxtList.add(NoteTxt.json2object(s));
            }
            return noteTxtList;
        }else {
            return null;
        }
    }
}

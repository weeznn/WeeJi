package com.weeznn.weeji.util;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by weeznn on 2018/3/30.
 */

public class FileUtil {
    private static final String TAG=FileUtil.class.getSimpleName();

    private static final String APPBASEPATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/WeeJi/";
    private static final String FILE_TYPE_MEETING="MEETING";
    private static final String FILE_TYPE_DAIRY="DAIRY";
    private static final String FILE_TYPE_NOTE="NOTE";

    public static boolean MakeDir(String type,String fileName){
        String path=APPBASEPATH+type+"/"+fileName;
        File file=new File(path);
        if (!file.exists()){
            return file.mkdir();
        }else {
            return true;
        }
    }


    public static String ReadText(String type,String fileName){
        String path=APPBASEPATH+type+"/"+fileName+"/"+fileName+".txt";
        File file=new File(path);
        StringBuilder builder=new StringBuilder();
        try {
            InputStream inputStream=new FileInputStream(file);
            BufferedInputStream stream=new BufferedInputStream(inputStream);
            int readcound;
            byte[] buffer=new byte[1024];
            while ((readcound=stream.read(buffer))!=-1){
                builder.append(buffer);
            }
            stream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void WriteText(String type,String fileName,String data){
        String path=APPBASEPATH+type+"/"+fileName+"/"+fileName+".txt";
        try {
            FileWriter fileWriter=new FileWriter(path);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

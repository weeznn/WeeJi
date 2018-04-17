package com.weeznn.mylibrary.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by weeznn on 2018/4/2.
 */

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static final String APPBASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WeeJi/";
    public static final String FILE_TYPE_MEETING = "MEETING";
    public static final String FILE_TYPE_DAIRY = "DAIRY";
    public static final String FILE_TYPE_NOTE = "NOTE";
    public static final int FILE_TYPE_JSON = 1;
    public static final int FILE_TYPE_TEXT = 2;
    public static final int FILE_TYPE_HEAD = 3;
    public static final int FILE_TYPE_IMAGE = 4;
    public static final String TYPE_JSON = "_JSON.txt";
    public static final String TYPE_TEXT = ".txt";
    public static final String TYPE_PCM = "_out_file.pcm";
    public static final String TYPE_IDEA = "_idea.txt";
    public static final String TYPE_MD = ".md";
    public static final String SAVE_IMAGE=APPBASEPATH+"image/";
    public static final String HEAD_PATH=APPBASEPATH+"head/";


    /**
     * 创建文件夹
     * @param dirPath
     * @return
     */
    public static boolean makeDir(String dirPath) {
        Log.i(TAG,"makeDir :"+dirPath);
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }
    public static boolean makeDir(String type,String name) {
        name=name.replace(" ","");
        String path = APPBASEPATH + type + "/"+ name;
        return makeDir(path);
    }

    public static String getPath(String type,String name,String fileType){
        name=name.replace(" ","");
        StringBuilder builder=new StringBuilder();
        builder.append(APPBASEPATH);
        builder.append(type+"/"+name+"/"+name+fileType);
        return builder.toString();
    }

    /**
     * 删除文件
     * @param type
     * @param name
     */
    public static void deleteFile(String type,String name){
        name=name.replace(" ","");
        String path=APPBASEPATH+type+"/"+name;
        Log.i(TAG,"deleteFile :"+path);
        File file=new File(path);
        if (file.exists()){
            file.delete();
        }
    }


    public static String getContentFromAssetsFile(AssetManager assets, String source) {
        InputStream is = null;
        FileOutputStream fos = null;
        String result = "";
        try {
            is = assets.open(source);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从Assets中复制文件
     *
     * @param assets
     * @param source
     * @return
     */
    public static boolean copyFromAssets(AssetManager assets, String source, String dest,
                                         boolean isCover) throws IOException {
        File file = new File(dest);
        boolean isCopyed = false;
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
                isCopyed = true;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }

        }
        return isCopyed;
    }

    /**
     * 读文本
     *
     * @param type
     * @param fileName
     * @return
     */
    public static String ReadText(String type, String fileName) {
        fileName=fileName.replace(" ","");
        String path = APPBASEPATH + type + "/" + fileName+"/" + fileName + ".txt";
        Log.i(TAG,"read text :"+path);
        return ReadText(path);
    }

    /**
     * 读文本
     * @return
     */
    public static String ReadText(String path) {
        File file = new File(path);
        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line="";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            inputStreamReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"read text resoule:"+builder.toString());
        return builder.toString();
    }

    /**
     * 写文本
     *
     * @param type
     * @param fileName
     * @param data
     */
    public static void WriteText(String type, String fileName,String filetype ,String data) {
        fileName=fileName.replace(" ","");
        String dirpath=APPBASEPATH + type + "/" +
                fileName;
        File filedir=new File(dirpath);
        if (!filedir.exists()){
            filedir.mkdir();
        }

        String path = dirpath + "/"+fileName+filetype;
        File file=new File(path);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG,"write text :"+path+" data :"+data);

        if (data==null){
            data="";
        }
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 写入音频PCM文件
     *
     * @param type  数据类型（会议/日记/笔记）
     * @param fileName  文件名
     * @param data  数据
     * @param length    数据长度
     * @param offset    追加的位置
     * @throws FileNotFoundException
     */
    public static void WriteAudio(String type, String fileName, byte[] data, int length, int offset) throws FileNotFoundException {
        fileName=fileName.replace(" ","" );
        String dirpath=APPBASEPATH + type + "/" +
               fileName;

        String path= dirpath+ "/" +
                fileName + ".pcm";
        //Log.i(TAG,"write  audio :"+path);
        File filedir=new File(dirpath);
        if (!filedir.exists()){
            filedir.mkdir();
        }

        File file = new File(path);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream stream = new FileOutputStream(file, true);
        try {
            stream.write(data, offset, length);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取音频文件
     * @param path  文件路径
     * @param offset    读取开始的地方
     * @param buffer    读取的大小
     */
    public static byte[] ReadAudio(String path,long offset,int buffer){
        Log.i(TAG,"read audio :"+path);

        byte[] data=null;
        File file=new File(path);
        if (!file.exists()){
            //文件未找到
            return data;
        }

        try {

            InputStream in=new FileInputStream(file);
            in.read(data,(int) offset,buffer);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static String getOutFile(String type,String name) {
        name=name.replace(" ","");
        //文件名格式 XXXX_XX_XX_未命名会议
        if (!FileUtil.makeDir(type,name)) {
            throw new RuntimeException("创建目录失败");
        }
        File file = new File(APPBASEPATH+type+"/"+name+"/"+name+"_out_file.pcm");

        Log.i(TAG, "创建pcm临时文件 " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public static void copyImage(@NonNull final String src, final String name, final int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path="";
                if (FILE_TYPE_HEAD==type){
                    //头像
                    path=HEAD_PATH;
                }else {
                    //图片
                    path=SAVE_IMAGE;
                }
                File file=new File(path);
                if (!file.exists()){
                    file.mkdir();
                }

                path=path+"/"+name+".jpg";
                File image=new File(path);

                if (!image.exists()){
                    try {
                        image.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                File in=new File(src);
                try {
                    InputStream inputStream=new FileInputStream(in);
                    OutputStream outputStream=new FileOutputStream(image);
                    BitmapFactory.decodeStream(inputStream)
                            .compress(Bitmap.CompressFormat.PNG,30,outputStream);
                    outputStream.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}

package com.weeznn.baidu_speech.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;

import com.baidu.speech.asr.SpeechConstant;
import com.google.gson.Gson;
import com.weeznn.baidu_speech.R;
import com.weeznn.baidu_speech.imp.IStatus;
import com.weeznn.baidu_speech.online.InFileStream;
import com.weeznn.baidu_speech.online.MicrophoneInputStream;
import com.weeznn.baidu_speech.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class Baidu_ASR extends MainActivity implements IStatus {
    private static final String TAG=Baidu_ASR.class.getSimpleName();
    private static final String MEETTING_NAME="meetting_name";
    private static  String path="";
    /**
     * 事件管理器，识别事件管理器只能维持一个。不能使用多个实例
     */
    private static  EventManager eventManager;
    /**
     * 事件监听回调
     */
    private static EventListener listener;

    /**
     * 麦克风流
     */
    private MicrophoneInputStream microphoneInputStream;
    /**
     * 权限申请
     */
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSION_RECORD_AUDIO={Manifest.permission.RECORD_AUDIO};

    private String meettingName=" ";
    private Button start;
    private Button stop;
    private Button cancel;
    private TextView textView;
    //private boolean isVoiceEnd=false;//是否在讲话
    private StringBuilder text=new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"oncreat");
        setContentView(R.layout.baidu_asr);
        //super.initPermition();

        microphoneInputStream= MicrophoneInputStream.getInstance();
       InFileStream.setInputStream(microphoneInputStream);


        // TODO: 2018/3/10  把会议名字放好
        //meettingName=getIntent().getStringExtra(Baidu_ASR.MEETTING_NAME);

        path= Environment.getExternalStorageDirectory().toString() + "/"+AppName+"/meetting/";
        //初始化
        eventManager= EventManagerFactory.create(Baidu_ASR.this,"asr");
        //注册自己的输出事件
        listener=new EventListener() {
            @Override
            public void onEvent(String s, String s1, byte[] bytes, int i, int i1) {
                switch (s){
                    case SpeechConstant.CALLBACK_EVENT_ASR_READY:
                        //引擎准备就绪，可以开始说话
                        Log.i(TAG,Thread.currentThread().getId()+"回调  引擎准备好");
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
                        //识别结果
                        Log.i(TAG,Thread.currentThread().getId()+"回调  识别结果finish  "+s1);

                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_BEGIN:
                        //检测到说话开始
                        Log.i(TAG,Thread.currentThread().getId()+"回调  说话开始"+s1);

                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_END:
                        //检测到说话结束


                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                        //识别结果
                        Log.i(TAG,Thread.currentThread().getId()+"回调  partical "+ s1);


                            try {
                                JSONObject jsonObject=new JSONObject(s1);
                                if (jsonObject.getString("result_type").equals("final_result")){
                                    text.append(jsonObject.getString("best_result"));
                                    textView.setText(text.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_VOLUME:
                        //音量回调
                        Log.i(TAG,Thread.currentThread().getId()+"回调  音量回调 "+ s1);
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_AUDIO:
                        //语音音频数据回调
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_EXIT:
                        //识别结束，资源释放--开始另外一次回调？
                        break;
                }
            }
        };
        eventManager.registerListener(listener);

        //以下为界面
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();

        eventManager.unregisterListener(listener);
        try {
            microphoneInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        start=findViewById(R.id.start);
        stop=findViewById(R.id.stop);
        cancel=findViewById(R.id.cancel);
        textView=findViewById(R.id.text);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"onclick  start");
                if (PackageManager.PERMISSION_GRANTED!=ActivityCompat.checkSelfPermission(Baidu_ASR.this,PERMISSION_RECORD_AUDIO[0])){
                    ActivityCompat.requestPermissions(Baidu_ASR.this,PERMISSION_RECORD_AUDIO,PERMISSION_REQUEST_CODE);
                }
                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String json=asrReady(sp);

                microphoneInputStream.start();
                eventManager.send(SpeechConstant.ASR_START,json,null,0,0);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"onclick  stop");
                eventManager.send(SpeechConstant.ASR_STOP,"{}",null,0,0);
                try {
                    Log.i(TAG,"reset microphone 资源");
                    microphoneInputStream.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"onclick  cancel");
                eventManager.send(SpeechConstant.ASR_CANCEL,"{}",null,0,0);
                try {
                    Log.i(TAG,"close microphone 资源");
                    microphoneInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String asrReady(SharedPreferences sp) {
        JSONObject jsonObject = new JSONObject();
            //语言
            try {
                switch (sp.getInt(LANGEWAGE,5)){
                    case LANGEWAGE_YUEYU:
                        jsonObject.put(SpeechConstant.PID,"1637");
                        break;
                    case LANGEWAGE_SICHUANHUA:
                        jsonObject.put(SpeechConstant.PID,"1837");
                        break;
                    case LANGEWAGE_ENGLISH:
                        jsonObject.put(SpeechConstant.PID,"1737");
                        break;
                    default:
                        jsonObject.put(SpeechConstant.PID,"1537");
                }
                //静音超时断句及长语音
                jsonObject.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT,"0");
                //保存识别过程产生的录音文件
                jsonObject.put(SpeechConstant.OUT_FILE,getOutFile());
                //语音音频数据回调
                jsonObject.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
                //语音音量数据回调
                jsonObject.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
                //输入
                jsonObject.put(SpeechConstant.IN_FILE, "#com.weeznn.baidu_speech.online.InFileStream.create16kStream()");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        Log.i(TAG," ASR START JSON  "+jsonObject.toString());
        return  jsonObject.toString();
    }

    private String getOutFile() {
        //文件名格式 XXXX_XX_XX_未命名会议
        Date date=new Date(System.currentTimeMillis());//获取当前时间
        java.text.SimpleDateFormat format=new java.text.SimpleDateFormat();
        String  string= format.format(date);
        Log.i(TAG,string);
        String[] dates0=string.split(" ");
        String[] dates=dates0[0].split("/");

        StringBuilder builder=new StringBuilder();
        builder.append(dates[0]);
        builder.append("_");
        builder.append(dates[1]);
        builder.append("_");
        builder.append(dates[2]);
        if (meettingName.equals(" ")){
            builder.append("_未命名会议");
        }else {
            builder.append(meettingName);
        }

        int permmition= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permmition!= PackageManager.PERMISSION_GRANTED){
            //没有获取到权限
            Log.i(TAG,"没有获取到向ecternal_storage写权限");
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
        }
        if (!FileUtil.makeDir(path+builder.toString())){
            throw new RuntimeException("创建目录失败："+path+builder.toString());
        }
        File file=new File(path+builder.toString()+"/out_file.pcm");

        Log.i(TAG,"创建目录 "+path+builder.toString());
        return file.getAbsolutePath();
    }


}

//package com.weeznn.baidu_speech.activity;
//
//import android.Manifest;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//
//
//import com.baidu.speech.EventListener;
//import com.baidu.speech.EventManager;
//import com.baidu.speech.EventManagerFactory;
//
//import com.baidu.speech.asr.SpeechConstant;
//import com.weeznn.baidu_speech.R;
//import com.weeznn.baidu_speech.imp.IStatus;
//import com.weeznn.baidu_speech.online.InFileStream;
//import com.weeznn.baidu_speech.online.MicrophoneInputStream;
//import com.weeznn.mylibrary.utils.FileUtil;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//
//
//public class Baidu_ASR extends BaseActivity implements IStatus {
//    private static final String TAG=Baidu_ASR.class.getSimpleName();
//
//    //Voice 的2个状态
//    private static final int VOICE_STATE_REDY=0;
//    private static final int VOICE_STATE_SPEAKING=1;
//    private static final int VOICE_STATE_PAUSE=2;
//    private int voiceState=VOICE_STATE_REDY;
//
//    private static  String path="";
//    /**
//     * 事件管理器，识别事件管理器只能维持一个。不能使用多个实例
//     */
//    private static  EventManager eventManager;
//    /**
//     * 事件监听回调
//     */
//    private static EventListener listener;
//
//    /**
//     * 麦克风流
//     */
//    private MicrophoneInputStream microphoneInputStream;
//    /**
//     * 权限申请
//     */
//    private static final int PERMISSION_REQUEST_CODE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private static String[] PERMISSION_RECORD_AUDIO={Manifest.permission.RECORD_AUDIO};
//
//    private String meettingName=" ";
//    private FloatingActionButton voice;
//    private FloatingActionButton down;
//    private FloatingActionButton cancel;
//    private TextView textView;
//    private Toolbar toolbar;
//
//    private StringBuilder text=new StringBuilder();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i(TAG,"oncreat");
//
//        // TODO: 2018/3/10  把会议名字放好
//        meettingName="会议名字";
//        //meettingName=getIntent().getStringExtra(Baidu_ASR.MEETTING_NAME);
//
//
//        path= Environment.getExternalStorageDirectory().toString() + "/"+AppName+"/meetting/";
//
//        //ASR初始化
//        eventManager= EventManagerFactory.create(Baidu_ASR.this,"asr");
//        //注册自己的输出事件
//        listener=new MyEventListener();
//        eventManager.registerListener(listener);
//
//        //以下为界面
//        initView();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        eventManager.unregisterListener(listener);
//        if (microphoneInputStream!=null){
//            try {
//                microphoneInputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
////    private void initView(){
////        //Toolbar
////        toolbar=findViewById(R.id.toolbar);
////        if (meettingName==null||meettingName.equals(" ")){
////            toolbar.setTitle("未命名的会议");
////        }else {
////            toolbar.setTitle(meettingName);
////        }
////        setSupportActionBar(toolbar);
////        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
////            @Override
////            public boolean onMenuItemClick(MenuItem item) {
////                switch (item.getItemId()){
////                    case R.id.nlu:
////                        // TODO: 2018/3/11 显示语义分析结果
////                        break;
////                }
////                return true;
////            }
////        });
//
////        //FAB Voice
////        MyClickListener myClickListener=new MyClickListener();
////        voice=findViewById(R.id.voice);
////        voice.setOnClickListener(myClickListener);
////        down=findViewById(R.id.down);
////        down.setOnClickListener(myClickListener);
////        cancel=findViewById(R.id.cance);
////        cancel.setOnClickListener(myClickListener);
////        cancel.setVisibility(View.GONE);
////        down.setVisibility(View.GONE);
//
//        //TextView
////        textView=findViewById(R.id.text);
//        //textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//        //textView.setTextSize(getSharedPreferences(AppName,0).getFloat("_text_size", ViewUtil.sp2px(16,ViewUtil.CHINESE)));
//
////    }
//
////    /**
////     * 更新FABView
////     */
////    private void updateFABView() {
////        switch (voiceState){
////            case VOICE_STATE_SPEAKING:
////                //说话中  要求暂停
////                cancel.setVisibility(View.VISIBLE);
////                down.setVisibility(View.VISIBLE);
////                voiceState=VOICE_STATE_PAUSE;
////                microphoneInputStream.pause();
////                break;
////            case VOICE_STATE_PAUSE:
////                //暂停中  要求继续
////                cancel.setVisibility(View.GONE);
////                down.setVisibility(View.GONE);
////                voiceState=VOICE_STATE_SPEAKING;
////                microphoneInputStream.start();
////                break;
////            case VOICE_STATE_REDY:
////                //初始状态
////
////                if (PackageManager.PERMISSION_GRANTED!=ActivityCompat.checkSelfPermission(Baidu_ASR.this,PERMISSION_RECORD_AUDIO[0])){
////                    ActivityCompat.requestPermissions(Baidu_ASR.this,PERMISSION_RECORD_AUDIO,PERMISSION_REQUEST_CODE);
////                }
////                SharedPreferences sp= getSharedPreferences(AppName,0);
////                String json=asrReady(sp);
////                microphoneInputStream= MicrophoneInputStream.getInstance();
////                InFileStream.setInputStream(microphoneInputStream);
////                microphoneInputStream.start();
////                eventManager.send(SpeechConstant.ASR_START,json,null,0,0);
////                voiceState=VOICE_STATE_SPEAKING;
////                break;
////        }
////    }
////
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_baidu_asr,menu);
////        return true;
////    }
//
//    private String asrReady(SharedPreferences sp) {
//        JSONObject jsonObject = new JSONObject();
//            //语言
//            try {
//                switch (sp.getString(LANGEWAGE,LANGEWAGE_PUTONGHUA)){
//                    case LANGEWAGE_YUEYU:
//                        jsonObject.put(SpeechConstant.PID,"1637");
//                        break;
//                    case LANGEWAGE_SICHUANHUA:
//                        jsonObject.put(SpeechConstant.PID,"1837");
//                        break;
//                    case LANGEWAGE_ENGLISH:
//                        jsonObject.put(SpeechConstant.PID,"1737");
//                        break;
//                    default:
//                        jsonObject.put(SpeechConstant.PID,"1537");
//                }
//                //静音超时断句及长语音
//                jsonObject.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT,"0");
//                //保存识别过程产生的录音文件
//                jsonObject.put(SpeechConstant.OUT_FILE,getOutFile());
//                //语音音频数据回调
//                jsonObject.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
//                //语音音量数据回调
//                jsonObject.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
//                //输入
//                jsonObject.put(SpeechConstant.IN_FILE, "#com.weeznn.baidu_speech.online.InFileStream.create16kStream()");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        Log.i(TAG," ASR START JSON  "+jsonObject.toString());
//        return  jsonObject.toString();
//    }
//
////    private String getOutFile() {
////        //文件名格式 XXXX_XX_XX_未命名会议
////        Date date=new Date(System.currentTimeMillis());//获取当前时间
////        java.text.SimpleDateFormat format=new java.text.SimpleDateFormat();
////        String  string= format.format(date);
////        Log.i(TAG,string);
////        String[] dates0=string.split(" ");
////        String[] dates=dates0[0].split("/");
////
////        StringBuilder builder=new StringBuilder();
////        builder.append(dates[0]);
////        builder.append("_");
////        builder.append(dates[1]);
////        builder.append("_");
////        builder.append(dates[2]);
////        if (meettingName.equals(" ")){
////            builder.append("_未命名会议");
////        }else {
////            builder.append("_"+meettingName);
////        }
//
//        int permmition= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permmition!= PackageManager.PERMISSION_GRANTED){
//            //没有获取到权限
//            Log.i(TAG,"没有获取到向ecternal_storage写权限");
//            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
//        }
//        if (!FileUtil.makeDir(path+builder.toString())){
//            throw new RuntimeException("创建目录失败："+path+builder.toString());
//        }
//        File file=new File(path+builder.toString()+"/out_file.pcm");
//
//        Log.i(TAG,"创建目录 "+path+builder.toString());
//        return file.getAbsolutePath();
//    }
//
//    /**
//     * SDK事件回调类
//     */
//    private class MyEventListener implements EventListener {
//        @Override
//        public void onEvent(String s, String s1, byte[] bytes, int i, int i1) {
//            switch (s){
//                case SpeechConstant.CALLBACK_EVENT_ASR_READY:
//                    //引擎准备就绪，可以开始说话
//                    Log.i(TAG,Thread.currentThread().getId()+"回调  引擎准备好");
//
//                    break;
//                case SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
//                    //识别结果
//                    Log.i(TAG,Thread.currentThread().getId()+"回调  识别结果finish  "+s1);
//
//                    break;
//                case SpeechConstant.CALLBACK_EVENT_ASR_BEGIN:
//                    //检测到说话开始
//                    Log.i(TAG,Thread.currentThread().getId()+"回调  说话开始"+s1);
//
//                    break;
//                case SpeechConstant.CALLBACK_EVENT_ASR_END:
//                    //检测到说话结束
//                    Log.i(TAG,Thread.currentThread().getId()+"回调  说话结束"+s1);
//                    break;
//                case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
//                    //识别结果
//                    Log.i(TAG,Thread.currentThread().getId()+"回调  partical "+ s1);
//                    JSONObject jsonObject= null;
//                    try {
//                        jsonObject = new JSONObject(s1);
//                        if (jsonObject.getString("result_type").equals("final_result")) {
//                            text.append(jsonObject.getString("best_result"));
//                            Log.i(TAG,"text :"+text.toString());
//                            textView.setText(text.toString());
//                        }
//                    } catch (JSONException e) {
//                        Log.i(TAG,"new jsonObject ERROR");
//                        e.printStackTrace();
//                    }
//
//                    break;
//                case SpeechConstant.CALLBACK_EVENT_ASR_VOLUME:
//                    //音量回调
//                    //Log.i(TAG,Thread.currentThread().getId()+"回调  音量回调 "+ s1);
//                    break;
//                case SpeechConstant.CALLBACK_EVENT_ASR_AUDIO:
//                    //语音音频数据回调
//                    break;
//                case SpeechConstant.CALLBACK_EVENT_ASR_EXIT:
//                    //识别结束，资源释放--开始另外一次回调？
//
//                    break;
//            }
//        }
//    }
//
//    private class MyClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.voice:
//                    //开始和暂停的功能
//                    updateFABView();
//                    break;
//                case R.id.cance:
//                    //取消本次录音，将文件清除
//                    eventManager.send(SpeechConstant.ASR_CANCEL,"{}",null,0,0);
//                    break;
//                case R.id.down:
//                    //已经完成录音，发送停止事件
//                    eventManager.send(SpeechConstant.ASR_STOP,"{}",null,0,0);
//                    break;
//            }
//        }
//    }
//}

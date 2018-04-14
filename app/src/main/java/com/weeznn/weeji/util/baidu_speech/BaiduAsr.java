package com.weeznn.weeji.util.baidu_speech;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.asr.SpeechConstant;
import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.activity.ASRActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.weeznn.mylibrary.utils.FileUtil.FILE_TYPE_NOTE;
import static com.weeznn.mylibrary.utils.FileUtil.getOutFile;


/**
 * Created by weeznn on 2018/3/12.
 */

public class BaiduAsr implements Constant {
    private static final String TAG = "BaiduAsr";
    private static BaiduAsr baiduAsr;
    //Voice 的2个状态
    private static final int VOICE_STATE_REDY = 0;
    private static final int VOICE_STATE_SPEAKING = 1;
    private static final int VOICE_STATE_PAUSE = 2;
    private int voiceState = VOICE_STATE_REDY;

    //百度语音的输入参数  Json
    private static String path = "";
    private SharedPreferences sharedPreferences;
    protected String fileName;
    protected String fileType;
    private int PID;
    private int PROP;
    private boolean acceptAudioData;
    private boolean acceptAudioVolince;
    private Handler handler;
    /**
     * 事件管理器，识别事件管理器只能维持一个。不能使用多个实例
     */
    private EventManager eventManager;
    /**
     * 事件监听回调
     */
    private EventListener listener;

    /**
     * 麦克风流
     */
    private MicrophoneInputStream microphoneInputStream;

    private Context context;
    private static JSONObject jsonObject;


    public BaiduAsr() {
        Log.i(TAG,"BaiduAsr");
        path =FileUtil.APPBASEPATH;
        jsonObject = new JSONObject();
    }

    /**
     * 开始语音识别
     * 注意确保程序有权限：
     * Manifest.permission.RECORD_AUDIO,
     * Manifest.permission.READ_EXTERNAL_STORAGE,
     * Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    public void start() {
        Log.i(TAG,"start");
        if (eventManager != null && listener != null) {
            eventManager.send(SpeechConstant.ASR_START, jsonObject.toString(), null, 0, 0);
            eventManager.registerListener(listener);
        } else {
            throw new RuntimeException("EventManager or EventListener is null ");
        }
    }

//    /**
//     * 暂停
//     */
//    public void stop(){
//        Log.i(TAG,"pause");
//        if (eventManager != null) {
//            eventManager.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
//            eventManager.unregisterListener(listener);
//        } else {
//            throw new RuntimeException("EventManager or EventListener is null");
//        }
//
//    }
//    /**
//     * 重启
//     */
//    public void reStart(){
//        Log.i(TAG,"restart");
//        if (eventManager != null && listener != null) {
//            eventManager.send(SpeechConstant.ASR_START, jsonObject.toString(), null, 0, 0);
//            eventManager.registerListener(listener);
//        } else {
//            throw new RuntimeException("EventManager or EventListener is null ");
//        }
//    }

    /**
     * 取消本次识别，将path目录下的数据清除
     */
    public void cancel() {
        Log.i(TAG,"cancel");
        if (eventManager != null && listener != null) {
            eventManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
            eventManager.unregisterListener(listener);
        } else {
            throw new RuntimeException("EventManager or EventListener is null");
        }

    }

    /**
     * 本次识别结束，将文本保存到path目录下
     */
    public void stop() {
        Log.i(TAG,"stop");
        if (eventManager != null) {
            eventManager.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
            eventManager.unregisterListener(listener);
        } else {
            throw new RuntimeException("EventManager or EventListener is null");
        }

    }

    public static class Builder {
        private static final String TAG = "Builder";

        public Builder() {
            baiduAsr = new BaiduAsr();
        }

        public Builder context(Context context) {
            baiduAsr.context = context;
            return this;
        }

        public Builder sharedPreferences(SharedPreferences sharedPreferences) {
            baiduAsr.sharedPreferences = sharedPreferences;
            return this;
        }

        public Builder fileName(String fileName) {
            if (fileName != null) {
                baiduAsr.fileName = fileName;
            } else {
                baiduAsr.fileName = " ";
            }

            return this;
        }

        public Builder fileType(String fileType) {
            baiduAsr.fileType = (fileType == null ? FILE_TYPE_NOTE : fileType);
            return this;
        }

        public Builder manager(EventManager manager) {
            if (null != manager) {
                baiduAsr.eventManager = manager;
            } else {
                new RuntimeException("EventListener is  null");
            }
            return this;
        }

        public Builder listener(EventListener listener) {
            if (listener != null) {
                baiduAsr.listener = listener;
            } else {
                new RuntimeException("EventListener is  null");
            }
            return this;
        }

        public Builder handler(Handler handler) {
            baiduAsr.handler = handler;
            return this;
        }

        public Builder micphoneStream(MicrophoneInputStream microphoneInputStream) {
            baiduAsr.microphoneInputStream = microphoneInputStream;
            InFileStream.setInputStream(microphoneInputStream);
            return this;
        }

        public Builder audioData(boolean b) {
            baiduAsr.acceptAudioData = b;
            return this;
        }

        public Builder audioVolince(boolean b) {
            baiduAsr.acceptAudioVolince = b;
            return this;
        }

        public BaiduAsr build() {
            //语言

            try {
                //语言
                jsonObject.put(SpeechConstant.LANGUAGE,baiduAsr.sharedPreferences.getInt("language",1537));
                //静音超时断句及长语音
                jsonObject.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, "0");
                //行业
                jsonObject.put(SpeechConstant.PROP, baiduAsr.sharedPreferences.getInt("porp", 20000));
                //保存识别过程产生的录音文件
                jsonObject.put(SpeechConstant.OUT_FILE, getOutFile(baiduAsr.fileType,baiduAsr.fileName));
                //语音音频数据回调
                jsonObject.put(SpeechConstant.ACCEPT_AUDIO_DATA, baiduAsr.sharedPreferences.getBoolean("audio",true));
                //语音音量数据回调
                jsonObject.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, baiduAsr.sharedPreferences.getBoolean("anim",false));
                //输入
                jsonObject.put(SpeechConstant.IN_FILE, "#com.weeznn.weeji.util.baidu_speech.InFileStream.create16kStream()");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, " ASR START JSON  " + jsonObject.toString());
            return baiduAsr;
        }



    }

    /**
     * SDK事件回调类
     */
    public static class MyEventListener implements EventListener {
        private static final String TAG = MyEventListener.class.getSimpleName();
        private String fileType;
        private String fileName;

        public MyEventListener(String type, String name) {
            this.fileName = name;
            this.fileType = type;
        }

        @Override
        public void onEvent(String s, String s1, byte[] bytes, int i, int i1) {
            switch (s) {
                case SpeechConstant.CALLBACK_EVENT_ASR_READY:
                    //引擎准备就绪，可以开始说话
                    Log.i(TAG, Thread.currentThread().getId() + "回调  引擎准备好");

                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
                    //识别结果
                    Log.i(TAG, Thread.currentThread().getId() + "回调  识别结果finish  " + s1);

                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_BEGIN:
                    //检测到说话开始
                    Log.i(TAG, Thread.currentThread().getId() + "回调  说话开始" + s1);

                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_END:
                    //检测到说话结束
                    Log.i(TAG, Thread.currentThread().getId() + "回调  说话结束" + s1);
                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                    //识别结果
                    Log.i(TAG, Thread.currentThread().getId() + "回调  partical " + s1);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s1);
                        if ("final_result".equals(jsonObject.getString("result_type"))) {
                            String str = jsonObject.getString("best_result");
                            Log.i(TAG, "text :" + str);
                            Message message = new Message();
                            message.what = ASRActivity.MSG_BAIDUASR_FINAL;
                            message.obj = str;
                            baiduAsr.handler.sendMessage(message);
                        }else if ("partial_result".equals(jsonObject.getString("result_type"))){
                            String str = jsonObject.getString("best_result");
                            Log.i(TAG, "text :" + str);
                            Message message = new Message();
                            message.what = ASRActivity.MSG_BAIDUASR_PART;
                            message.obj = str;
                            baiduAsr.handler.sendMessage(message);
                        }
                    } catch (JSONException e) {
                        Log.i(TAG, "new jsonObject ERROR");
                        e.printStackTrace();
                    }
                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_VOLUME:
                    //音量回调
                    //Log.i(TAG,Thread.currentThread().getId()+"回调  音量回调 "+ s1);
                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_AUDIO:
                    //语音音频数据回调
                    //Log.i(TAG, "语音音频数据回调   size "+i);
                    if (bytes.length>0){
                        try {
                            FileUtil.WriteAudio(fileType, fileName, bytes, i1, i);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case SpeechConstant.CALLBACK_EVENT_ASR_EXIT:
                    //识别结束，资源释放--开始另外一次回调？
                    Log.i(TAG, "exit");
                    break;
            }
        }
    }
}





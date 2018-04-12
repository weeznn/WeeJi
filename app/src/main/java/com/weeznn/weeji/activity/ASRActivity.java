package com.weeznn.weeji.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.speech.EventManagerFactory;
import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.util.baidu_speech.BaiduAsr;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.weeznn.weeji.R;
import com.weeznn.weeji.util.baidu_speech.MicrophoneInputStream;

import static com.weeznn.mylibrary.utils.FileUtil.FILE_TYPE_MEETING;

public class ASRActivity extends BaseActivity implements Constant,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = ASRActivity.class.getSimpleName();

    private static final String[] STORAGE_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int STORAGE_PERMISSIONS_CODE = 0;
    private static final String[] RADIO_PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO
    };
    private static final int RADIO_PERMISSIONS_CODE = 1;
    private static final int RESOULT_CODE_CANCEL=0;
    private static final int RESOULT_CODE_DOWN=1;

    /**
     * 以下为View相关的控件和变量
     */
    private TextView textView;
    private FloatingActionButton voice;
    private FloatingActionButton down;
    private FloatingActionButton cancel;
    private Toolbar toolbar;

    //Voice 的3个状态
    private static final int VOICE_STATE_REDY = 0;
    private static final int VOICE_STATE_SPEAKING = 1;
    private static final int VOICE_STATE_PAUSE = 2;
    private int voiceState = VOICE_STATE_REDY;

    /**
     * 以下为和逻辑相关的变量
     */
    private String fileName;
    private String fileType;
    private String filesub;
    private String peoples;
    private String data;
    private String[] keyWords=new String[3];
    private StringBuilder stringBuilder = new StringBuilder();
    private BaiduAsr asr;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == BaiduAsr.MSGCODE_FINAL) {
                stringBuilder.append(msg.obj);
                textView.setText(stringBuilder.toString());
            }else if (msg.what==BaiduAsr.MSGCODE_PART){
                textView.setText(stringBuilder.toString()+msg.obj);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asr);

        Intent intent = getIntent();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        data=simpleDateFormat.format(new Date());
        fileName =data+"|"+intent.getStringExtra("title");
        filesub=intent.getStringExtra("sub");
        fileType = intent.getStringExtra("type");
        peoples=intent.getStringExtra("peoples");

        writePeoples2Storge(peoples);

        Log.i(TAG,"title :"+fileName+"      sub :"+filesub+"    type  :"+fileType);

        initView();

        int permission = ActivityCompat.checkSelfPermission(this, STORAGE_PERMISSIONS[1]);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, STORAGE_PERMISSIONS_CODE);
        }
        asr = new BaiduAsr.Builder()
                .context(ASRActivity.this)
                .audioData(false)
                .audioVolince(false)
                .fileName(fileName)
                .manager(EventManagerFactory.create(this, "asr"))
                .fileType(FILE_TYPE_MEETING)
                .listener(new BaiduAsr.MyEventListener(fileType, fileName))
                .sharedPreferences(getSharedPreferences(getString(R.string.SharedPreferences_name), 0))
                .micphoneStream(new MicrophoneInputStream())
                .handler(handler)
                .build();
    }

    private void writePeoples2Storge(final String peoples) {
        Log.i(TAG,"writePeoples2Storge data :"+peoples);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"writing...");
                FileUtil.WriteText(FILE_TYPE_MEETING,fileName,FileUtil.FILE_TYPE_JSON,peoples);
            }
        }).start();
    }

    private void initView() {
        textView = findViewById(R.id.text);

        voice = findViewById(R.id.voice);
        down = findViewById(R.id.down);
        cancel = findViewById(R.id.cance);
        MyClickListener myClickListener = new MyClickListener();
        voice.setOnClickListener(myClickListener);
        down.setOnClickListener(myClickListener);
        cancel.setOnClickListener(myClickListener);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(fileName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWright));
        toolbar.setSubtitle(filesub);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWright));
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.voice:
                    //开始和暂停的功能
                    switch (voiceState) {
                        case VOICE_STATE_SPEAKING:
                            //说话中  要求暂停
                            Log.i(TAG, "voice 被点击 VOICE_STATE_SPEAKING 要求 暂停");
                            voiceState = VOICE_STATE_PAUSE;
                            cancel.setVisibility(View.VISIBLE);
                            down.setVisibility(View.VISIBLE);
                            voice.setImageResource(R.drawable.ic_mic_off);
                            asr.stop();

                            break;
                        case VOICE_STATE_PAUSE:
                            //暂停中  要求继续
                            Log.i(TAG, "voice 被点击 VOICE_STATE_PAUSE 要求继续");
                            voiceState = VOICE_STATE_SPEAKING;
                            cancel.setVisibility(View.GONE);
                            down.setVisibility(View.GONE);
                            voice.setImageResource(R.drawable.ic_microphone);
                            asr.start();

                            break;
                        case VOICE_STATE_REDY:
                            //初始状态
                            Log.i(TAG, "voice 被点击 VOICE_STATE_REDY ");
                            voiceState = VOICE_STATE_SPEAKING;
                            int permission = ActivityCompat.checkSelfPermission(ASRActivity.this, RADIO_PERMISSIONS[0]);
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ASRActivity.this, RADIO_PERMISSIONS, RADIO_PERMISSIONS_CODE);
                            }
                            asr.start();
                            break;
                    }

                    break;
                case R.id.cance:
                    //取消本次录音，将文件清除
                    Log.i(TAG, "click cance ,deleteFile");
                    asr.cancel();
                    FileUtil.deleteFile(fileType, fileName);
                    // TODO: 2018/4/7 结束本activity前去判断是否写完了
                    setResult(RESOULT_CODE_CANCEL);
                    finish();
                    break;
                case R.id.down:
                    //已经完成录音，发送停止事件
                    Log.i(TAG, "click stop ,writeFile");
                    asr.stop();
                    //写文件
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileUtil.WriteText(fileType, fileName,FileUtil.FILE_TYPE_TEXT,stringBuilder.toString());
                        }
                    }).start();

                    Bundle bundle=new Bundle();
                    bundle.putString(getResources().getString(R.string.TABLE_MET_metID),""+(fileType+fileName).hashCode());
                    bundle.putString(getResources().getString(R.string.TABLE_MET_time),data);
                    bundle.putString(getResources().getString(R.string.TABLE_MET_title),fileName);
                    bundle.putString(getResources().getString(R.string.TABLE_MET_sub),filesub);
                    bundle.putString(getResources().getString(R.string.TABLE_MET_keyword1),keyWords[0]);
                    bundle.putString(getResources().getString(R.string.TABLE_MET_keyword2),keyWords[1]);
                    bundle.putString(getResources().getString(R.string.TABLE_MET_keyword3),keyWords[2]);
                    Intent intent=new Intent();
                    intent.putExtra("result",bundle);
                    setResult(RESOULT_CODE_DOWN,intent);
                    // TODO: 2018/4/7 结束本activity前去判断是否写完了
                    finish();
                    break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG,"onBackPressed");
    }
}

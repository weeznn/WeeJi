package com.weeznn.baidu_speech.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.speech.EventManagerFactory;
import com.weeznn.baidu_speech.R;
import com.weeznn.baidu_speech.imp.CONS;
import com.weeznn.baidu_speech.online.BaiduAsr;
import com.weeznn.baidu_speech.online.MicrophoneInputStream;

public class MainActivity extends BaseActivity implements CONS,ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG=MainActivity.class.getSimpleName();
    private static final String[] STORAGE_PERMISSIONS=new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int STORAGE_PERMISSIONS_CODE=0;
    private static final String[] RADIO_PERMISSIONS=new String[]{
            Manifest.permission.RECORD_AUDIO
    };
    private static final int RADIO_PERMISSIONS_CODE=1;
    /**
     * 以下为View相关的控件和变量
     */
    private TextView textView;
    private FloatingActionButton voice;
    private FloatingActionButton down;
    private FloatingActionButton cancel;
    private Toolbar toolbar;
    //Voice 的2个状态
    private static final int VOICE_STATE_REDY=0;
    private static final int VOICE_STATE_SPEAKING=1;
    private static final int VOICE_STATE_PAUSE=2;
    private int voiceState=VOICE_STATE_REDY;

    /**
     * 以下为和逻辑相关的变量
     */
    private String fileName="";
    private String fileType=FILE_TYPE_NOTE;
    private StringBuilder stringBuilder=new StringBuilder();
    private BaiduAsr asr;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==BaiduAsr.MSGCODE){
                stringBuilder.append(msg.obj);
                textView.setText(stringBuilder.toString());
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        Intent intent=getIntent();
        fileName=intent.getStringExtra(FILE_NAME_CODE);
        fileType=intent.getStringExtra(FILE_TYPE_CODE);

        int permission=ActivityCompat.checkSelfPermission(this,STORAGE_PERMISSIONS[1]);
        if (permission!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,STORAGE_PERMISSIONS,STORAGE_PERMISSIONS_CODE);
        }
        asr= new BaiduAsr.Builder()
                .context(MainActivity.this)
                .audioData(false)
                .audioVolince(false)
                .fileName(fileName)
                .manager(EventManagerFactory.create(this,"asr"))
                .fileType(FILE_TYPE_MEETING)
                .listener(new BaiduAsr.MyEventListener())
                .sharedPreferences(getSharedPreferences(APPNAME,0))
                .micphoneStream(new MicrophoneInputStream())
                .handler(handler)
                .build();
    }

    private void initView() {
        textView=findViewById(R.id.text);
        voice=findViewById(R.id.voice);
        down=findViewById(R.id.down);
        cancel=findViewById(R.id.cance);
        MyClickListener myClickListener=new MyClickListener();
        voice.setOnClickListener(myClickListener);
        down.setOnClickListener(myClickListener);
        cancel.setOnClickListener(myClickListener);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(fileName);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO: 2018/3/13
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_baidu_asr,menu);
        return true;
    }


    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.voice:
                    //开始和暂停的功能
                    switch (voiceState){
                        case VOICE_STATE_SPEAKING:
                            //说话中  要求暂停
                            cancel.setVisibility(View.VISIBLE);
                            down.setVisibility(View.VISIBLE);
                            asr.pause();
                            voiceState=VOICE_STATE_PAUSE;
                            break;
                        case VOICE_STATE_PAUSE:
                            //暂停中  要求继续
                            cancel.setVisibility(View.GONE);
                            down.setVisibility(View.GONE);
                            asr.start();
                            voiceState=VOICE_STATE_SPEAKING;
                            break;
                        case VOICE_STATE_REDY:
                            //初始状态
                            voiceState=VOICE_STATE_SPEAKING;
                            int permission=ActivityCompat.checkSelfPermission(MainActivity.this,RADIO_PERMISSIONS[0]);
                            if (permission!=PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(MainActivity.this,RADIO_PERMISSIONS,RADIO_PERMISSIONS_CODE);
                            }
                            asr.start();
                            break;
                    }
                    break;
                case R.id.cance:
                    //取消本次录音，将文件清除
                    asr.cancel();
                    break;
                case R.id.down:
                    //已经完成录音，发送停止事件
                   asr.down();
                    break;
            }
        }
    }

}

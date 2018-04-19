package com.weeznn.weeji.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.weeznn.mylibrary.utils.FileAudioInputStream;
import com.weeznn.mylibrary.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AudioService extends Service {
    private static final String TAG=AudioService.class.getSimpleName();

    public static final int RESULT_CODE_NOFILE = 1;
    public static final int RESULT_CODE_SUC = 2;
    public static final int RESULT_CODE_INPUTSTREAM = 3;

    //广播发送进度信息
    private static LocalBroadcastManager localBroadcastManager;
    public static final String AUDIO_PROGRESS = "audio_progress";


    private static FileAudioInputStream audioInputStream;
    private static String path;
    private AudioTrack player;
    private boolean play=true;


    private byte[] pauseData ;
    int bufOffset = 0;
    int bufsize;
    static long dataSize = 0;
    boolean playOver = false;

    public AudioService() {
        bufsize= AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Log.i(TAG,"bufsize "+bufsize);
        pauseData=new byte[bufsize];
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder{

        public int prepare(Context context,String name, String type){
            path = FileUtil.getPath(type, name, FileUtil.TYPE_PCM);
            Log.i(TAG, path);
            File file = new File(path);
            if (!file.exists()) {
                //文件不存在
                Log.i(TAG,"文件不存在");
                return RESULT_CODE_NOFILE;
            }
            try {
                audioInputStream = new FileAudioInputStream(path);
            } catch (FileNotFoundException e) {
                Log.i(TAG,"audioInputStream");
                e.printStackTrace();
            }
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
            dataSize = file.length();
            return RESULT_CODE_SUC;
        }

        public void start(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 准备播放器
                    play=true;
                    byte[] data = new byte[bufsize];
                    if (player == null) {
                        player = new AudioTrack(AudioManager.STREAM_MUSIC,
                                16000,
                                AudioFormat.CHANNEL_OUT_MONO,
                                AudioFormat.ENCODING_PCM_16BIT,
                                bufsize,
                                AudioTrack.MODE_STREAM);
                    }

                    player.play();
                    //是否暂停再播放
                    if (pauseData != null && pauseData.length != 0 && playOver == false) {
                        player.write(data, 0, data.length);
                    }

                    try {
                        while (play && audioInputStream.read(data, 0, bufsize) != -1) {
                            pauseData = data.clone();
                            player.write(data, 0, data.length);
                            bufOffset += data.length;
                            sentAudioProgress(bufOffset);
                        }
                        //音频播放完毕
                        playOver = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i(TAG, "音频读取出错");
                    }
                }
            }).start();
        }



        public void pause(){
            Log.i(TAG,"handleActionPause");
            play=false;
            if (player != null) {
                player.pause();
                player.flush();
            }
        }
        public void stop(){
            play=false;
            if (player != null) {
                player.stop();
                player.release();
            }
        }

        public void sentAudioProgress(int count) {
            int progress =  (int) (100*count / dataSize);
            Intent intent = new Intent();
            intent.setAction(AudioIntentService.ACTION_AUDIO_PRO);
            intent.putExtra(AUDIO_PROGRESS, progress);
            localBroadcastManager.sendBroadcast(intent);
        }
    }
}

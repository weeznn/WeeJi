package com.weeznn.weeji.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.weeznn.mylibrary.utils.FileAudioInputStream;
import com.weeznn.mylibrary.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.AttributedCharacterIterator;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AudioIntentService extends IntentService {
    private static final String TAG = AudioIntentService.class.getSimpleName();

    private static final String ACTION_START = "com.weeznn.weeji.action.START";
    private static final String ACTION_STOP = "com.weeznn.weeji.action.STOP";
    private static final String ACTION_PAUSE = "com.weeznn.weeji.action.PAUSE";
    public static final String ACTION_AUDIO_PRO = "com.weeznn.weeji.action.AUDIO_PRO";

    public static final int RESULT_CODE_NOFILE = 1;
    public static final int RESULT_CODE_SUC = 2;
    public static final int RESULT_CODE_INPUTSTREAM = 3;

    //广播发送进度信息
    private static LocalBroadcastManager localBroadcastManager;
    public static final String AUDIO_PROGRESS = "audio_progress";


    private static FileAudioInputStream audioInputStream;
    private static String path;
    private AudioTrack player;


    private byte[] pauseData ;
    int bufOffset = 0;
    int bufsize;
    static long dataSize = 0;
    boolean playOver = false;


    public AudioIntentService() {
        super("AudioIntentService");
        bufsize= AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Log.i(TAG,"bufsize "+bufsize);
        pauseData=new byte[bufsize];
    }


    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static int ActionStart(Context context, String name, String type) {
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
        Intent intent = new Intent(context, AudioIntentService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
        return RESULT_CODE_SUC;
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void ActionStop(Context context) {
        Intent intent = new Intent(context, AudioIntentService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void ActionPause(Context context) {
        Log.i(TAG,"ActionPause");
        Intent intent = new Intent(context, AudioIntentService.class);
        intent.setAction(ACTION_PAUSE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                handleActionStart();
            } else if (ACTION_STOP.equals(action)) {
                handleActionStop();
            } else if (ACTION_PAUSE.equals(action)) {
                handleActionPause();
            }
        }
    }

    public void sentAudioProgress(int count) {
        int progress =  (int) (100*count / dataSize);
        Log.i(TAG,"sentAudioProgress "+progress +"  count "+count);
        Intent intent = new Intent();
        intent.setAction(AudioIntentService.ACTION_AUDIO_PRO);
        intent.putExtra(AUDIO_PROGRESS, progress);
        localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 暂停
     */
    private void handleActionPause() {
        Log.i(TAG,"handleActionPause");
        if (player != null) {
            player.pause();
            player.flush();
        }
    }

    /**
     * 开始
     */
    private void handleActionStart() {
        // 准备播放器
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
            while (audioInputStream.read(data, 0, bufsize) != -1) {
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

    /**
     * 退出
     */
    private void handleActionStop() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }
}

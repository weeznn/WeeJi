package com.weeznn.mylibrary.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.File;

/**
 * Created by weeznn on 2018/4/2.
 */

public class AudioReaderUtil {
    private static final String TAG = AudioReaderUtil.class.getSimpleName();
    private String filepath;
    private int playerState;
    private AudioTrack track;
    private int bufferSize = AudioTrack.getMinBufferSize(16000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

    public AudioReaderUtil(String type, String fileName) {
        this.filepath = FileUtil.APPBASEPATH +
                type + "/" + fileName + "/" + fileName + ".pcm";
        //bufferSize


        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize*2,
                AudioTrack.MODE_STATIC);


    }

    public void start(){
        playerState=AudioTrack.PLAYSTATE_PLAYING;
        track.play();
        long offset=0;
        File file=new File(filepath);
        long fileSize = 0;
        if (file.exists()){
            fileSize=file.length();
        }
        // TODO: 2018/4/2 待测试是否正确
        while (offset<fileSize){
            byte[] data= FileUtil.ReadAudio(filepath,offset,bufferSize*2);
            offset=offset+track.write(data,0,data.length);
        }
    }

    public void pause(){
        playerState=AudioTrack.PLAYSTATE_PAUSED;
        track.pause();
    }

    public void stop(){
        playerState=AudioTrack.PLAYSTATE_STOPPED;
        track.stop();
        track.release();
    }

}

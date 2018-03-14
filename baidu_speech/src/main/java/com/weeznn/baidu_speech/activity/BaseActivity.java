package com.weeznn.baidu_speech.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.weeznn.baidu_speech.R;
import com.weeznn.baidu_speech.util.Logger;

import java.util.ArrayList;

public   class BaseActivity extends AppCompatActivity{
    private static final String TAG="BaseActivity";
    private static final String EXITACTION="action.exit";
    private ExitReceiver exitReceiver=new ExitReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.i(TAG,"oncreat");
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(EXITACTION);
        registerReceiver(exitReceiver,intentFilter);

        initPermition();
    }

    protected void initPermition() {
        Log.i(TAG,"initPermition");
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR
        };
        ArrayList<String> permitionList=new ArrayList<String>();
        for (String perm:permissions){
            if (PackageManager.PERMISSION_GRANTED!= ContextCompat.checkSelfPermission(this,perm)){
                //没有权限
                permitionList.add(perm);
            }
        }
        String[] tmpList=new String[permitionList.size()];
        if (!permitionList.isEmpty()){
            ActivityCompat.requestPermissions(this,tmpList,0);
            Log.i(TAG,"NO  "+permitionList.size()+" PERMITION :"+permitionList.toString());
        }
    }


    protected void askForPermition() {
        Log.i(TAG,"ask for permission ");
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.permission_title))
                .setMessage(getString(R.string.permission_message))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去设置界面
                        Uri packageURI=Uri.parse("package:"+"com.weeznn.baidu_speech");
                        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //不给我权限
                        Intent intent=new Intent();
                        intent.setAction(EXITACTION);
                        sendBroadcast(intent);
                    }
                })
                .create();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "on request permissions resoult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION_CODES.M < Build.VERSION.SDK_INT) {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            if (!shouldShowRequestPermissionRationale(permissions[i])) {
                                askForPermition();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(exitReceiver);
    }

    public class ExitReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    }


}

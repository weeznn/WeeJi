package com.weeznn.weeji.fragment;


import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.weeznn.weeji.R;

import java.util.ArrayList;


public class BaiduSeettingFragment extends PreferenceFragment {
    private static final String TAG="BaiduSeettingFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"oncreat");
        addPreferencesFromResource(R.xml.pref_baidu);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onViewCreate");

        Mylistener mylistener=new Mylistener();
        findPreference("language").setOnPreferenceChangeListener(mylistener);
        findPreference("anim").setOnPreferenceChangeListener(mylistener);
        findPreference("audio").setOnPreferenceChangeListener(mylistener);
        findPreference("ring").setOnPreferenceChangeListener(mylistener);
        findPreference("prop").setOnPreferenceChangeListener(mylistener);
    }

    class Mylistener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()){
                case "language":
                    Log.i(TAG,"MyListener language  changed  newValue="+newValue.toString()+"\n" +
                            "SharedPreferences name = "+getPreferenceManager().getSharedPreferencesName());
                    getPreferenceManager().getSharedPreferences().edit()
                            .putInt(getString(R.string.pref_baidu_language),Integer.parseInt(newValue.toString()))
                            .apply();
                    break;
                case "anim":
                    Log.i(TAG,"MyListener anmi  changed  newValue="+newValue.toString()+"\n" +
                            "SharedPreferences name = "+getPreferenceManager().getSharedPreferencesName());
                    getPreferenceManager().getSharedPreferences().edit()
                            .putBoolean(getString(R.string.pref_baidu_anim),(Boolean) newValue)
                            .apply();
                    break;
                case "audio":
                    Log.i(TAG,"MyListener audio  changed  newValue="+newValue.toString()+"\n" +
                            "SharedPreferences name = "+getPreferenceManager().getSharedPreferencesName());
                    getPreferenceManager().getSharedPreferences().edit()
                            .putBoolean(getString(R.string.pref_baidu_audio),(Boolean)newValue)
                            .apply();
                    break;
                case "ring":
                    Log.i(TAG,"MyListener ring  changed  newValue="+newValue.toString()+"\n" +
                            "SharedPreferences name = "+getPreferenceManager().getSharedPreferencesName());
                    getPreferenceManager().getSharedPreferences().edit()
                            .putBoolean(getString(R.string.pref_baidu_ring),(Boolean) newValue)
                            .apply();
                    break;
                case "prop":
                    Log.i(TAG,"MyListener porp  changed  newValue="+newValue.toString()+"\n" +
                            "SharedPreferences name = "+getPreferenceManager().getSharedPreferencesName());
                    getPreferenceManager().getSharedPreferences().edit()
                            .putInt(getString(R.string.pref_baidu_prop),Integer.parseInt(newValue.toString()))
                            .apply();
                    break;
            }

            return true;
        }
    }

}

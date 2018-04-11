package com.weeznn.weeji.activity;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.weeznn.weeji.R;

import java.util.List;


public class SettingsActivity extends AppCompatPreferenceActivity {
    private static final String TAG=SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"oncreat");
    }

    @Override
    public void onBuildHeaders(List<PreferenceActivity.Header> target) {
        //加载headers
        loadHeadersFromResource(R.xml.preference_header,target);
    }

    /**
     * 验证fragment 是否有效
     * @param fragmentName
     * @return
     */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

}

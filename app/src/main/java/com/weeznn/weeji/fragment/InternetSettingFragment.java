package com.weeznn.weeji.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weeznn.weeji.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InternetSettingFragment extends PreferenceFragment {
    private static final String TAG=InternetSettingFragment.class.getSimpleName();
    
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences(getString(R.string.SharedPreferences_name),0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO: 2018/4/14  
    }

    class MyListener implements Preference.OnPreferenceChangeListener{

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // TODO: 2018/4/14  
            return true;
        }
    }

}

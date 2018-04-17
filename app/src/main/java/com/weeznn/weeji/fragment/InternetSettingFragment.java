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

import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.weeji.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InternetSettingFragment extends PreferenceFragment
                    implements Constant{
    private static final String TAG=InternetSettingFragment.class.getSimpleName();
    
    private SharedPreferences sharedPreferences;
    private MyListener listener;

    Preference asr;
    Preference nlp;
    Preference onlyWifi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences(getString(R.string.SharedPreferences_name),0);
        listener=new MyListener();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      onlyWifi=findPreference("only_wifi");
        onlyWifi.setOnPreferenceChangeListener(listener);

       asr=findPreference("4G_asr");
        asr.setOnPreferenceChangeListener(listener);

      nlp=findPreference("4G_nlp");
        nlp.setOnPreferenceChangeListener(listener);
    }

    class MyListener implements Preference.OnPreferenceChangeListener{

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()){
                case "only_wifi":
                    if (true==(Boolean) newValue){
                        asr.setEnabled(false);
                        nlp.setEnabled(false);
                    }else {
                        asr.setEnabled(true);
                        nlp.setEnabled(true);
                    }
                    sharedPreferences.edit().putBoolean(getString(R.string.pref_intent_only_wifi), (Boolean) newValue);
                    break;
                case "4G_asr":
                    sharedPreferences.edit().putBoolean(getString(R.string.pref_intent_4G_asr), (Boolean) newValue);
                    break;
                case "4G_nlp":
                    sharedPreferences.edit().putBoolean(getString(R.string.pref_intent_4G_nlp), (Boolean) newValue);
                    break;

            }
            return true;
        }
    }

}

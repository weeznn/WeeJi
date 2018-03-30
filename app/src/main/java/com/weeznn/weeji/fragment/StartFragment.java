package com.weeznn.weeji.fragment;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.weeznn.weeji.R;


public class StartFragment extends Fragment {
    private static final String TAG="startfragment";

    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragmnet_start,container,false);
        image=view.findViewById(R.id.image);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"animation  start");
        Drawable drawable=image.getDrawable();
        if (drawable instanceof Animatable){
            ((Animatable) drawable).start();
        }


    }
}

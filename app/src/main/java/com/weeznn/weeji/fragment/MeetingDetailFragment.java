package com.weeznn.weeji.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.adpater.DetailAdapter;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.entry.Meeting;
import com.weeznn.weeji.util.db.entry.People;

import java.util.List;


public class MeetingDetailFragment extends Fragment implements DetailAdapter.UpdateFragmentDetail {
    private static final String TAG = "MeetingDetailFragment";
    private static final String MEETING_CODE = "CODE";

    private String code;
    private String title;
    private String sub;
    private String keyWard1;
    private String keyWard2;
    private String keyWard3;
    private String path;
    private String text;

    private OnFragmentInteractionListener mListener;

    public MeetingDetailFragment() {
        // Required empty public constructor
    }


    //view
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView titleView;
    private TextView subView;
    private TextView keyWord1View;
    private TextView keyWord2View;
    private TextView keyWord3View;
    private TextView textView;
    private FloatingActionButton player;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            titleView.setText(title);
            subView.setText(sub);
            keyWord1View.setText(keyWard1);
            keyWord2View.setText(keyWard2);
            keyWord3View.setText(keyWard3);
            textView.setText(text);
            return true;
        }
    });


    private final float screenW = getResources().getDisplayMetrics().widthPixels;
    private final float toolBarHeight = getResources().getDimension(R.dimen.toolbarHeight);
    private final float initHeight = getResources().getDimension(R.dimen.subScription_Head);
    private float titleOffset;
    private float subOffset;
    private float keyWordOffset;
    private float mHeight = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MeetingDetailFragment.
     */
    public static MeetingDetailFragment newInstance(String code) {
        MeetingDetailFragment fragment = new MeetingDetailFragment();
        Bundle args = new Bundle();
        args.putString(MEETING_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString(MEETING_CODE);
            initdata(code);
        }
    }

    private void initdata(final String code) {
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                List<Meeting> list = MyApplication.getInstant().getMeetingDao().queryBuilder()
                        .where(MeetingDao.Properties._metID.eq(code))
                        .list();
                Log.i(TAG, "GET  INFO :" + list.get(0).toString());

                title = list.get(0).getTitle();
                sub = list.get(0).getSub();
                keyWard1 = list.get(0).getKeyword1();
                keyWard2 = list.get(0).getKeyword2();
                keyWard3 = list.get(0).getKeyword3();
                text = FileUtil.ReadText(FileUtil.FILE_TYPE_MEETING, list.get(0).getTitle());
                handler.sendMessage(new Message());
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_detail, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        titleView = view.findViewById(R.id.title);
        subView = view.findViewById(R.id.subtext);
        textView = view.findViewById(R.id.textView);
        keyWord1View = view.findViewById(R.id.key_word_1);
        keyWord2View = view.findViewById(R.id.key_word_2);
        keyWord3View = view.findViewById(R.id.key_word_3);
        appBarLayout = view.findViewById(R.id.appbar);
        player=view.findViewById(R.id.player);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appBarLayout.addOnOffsetChangedListener(new MyAppBarStateChangeListener());
        // TODO: 2018/4/7

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void upDataTableMET(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void update(String code) {
        initdata(code);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Appbarlayout listener
     */
    private class MyAppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
        private static final String TAG = "AppBarState";

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            if (mHeight == 0) {
                mHeight = titleView.getHeight();
                float titleDiatance = titleView.getTop() + (mHeight - toolBarHeight) / 2;
                float subDistance = subView.getTop() - toolBarHeight;
                float keyWordDistance = keyWord1View.getBottom() - toolBarHeight;
                titleOffset = titleDiatance / (initHeight - toolBarHeight);
                subOffset = subDistance / (initHeight - toolBarHeight);
                keyWordOffset = keyWordDistance / (initHeight - toolBarHeight);
            }

            titleView.setTranslationY(titleOffset * verticalOffset);
            subView.setAlpha(subOffset * verticalOffset);
            player.setScaleX(keyWordOffset*verticalOffset);
            player.setScaleY(keyWordOffset*verticalOffset);
            keyWord1View.setTranslationY(keyWordOffset * verticalOffset);
        }
    }


//    private abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener{
//        public enum State{
//            EXPANDED,
//            COLLAPSED,
//            IDLE
//        }
//        private State currentState=State.IDLE;
//
//        @Override
//        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//            if (0==verticalOffset){
//                if (currentState!=State.EXPANDED){
//                    onStateChanged(appBarLayout,State.EXPANDED,verticalOffset);
//                }
//                currentState=State.EXPANDED;
//            }else if (Math.abs(verticalOffset)>=appBarLayout.getTotalScrollRange()){
//                if (currentState!=State.COLLAPSED){
//                    onStateChanged(appBarLayout,State.COLLAPSED,verticalOffset);
//                }
//                currentState=State.COLLAPSED;
//            }else{
//                if (currentState!=State.IDLE){
//                    onStateChanged(appBarLayout,State.IDLE,verticalOffset);
//                }
//                currentState=State.IDLE;
//            }
//        }
//        public abstract void onStateChanged(AppBarLayout appBarLayout,State state,int verticalOffset);
//    }
}

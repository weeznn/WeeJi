package com.weeznn.weeji.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.DetailActivity;
import com.weeznn.weeji.interfaces.UpdataFragmentDetailListener;
import com.weeznn.weeji.service.AudioIntentService;
import com.weeznn.weeji.util.db.MeetingDao;
import com.weeznn.weeji.util.db.entry.Meeting;

import java.util.List;

public class MeetingDetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MeetingDetailFragment";

    private static final String ARG_PARAM1 = "CODE";
    private static final int MSG_CODE_METTING_DB = 1;
    private static final int MSG_CODE_METTING_UPDATA = 2;
    private static final int MSG_CODE_METTING_FILE = 3;

    private static final int PLAYER_START = 1;
    private static final int PLAYER_PAUSE = 2;


    //view
    private AppBarLayout appBarLayout;
    private FloatingActionButton player;
    private ProgressBar progressBar;

    private TextView titleView;
    private TextView timeView;
    private TextView addrView;
    private TextView keyWord1View;
    private TextView keyWord2View;
    private TextView keyWord3View;
    private TextView textView;
    private ImageButton btnPlayer;
    private TextView toolbarTitle;


    //逻辑
    private long code;
    private Meeting meeting;
    private String txt;//正文
    private int playerState = PLAYER_PAUSE;

    private OnFragmentInteractionListener mListener;

    private LocalBroadcastManager localBroadcastManager;
    private MyAudioProgressResiver resiver;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            onHandleMessage(msg);
            return true;
        }
    });

    public static MeetingDetailFragment newInstance(long code) {
        MeetingDetailFragment fragment = new MeetingDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getLong(ARG_PARAM1);
            initInfo();
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioIntentService.ACTION_AUDIO_PRO);
        resiver = new MyAudioProgressResiver();
        localBroadcastManager.registerReceiver(resiver, filter);

        ((DetailActivity) getActivity()).setFragmentDetailListener(new UpdataFragmentDetailListener() {
            @Override
            public void updata(long code) {
                Message message=new Message();
                message.what=MSG_CODE_METTING_UPDATA;
                message.obj=code;
               handler.sendMessage(message);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_detail, container, false);
        appBarLayout = view.findViewById(R.id.appbar);
        player = view.findViewById(R.id.fab);
        progressBar = view.findViewById(R.id.progressbar);

        titleView = view.findViewById(R.id.title);
        timeView = view.findViewById(R.id.time);
        addrView = view.findViewById(R.id.address);
        keyWord1View = view.findViewById(R.id.key_word_1);
        keyWord2View = view.findViewById(R.id.key_word_2);
        keyWord3View = view.findViewById(R.id.key_word_3);
        textView = view.findViewById(R.id.text);
        btnPlayer = view.findViewById(R.id.btn_player);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //player
        player.setOnClickListener(this);
        btnPlayer.setOnClickListener(this);

        //appbar
        appBarLayout.addOnOffsetChangedListener(new MyOffsetChangeListener());

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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
        localBroadcastManager.unregisterReceiver(resiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_player:
            case R.id.fab:
                if (playerState == PLAYER_PAUSE) {
                    Log.i(TAG, "开始播放");
                    playerState = PLAYER_START;
                    player.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    btnPlayer.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    player.invalidate();
                    btnPlayer.invalidate();
                } else {
                    Log.i(TAG, "暂停播放");
                    playerState = PLAYER_PAUSE;
                    player.setImageResource(R.drawable.ic_pause_black_24dp);
                    btnPlayer.setImageResource(R.drawable.ic_pause_black_24dp);
                    player.invalidate();
                    btnPlayer.invalidate();
                }
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void onHandleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CODE_METTING_DB:
                //读文件
                readText(meeting.getTitle());
                titleView.setText(meeting.getTitle());
                timeView.setText(meeting.getTime());
                addrView.setText(meeting.getAddress());
                keyWord1View.setText(meeting.getKeyword1());
                keyWord2View.setText(meeting.getKeyword2());
                keyWord3View.setText(meeting.getKeyword3());
                toolbarTitle.setText(meeting.getTitle());
                break;
            case MSG_CODE_METTING_FILE:
                textView.setText(txt);
                break;
            case MSG_CODE_METTING_UPDATA:
                code= (long) msg.obj;
                initInfo();
        }
    }

    /**
     * 从数据库中读取该meeting 的信息
     */
    private void initInfo() {
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                List<Meeting> resule = MyApplication.getInstant().getMeetingDao().queryBuilder()
                        .where(MeetingDao.Properties._metID.eq(code))
                        .list();
                if (resule != null && resule.size() > 0) {
                    meeting = resule.get(0);
                    handler.sendEmptyMessage(MSG_CODE_METTING_DB);
                }
            }
        });
    }

    /**
     * 从本地读取文本文件
     *
     * @param fileName
     */
    private void readText(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                txt = FileUtil.ReadText(FileUtil.FILE_TYPE_MEETING, fileName);
                handler.sendEmptyMessage(MSG_CODE_METTING_FILE);
            }
        }).start();

    }

    /**
     * 滑动折叠监听器
     */
    private class MyOffsetChangeListener implements AppBarLayout.OnOffsetChangedListener {
        private final int EXPAND = 1;
        private final int CLOOSE = 2;
        private final int IDEO = 3;
        private int currontState = IDEO;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset == 0) {
                if (currontState != EXPAND) {
                    currontState = EXPAND;
                    //展开状态  toolbar 不显示 fab显示
                }
                btnPlayer.setVisibility(View.GONE);
                toolbarTitle.setVisibility(View.GONE);

                player.setVisibility(View.VISIBLE);
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (currontState != CLOOSE) {
                    currontState = CLOOSE;
                }
                //闭合状态  toolbar显示  其他的不显示
                btnPlayer.setVisibility(View.VISIBLE);
                toolbarTitle.setVisibility(View.VISIBLE);

                player.setVisibility(View.INVISIBLE);
            } else {
                currontState = IDEO;
                toolbarTitle.setVisibility(View.GONE);
                btnPlayer.setVisibility(View.INVISIBLE);

            }
        }
    }

    /**
     * 广播接收器  接收音频播放的进度条消息
     */
    private class MyAudioProgressResiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //填写进度条
            progressBar.setProgress(intent.getIntExtra(AudioIntentService.AUDIO_PROGRESS, 0));
        }
    }
}

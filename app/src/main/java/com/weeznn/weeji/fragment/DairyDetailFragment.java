package com.weeznn.weeji.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.DetailActivity;
import com.weeznn.weeji.activity.MarkDownActivity;
import com.weeznn.weeji.interfaces.UpdataFragmentDetailListener;
import com.weeznn.weeji.util.db.DiaryDao;
import com.weeznn.weeji.util.db.entry.Diary;

import java.util.List;

public class DairyDetailFragment extends Fragment {
    public static final String TAG=DairyDetailFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "code";

    private static final int MSG_CODE_METTING_DB = 1;
    private static final int MSG_CODE_METTING_UPDATA = 2;
    private static final int MSG_CODE_METTING_FILE = 3;

    //View
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private FloatingActionButton fab;
    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            onHandleMessage(msg);
            return true;
        }
    });



    //逻辑
    private long code;
    private Diary diary;
    private String txt;
    private String title;

    private OnFragmentInteractionListener mListener;

    public static DairyDetailFragment newInstance(long code) {
        DairyDetailFragment fragment = new DairyDetailFragment();
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
        }
        Log.i(TAG,"onCreate  code:"+code);
        initInfo(code);

        ((DetailActivity)getActivity()).setFragmentDetailListener(new UpdataFragmentDetailListener() {
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
        View view=inflater.inflate(R.layout.fragment_dairy_detail, container, false);
        toolbar=view.findViewById(R.id.toolbar);
        appBarLayout=view.findViewById(R.id.appbar);
        imageView=view.findViewById(R.id.image);
        fab=view.findViewById(R.id.fab);
        progressBar=view.findViewById(R.id.progressbar);
        textView=view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //appbar

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MarkDownActivity.class);
                intent.putExtra(MarkDownActivity.INTENT_FILE_NAME,title);
                intent.putExtra(MarkDownActivity.INTENT_FILE_TYPE,FileUtil.FILE_TYPE_DAIRY);
                intent.putExtra(MarkDownActivity.INTENT_FILE_DATA,txt);
                startActivity(intent);
            }
        });
    }

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
    }

    private void onHandleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CODE_METTING_DB:
                //读文件
                readText(diary.getDate());
                //toobar
                title=diary.getDate();
                toolbar.setTitle(title);
                toolbar.setSubtitle(diary.getAddress());

                //image
                Glide.with(imageView)
                        .load(diary.getImage())
                        .into(imageView);
                break;
            case MSG_CODE_METTING_FILE:
                textView.setText(txt);
                break;
            case MSG_CODE_METTING_UPDATA:
                code= (long) msg.obj;
                initInfo(code);
        }
    }

    /**
     * 从数据库读取信息
     * @param code
     */
    private void initInfo(final long code) {
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                List<Diary> result=MyApplication.getInstant().getDiaryDao()
                        .queryBuilder()
                        .where(DiaryDao.Properties._DAIID.eq(code))
                        .list();
                if (result!=null && result.size()>0){
                    diary=result.get(0);
                    handler.sendEmptyMessage(MSG_CODE_METTING_DB);
                }
            }
        });
    }

    private void readText(final String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                txt=FileUtil.ReadText(FileUtil.FILE_TYPE_DAIRY,date);
                handler.sendEmptyMessage(MSG_CODE_METTING_FILE);
            }
        }).start();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

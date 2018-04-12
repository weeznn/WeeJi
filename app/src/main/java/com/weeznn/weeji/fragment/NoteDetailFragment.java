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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.DetailActivity;
import com.weeznn.weeji.adpater.NoteDetailAdapter;
import com.weeznn.weeji.interfaces.UpdataFragmentDetailListener;
import com.weeznn.weeji.util.db.NoteDao;
import com.weeznn.weeji.util.db.entry.Note;

import java.util.LinkedList;
import java.util.List;


public class NoteDetailFragment extends Fragment {
   private static final String TAG=NoteDetailFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "code";

    private static final int MSG_CODE_METTING_DB = 1;
    private static final int MSG_CODE_METTING_UPDATA = 2;
    private static final int MSG_CODE_METTING_FILE = 3;

    //view
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout appBarLayout;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            onHandlerMessage(msg);
            return true;
        }
    });



    //逻辑
    private long code;
    private List<Note> data=new LinkedList<>();
    private String title="";
    private String sub="";
    private String imagePath;
    private NoteDetailAdapter adapter;

    private OnFragmentInteractionListener mListener;


    public static NoteDetailFragment newInstance(long param1) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getLong(ARG_PARAM1);
            initInfo(code);
        }

        ((DetailActivity)getActivity()).setFragmentDetailListener(new UpdataFragmentDetailListener() {
            @Override
            public void updata(long code) {
                initInfo(code);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_note_detail, container, false);
        toolbar=view.findViewById(R.id.toolbar);
        appBarLayout=view.findViewById(R.id.appbar);
        toolbarLayout=view.findViewById(R.id.toolbar_layout);
        fab=view.findViewById(R.id.fab);
        recyclerView=view.findViewById(R.id.recyclerView);
        imageView=view.findViewById(R.id.image);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //appbar
        //toolbar
        toolbarLayout.setTitleEnabled(false);

        //recycler
        adapter=new NoteDetailAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/4/12 添加
            }
        });
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
    }


    private void onHandlerMessage(Message msg) {
        switch (msg.what) {
            case MSG_CODE_METTING_DB:
                readText(title);

                toolbar.setTitle(title);
                toolbar.setSubtitle(sub);
                Glide.with(imageView)
                        .load(imagePath)
                        .into(imageView);
                break;
            case MSG_CODE_METTING_FILE:
                break;
            case MSG_CODE_METTING_UPDATA:
                code= (long) msg.obj;
                initInfo(code);
        }
    }

    private void readText(String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: 2018/4/12 找到文件夹下的所有目录下的文件并读出来
            }
        }).start();
    }


    private void initInfo(final long code) {
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                NoteDao dao=MyApplication.getInstant().getNoteDao();
                List<Note> result=dao.queryBuilder()
                        .where(NoteDao.Properties._noteID.eq(code))
                        .list();
                if (result!=null && result.size()>0){
                    title=result.get(0).getSource();
                    sub=result.get(0).getSub();
                    imagePath=result.get(0).getImage();

                    List<Note> noteList=dao.queryBuilder()
                            .where(NoteDao.Properties.Source.eq(title))
                            .list();
                    data.addAll(noteList);
                    adapter.notifyDataSetChanged();
                }


                handler.sendEmptyMessage(MSG_CODE_METTING_DB);
            }
        });
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

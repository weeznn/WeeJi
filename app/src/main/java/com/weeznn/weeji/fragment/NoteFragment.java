package com.weeznn.weeji.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.DetailActivity;
import com.weeznn.weeji.activity.MarkDownActivity;
import com.weeznn.weeji.adpater.NoteAdapter;
import com.weeznn.weeji.interfaces.ItemClickListener;
import com.weeznn.weeji.util.db.NoteDao;
import com.weeznn.weeji.util.db.entry.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weeznn on 2018/4/2.
 */

public class NoteFragment extends Fragment implements
        Constant,
        ItemClickListener {
    private static final String TAG = NoteFragment.class.getSimpleName();
    private static final int MSG_CODE_UPDATA=1;

    //view
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private FloatingActionButton fab;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            refreshLayout.setRefreshing(false);
            return true;
        }
    });

    //逻辑
    private List<Note> data = new ArrayList<>();
    private NoteAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.freshLayout);
        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle=view.findViewById(R.id.toolbar_title);
        fab = view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //freshlayout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updata();
                handler.sendEmptyMessage(MSG_CODE_UPDATA);
            }
        });


        //recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter(getContext(), data);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbarTitle.setText("笔记");

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
               final EditText editText=new EditText(getContext());
               builder.setView(editText)
                       .setTitle("笔记本名称")
                       .setNeutralButton("新建", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Intent intent=new Intent(v.getContext(), MarkDownActivity.class);
                               intent.putExtra(MarkDownActivity.INTENT_FILE_NAME,editText.getText().toString() );
                               intent.putExtra(MarkDownActivity.INTENT_FILE_TYPE, FileUtil.FILE_TYPE_NOTE);
                               startActivityForResult(intent,REQUEST_CODE_DIA);
                           }
                       }).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshLayout.setRefreshing(true);
        while (refreshLayout.isRefreshing()) {
            updata();
            refreshLayout.setRefreshing(false);
        }
    }

    public void updata() {
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                NoteDao dao = MyApplication.getInstant().getNoteDao();
                data = dao.queryBuilder()
                        .limit(10)
                        .list();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(getString(R.string.LEFT_TYPE),CODE_NOT);
        intent.putExtra(getString(R.string.LEFT_CODE),data.get(position).get_noteID());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {

    }
}

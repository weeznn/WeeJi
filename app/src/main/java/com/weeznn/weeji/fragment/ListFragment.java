//package com.weeznn.weeji.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.weeznn.weeji.MyApplication;
//import com.weeznn.weeji.R;
//import com.weeznn.weeji.adpater.DiaryAdapter;
//import com.weeznn.weeji.adpater.MettingAdapter;
//import com.weeznn.weeji.adpater.NoteAdapter;
//import com.weeznn.weeji.util.db.DiaryDao;
//import com.weeznn.weeji.util.db.NoteDao;
//import com.weeznn.weeji.util.db.entry.Diary;
//import com.weeznn.weeji.util.db.entry.Note;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ListFragment extends Fragment {
//    private static final String TAG="ListFragment";
//
//    //View
//
//    private RecyclerView recyclerView;
//    private SwipeRefreshLayout refreshLayout;
//    private FloatingActionButton fab;
//    private RecyclerView.LayoutManager layoutManager;
//
//    //逻辑
//    private MenuItem item;
//    private BaseAdapter myAdapter;
//    private ArrayList<Object> data=new ArrayList<>();
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i(TAG,"onCreat");
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.fragment_list,container,false);
//        recyclerView=view.findViewById(R.id.recyclerView);
//
//        refreshLayout=view.findViewById(R.id.refreshLayout);
//        refreshLayout.setOnRefreshListener(new MyRefreshListener());
//
//        //开始新的
//        fab=view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=getActivity().getPackageManager()
//                        .getLaunchIntentForPackage("com.weeznn.baidu_speech");
//               if (intent!=null){
//                   DialogFragment fragment=new AlterDialogFragment();
//                   fragment.show(getActivity().getSupportFragmentManager(),"dialog");
//               }else {
//                   Toast.makeText(getActivity(),"未安装百度语音服务！",Toast.LENGTH_SHORT);
//               }
//            }
//        });
//        Log.i(TAG,"onCreatView");
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Log.i(TAG,"onViewCreate");
//
//        if (layoutManager==null){
//            layoutManager=new LinearLayoutManager(getActivity());
//        }
//        recyclerView.setLayoutManager(layoutManager);
//        switch (item.getItemId()){
//            case R.id.nav_menu_collection:
//            case R.id.nav_menu_dairy:
//                myAdapter=new DiaryAdapter(getContext(),data);
//                break;
//            case R.id.nav_menu_meeting:
//                myAdapter=new MettingAdapter(getContext(),data);
//                break;
//            case R.id.nav_menu_note:
//                myAdapter=new NoteAdapter(getContext(),data);
//                break;
//        }
//        recyclerView.setAdapter(myAdapter);
//
//    }
//
//    public void update(@NonNull MenuItem item){
//       this.item=item;
//        refreshLayout.setRefreshing(true);
//    }
//
//    public void setLayoutManagerType(MenuItem item){
//        if (item.getItemId()==R.id.linear){
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        }else {
//            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
//        }
//    }
//
//    private class MyRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
//        @Override
//        public void onRefresh() {
//
//            MyApplication.getInstant().runInTx(new Runnable() {
//                @Override
//                public void run() {
//                    switch (item.getItemId()){
//                        case R.id.nav_menu_collection:
//                            break;
//                        case R.id.nav_menu_dairy:
//                            DiaryDao diaryDao= MyApplication.getInstant().getDiaryDao();
//                            List<Diary> diaries =diaryDao.queryBuilder()
//                                    .limit(10)
//                                    .list();
//                            for (Diary diary:diaries){
//                                data.add(diary);
//                            }
//                            myAdapter.notifyDataSetChanged();
//                            refreshLayout.setRefreshing(false);
//                            break;
//                        case R.id.nav_menu_meeting:
//
//                            break;
//                        case R.id.nav_menu_note:
//                            NoteDao noteDao=MyApplication.getInstant().getNoteDao();
//                            List<Note>notes=noteDao.queryBuilder()
//                                    .limit(10)
//                                    .list();
//                           myAdapter.notifyDataSetChanged();
//                           refreshLayout.setRefreshing(false);
//                            break;
//                    }
//
//                }
//            });
//
//        }
//    }
//
//
//}
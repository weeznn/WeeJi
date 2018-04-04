package com.weeznn.weeji.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.R;
import com.weeznn.weeji.util.SimplePeople;

import java.lang.annotation.ElementType;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingPreEditFragment extends Fragment {
    private static final String TAG = MeetingPreEditFragment.class.getSimpleName();
    public static final String TAG_BACK = "MeetingPreEdit";
    public static final String FLAG_PEOPLES = "peoples";

    private static final String KEY_TITLE = "titleView";
    private static final String KEY_SUB = "sub";
    private static final String KEY_PEOPLELIST = "peopleList";

    //View
    private FloatingActionButton fab;
    private TextInputEditText titleView;
    private TextInputEditText subView;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ActionBar actionBar;

    //逻辑
    private List<SimplePeople> list;
    private String title;
    private String sub;
    private Fragment fragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        fragment=this;

        //该方法可以让toolbar的显示正常
        setHasOptionsMenu(true);
        //获取参会人信息
        String json = getArguments().getString(FLAG_PEOPLES);
        Log.i(TAG,"arg :"+json);
        list = SimplePeople.getListFromJson(json);

        if (savedInstanceState != null && !"".equals(savedInstanceState.getString(FLAG_PEOPLES))) {
            list.clear();
            String json1 = savedInstanceState.getString(KEY_PEOPLELIST);
            for (SimplePeople people : SimplePeople.getListFromJson(json1)) {
                list.add(people);
            }
            sub = savedInstanceState.getString(KEY_SUB);
            title = savedInstanceState.getString(KEY_TITLE);
        }
        Log.i(TAG, "onViewStateRestored");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_pre_edit, container, false);
        fab = view.findViewById(R.id.fab);
        titleView = view.findViewById(R.id.editText_title);
        subView = view.findViewById(R.id.editText_sub);
        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/4/3 添加动画
                Fragment peopleListFragment=new PeopleListFragment();
                Bundle bundle=new Bundle();
                bundle.putInt(PeopleListFragment.LIST_TYPE,PeopleListFragment.LIST_TYPE_SELETE);
                peopleListFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frameLayout,peopleListFragment , PeopleListFragment.TAG_BACK);
                transaction.addToBackStack(MeetingPreEditFragment.TAG_BACK);
                transaction.commit();
            }
        });
        //textview
        titleView.setText(title);
        subView.setText(sub);
        //recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new PreEditAdapter(getContext(), list));

        //toolbar
        // TODO: 2018/4/4 有时间升级
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        actionBar= ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.only_yes_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.yes){
            Intent intent=getActivity().getPackageManager().getLaunchIntentForPackage(getString(R.string.BAIDU_ASR_PACKAGE_NAME));
            if (intent!=null){
                Log.i(TAG,"GO TO BAIDU ASR");
                intent.putExtra("title",titleView.getText().toString());
                intent.putExtra("sub",subView.getText().toString());
                writePeoples2Stonge(list,titleView.getText().toString());
                startActivity(intent);
            }

            Log.i(TAG,"PRE EDIT DOWN");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 将参会人员的简单信息保存到磁盘
     * @param list
     */
    private void writePeoples2Stonge(final List<SimplePeople> list,final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json=SimplePeople.list2String(list);
                if (FileUtil.makeDir(FileUtil.FILE_TYPE_MEETING,title)){
                    FileUtil.WriteText(FileUtil.FILE_TYPE_MEETING,title,FileUtil.FILE_TYPE_JSON,json);
                }else {
                    Log.i(TAG,"未写入文件");
                }
            }
        });

    }

    private class PreEditAdapter extends RecyclerView.Adapter {

        private List<SimplePeople> data;
        private LayoutInflater inflater;

        public PreEditAdapter(Context context, List<SimplePeople> list) {
            this.data = list!=null?list:null;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_meeting_people, parent, false);
            return new PreEditViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final PreEditViewHolder viewHolder = (PreEditViewHolder) holder;
            SimplePeople people = data.get(position);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.btn.setVisibility(View.VISIBLE);
                }
            });

            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });

            RequestOptions options=new RequestOptions()
                    .placeholder(R.drawable.ic_user_black)
                    .circleCrop()
                    .error(R.drawable.ic_user_black);
            Glide.with(viewHolder.imageView)
                    .load(people.getPhoto())
                    .apply(options)
                    .into(viewHolder.imageView);

            viewHolder.name.setText(people.getName());
        }

        @Override
        public int getItemCount() {
            return data.isEmpty() ? 0 : data.size();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, titleView.getText().toString());
        outState.putString(KEY_SUB, subView.getText().toString());
        outState.putString(KEY_PEOPLELIST, SimplePeople.list2String(list));

        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null && !"".equals(savedInstanceState.getString(FLAG_PEOPLES))) {
//            list.clear();
//            String json1 = savedInstanceState.getString(KEY_PEOPLELIST);
//            for (SimplePeople people : SimplePeople.getListFromJson(json1)) {
//                list.add(people);
//            }
//            sub = savedInstanceState.getString(KEY_SUB);
//            title = savedInstanceState.getString(KEY_TITLE);
//        }
//        Log.i(TAG, "onViewStateRestored");
    }

    private class PreEditViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private Button btn;
        private ImageView imageView;
        private CardView cardView;

        public PreEditViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            btn = itemView.findViewById(R.id.btn);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card);
        }
    }

}

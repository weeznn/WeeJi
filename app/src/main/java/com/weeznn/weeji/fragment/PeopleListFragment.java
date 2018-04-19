package com.weeznn.weeji.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.DetailActivity;
import com.weeznn.weeji.adpater.PeopleListSeleteAdapter;
import com.weeznn.weeji.adpater.PeopleListShowAdapter;
import com.weeznn.weeji.interfaces.ItemClickListener;
import com.weeznn.weeji.util.SimplePeople;
import com.weeznn.weeji.util.db.entry.People;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleListFragment extends Fragment implements Constant {
    private static final String TAG = PeopleListFragment.class.getSimpleName();
    public static final String TAG_BACK = "PeopleList";


    public static final String LIST_TYPE = "list_type";
    public static final int LIST_TYPE_SHOW = 1;
    public static final int LIST_TYPE_SELETE = 2;


    private int listType;
    private static int count = 0;
    private List<People> list = new ArrayList<>();
    private static List<SimplePeople> selectList = new ArrayList<>();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            recyclerView.setVisibility(View.VISIBLE);
            lxr_null.setVisibility(View.GONE);
            toolbatText.setText(count + " | " + list.size());
            return true;
        }
    });


    //view
    private Toolbar toolbar;
    private TextView toolbatText;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TextView lxr_null;
    private RecyclerView.Adapter adapter;
    private MeetingPreEditFragment.SimpeopleListChangelistener listChangelistener;

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        list.add(new People(15513093092l, "qqqqqq", "qqqqqq", "qqqqqq", "qqqqqqqqqqqq", "qqqqqqqqqq"));
        list.add(new People(15513093092l, "wwwwwww", "wwwwwww", "wwwwwww", "wwwwwwwwwwwwww", "wwwwwww"));
        list.add(new People(15513093092l, "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss"));
        list.add(new People(15513093092l, "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee"));
        list.add(new People(15513093092l, "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz"));


        count = selectList.size();

        listType = getArguments().getInt(LIST_TYPE);
        Log.i(TAG, "onCreate listTyoe=" + listType);
        if (listType == LIST_TYPE_SELETE) {
            adapter = new PeopleListSeleteAdapter(getContext(), list, selectList);
            //设置人数的监听
            ((PeopleListSeleteAdapter) adapter).setPeopleListChangeListener(new PeopleListSeleteAdapter.PeopleListChangeInterface() {
                @Override
                public void add(int position) {
                    People p = list.get(position);
                    SimplePeople simplePeople = new SimplePeople(p.getName(), p.getPhoto(), p.getJob(), p.getCompany());
                    selectList.add(simplePeople);
                    count++;
                    toolbatText.setText(count + " | " + list.size());
                    Log.i(TAG, "add " + position + "  size:" + count);
                }

                @Override
                public void remove(int position) {
                    People p = list.get(position);
                    SimplePeople simplePeople = new SimplePeople(p.getName(), p.getPhoto(), p.getJob(), p.getCompany());
                    selectList.remove(simplePeople);
                    count--;
                    toolbatText.setText(count + " | " + list.size());
                    Log.i(TAG, "remove " + position + "  size:" + count);
                }
            });
        } else {
            adapter = new PeopleListShowAdapter(getContext(), list);
            ((PeopleListShowAdapter) adapter).setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(getString(R.string.LEFT_TYPE), CODE_PEO);
                    intent.putExtra(getString(R.string.LEFT_CODE), list.get(position).getPhone());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(int position) {

                }
            });
        }
    }

    public void setSeletedPeople(List<SimplePeople> list) {
        Log.i(TAG, "setSeletedPeople size:" + list.size());
        selectList = list;
        count = selectList.size();
    }

    public void setSeletedListChangeListener(MeetingPreEditFragment.SimpeopleListChangelistener listChangeListener) {
        this.listChangelistener = listChangeListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_list, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbatText = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.freshLayout);
        lxr_null = view.findViewById(R.id.list_null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
        //recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        if (list == null || 0 == list.size()) {
            recyclerView.setVisibility(View.GONE);
            lxr_null.setVisibility(View.VISIBLE);
        } else {
            lxr_null.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        //refreshlayout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updata();
                refreshLayout.setRefreshing(false);
            }
        });

        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                manager.popBackStack();
                transaction.show(manager.findFragmentByTag(MeetingPreEditFragment.TAG_BACK));
            }
        });

        toolbatText.setText(count + " | " + list.size());
        toolbatText.setGravity(View.TEXT_ALIGNMENT_CENTER);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.only_yes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish2PreEdit();
        return true;
    }

    /**
     * 更新数据
     */
    private void updata() {
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                List<People> resoult = MyApplication.getInstant().getPeopleDao()
                        .queryBuilder()
                        .list();
                if (resoult != null && !resoult.isEmpty()) {
                    list.addAll(resoult);
                }


                list.add(new People(15513093092l, "qqqqqq", "qqqqqq", "qqqqqq", "qqqqqqqqqqqq", "qqqqqqqqqq"));
                list.add(new People(15513093092l, "wwwwwww", "wwwwwww", "wwwwwww", "wwwwwwwwwwwwww", "wwwwwww"));
                list.add(new People(15513093092l, "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss"));
                list.add(new People(15513093092l, "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee"));
                list.add(new People(15513093092l, "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz"));
                Log.i(TAG, "updata list size " + list.size());

                adapter.notifyDataSetChanged();

                handler.sendMessage(new Message());
            }
        });
    }

    /**
     * 结束该fragment 返回
     */
    private void finish2PreEdit() {
        //移除本fragment
        Log.i(TAG, "finish2PreEdit  selected size " + selectList.size());
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        listChangelistener.changed(selectList);

        manager.popBackStack();

        transaction.show(manager.findFragmentByTag(MeetingPreEditFragment.TAG_BACK));
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
}



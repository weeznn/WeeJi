package com.weeznn.weeji.fragment;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CheckableImageButton;
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
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.adpater.PeopleListSeleteAdapter;
import com.weeznn.weeji.adpater.PeopleListShowAdapter;
import com.weeznn.weeji.util.SimplePeople;
import com.weeznn.weeji.util.db.PeopleDao;
import com.weeznn.weeji.util.db.entry.People;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleListFragment extends Fragment {
    private static final String TAG = PeopleListFragment.class.getSimpleName();
    public static final String TAG_BACK = "PeopleList";


    public static final String LIST_TYPE = "list_type";
    public static final int LIST_TYPE_SHOW = 1;
    public static final int LIST_TYPE_SELETE = 2;


    private int listType;
    private int count=0;
    private List<People> list = new ArrayList<>();
    private List<SimplePeople> selectList = new ArrayList<>();
    private Fragment fragment = this;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            recyclerView.setVisibility(View.VISIBLE);
            lxr_null.setVisibility(View.GONE);
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

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasOptionsMenu();

        list.add(new People("", "qqqqqq", "qqqqqq", "qqqqqq", "qqqqqqqqqqqq", "qqqqqqqqqq"));
        list.add(new People("", "wwwwwww", "wwwwwww", "wwwwwww", "wwwwwwwwwwwwww", "wwwwwww"));
        list.add(new People("", "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss"));
        list.add(new People("", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee"));
        list.add(new People("", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz"));


        count=selectList.size();

        listType = getArguments().getInt(LIST_TYPE);
        Log.i(TAG, "onCreate listTyoe=" + listType);
        if (listType == LIST_TYPE_SELETE) {
            adapter = new PeopleListSeleteAdapter(getContext(), list, selectList);
        } else {
            adapter = new PeopleListShowAdapter(getContext(), list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_people_list, container, false);
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

        //recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

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
        toolbatText.setText(count+" | " +list.size());
        toolbatText.setGravity(View.TEXT_ALIGNMENT_CENTER);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.only_yes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 2018/4/3 怎么发送simplePeople给PreEditFragment
        //移除本fragment
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.popBackStack();
        Fragment fragment = manager.findFragmentByTag(MeetingPreEditFragment.TAG_BACK);
        Bundle bundle = new Bundle();
        bundle.putString(MeetingPreEditFragment.FLAG_PEOPLES, SimplePeople.list2String(selectList));
        fragment.setArguments(bundle);
        transaction.show(fragment);
        return true;
    }

    private void updata() {
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                PeopleDao dao = MyApplication.getInstant().getPeopleDao();
                list.addAll(dao.queryBuilder().list());

                list.add(new People("", "qqqqqq", "qqqqqq", "qqqqqq", "qqqqqqqqqqqq", "qqqqqqqqqq"));
                list.add(new People("", "wwwwwww", "wwwwwww", "wwwwwww", "wwwwwwwwwwwwww", "wwwwwww"));
                list.add(new People("", "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss", "sssssssssss"));
                list.add(new People("", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee", "eeeeeeeeeeeee"));
                list.add(new People("", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz", "zzzzzzzzzzzzzz"));
                Log.i(TAG, "updata list size " + list.size());

                adapter.notifyDataSetChanged();

                handler.sendMessage(new Message());
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
}



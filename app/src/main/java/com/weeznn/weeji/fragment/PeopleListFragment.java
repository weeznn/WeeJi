package com.weeznn.weeji.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.util.SimplePeople;
import com.weeznn.weeji.util.db.PeopleDao;
import com.weeznn.weeji.util.db.entry.People;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleListFragment extends Fragment {

    private static final String TAG = PeopleListFragment.class.getSimpleName();
    public static final String TAG_BACK = "PeopleList";


    public static final String LIST_TYPE="list_type";
    public static final int LIST_TYPE_SHOW=1;
    public static final int LIST_TYPE_SELETE=2;


    private int listType;
    private int count;
    private List<People> list;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==1){
                list= (List<People>) msg.obj;
            }
            if (refreshLayout!=null&&refreshLayout.isRefreshing()){
                refreshLayout.setRefreshing(false);
            }
            return true;
        }
    });
    private Fragment fragment=this;

    //view
    private Toolbar toolbar;
    private TextView toolbatText;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listType=getArguments().getInt(LIST_TYPE);
        updata();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_people_list, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbatText = view.findViewById(R.id.text);
        recyclerView=view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.freshLayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updata();
            }
        });

        if (list==null){
            getActivity().getWindowManager().removeView(recyclerView);
            ImageView imageView=new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackground(getResources().getDrawable(R.drawable.ic_404));
            getActivity().getWindowManager().addView(imageView,refreshLayout.getLayoutParams());
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new PeopleListAdapter(getContext(),list,listType));
        }


        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
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
        getActivity().getMenuInflater().inflate(R.menu.only_yes_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 2018/4/3 怎么发送simplePeople给PreEditFragment
        //移除本fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        return true;
    }

    private void updata(){
        MyApplication.getInstant().runInTx(new Runnable() {
            @Override
            public void run() {
                PeopleDao dao= MyApplication.getInstant().getPeopleDao();
                List<People>list=dao.queryBuilder().list();
                Message message=new Message();
                message.what=1;
                message.obj=list;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * adapter
     */
    private class PeopleListAdapter extends RecyclerView.Adapter {
        private final String TAG = PeopleListAdapter.class.getSimpleName();

        private LayoutInflater inflater;
        private List<People> data;
        private List<SimplePeople> simpleData;

        private int listType;

        public PeopleListAdapter(Context context, List<People> list, int type) {
            if (list!=null){
                this.data = list;
            }else {
                this.data=null;
            }

            this.listType = type;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            if (listType ==LIST_TYPE_SHOW) {
                view = inflater.inflate(R.layout.item_people_list_show, parent, false);
                return new PeopleShow(view);
            } else {
                view = inflater.inflate(R.layout.item_people_list_select, parent, false);
                return new PeopleSelete(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (listType == LIST_TYPE_SHOW) {
                bindShowViewHolder(holder, position);
            } else {
                bindSeleteViewHolder(holder, position);
            }
        }

        @SuppressLint("RestrictedApi")
        private void bindSeleteViewHolder(RecyclerView.ViewHolder holder, int position) {
            final PeopleSelete view = (PeopleSelete) holder;
            final People people = data.get(position);


            Glide.with(view.imageView)
                    .load(people.getPhoto())
                    .into(view.imageView);

            view.chrckBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckableImageButton btn = (CheckableImageButton) v;
                    if (btn.isChecked()) {
                        Log.i(TAG,"被选中，添加");
                        @SuppressLint("StringFormatMatches")
                        //修改toobar中textview 选择的人数
                        String s = String.format(getString(R.string.toolbar_text_select, ++count));
                        toolbatText.setText(s);
                        //添加到simpledata中
                        simpleData.add(new SimplePeople(people.getName(),people.getPhoto()));
                    }else {
                        Log.i(TAG,"被删除");
                        //修改toobar中textview 选择的人数
                        @SuppressLint("StringFormatMatches")
                        String s = String.format(getString(R.string.toolbar_text_select, --count));
                        toolbatText.setText(s);
                        //删除simpledata中
                        simpleData.remove(new SimplePeople(people.getName(),people.getPhoto()));
                    }


                }
            });

            view.name.setText(people.getName());
            view.job.setText(people.getJob());

            view.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckableImageButton view1=v.findViewById(R.id.check);
                    if (view1.isChecked()) {
                        Log.i(TAG,"被选中，添加");
                        @SuppressLint("StringFormatMatches")
                        //修改toobar中textview 选择的人数
                                String s = String.format(getString(R.string.toolbar_text_select, ++count));
                        toolbatText.setText(s);
                        //添加到simpledata中
                        simpleData.add(new SimplePeople(people.getName(),people.getPhoto()));
                    }else {
                        Log.i(TAG,"被删除");
                        //修改toobar中textview 选择的人数
                        @SuppressLint("StringFormatMatches")
                        String s = String.format(getString(R.string.toolbar_text_select, --count));
                        toolbatText.setText(s);
                        //删除simpledata中
                        simpleData.remove(new SimplePeople(people.getName(),people.getPhoto()));
                    }
                }
            });
        }

        private void bindShowViewHolder(RecyclerView.ViewHolder holder, int position) {
            PeopleShow view= (PeopleShow) holder;
            People people=data.get(position);

            view.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2018/4/3 跳转联系人详情
                }
            });

            view.job.setText(people.getCompany()+" | "+people.getJob());
            view.name.setText(people.getName());

            Glide.with(view.imageView)
                    .load(people.getPhoto())
                    .into(view.imageView);
        }

        @Override
        public int getItemCount() {
            return data==null?0:data.size();
        }

        public List<SimplePeople> getSimpleData() {
            return simpleData;
        }
    }

    /**
     * show ViewHolder
     */
    private class PeopleShow extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView job;
        private ConstraintLayout layout;

        public PeopleShow(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            job = itemView.findViewById(R.id.job);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    /**
     * Selete  ViewHolder
     */
    private class PeopleSelete extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView job;
        private ConstraintLayout layout;
        private CheckableImageButton chrckBtn;

        public PeopleSelete(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            job = itemView.findViewById(R.id.job);
            layout = itemView.findViewById(R.id.layout);
            chrckBtn = itemView.findViewById(R.id.check);
        }
    }

}

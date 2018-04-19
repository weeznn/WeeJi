package com.weeznn.weeji.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.weeznn.mylibrary.utils.Constant;
import com.weeznn.mylibrary.utils.FileUtil;
import com.weeznn.weeji.R;
import com.weeznn.weeji.activity.ASRActivity;
import com.weeznn.weeji.util.SimplePeople;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingPreEditFragment extends Fragment implements Constant {
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
    private List<SimplePeople> data = new LinkedList<>();
    private String title;
    private String sub;
    private Fragment fragment;
    private PreEditAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        //该方法可以让toolbar的显示正常
        setHasOptionsMenu(true);
        fragment = this;

        data.clear();

        if (savedInstanceState != null && !"".equals(savedInstanceState.getString(FLAG_PEOPLES))) {
            String json1 = savedInstanceState.getString(KEY_PEOPLELIST);
            data.addAll(SimplePeople.getListFromJson(json1));
            sub = savedInstanceState.getString(KEY_SUB);
            title = savedInstanceState.getString(KEY_TITLE);
        }

        adapter = new PreEditAdapter(getContext(), data);
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
                Fragment peopleListFragment = new PeopleListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PeopleListFragment.LIST_TYPE, PeopleListFragment.LIST_TYPE_SELETE);
                peopleListFragment.setArguments(bundle);
                ((PeopleListFragment) peopleListFragment).setSeletedPeople(data);

                ((PeopleListFragment) peopleListFragment).setSeletedListChangeListener(new SimpeopleListChangelistener() {
                    @Override
                    public void changed(List<SimplePeople> list) {
                        Log.i(TAG, "data  changed size:" + list.size());

                        data.clear();
                        data.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(MeetingPreEditFragment.TAG_BACK)
                        .remove(fragment)
                        .setCustomAnimations(R.animator.fragment_enter_from_bottom, R.animator.fragment_exit_to_left)
                        .add(R.id.frameLayout, peopleListFragment, PeopleListFragment.TAG_BACK)
                        .commit();
            }
        });
        //textview
        titleView.setText(title);
        subView.setText(sub);
        //recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
                manager.beginTransaction().show(manager.findFragmentByTag(MettingFragment.TAG_BACK));

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, titleView.getText().toString());
        outState.putString(KEY_SUB, subView.getText().toString());
        outState.putString(KEY_PEOPLELIST, SimplePeople.list2String(data));

        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.only_yes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.yes) {
            String json = SimplePeople.list2String(data);
            //转到百度语音
            Intent intent = new Intent(getActivity(), ASRActivity.class);
            if (intent != null) {
                Log.i(TAG, "GO TO BAIDU ASR");
                intent.putExtra("title", titleView.getText().toString());
                intent.putExtra("sub", subView.getText().toString());
                intent.putExtra("type", FileUtil.FILE_TYPE_MEETING);
                intent.putExtra("peoples", json);

                getActivity().startActivityForResult(intent, REQUEST_CODE_MET);
            }

            Log.i(TAG, "PRE EDIT DOWN");
        }
        return true;
    }


    private class PreEditAdapter extends RecyclerView.Adapter<PreEditViewHolder> {
        private final String TAG = PreEditAdapter.class.getSimpleName();

        private List<SimplePeople> data;
        private LayoutInflater inflater;

        public PreEditAdapter(Context context, List<SimplePeople> list) {
            this.data = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public PreEditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_meeting_people, parent, false);
            return new PreEditViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PreEditViewHolder viewHolder, final int position) {
            SimplePeople people = data.get(position);

            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.btn.setVisibility(View.VISIBLE);
                }
            });

            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    getView().setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            });

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_user_black)
                    .circleCrop()
                    .error(R.drawable.ic_user_black);
            Glide.with(viewHolder.imageView)
                    .load(people.getPhoto())
                    .apply(options)
                    .into(viewHolder.imageView);

            viewHolder.name.setText(people.getName());
            viewHolder.job.setText(people.getCompany() + " | " + people.getJob());
        }


        @Override
        public int getItemCount() {
            return data.isEmpty() ? 0 : data.size();
        }
    }

    public class PreEditViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView job;
        public Button btn;
        public ImageView imageView;
        public ConstraintLayout layout;

        public PreEditViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            job = itemView.findViewById(R.id.job);
            btn = itemView.findViewById(R.id.btn);
            imageView = itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.card);
        }
    }


    public interface SimpeopleListChangelistener {
        void changed(List<SimplePeople> list);
    }

}

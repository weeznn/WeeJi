package com.weeznn.weeji.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.weeznn.weeji.MyApplication;
import com.weeznn.weeji.R;
import com.weeznn.weeji.util.db.PeopleDao;
import com.weeznn.weeji.util.db.entry.People;

import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONObject;

public class PeopleDetailFragment extends Fragment {
    public static final String TAG = PeopleDetailFragment.class.getSimpleName();
    public static final String FLAG_BACK="PeopleDetail";
    public static final String FLAG_ARG_PEOPLE_INFO="info";

    public static final int FAB_EDIT = 1;
    public static final int FAB_DOWN = 2;

    private String name;
    private String job;
    private String number;
    private String email;
    private String photo;
    private String company;

    private int fabState=FAB_EDIT;
    private boolean isSelf=true;


    //view
    private ImageView photoView;
    private TextInputEditText nameView;
    private KeyListener namelistener;
    private TextInputEditText jobView;
    private KeyListener joblistener;
    private TextInputEditText numberView;
    private KeyListener numberlistener;
    private TextInputEditText emailView;
    private KeyListener emaillistener;
    private TextInputEditText companyView;
    private KeyListener companylistener;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()==null){
            isSelf=true;
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences(getString(R.string.SharedPreferences_name),0);
            name = sharedPreferences.getString(getString(R.string.pref_self_name),getString(R.string.pref_self_def_name));
            photo = sharedPreferences.getString(getString(R.string.pref_self_photo),getString(R.string.pref_self_def_photo));
            job = sharedPreferences.getString(getString(R.string.pref_self_job),getString(R.string.pref_self_def_job));
            company = sharedPreferences.getString(getString(R.string.pref_self_company),getString(R.string.pref_self_def_company));
            number=sharedPreferences.getString(getString(R.string.pref_self_number),getString(R.string.pref_self_def_number));
            email=sharedPreferences.getString(getString(R.string.pref_self_email),getString(R.string.pref_self_def_email));
        }else {
            isSelf=false;
            Bundle bundle=getArguments().getBundle(FLAG_ARG_PEOPLE_INFO);
            name=bundle.getString(getString(R.string.pref_self_name));
            photo = bundle.getString(getString(R.string.pref_self_photo));
            job = bundle.getString(getString(R.string.pref_self_job));
            company = bundle.getString(getString(R.string.pref_self_company));
            number=bundle.getString(getString(R.string.pref_self_number));
            email=bundle.getString(getString(R.string.pref_self_email));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_detail, container, false);
        photoView=view.findViewById(R.id.photo);

        nameView=view.findViewById(R.id.name);
        namelistener=nameView.getKeyListener();
        nameView.setKeyListener(null);

        jobView=view.findViewById(R.id.job);
        joblistener=jobView.getKeyListener();
        jobView.setKeyListener(null);

        companyView=view.findViewById(R.id.company);
        companylistener=companyView.getKeyListener();
        companyView.setKeyListener(null);

        emailView=view.findViewById(R.id.email);
        emaillistener=emailView.getKeyListener();
        emailView.setKeyListener(null);

        numberView=view.findViewById(R.id.number);
        numberlistener=numberView.getKeyListener();
        numberView.setKeyListener(null);

        fab=view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(photoView)
                .load(photo);

        nameView.setText(name);
        numberView.setText(number);
        emailView.setText(email);
        companyView.setText(company);
        jobView.setText(job);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FAB_EDIT==fabState){
                    //编辑
                    edit();
                }else {
                    //编辑完毕
                    down();
                }
            }
        });

    }

    private void down() {
        // 完成编辑，校验，传送
        name=nameView.getText().toString();
        number=numberView.getText().toString();
        email=emailView.getText().toString();
        job=jobView.getText().toString();
        company=companyView.getText().toString();
        // TODO: 2018/4/4 photo 的地址待获取
        if (isSelf){
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences(getString(R.string.SharedPreferences_name),0);
            String json="{"+
                            "\"people\":"+
                            "[{"+
                                "\"name\":"+ "\""+name+"\","+
                                "\"photo\":"+"\""+photo+"\""+
                            "}]"+
                        "}";
            sharedPreferences.edit().putString(getString(R.string.pref_self_name),name)
                    .putString(getString(R.string.pref_self_number),number)
                    .putString(getString(R.string.pref_self_email),email)
                    .putString(getString(R.string.pref_self_company),company)
                    .putString(getString(R.string.pref_self_job),job)
                    .putString(getString(R.string.pref_sim_self_json),json)
                    .apply();
        }else {
            People people=new People(number,name,email,photo,company,job);
            PeopleDao dao= MyApplication.getInstant().getPeopleDao();
            if (dao.queryBuilder().where(PeopleDao.Properties.Phone.eq(number))==null){
                dao.insert(people);
            }else {
                dao.update(people);
            }
        }
        fabState=FAB_EDIT;
        changeFABState();

        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this);
    }

    private void edit() {
        fabState=FAB_DOWN;
        changeFABState();

        nameView.setKeyListener(namelistener);
        numberView.setKeyListener(numberlistener);
        emailView.setKeyListener(emaillistener);
        companyView.setKeyListener(companylistener);
        jobView.setKeyListener(joblistener);


    }

    private void changeFABState() {
        if (fabState==FAB_EDIT){
            fab.setBackgroundResource(R.drawable.ic_edit);
        }else {
            fab.setBackgroundResource(R.drawable.ic_yes);
        }
    }


}

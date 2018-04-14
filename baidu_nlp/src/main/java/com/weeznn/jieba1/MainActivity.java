package com.weeznn.jieba1;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import com.weeznn.jieba1.R;

import com.weeznn.jieba1.baidu_nlp.BaiduNlp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private EditText editText;
    private Button button;
    private List<String> keylist = new ArrayList<>();
    private List<String> interPointlist = new ArrayList<>();
    private View correntView;
    String text = "信息技术的发展日新月异,人们获得信息的渠道也是多种多样,用户如何从海量的信息中获得" +
            "所需要的摘要信息已经成为当今信息领域研究的一个热门话题。现如今,每天的会议数不胜数,且由于会" +
            "议的本身自发性的特征,从而很容易出现一些与会议内容无关的话题,人们若是花费时间去参加这些会议或者浏" +
            "览会议全部内容必定浪费大量的时间,若是人们能够通过浏览会议摘要就可以获得会议有效信息的话,不" +
            "仅能够节约冗长的参会时间,而且也提高了信息访问的工作效率。自动会议摘要提取系统使得用户仅仅通" +
            "过浏览会议摘要就可以知道会议的内容,从而避免了用户访问冗余信息,该系统目前引起了研究者的广泛关注。" +
            "提取技术主要包括有监督学习方法和无监督学习方法,在使用有监督学习方法进行会议摘要提取时,摘" +
            "要提取的任务被视为一个二元分类问题旨在决定一个句子是否为摘要句。有监督学习方法重点描述了支持向量机" +
            "SVM方法,SVM二元分类方法是近几年广泛使用的分类方法,SVM在很多二元分类任务中具有较好的性能,然" +
            "而那些接近分类面的样本本身不具备划分摘要句与非摘要句的明显特征,虽然这些样本的置信值有大小区分," +
            "但是仍然是处于分类上的一个模糊地带,相互之间不存在明显的优先级。针对SVM存在的一些缺陷," +
            "后又使用MMR方法对SVM进行了后处理,并通过实验表明MMR方法不仅可以去除摘要中的冗余信息," +
            "而且相比单纯使用SVM方法进行摘要提取时性能更高。本文以会议文本为处理对象,以SVM和MMR摘要提取算法为基础" +
            ",针对当前会议是围绕某个特定的主题进行讨论和交流的特点,提出了一种基于主题的MMR与SVM相融合的会议" +
            "摘要提取方法。这种方法以主题关键词为依据进行打分,并对MMR打分方法进行了改进的同时也兼顾句子位置特" +
            "征等信息进行评分及重要性排序,使用ROUGE值评估方法进行摘要提取性能的评估。前人的研究中针对会议摘要提" +
            "取算法已经给出了大量的改进算法和不同的评估方法。结果表明,将基于主题的MMR与SVM相融合的摘要系统分别与" +
            "SVM摘要系统、MMR摘要系统以及SVM与MMR相结合摘要系统进行对比时,前者提取的摘要效果更好。大部分的研" +
            "究是针对会议摘要提取算法的改进和使用,有的研究使用有监督摘要提取方法进行摘要句与非摘要句分类来提取" +
            "会议摘要,有的研究者认为,会议语料库是庞大的,人为的标注也是费时费力的,因此他们使用无监督摘要提取方法" +
            "进行会议摘要,也有的结合了有监督学习方法和无监督学习方法优点,提出了一种半监督学习方法来提取会议摘要," +
            "也有的研究者将多种算法进行结合进行摘要的提取等等。根据会议所具有的自发语音的特征进行研究的少之又少。" +
            "本研究根据会议所特有的特征,提出一种基于主题的会议摘要提取方法,该方法主要是沿着一个会议中某一" +
            "个或者若干个特定话题进行摘要句的提取,实验结果表明,基于主题的摘要提取方法在会议文本中表现出了较高" +
            "的性能与优点,更便于用户的阅读与理解。";


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            View popview = LayoutInflater.from(MainActivity.this).inflate(R.layout.popupwindowlist, null);
            PopupWindow popupWindow = new PopupWindow(MainActivity.this);
            popupWindow.setContentView(popview);
            popupWindow.setHeight(getResources().getDisplayMetrics().heightPixels * 2 / 3);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setAnimationStyle(R.style.popupanimation);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);
                }
            });
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.3f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);

            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.LEFT, 0, 0);


            RecyclerView recyclerViewH = popview.findViewById(R.id.recycler_h);
            LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerViewH.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewH.setLayoutManager(layoutManager);
            recyclerViewH.addItemDecoration(new DividerItemDecoration(recyclerViewH.getContext(), DividerItemDecoration.HORIZONTAL));
            recyclerViewH.setAdapter(new ItemAdapter(keylist, recyclerViewH.getContext(), ItemAdapter.TYPE.RecyclerH));

            RecyclerView recyclerViewV = popview.findViewById(R.id.recycler_v);
            recyclerViewV.setLayoutManager(new LinearLayoutManager(recyclerViewV.getContext()));
            recyclerViewV.addItemDecoration(new DividerItemDecoration(recyclerViewH.getContext(), DividerItemDecoration.VERTICAL));
            recyclerViewV.setAdapter(new ItemAdapter(keylist, recyclerViewV.getContext(), ItemAdapter.TYPE.RecyclerV));

            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textview);
        button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                char[] des = new char[15];
                text.getChars(0, 14, des, 0);
                final String title = des.toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        keylist = BaiduNlp.keyWord(title, text);
                        keylist.addAll(BaiduNlp.topic(title, text));
                        Log.i(TAG, "key list size:" + keylist.size());
                        handler.sendEmptyMessage(1);
                    }

                }).start();

            }
        });

    }

}

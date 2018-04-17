package com.weeznn.weeji.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.weeznn.mylibrary.utils.baidu_nlp.BaiduNlp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NLPIntentService extends IntentService {
    private static final String TAG=NLPIntentService.class.getSimpleName();
    private static final String ACTION_CLIPWORD = "com.weeznn.weeji.service.action.ClipWord";
    private static final String ACTION_SUM = "com.weeznn.weeji.service.action.Sum";
    private static final String ACTION_PREPARE = "com.weeznn.weeji.service.action.Prepare";

    // TODO: Rename parameters
    private static final String SENTENCE = "com.weeznn.weeji.service.extra.PARAM1";


    //专业名词
    static Set<String> wordSet=new HashSet<>();

    public NLPIntentService() {
        super("NLPIntentService");
    }

    /**
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionClipWord(Context context, String param1) {
        Intent intent = new Intent(context, NLPIntentService.class);
        intent.setAction(ACTION_CLIPWORD);
        intent.putExtra(SENTENCE, param1);
        context.startService(intent);
    }

    /**
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSUM(Context context) {
        Intent intent = new Intent(context, NLPIntentService.class);
        intent.setAction(ACTION_SUM);
        context.startService(intent);
    }

    public static void startActionPrepare(Context context){
        Intent intent=new Intent(context,NLPIntentService.class);
        intent.setAction(ACTION_PREPARE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CLIPWORD.equals(action)) {
                final String param1 = intent.getStringExtra(SENTENCE);
                handleActionClipWord(param1);
            } else if (ACTION_SUM.equals(action)) {
                handleActionSum();
            }else if (ACTION_PREPARE.equals(action)){
                handleActionPrepare();
            }
        }
    }

    /**
     * 准备加载停用词，语气词
     */
    private void handleActionPrepare() {
        Log.i(TAG,"handleActionPrepare");

    }

    /**
     * 将句子进行分词，去停用词，统计词汇空间
     * @param param1 待分词的句子
     */
    private void handleActionClipWord(String param1) {
        Log.i(TAG,"handleActionClipWord");
        wordSet.addAll(BaiduNlp.lexer(param1));
    }

    /**
     * 返回整篇的专业名词
     * @return
     */
    public static Set<String> getWordSet() {
        return wordSet;
    }

    /**
     * 使用MMR算法进行自动文本摘要
     */
    private void handleActionSum() {
        Log.i(TAG,"handleActionSum");

    }
}

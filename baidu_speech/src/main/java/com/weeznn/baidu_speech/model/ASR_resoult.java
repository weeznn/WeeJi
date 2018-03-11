package com.weeznn.baidu_speech.model;

/**
 * 百度语音识别结果回调
 * Created by weeznn on 2018/3/10.
 */

public class ASR_resoult {
//    {
//        "results_recognition": ["开始事件的参数，"],
//        "origin_result": {
//        "corpus_no": 6531300288656158529,
//                "err_no": 0,
//                "result": {
//            "word": ["开始事件的参数，"]
//        },
//        "sn": "50d6ebee-314b-4882-b0df-234509c103b5_s-0"
//    },
//        "error": 0,
//            "best_result": "开始事件的参数，",
//            "result_type": "final_result"
//    }

    private String results_recognition;
    private int error;
    private String best_result;
    private String result_type;

    private class origin_result{

    }
}



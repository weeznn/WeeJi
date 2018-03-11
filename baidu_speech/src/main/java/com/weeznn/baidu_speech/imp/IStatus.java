package com.weeznn.baidu_speech.imp;

/**
 * Created by weeznn on 2018/3/8.
 */

public interface IStatus {
    int STATUS_NONE = 2;

    int STATUS_READY = 3;
    int STATUS_SPEAKING = 4;
    int STATUS_RECOGNITION = 5;

    int STATUS_FINISHED = 6;
    int STATUS_STOPPED = 10;

    int STATUS_WAITING_READY = 8001;
    int WHAT_MESSAGE_STATUS = 9001;

    int STATUS_WAKEUP_SUCCESS = 7001;
    int STATUS_WAKEUP_EXIT = 7003;

    String LANGEWAGE= "_langewage";
    int LANGEWAGE_PUTONGHUA=5;
    int LANGEWAGE_SICHUANHUA=8;
    int LANGEWAGE_YUEYU=6;
    int LANGEWAGE_ENGLISH=7;

    String AppName="weeji";
}

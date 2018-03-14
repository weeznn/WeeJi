package com.weeznn.baidu_speech.imp;

/**
 * Created by weeznn on 2018/3/12.
 */

public interface CONS {
    public final String APPNAME="WeeJi";

    //文件类型
    public final String FILE_TYPE_CODE="FILE_TYPE";
    public final String FILE_NAME_CODE="FILE_NAME";
    public final String FILE_TYPE_MEETING="MEETING";
    public final String FILE_TYPE_DIARY="DIARY";
    public final String FILE_TYPE_NOTE="NOTE";

    //ASR 垂直领域
    String PROP= "_prop";
    public final int HOTWORD=10005;
    public final int MAP=10060;
    public final int MUSIC=10001;
    public final int VIDEO=10002;
    public final int APPLICATION=10003;
    public final int WEB=10004;
    public final int SHOP=10006;
    public final int HEALTH=10007;
    public final int CALL=10008;
    public final int MEDICAL=10052;
    public final int CAR=10053;
    public final int ENTERTAINMENT=10054;
    public final int FINANCE=10055;
    public final int GAME=10056;
    public final int FOOD=10057;
    public final int HELP=10058;
    public final int INPUT=20000;

    //语言
    String LANGEWAGE= "_language";
    String LANGEWAGE_PUTONGHUA="PUTONGHUA";
    String LANGEWAGE_SICHUANHUA="SICHUANHUA";
    String LANGEWAGE_YUEYU="YUEYU";
    String LANGEWAGE_ENGLISH="ENGLISH";

    String AUDIO="_audio";
    String RING="_ring";
    String ANIM="_anim";



}

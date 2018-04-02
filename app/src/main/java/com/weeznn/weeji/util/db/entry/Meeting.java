package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weeznn on 2018/3/14.
 */

@Entity
public class Meeting {
    @Id
    private long _metID;
    private String time;
    private String title;
    private String sub;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String address;
    private String modetator;
    @Generated(hash = 1926001165)
    public Meeting(long _metID, String time, String title, String sub,
            String keyword1, String keyword2, String keyword3, String address,
            String modetator) {
        this._metID = _metID;
        this.time = time;
        this.title = title;
        this.sub = sub;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.address = address;
        this.modetator = modetator;
    }

    @Generated(hash = 171861101)
    public Meeting() {
    }

    public long get_metID() {
        return this._metID;
    }
    public void set_metID(long _metID) {
        this._metID = _metID;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSub() {
        return this.sub;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }
    public String getKeyword1() {
        return this.keyword1;
    }
    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }
    public String getKeyword2() {
        return this.keyword2;
    }
    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }
    public String getKeyword3() {
        return this.keyword3;
    }
    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getModetator() {
        return this.modetator;
    }
    public void setModetator(String modetator) {
        this.modetator = modetator;
    }


}

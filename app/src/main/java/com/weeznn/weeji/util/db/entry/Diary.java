package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by weeznn on 2018/3/22.
 */
@Entity
public class Diary {
    private static final String TAG=Diary.class.getSimpleName();

    @Id
    private long _DAIID;
    private String date;
    private String address;
    private int mood;
    private String image;
    @Generated(hash = 59300648)
    public Diary(long _DAIID, String date, String address, int mood, String image) {
        this._DAIID = _DAIID;
        this.date = date;
        this.address = address;
        this.mood = mood;
        this.image = image;
    }

    @Generated(hash = 112123061)
    public Diary() {
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getMood() {
        return this.mood;
    }
    public void setMood(int mood) {
        this.mood = mood;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long get_DAIID() {
        return this._DAIID;
    }

    public void set_DAIID(long _DAIID) {
        this._DAIID = _DAIID;
    }

}

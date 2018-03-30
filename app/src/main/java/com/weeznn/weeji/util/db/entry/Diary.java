package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weeznn on 2018/3/22.
 */
@Entity
public class Diary {
    private static final String TAG=Diary.class.getSimpleName();

    private String date;
    private String address;
    private int mood;
    @Generated(hash = 1915017728)
    public Diary(String date, String address, int mood) {
        this.date = date;
        this.address = address;
        this.mood = mood;
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

}

package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weeznn on 2018/3/30.
 */

@Entity
public class Other {
    @Id
    private long _otherID;
    private String time;
    private String address;
    private int type;
    private String title;
    private String sub;
    @Generated(hash = 23787411)

    public Other(long _otherID, String time, String address, int type, String title,
                 String sub) {
        this._otherID = _otherID;
        this.time = time;
        this.address = address;
        this.type = type;
        this.title = title;
        this.sub = sub;
    }
    @Generated(hash = 331122991)
    public Other() {
    }
    public long get_otherID() {
        return this._otherID;
    }
    public void set_otherID(long _otherID) {
        this._otherID = _otherID;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
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
}

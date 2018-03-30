package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weeznn on 2018/3/30.
 */

@Entity
public class Note {
    @Id
    private long _noteID;
    private String time;
    private String cache;
    private String sub;
    private int source;
    @Generated(hash = 1463841500)
    public Note(long _noteID, String time, String cache, String sub, int source) {
        this._noteID = _noteID;
        this.time = time;
        this.cache = cache;
        this.sub = sub;
        this.source = source;
    }
    @Generated(hash = 1272611929)
    public Note() {
    }
    public long get_noteID() {
        return this._noteID;
    }
    public void set_noteID(long _noteID) {
        this._noteID = _noteID;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getCache() {
        return this.cache;
    }
    public void setCache(String cache) {
        this.cache = cache;
    }
    public String getSub() {
        return this.sub;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }
    public int getSource() {
        return this.source;
    }
    public void setSource(int source) {
        this.source = source;
    }

}

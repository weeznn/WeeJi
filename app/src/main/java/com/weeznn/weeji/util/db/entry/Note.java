package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weeznn on 2018/4/12.
 */

@Entity
public class Note {
    @Id
    private long _noteID;
    private String time;
    private String image;
    private String sub;
    private String source;
    @Generated(hash = 715076257)
    public Note(long _noteID, String time, String image, String sub,
            String source) {
        this._noteID = _noteID;
        this.time = time;
        this.image = image;
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
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getSub() {
        return this.sub;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }
    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
}

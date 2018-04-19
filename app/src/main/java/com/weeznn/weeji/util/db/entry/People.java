package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weeznn on 2018/4/19.
 */

@Entity
public class People {

    @Id
    private long phone;
    private String name;
    private String email;
    private String photo;
    private String company;
    private String job;

    @Generated(hash = 1222984958)
    public People(long phone, String name, String email, String photo,
            String company, String job) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.company = company;
        this.job = job;
    }
    @Generated(hash = 1406030881)
    public People() {
    }
    public long getPhone() {
        return this.phone;
    }
    public void setPhone(long phone) {
        this.phone = phone;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getCompany() {
        return this.company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getJob() {
        return this.job;
    }
    public void setJob(String job) {
        this.job = job;
    }
}

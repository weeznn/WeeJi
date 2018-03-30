package com.weeznn.weeji.util.db.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by weeznn on 2018/3/30.
 */

@Entity
public class People {
    private String phone;
    private String name;
    private String email;
    private String photo;
    private String company;
    private String job;
    @Generated(hash = 555995750)
    public People(String phone, String name, String email, String photo,
                  String company, String job) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.company = company;
        this.job = job;
    }
    @Generated(hash = 1277389469)
    public People() {
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
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

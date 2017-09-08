package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VodLecturer implements Serializable {

    private Long userId;

    private String creater;

    private Byte state;

    private Date createTime;

    public VodLecturer() {

    }

    public VodLecturer(Long userId, String creater, Byte state) {
        this.userId = userId;
        this.creater = creater;
        this.state = state;
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

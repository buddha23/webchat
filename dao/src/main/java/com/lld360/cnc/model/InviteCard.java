package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InviteCard implements Serializable {

    private Long id;

    private Byte type;

    private Long objectId;

    private String inviteCode;

    private Byte state;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    public InviteCard() {

    }

    public InviteCard(Byte type, Long objectId) {
        this.type = type;
        this.objectId = objectId;
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
        this.updateTime = Calendar.getInstance(Locale.CHINA).getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

package com.lld360.cnc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

/**
 * 文档版主
 */
public class Moderator implements Serializable{

    private Integer categoryId;

    private Long userId;

    private Integer authorizerId;

    private Timestamp createTime;

    private Integer state;

    private String remarks;

    public Moderator() {
    }

    public Moderator(Integer categoryId, Long userId, Integer authorizerId, Integer state) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.authorizerId = authorizerId;
        this.state = state;
        this.createTime = new Timestamp(Calendar.getInstance(Locale.CHINA).getTimeInMillis());
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAuthorizerId() {
        return authorizerId;
    }

    public void setAuthorizerId(Integer authorizerId) {
        this.authorizerId = authorizerId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}

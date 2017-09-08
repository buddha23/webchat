package com.lld360.cnc.model;


import java.io.Serializable;
import java.util.Date;

public class UserClickHabit implements Serializable {

    private Long id;

    private Long userId;

    private String site;

    private Date createTime;

    private String method;

    private String ip;

    public UserClickHabit() {

    }

    public UserClickHabit(Long userId, String site, String method, String ip) {
        this.userId = userId;
        this.site = site;
        this.method = method;
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserLoginHistory implements Serializable {

    private Long id;
    /*
    * 用户Id
    * */
    private Long userId;
    /*
    * IP
    * */
    private String ip;
    /*
    * 登录平台
    * */
    private String platform;
    /*
    * 登录时间
    * */
    private Date loginTime;

    public UserLoginHistory() {}

    public UserLoginHistory(Long userId, String ip, String platform) {
        this.userId = userId;
        this.platform = platform;
        this.ip = ip;
        this.setLoginTime(Calendar.getInstance(Locale.CHINA).getTime());
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}

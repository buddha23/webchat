package com.lld360.cnc.dto;

import com.lld360.cnc.model.UserLoginHistory;

import java.util.Date;

public class UserLoginHistoryDto extends UserLoginHistory {

    private String mobile;
    private String userNickname;
    private Date lastLogin;
    private Long pcLoginNum;
    private Long mobileLoginNum;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getPcLoginNum() {
        return pcLoginNum;
    }

    public void setPcLoginNum(Long pcLoginNum) {
        this.pcLoginNum = pcLoginNum;
    }

    public Long getMobileLoginNum() {
        return mobileLoginNum;
    }

    public void setMobileLoginNum(Long mobileLoginNum) {
        this.mobileLoginNum = mobileLoginNum;
    }
}

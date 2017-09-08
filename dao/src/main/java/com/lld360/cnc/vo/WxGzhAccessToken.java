package com.lld360.cnc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;

/**
 * 微信公众号AccessToken对象
 */
public class WxGzhAccessToken {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;
    private Long expireTime = 0L;

    private Integer errcode;
    private String errmsg;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        this.expireTime = Calendar.getInstance().getTimeInMillis() + expiresIn * 1000;
    }

    public boolean isExpired() {
        return Calendar.getInstance().getTimeInMillis() > this.expireTime;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}

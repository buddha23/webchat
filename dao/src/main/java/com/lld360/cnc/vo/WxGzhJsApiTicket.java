package com.lld360.cnc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Author: dhc
 * Date: 2016-07-22 15:28
 */
public class WxGzhJsApiTicket implements Serializable {
    private String ticket;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    private Integer errcode;
    private String errmsg;

    private Long expireTime = 0L;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        this.expireTime = Calendar.getInstance().getTimeInMillis() + expiresIn * 1000;
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

    public boolean isExpired() {
        return Calendar.getInstance().getTimeInMillis() > this.expireTime;
    }
}

package com.lld360.weixin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
 * 微信平台公共错误属性类
 */
public abstract class ErrCode implements Serializable {
    private Integer errcode;
    private String errmsg;
    @JsonProperty("expires_in")
    protected Integer expiresIn;
    private Long expiredTime;

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

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        if (expiresIn != null) {
            this.expiredTime = Calendar.getInstance(Locale.CHINA).getTimeInMillis() + expiresIn * 1000;
        }
    }

    public Long getExpiredTime() {
        return expiredTime;
    }
}

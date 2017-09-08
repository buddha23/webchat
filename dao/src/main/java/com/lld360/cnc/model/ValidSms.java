package com.lld360.cnc.model;

import java.util.Date;

public class ValidSms {

    private String mobile;
    private Byte type;
    private String validCode;
    private Date expiredTime;

    public ValidSms() {
    }

    public ValidSms(String mobile, Byte type, String validCode, Date expiredTime) {
        this.mobile = mobile;
        this.type = type;
        this.validCode = validCode;
        this.expiredTime = expiredTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }
}

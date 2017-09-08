package com.lld360.cnc.vo;

import java.io.Serializable;

/**
 * Author: dhc
 * Date: 2016-07-20 18:31
 */
public class WxApiBack implements Serializable {
    private Integer errcode;
    private String errmsg;

    public WxApiBack() {
    }

    public WxApiBack(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
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

package com.lld360.cnc.core.vo;

import com.lld360.cnc.core.M;

import java.io.Serializable;

/**
 * Author: dhc
 * Date: 2016-06-29 14:36
 */
public class ResultOut implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public ResultOut() {
        this.code = 10200;
        this.message = "OK";
    }

    public ResultOut(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultOut(M m, String message) {
        this.code = m.getCode();
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(M m) {
        this.code = m.getCode();
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public ResultOut setData(Object data) {
        this.data = data;
        return this;
    }
}

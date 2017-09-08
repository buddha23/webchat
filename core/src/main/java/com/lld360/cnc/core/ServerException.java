package com.lld360.cnc.core;

import org.springframework.http.HttpStatus;

/**
 * Author: dhc
 * Date: 2016-07-07 09:34
 */
public class ServerException extends RuntimeException {
    private static final int DEFAULT_ERROR_CODE = 90500;
    private static final String DEFAULT_ERROR_INFO = "Internal Server Error";

    private HttpStatus status;
    private Integer code;
    private Object[] args;
    private String message;
    private Object data;


    public ServerException(HttpStatus status) {
        super();
        this.status = status;
    }

    public ServerException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public ServerException(HttpStatus status, Integer code, Object... args) {
        super(String.valueOf(code));
        this.status = status;
        this.code = code;
        this.args = args;
    }

    public ServerException(HttpStatus status, M m, Object... args) {
        super(m.toString());
        this.status = status;
        this.code = m.getCode();
        this.args = args;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
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

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ServerException setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ServerException setData(Object data) {
        this.data = data;
        return this;
    }
}

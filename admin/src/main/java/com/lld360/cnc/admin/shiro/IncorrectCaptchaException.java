package com.lld360.cnc.admin.shiro;


import org.apache.shiro.authc.AuthenticationException;

/**
 * Author: dhc
 * Date: 2016-09-27 17:33
 */
public class IncorrectCaptchaException extends AuthenticationException {
    public IncorrectCaptchaException() {
    }

    public IncorrectCaptchaException(String message) {
        super(message);
    }

    public IncorrectCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}

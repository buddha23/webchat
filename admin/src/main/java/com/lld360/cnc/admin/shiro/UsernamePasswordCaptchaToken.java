package com.lld360.cnc.admin.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Author: dhc
 * Date: 2016-09-27 17:24
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {
    private String captcha;

    public UsernamePasswordCaptchaToken(String username, String password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}

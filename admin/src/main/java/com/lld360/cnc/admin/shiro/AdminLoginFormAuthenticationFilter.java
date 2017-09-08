package com.lld360.cnc.admin.shiro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.service.AdminService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.gonvan.kaptcha.Constants;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-07-13 14:26
 */
public class AdminLoginFormAuthenticationFilter extends FormAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(AdminLoginFormAuthenticationFilter.class);

    @Autowired
    private AdminService adminService;

    private void outJson(HttpServletResponse response, HttpStatus status, Object o) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(status.value());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(mapper.writeValueAsString(o));
            writer.flush();
        } catch (IOException e) {
            logger.warn("输出异常：" + e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    protected UsernamePasswordCaptchaToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = WebUtils.getCleanParam(request, "captcha");
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);

        return new UsernamePasswordCaptchaToken(username, password, rememberMe, host, captcha);
    }

    // 验证码校验
    private void doCaptchaValidate(HttpServletRequest request, String ica) {
        String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (captcha == null || !captcha.equalsIgnoreCase(ica)) {
            throw new IncorrectCaptchaException();
        }
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        UsernamePasswordCaptchaToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            doCaptchaValidate((HttpServletRequest) request, token.getCaptcha());
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Map<String, String> result = new HashMap<>();
        String msg = "message";
        if (e instanceof IncorrectCaptchaException) {
            result.put(msg, "验证码错误!");
        } else {
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
            String userName = usernamePasswordToken.getUsername();
            Admin admin = adminService.get(userName);
            if (admin == null) {
                result.put(msg, "帐号和密码错误！");
            } else if (admin.getStatus().equals(Const.ADMIN_STATUS_FREEZE)) {
                result.put(msg, "账号已被冻结！请联系超级管理员");
            } else {
                dealLoginFail(admin);
                result.put(msg, "帐号和密码错误！");
            }
        }
        outJson((HttpServletResponse) response, HttpStatus.UNAUTHORIZED, result);
        return true;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        Admin admin = (Admin) subject.getPrincipal();
        admin.setLastLogin(new Date());
        admin.setFailedTimes(0);
        adminService.update(admin);
        outJson((HttpServletResponse) response, HttpStatus.OK, admin);
        return false;
    }

    private void dealLoginFail(Admin admin) {
        if (admin.getFailedTimes() == null) {
            admin.setFailedTimes(1);
        } else if (admin.getFailedTimes() < Const.MAX_LOGIN_FAIL_TIMES - 1) {
            admin.setFailedTimes(admin.getFailedTimes() + 1);
        } else {
            admin.setFailedTimes(Const.MAX_LOGIN_FAIL_TIMES);
            admin.setStatus(Const.ADMIN_STATUS_FREEZE);
        }
        adminService.update(admin);
    }
}

package com.lld360.cnc.website.interceptor;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Author: dhc
 * Date: 2016-09-07 14:40
 */
public class RememberIntercptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            Cookie cookie = ClientUtils.getCookie(request, "rememberMe");
            if (cookie != null && StringUtils.isNotEmpty(cookie.getValue())) {
                if (userCookieIsexist(session, cookie)) return true;
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return true;
    }

    private boolean userCookieIsexist(HttpSession session, Cookie cookie) {
        String[] value = cookie.getValue().split("/");
        if (value.length == 2) {
            long id = Long.parseLong(cookie.getValue().split("/")[1], Character.MAX_RADIX) - 10000;
            if (id > 0) {
                UserDto user = userService.getUserDto(id);
                if (user != null && user.getPassword() != null && user.getPassword().equals(StringUtils.reverse(value[0]))) {
                    session.setAttribute(Const.SS_USER, user);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView view) throws Exception {
        /*do something*/
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /**/
    }
}

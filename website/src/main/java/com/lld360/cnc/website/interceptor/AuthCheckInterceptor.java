package com.lld360.cnc.website.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: dhc
 * Date: 2016-09-09 10:41
 */
public class AuthCheckInterceptor implements HandlerInterceptor {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute(Const.SS_USER);
        if (user == null || !(user instanceof UserDto)) {
            if (ClientUtils.isAjax(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ResultOut out = new ResultOut(M.S90401, "需要登录");
                String json = objectMapper.writeValueAsString(out);
                response.getWriter().write(json);
                response.getWriter().close();
            } else {
                String redirectUrl = ClientUtils.isMobileBrowser(request) ? "/m/auth/login" : "/auth/login";
                response.sendRedirect(request.getContextPath() + redirectUrl);
            }
            return false;
        } else if (((UserDto) user).getId() == null) {
            String redirectUrl = ClientUtils.isMobileBrowser(request) ? "/m/auth/bindingId" : "/auth/bindingId";
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

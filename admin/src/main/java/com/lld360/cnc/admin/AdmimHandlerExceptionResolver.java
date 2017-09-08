package com.lld360.cnc.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.JsonUtils;
import com.lld360.cnc.model.SystemLog;
import com.lld360.cnc.model.User;
import com.lld360.cnc.repository.SystemLogDao;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: dhc
 * Date: 2016-07-18 14:41
 */
public class AdmimHandlerExceptionResolver implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(AdmimHandlerExceptionResolver.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemLogDao systemLogDao;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        RequestContext ctx = new RequestContext(request);

        // 忽略 Broken pipe 和 Connection reset by peer 异常
        String message = ex.getMessage();
        if (message == null) {
            message = ex.getClass().getName();
        }
        if (message.contains("Broken pipe") || message.contains("Connection reset by peer"))
            return null;

        ModelAndView view = new ModelAndView(new MappingJackson2JsonView(objectMapper));
        if (ex instanceof ServerException) {
            ServerException e = (ServerException) ex;

            if (e.getMessage() != null) {
                message = e.getMessage();
                if (e.getCode() == null) {
                    e.setCode(M.E90002);
                }
            } else if (e.getCode() != null) {
                message = ctx.getMessage(String.valueOf(e.getCode()), e.getArgs());
            } else {
                e.setCode(M.E90002);
                message = e.getStatus().name();
            }
            view.addObject("code", e.getCode());
            view.addObject("message", message);
            if (e.getData() != null) {
                view.addObject("data", e.getData());
            }
            response.setStatus(e.getStatus().value());
        } else if (ex instanceof ShiroException) {
            if (ex instanceof AuthorizationException) {
                view.addObject("code", M.S90403);
                view.addObject("message", "没有操作权限。");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            view.addObject("code", M.S90500.getCode());
            view.addObject("message", ctx.getMessage(M.S90500.toString()));
            view.addObject("data", message);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("★★★发现未处理异常：", ex);

            // add by yangb
            User user = null;
            Object object = request.getSession().getAttribute(Const.SS_USER);
            if (object != null) {
                user = (User) object;
            }

            SystemLog log = new SystemLog(ex);
            log.setPlatform("ADMIN");
            log.setUrl(request.getRequestURL().toString() + (user == null ? " >>> NoLogin !" : " >>> UserId: " + user.getId()));
            log.setParams(JsonUtils.toJson(request.getParameterMap(), JsonInclude.Include.ALWAYS));
            systemLogDao.create(log);
        }
        return view;
    }
}

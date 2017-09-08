package com.lld360.cnc.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.core.utils.JsonUtils;
import com.lld360.cnc.model.SystemLog;
import com.lld360.cnc.model.User;
import com.lld360.cnc.repository.SystemLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: dhc
 * Date: 2016-07-18 14:41
 */
public class SiteHandlerExceptionResolver extends HandlerExceptionResolverComposite {
    Logger logger = LoggerFactory.getLogger(SiteHandlerExceptionResolver.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SystemLogDao systemLogDao;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        RequestContext ctx = new RequestContext(request);

        ServerException e = null;

        // 忽略 Broken pipe 和 Connection reset by peer 异常
        String message = ex.getMessage();
        if (message == null) {
            message = ex.getClass().getName();
        }
        if (message.contains("Broken pipe") || message.contains("Connection reset by peer"))
            return null;

        if (ex instanceof ServerException) {
            e = (ServerException) ex;

            if (e.getCode() != null) {
                e.setMessage(ctx.getMessage(String.valueOf(e.getCode()), e.getArgs()));
            } else if (e.getMessage() != null) {
                e.setCode(M.E90002);
            } else {
                e.setCode(M.E90002);
                e.setMessage(e.getStatus().name());
            }
        } else {
            logger.error("★★★发现未处理异常：", ex);
            // add by yangb
            User user = null;
            Object object = request.getSession().getAttribute(Const.SS_USER);
            if (object != null) {
                user = (User) object;
            }
            SystemLog log = new SystemLog(ex);
            log.setPlatform("WEB");
            log.setUrl(request.getRequestURL().toString() + (user == null ? " >>> NoLogin !" : " >>> UserId: " + user.getId()));
            log.setParams(JsonUtils.toJson(request.getParameterMap(), JsonInclude.Include.ALWAYS));
            systemLogDao.create(log);
        }

        // 进行Ajax输出
        if (ClientUtils.isAjax(request)) {
            ModelAndView view = new ModelAndView(new MappingJackson2JsonView(objectMapper));
            if (e != null) {
                view.addObject("code", e.getCode());
                view.addObject("message", e.getMessage());
                if (e.getData() != null) {
                    view.addObject("data", e.getData());
                }
                response.setStatus(e.getStatus().value());
            } else {
                view.addObject("code", M.S90500.getCode());
                view.addObject("message", ctx.getMessage(M.S90500.toString()));
                view.addObject("data", message);
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            return view;
        } else if (e != null) {
            if (e.getStatus() == HttpStatus.UNAUTHORIZED) {
                String loginUrl = ClientUtils.isMobileBrowser(request) ? "/m/auth/login" : "/auth/login";
                return new ModelAndView("redirect:" + loginUrl);
            }
            return new ModelAndView("errors/xxx").addObject("e", e);
        }
        return super.resolveException(request, response, o, ex);
    }
}

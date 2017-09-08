package com.lld360.cnc.core;

import com.lld360.cnc.core.vo.ResultOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Author: dhc
 * Date: 2016-07-06 10:31
 */
public abstract class ServletWired {
    protected Logger logger = LoggerFactory.getLogger(ServletWired.class);

    @Autowired
    protected ServletContext context;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected Configer configer;

    private RequestContext requestContext;

    protected RequestContext getRequestContext() {
        if (requestContext == null) {
            requestContext = new RequestContext(request);
        }
        return requestContext;
    }

    /**
     * 获取文字内容
     *
     * @param code 配置的文字标题
     * @param args 参数集合
     * @return 需要输出的文字
     */
    protected String getMessage(String code, Object... args) {
        return getRequestContext().getMessage(code, args);
    }

    /**
     * 生成消息输出对象
     *
     * @param code 消息代码
     * @return 结果输出对象
     */
    protected ResultOut getResultOut(int code) {
        String message = getMessage(String.valueOf(code));
        return new ResultOut(code, message);
    }

    /**
     * 生成消息输出对象
     *
     * @param code 消息代码
     * @param args 消息参数
     * @return 消息结果对象
     */
    protected ResultOut getResultOut(int code, Object... args) {
        String message = getMessage(String.valueOf(code), args);
        return new ResultOut(code, message);
    }

    /**
     * 从ServletContext中获取对象
     *
     * @param attribute 对象保存名称
     * @param <T>       获取的对象
     * @return 如果不存在则返回null
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextAttribute(String attribute) {
        Object o = context.getAttribute(attribute);
        if (o != null) {
            return (T) o;
        }
        return null;
    }
}

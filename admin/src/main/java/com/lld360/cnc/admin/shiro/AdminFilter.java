package com.lld360.cnc.admin.shiro;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: dhc
 * Date: 2016-11-22 20:23
 */
public class AdminFilter extends UserFilter {
    private Logger logger = LoggerFactory.getLogger(AdminFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        saveRequest(request);
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write("{\"message\":\"需要登录\",\"code\":\"S90401\"}");
            writer.flush();
        } catch (IOException e) {
            logger.warn("输出异常：" + e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return false;
    }
}

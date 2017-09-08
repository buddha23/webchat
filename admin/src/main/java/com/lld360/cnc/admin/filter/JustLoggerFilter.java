package com.lld360.cnc.admin.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.lld360.cnc.repository.UserDao;
import org.apache.shiro.SecurityUtils;

import com.lld360.cnc.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;

public class JustLoggerFilter implements Filter {

    private static List<String> whiteUrlList;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        String path = ((HttpServletRequest) request).getRequestURI();
        if (admin == null) {
            chain.doFilter(request, response);
            return;
        }
        if ("tech001".equals(admin.getAccount()) && !isAuth(path)) {
            return;
        }
        chain.doFilter(request, response);

    }

    private boolean isAuth(String url) {
        if (whiteUrlList.contains(url) || url.startsWith("/admin/admin/systemLog")) {
            return true;
        }
        if (url.endsWith(".css") || url.endsWith(".js")) {
            return true;
        }
        return false;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        whiteUrlList = new ArrayList<>();
        whiteUrlList.add("/admin/i/config");
        whiteUrlList.add("/admin/admin/login");
        whiteUrlList.add("/admin/scripts/controller/views/systemLogList.html");
        whiteUrlList.add("/admin/justlog.html");
        whiteUrlList.add("/admin/scripts/directive/views/ng-pager.html");
    }

}

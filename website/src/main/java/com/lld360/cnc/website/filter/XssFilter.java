package com.lld360.cnc.website.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class XssFilter implements Filter {

    public XssFilter() {
        /*donothing*/
    }

    public void destroy() {
        /*donothing*/
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (StringUtils.isNotEmpty(uri) && uri.contains("commodity/publish")) {
//            Map<String, String[]> params = req.getParameterMap();
//            Set<String> keys = params.keySet();
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XSSRequestWrapper(req), resp);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {
        /*donothing*/
    }

}
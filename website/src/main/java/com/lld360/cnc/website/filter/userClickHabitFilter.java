//package com.lld360.cnc.website.filter;
//
//import com.lld360.cnc.core.utils.ClientUtils;
//import com.lld360.cnc.model.User;
//import com.lld360.cnc.model.UserClickHabit;
//import com.lld360.cnc.service.UserClickHabitService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//public class userClickHabitFilter implements Filter {
//
//    @Autowired
//    private UserClickHabitService userClickHabitService;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        Object user = ((HttpServletRequest) request).getSession().getAttribute("user");
//        String site = ((HttpServletRequest) request).getRequestURI();
//        if (user != null && !ClientUtils.isAjax((HttpServletRequest) request) && writeSite(site)) {
//            UserClickHabit userClickHabit = new UserClickHabit(((User) user).getId(), site);
//            userClickHabitService.create(userClickHabit);
//        }
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    private boolean writeSite(String site) {
//        String writeSite = "/doc/,/soft/,/video/,/course/,/post/";
//        for (String ws : writeSite.split(","))
//            if (site.contains(ws)) return true;
//
//        return false;
//    }
//}

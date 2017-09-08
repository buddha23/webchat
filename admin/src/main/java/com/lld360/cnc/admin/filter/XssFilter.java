package com.lld360.cnc.admin.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class XssFilter implements Filter {

	public XssFilter() {
		/*donothing*/
	}

	public void destroy() {
		/*donothing*/
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		chain.doFilter(new XSSRequestWrapper(req), resp);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		/*donothing*/
	}

}
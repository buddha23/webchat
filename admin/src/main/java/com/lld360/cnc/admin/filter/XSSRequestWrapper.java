package com.lld360.cnc.admin.filter;

import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Iterator;
import java.util.Map;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

	private static Policy policy = null;

	static {
		String path = XSSRequestWrapper.class.getClassLoader().getResource("antisamy-anythinggoes.xml").getFile();
		//System.out.println("policy_filepath:" + path);
		if (path.startsWith("file")) {
			path = path.substring(6);
		}
		try {
			policy = Policy.getInstance(path);
		} catch (PolicyException e) {
			e.printStackTrace();
		}
	}

	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String getParameter(String paramString) {
		String str = super.getParameter(paramString);
		if (str == null)
			return null;
		return xssClean(str);
	}

	public String getHeader(String paramString) {
		String str = super.getHeader(paramString);
		if (str == null)
			return null;
		return xssClean(str);
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> request_map = super.getParameterMap();
		Iterator iterator = request_map.entrySet().iterator();
		//System.out.println("request_map" + request_map.size());
		while (iterator.hasNext()) {
			Map.Entry me = (Map.Entry) iterator.next();
			String[] values = (String[]) me.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = xssClean(values[i]);
			}
		}
		return request_map;
	}

	public String[] getParameterValues(String paramString) {
		String[] arrayOfString1 = super.getParameterValues(paramString);
		if (arrayOfString1 == null)
			return null;
		int i = arrayOfString1.length;
		String[] arrayOfString2 = new String[i];
		for (int j = 0; j < i; j++)
			arrayOfString2[j] = xssClean(arrayOfString1[j]);
		return arrayOfString2;
	}

	private String xssClean(String value) {
		AntiSamy antiSamy = new AntiSamy();
		try {
			final CleanResults cr = antiSamy.scan(value, policy);
			// 安全的HTML输出
			return cr.getCleanHTML();
		} catch (ScanException e) {
			e.printStackTrace();
		} catch (PolicyException e) {
			e.printStackTrace();
		}
		return value;
	}
}
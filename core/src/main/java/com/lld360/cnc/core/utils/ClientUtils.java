package com.lld360.cnc.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-30 14:13
 */
public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    /**
     * 判断是否jQuery的Ajax请求
     *
     * @param request 请求对象
     * @return 判断结果
     */
    public static boolean isAjax(HttpServletRequest request) {
        String xrw = request.getHeader("X-Requested-With");
        return xrw != null && xrw.equals("XMLHttpRequest");
    }

    // 获取浏览器标识符
    public static String getUserAgent(HttpServletRequest request) {
        Enumeration<String> userAgents = request.getHeaders("User-Agent");
        if (userAgents.hasMoreElements()) {
            return userAgents.nextElement();
        }
        return null;
    }

    // 判断是否手机浏览器
    public static boolean isMobileBrowser(HttpServletRequest request) {
        String agent = getUserAgent(request);
        if (agent != null) {
            String[] uaKeys = "Mobile,Android,iPhone,iPod,Windows Phone,MQQBrowser,MicroMessenger".split(",");
            for (String key : uaKeys) {
                if (agent.contains(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 判断是否是微信
    public static boolean isWechat(HttpServletRequest request) {
        String agent = getUserAgent(request);
        if (agent != null) {
            String key = "MicroMessenger";
            if (agent.contains(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据名称获取指定Cookie
     *
     * @param request 用户请求
     * @param name    Cookie名称
     * @return 需要取得的Cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 输出下载文件
     *
     * @param response 输出对象
     * @param file     文件
     * @param name     下载名称
     * @throws IOException 输出异常
     */
    public static void outputFile(HttpServletResponse response, File file, String name) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"), "iso8859-1"));

        byte[] bytes = new byte[1024];
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            while (fis.read(bytes) > 0) {
                out.write(bytes);
            }
            out.flush();
        }
    }

    /**
     * 获取访问者IP地址，如果是nginx代理则根据header获取
     *
     * @param req HttpServletRequest
     * @return IP地址
     */
    public static String getRemoteAddr(HttpServletRequest req) {
        String ip = req.getHeader("X-Real-IP");
        return org.apache.commons.lang3.StringUtils.isEmpty(ip) ? req.getRemoteAddr() : ip;
    }


    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isAvalibleIP(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        }
        if (!isAvalibleIP(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (!isAvalibleIP(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isAvalibleIP(String ip) {
        return !StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip);
    }

    public static String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = null;
        reader = request.getReader();
        String line = "";
        StringBuffer inputString = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        return inputString.toString();
    }

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<>();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) map.put(e.getName(), e.getText());
        // 释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }
}

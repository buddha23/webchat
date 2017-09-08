package com.lld360.cnc.model;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Date;

public class SystemLog {
    private Long id;
    private String url;
    private String params;
    private String platform;
    private Date occurTime;
    private String message;
    private String detail;

    public SystemLog() {
    }

    public SystemLog(Exception e) {
        message = e.getMessage();
        if (message == null) {
            message = e.getClass().getName();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"exception\"><h3>").append(StringEscapeUtils.escapeHtml4(message)).append("</h3><code class=\"trace\">");
        for (StackTraceElement ste : e.getStackTrace()) {
            sb.append(ste.toString()).append("<br>");
        }
        sb.append("</code></div>");
        detail = sb.toString();

        if (message.length() > 100) {
            message = message.substring(0, 100) + "……";
        }
        occurTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        if (params != null && params.length() > 500) {
            params = params.substring(0, 500);
        }
        this.params = params;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}

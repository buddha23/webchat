package com.lld360.weixin.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;

/**
 * 微信事件公共属性
 */
@JacksonXmlRootElement(localName = "xml")
public abstract class Event implements Serializable {
    @JacksonXmlProperty(localName = "AppId")
    protected String appId;
    @JacksonXmlProperty(localName = "CreateTime")
    protected Long createTime;
    @JacksonXmlProperty(localName = "InfoType")
    protected String infoType;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }
}

package com.lld360.weixin.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;

/**
 * Author: dhc
 * Date: 2016-10-18 16:27
 */
@JacksonXmlRootElement(localName = "xml")
public class EncryptMessage implements Serializable {
    @JacksonXmlProperty(localName = "AppId")
    private String appId;
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    @JacksonXmlProperty(localName = "Encrypt")
    private String content;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("EncryptMessage{appId='%s', toUserName='%s', content='%s'", appId, toUserName, content);
    }
}

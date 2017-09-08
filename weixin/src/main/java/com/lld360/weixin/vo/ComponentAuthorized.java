package com.lld360.weixin.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 微信公众号授权平台的事件对象
 */
public class ComponentAuthorized extends Event {
    @JacksonXmlProperty(localName = "AuthorizerAppid")
    private String authorizerAppid;
    @JacksonXmlProperty(localName = "AuthorizationCode")
    private String authorizationCode;
    @JacksonXmlProperty(localName = "AuthorizationCodeExpiredTime")
    private Long authorizationCodeExpiredTime;

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Long getAuthorizationCodeExpiredTime() {
        return authorizationCodeExpiredTime;
    }

    public void setAuthorizationCodeExpiredTime(Long authorizationCodeExpiredTime) {
        this.authorizationCodeExpiredTime = authorizationCodeExpiredTime;
    }

    @Override
    public String toString() {
        return String.format("ComponentAuthorized{authorizerAppid='%s', authorizationCode='%s', authorizationCodeExpiredTime=%d}",
                authorizerAppid, authorizationCode, authorizationCodeExpiredTime);
    }
}

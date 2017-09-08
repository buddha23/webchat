package com.lld360.weixin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 通过ComponentVerifyTicket请求微信服务器获取到的AccessToken
 * 两个小时过期，有访问限制，需要做服务器缓存
 */
public class ComponentAccessToken extends ErrCode {
    @JsonProperty("component_access_token")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("ComponentAccessToken{content='%s', expiresIn=%d}", content, expiresIn);
    }
}

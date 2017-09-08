package com.lld360.weixin.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 在公众号第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”
 * 每隔10分钟定时推送component_verify_ticket。
 * 该类用于接收微信推送的component_verify_ticket对象
 */
public class ComponentVerifyTicket extends Event {
    @JacksonXmlProperty(localName = "ComponentVerifyTicket")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("ComponentVerifyTicket{appId='%s', createTime=%d, infoType='%s', content='%s'}",
                appId, createTime, infoType, content);
    }
}

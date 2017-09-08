package com.lld360.weixin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信第三方平台授权的预授权码
 * 需要通过ComponentAccessToken获取
 */
public class PreAuthCode extends ErrCode {
    @JsonProperty("pre_auth_code")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("PreAuthCode{content='%s', expiresIn=%d}", content, expiresIn);
    }
}

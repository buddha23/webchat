package com.lld360.cnc.core.enums;

/**
 * Author: dhc
 * Date: 2016-07-05 10:12
 */
public enum WxMsgType {

    TEXT("text"),   // 文本
    IMAGE("image"), // 图片
    VOICE("voice"), // 声音
    VIDEO("video"), // 视频
    SHORTVIDEO("shortvideo"),   // 小视频，仅发送
    LOCATION("location"),   // 位置
    LINK("link"),   // 链接，仅发送
    MUSIC("music"), // 音乐，仅回复
    NEWS("news");   // 图文，仅回复

    private final String value;

    WxMsgType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.lld360.cnc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: dhc
 * Date: 2016-07-04 19:57
 */
public class WxGzhUploadMediaBack extends WxApiBack {
    private String type;

    @JsonProperty("media_id")
    private String mediaId;

    @JsonProperty("created_at")
    private Long createAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}

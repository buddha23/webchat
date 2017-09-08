package com.lld360.cnc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author: dhc
 * Date: 2016-07-11 17:34
 */
public class WxGzhButton {
    private String type;
    private String name;
    private String key;
    private String url;
    @JsonProperty("sub_button")
    private List<WxGzhButton> subButton;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WxGzhButton> getSubButton() {
        return subButton;
    }

    public void setSubButton(List<WxGzhButton> subButton) {
        this.subButton = subButton;
    }
}

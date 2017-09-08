package com.lld360.cnc.wxmsg;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class DataTemp implements Serializable {

    private String value;

    private String color;

    public DataTemp(String value, String color) {
        this.value = value;
        if (StringUtils.isEmpty(color)) {
            this.color = "#000";
        } else {
            this.color = color;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

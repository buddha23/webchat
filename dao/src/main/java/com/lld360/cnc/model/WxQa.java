package com.lld360.cnc.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Author: dhc
 * Date: 2016-07-15 13:38
 */
public class WxQa implements Serializable {
    private Long id;
    @NotBlank(message = "问题不能为空")
    private String q;
    @NotBlank(message = "答案内容不能为空")
    private String a;
    private Long volume;

    public WxQa() {
    }

    public WxQa(String q, String a) {
        this.q = q;
        this.a = a;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }
}

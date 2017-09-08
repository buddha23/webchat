package com.lld360.cnc.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class Setting implements Serializable {

    private Long id;
    private String lable;           //设置项中文显示
    @NotBlank(message = "设置代码不能为空")
    private String name;            //设置项英文代码
    private String content;         //值
    private String description;     //描述

    public Setting() {
    }

    public Setting(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

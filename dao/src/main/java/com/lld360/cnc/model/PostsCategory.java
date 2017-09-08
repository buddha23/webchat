package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class PostsCategory implements Serializable {

    /**
     * id
     */
    private Integer id;
    /**
     * 类型图标
     */
    @Length(max = 150, message = "类型图标 最大长度不能超过150个字符")
    private String icon;
    /**
     * 名称
     */
    @NotBlank(message = "名称 不能为空。")
    @Length(max = 63, message = "名称 最大长度不能超过45个字符")
    private String name;
    /**
     * 描述
     */
    @Length(max = 255, message = "描述 最大长度不能超过200个字符")
    private String description;
    /*
    * 排序
    * */
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

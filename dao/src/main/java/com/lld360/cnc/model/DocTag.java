package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * DocTag
 */
public class DocTag implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称 不能为空。")
    @Length(max = 45, message = "标签名称 最大长度不能超过45个字符")
    private String name;
    /**
     * 标签描述
     */
    @Length(max = 200, message = "标签描述 最大长度不能超过200个字符")
    private String description;

    public DocTag() {
    }

    public DocTag(String name) {
        this.name = name;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return this.id;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }
}
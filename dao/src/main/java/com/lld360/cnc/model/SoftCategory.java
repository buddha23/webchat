package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class SoftCategory implements Serializable{
	  /**
     * id
     */
    private Integer id;
    /**
     * 上级ID
     */
    private Integer fid;
    /**
     * 类型图标
     */
    @Length(max = 150, message = "类型图标 最大长度不能超过150个字符")
    private String icon;
    /**
     * 名称
     */
    @NotBlank(message = "名称 不能为空。")
    @Length(max = 45, message = "名称 最大长度不能超过45个字符")
    private String name;
    /**
     * 描述
     */
    @Length(max = 200, message = "描述 最大长度不能超过200个字符")
    private String description;

    private List<DocCategory> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFid(Integer value) {
        this.fid = value;
    }

    public Integer getFid() {
        return this.fid;
    }

    public void setIcon(String value) {
        this.icon = value;
    }

    public String getIcon() {
        return this.icon;
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

    public List<DocCategory> getChildren() {
        return children;
    }

    public void setChildren(List<DocCategory> children) {
        this.children = children;
    }
}

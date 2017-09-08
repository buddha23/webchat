package com.lld360.cnc.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * AdminPermission
 */
public class AdminPermission implements Serializable {
    /**
     * ID
     */
    @NotBlank(message = "ID 不能为空。")
    private Integer id;
    /**
     * 权限名
     */
    @NotBlank(message = "权限名 不能为空。")
    @Length(max = 45, message = "权限名 最大长度不能超过45个字符")
    private String name;
    /**
     * 权限值
     */
    @NotBlank(message = "权限值 不能为空。")
    @Length(max = 45, message = "权限值 最大长度不能超过45个字符")
    private String permit;
    /**
     * 描述
     */
    @Length(max = 255, message = "描述 最大长度不能超过255个字符")
    private String description;


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

    public void setPermit(String value) {
        this.permit = value;
    }

    public String getPermit() {
        return this.permit;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }
}
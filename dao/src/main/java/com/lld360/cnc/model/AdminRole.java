package com.lld360.cnc.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * AdminRole
 */
public class AdminRole implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 角色名
     */
    @NotBlank(message = "角色名 不能为空。")
    @Length(max = 45, message = "角色名 最大长度不能超过45个字符")
    private String name;
    /**
     * 角色代码
     */
    @NotBlank(message = "角色代码 不能为空。")
    @Length(max = 45, message = "角色代码 最大长度不能超过45个字符")
    private String role;
    /**
     * 权限集合
     */
    @NotBlank(message = "权限集合 不能为空。")
    @Length(max = 1024, message = "权限集合 最大长度不能超过120个字符")
    private String permissions;
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

    public void setRole(String value) {
        this.role = value;
    }

    public String getRole() {
        return this.role;
    }

    public void setPermissions(String value) {
        this.permissions = value;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }
}
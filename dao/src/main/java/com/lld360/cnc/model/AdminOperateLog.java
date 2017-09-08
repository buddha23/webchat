package com.lld360.cnc.model;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员操作日志类
 */
public class AdminOperateLog implements Serializable {
    private Long id;

    /**
     * 操作名称
     */
    @NotBlank(message = "操作名称不能为空。")
    @Length(max = 63, message = "操作名称最大长度不能超过63个字符")
    private String name;

    /**
     * 方法
     */
    @NotBlank(message = "方法不能为空。")
    @Length(max = 63, message = "方法最大长度不能超过63个字符")
    private String method;

    /**
     *
     */
    @Length(max = 511, message = "最大长度不能超过511个字符")
    private String args;

    /**
     * 是否成功
     */
    @NotBlank(message = "是否成功不能为空。")
    private Boolean successed;

    /**
     * 返回数据
     */
    @Length(max = 65535, message = "返回数据最大长度不能超过65535个字符")
    private String returnValue;

    /**
     * 异常详细
     */
    @Length(max = 65535, message = "异常详细最大长度不能超过65535个字符")
    private String exception;

    /**
     * 管理员帐号
     */
    private int adminId;

    /**
     * 访问地址
     */
    @Length(max = 255, message = "最大长度不能超过255个字符")
    private String url;

    /**
     * IP地址
     */
    @Length(max = 45, message = "最大长度不能超过45个字符")
    private String ip;

    /**
     * 创建时间
     */
    @NotBlank(message = "不能为空。")
    private Date createTime;

    private String adminName;

    private String adminMobile;


    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    public void setMethod(String value) {
        this.method = value;
    }

    public String getMethod() {
        return this.method;
    }

    public void setArgs(String value) {
        this.args = value;
    }

    public String getArgs() {
        return this.args;
    }

    public void setSuccessed(Boolean value) {
        this.successed = value;
    }

    public Boolean getSuccessed() {
        return this.successed;
    }

    public void setReturnValue(String value) {
        this.returnValue = value;
    }

    public String getReturnValue() {
        return this.returnValue;
    }

    public void setException(String value) {
        this.exception = value;
    }

    public String getException() {
        return this.exception;
    }

    public void setAdminId(int value) {
        this.adminId = value;
    }

    public int getAdminId() {
        return this.adminId;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public String getUrl() {
        return this.url;
    }

    public void setIp(String value) {
        this.ip = value;
    }

    public String getIp() {
        return this.ip;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }
}
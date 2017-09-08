package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * Course
 */
public class Course implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Long id;
    /**
     * 标题
     */
    @NotBlank(message = "标题 不能为空。")
    @Length(max = 100, message = "标题 最大长度不能超过100个字符")
    private String title;
    /**
     * 图片路径
     */
    @NotBlank(message = "图片路径 不能为空。")
    @Length(max = 100, message = "图片路径 最大长度不能超过100个字符")
    private String imgUrl;
    /**
     * 内容
     */
    @NotBlank(message = "内容 不能为空。")
    @Length(max = 65535, message = "内容 最大长度不能超过65535个字符")
    private String content;
    /**
     * 发布时间
     */
    @NotBlank(message = "发布时间 不能为空。")
    private Date publishTime;
    /**
     * 访问量
     */
    @NotBlank(message = "访问量 不能为空。")
    private Long visits;
    /**
     * 排序
     */
    private Long sequence;
    /**
     * 状态
     */
    @NotBlank(message = "状态 不能为空。")
    private Byte status;
    /**
     * 状态
     */
    @NotBlank(message = "类型 不能为空。")
    private Byte type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    public void setImgUrl(String value) {
        this.imgUrl = value;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    public void setPublishTime(Date value) {
        this.publishTime = value;
    }

    public Date getPublishTime() {
        return this.publishTime;
    }

    public void setVisits(Long value) {
        this.visits = value;
    }

    public Long getVisits() {
        return this.visits;
    }

    public void setSequence(Long value) {
        this.sequence = value;
    }

    public Long getSequence() {
        return this.sequence;
    }

    public void setStatus(Byte value) {
        this.status = value;
    }

    public Byte getStatus() {
        return this.status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
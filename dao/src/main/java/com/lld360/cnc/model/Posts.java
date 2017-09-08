package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * Posts
 */
public class Posts implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    @NotBlank(message = "标题 不能为空。")
    @Length(max = 100, message = "标题 最大长度不能超过100个字符")
    private String title;
    /**
     * 作者（用户ID）
     */
    private Long userId;
    /**
     * 主题
     */
    @Length(max = 100, message = "主题 最大长度不能超过100个字符")
    private String subject;
    /**
     * 概述
     */
    @Length(max = 255, message = "概述 最大长度不能超过255个字符")
    private String summary;
    /**
     * 内容
     */
    @NotBlank(message = "内容 不能为空。")
    @Length(max = 65535, message = "内容 最大长度不能超过65535个字符")
    private String content;
    /**
     * categoryId
     */
    private Integer categoryId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 浏览量
     */
    private Long views;
    /**
     * 状态
     */
    private Byte state;
    /*
    * 悬赏
    * */
    private PostsReward postsReward;

    private Integer sorting;

    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    public void setUserId(Long value) {
        this.userId = value;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setSubject(String value) {
        this.subject = value;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSummary(String value) {
        this.summary = value;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    public void setCategoryId(Integer value) {
        this.categoryId = value;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setUpdateTime(Date value) {
        this.updateTime = value;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public void setState(Byte value) {
        this.state = value;
    }

    public Byte getState() {
        return this.state;
    }

    public PostsReward getPostsReward() {
        return postsReward;
    }

    public void setPostsReward(PostsReward postsReward) {
        this.postsReward = postsReward;
    }

    public Integer getSorting() {
        return sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }
}
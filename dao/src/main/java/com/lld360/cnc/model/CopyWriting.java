package com.lld360.cnc.model;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

public class CopyWriting implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Long id;
    /**
     * 标题
     */
    @NotBlank(message = "标题 不能为空。")
    @Length(max = 100,message = "标题 最大长度不能超过100个字符")
    private String title;
    /**
     * 图片路径
     */
    @NotBlank(message = "key值 不能为空。")
    @Length(max = 100,message = "key值 最大长度不能超过100个字符")
    private String keyword;
    /**
     * 内容
     */
    @NotBlank(message = "内容 不能为空。")
    @Length(max = 65535,message = "内容 最大长度不能超过65535个字符")
    private String content;
    /**
     * 发布时间
     */
    @NotBlank(message = "发布时间 不能为空。")
    private Date publishTime;
    /**
     * 创建者
     */
    @NotBlank(message = "创建者 不能为空。")
    private Integer creater;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getCreater() {
        return creater;
    }

    public void setCreater(Integer creater) {
        this.creater = creater;
    }
}

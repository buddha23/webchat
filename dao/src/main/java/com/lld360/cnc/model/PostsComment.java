package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * PostsComment
 */
public class PostsComment implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 用户
     */
    private Long userId;
    /**
     * 帖子
     */
    @NotBlank(message = "帖子 不能为空。")
    private Long postsId;
    /**
     * 内容
     */
    @NotBlank(message = "内容 不能为空。")
    @Length(max = 1000, message = "内容 最大长度不能超过1000个字符")
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态
     */
    private Byte state;


    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setUserId(Long value) {
        this.userId = value;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setPostsId(Long value) {
        this.postsId = value;
    }

    public Long getPostsId() {
        return this.postsId;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setState(Byte value) {
        this.state = value;
    }

    public Byte getState() {
        return this.state;
    }
}
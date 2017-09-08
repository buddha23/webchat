package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

public class UserSuggest implements Serializable {
    private Long id;
    /*
    用户id
    * */
    private Long userId;
    /*
   fid追问问题
   * */
    private Long fid;
    /**
     * 内容
     */
    @NotBlank(message = "内容 不能为空。")
    @Length(max = 65535, message = "内容 最大长度不能超过65535个字符")
    private String content;
    /**
     * 回复
     */
    @Length(max = 65535, message = "回复 最大长度不能超过65535个字符")
    private String reply;
    /**
     * categoryId 分类
     */
    private Integer categoryId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 回复时间
     */
    private Date replyTime;
    /**
     * 状态
     */
    private Byte state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}

package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 用户消息
 */
public class UserMessage implements Serializable {
    public static final byte TYPE_SCORE_ADD = 1; // 积分增加
    public static final byte TYPE_SCORE_MINUS = 2; // 积分减少
    public static final byte TYPE_DOC_REVIEW_OK = 3;   // 文档审核通过
    public static final byte TYPE_DOC_REVIEW_DENY = 4;   // 文档审核未通过
    public static final byte TYPE_DOC_DELETE = 5;  // 文档被删除
    public static final byte TYPE_MODERATOR_SCORE_ENOUGH = 6;  // 版主贡献值够
    public static final byte TYPE_MODERATOR_SCORE_NOTENOUGH = 7;  // 版主贡献值不够
    public static final byte TYPE_WITHDRAW_REVIEW_OK = 8;   // 文档审核通过
    public static final byte TYPE_WITHDRAW_REVIEW_DENY = 9;   // 文档审核通过

    /**
     * id
     */
    private Long id;
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID 不能为空。")
    private Long userId;
    /**
     * 类型
     */
    @NotNull(message = "类型 不能为空。")
    private Byte type;
    /**
     * 链接对象ID
     */
    private Long objectId;
    /**
     * 标题
     */
    @Length(max = 100, message = "标题 最大长度不能超过100个字符")
    private String title;
    /**
     * 消息内容
     */
    @Length(max = 1000, message = "消息内容 最大长度不能超过1000个字符")
    private String message;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否已读
     */
    private Boolean isRead;

    public UserMessage() {
    }

    public UserMessage(Long userId, Byte type, Long objectId, String title, String message) {
        this.userId = userId;
        this.type = type;
        this.objectId = objectId;
        this.title = title;
        this.message = message;
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
        this.isRead = false;
    }

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

    public void setType(Byte value) {
        this.type = value;
    }

    public Byte getType() {
        return this.type;
    }

    public void setObjectId(Long value) {
        this.objectId = value;
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setIsRead(Boolean value) {
        this.isRead = value;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }
}
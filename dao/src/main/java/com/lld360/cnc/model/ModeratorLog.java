package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 版主操作日志
 */
public class ModeratorLog implements Serializable {
    public static final byte TYPE_VIEW = 1; // 审核: object = doc
    public static final byte TYPE_MOVE = 2; // 移动版块  object = doc
    public static final byte TYPE_DELETE = 3; // 删除  object = doc(被删除)

    /**
     * id
     */
    private Long id;

    /**
     * 版主（用户）ID
     */
    @NotNull(message = "版主（用户）ID 不能为空。")
    private Long moderatorId;

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
     * 操作内容
     */
    @Length(max = 1000, message = "操作内容 最大长度不能超过1000个字符")
    private String content;

    /**
     * 操作时间
     */
    private Date createTime;

    public ModeratorLog() {
    }

    public ModeratorLog(Long moderatorId, Byte type, Long objectId, String content) {
        this.moderatorId = moderatorId;
        this.type = type;
        this.objectId = objectId;
        this.content = content;
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
    }

    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setModeratorId(Long value) {
        this.moderatorId = value;
    }

    public Long getModeratorId() {
        return this.moderatorId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public void setObjectId(Long value) {
        this.objectId = value;
    }

    public Long getObjectId() {
        return this.objectId;
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
}
package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserScoreHistory implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Long id;
    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID 不能为空。")
    private Long userId;
    /**
     * 类型（上传、下载）
     */
    @NotBlank(message = "类型（上传、下载） 不能为空。")
    private Byte type;
    /**
     * 分数变动
     */
    @NotBlank(message = "分数变动 不能为空。")
    private Integer scoreChange;
    /**
     * 对应数据
     */
    private Long objectId;
    /**
     * 说明
     */
    @Length(max = 200, message = "说明 最大长度不能超过200个字符")
    private String description;

    /**
     * createTime
     */
    @NotNull(message = "createTime 不能为空。")
    private Date createTime;

    public UserScoreHistory() {
    }

    public UserScoreHistory(Long userId, Byte type, Integer scoreChange, Long objectId) {
        this.userId = userId;
        this.type = type;
        this.scoreChange = scoreChange;
        this.objectId = objectId;
        this.setCreateTime(Calendar.getInstance(Locale.CHINA).getTime());
    }

    public UserScoreHistory(Long userId, Byte type, Integer scoreChange, Long objectId, String description) {
        this.userId = userId;
        this.type = type;
        this.scoreChange = scoreChange;
        this.objectId = objectId;
        this.description = description;
        this.setCreateTime(Calendar.getInstance(Locale.CHINA).getTime());
    }

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getScoreChange() {
        return scoreChange;
    }

    public void setScoreChange(Integer scoreChange) {
        this.scoreChange = scoreChange;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
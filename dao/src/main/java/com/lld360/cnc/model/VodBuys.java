package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 视频购买记录
 */
public class VodBuys implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 视频Id
     */
    @NotNull(message = "视频Id 不能为空。")
    private Integer volumeId;
    /**
     * 用户Id
     */
    @NotNull(message = "用户Id 不能为空。")
    private Long userId;
    /**
     * 花费积分
     */
    @NotNull(message = "花费积分 不能为空。")
    private Integer costScore;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间 不能为空。")
    private Date createTime;
    /**
     * 备注
     */
    @Length(max = 200, message = "备注 最大长度不能超过200个字符")
    private String remark;
    /*
    * 购买类型
    * */
    private Byte type;

    private String volumeName;
    private String userName;

    public VodBuys() {

    }

    public VodBuys(Integer volumeId, Long userId, Integer costScore, String remark, Byte type) {
        this.volumeId = volumeId;
        this.userId = userId;
        this.costScore = costScore;
        this.remark = remark;
        this.type = type;
        this.setCreateTime(Calendar.getInstance(Locale.CHINA).getTime());
    }

    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setVolumeId(Integer value) {
        this.volumeId = value;
    }

    public Integer getVolumeId() {
        return this.volumeId;
    }

    public void setUserId(Long value) {
        this.userId = value;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setCostScore(Integer value) {
        this.costScore = value;
    }

    public Integer getCostScore() {
        return this.costScore;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setRemark(String value) {
        this.remark = value;
    }

    public String getRemark() {
        return this.remark;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
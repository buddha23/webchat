package com.lld360.cnc.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class SoftDownload {
	 /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Long id;
    /**
     * userId
     */
    @NotBlank(message = "userId 不能为空。")
    private Long userId;
    /**
     * docId
     */
    @NotBlank(message = "uuId 不能为空。")
    private Long uuId;
    /**
     * costScore
     */
    @NotBlank(message = "costScore 不能为空。")
    private Integer costScore;
    /**
     * 用户评分
     */
    private Float evaluate;
    /**
     * 评论
     */
    @Length(max = 500, message = "评论 最大长度不能超过500个字符")
    private String comment;
    /**
     * createTime
     */
    @NotNull(message = "createTime 不能为空。")
    private Date createTime;

    public SoftDownload() {
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
    }

    public SoftDownload(Long userId, Long uuId,Integer costScore) {
        this.userId = userId;
        this.uuId = uuId;
        this.costScore = costScore;
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
    }

    private String uName;
    private String uNickname;

    public String getuNickname() {
        return uNickname;
    }

    public void setuNickname(String uNickname) {
        this.uNickname = uNickname;
    }

    private String dTitle;

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

    

	public Long getUuId() {
		return uuId;
	}

	public void setUuId(Long uuId) {
		this.uuId = uuId;
	}

	public Integer getCostScore() {
        return costScore;
    }

    public void setCostScore(Integer costScore) {
        this.costScore = costScore;
    }

    public Float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Float evaluate) {
        this.evaluate = evaluate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getdTitle() {
        return dTitle;
    }

    public void setdTitle(String dTitle) {
        this.dTitle = dTitle;
    }
}

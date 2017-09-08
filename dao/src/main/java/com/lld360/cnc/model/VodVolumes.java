package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 视频合集
 */
public class VodVolumes implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 名称
     */
    @NotBlank(message = "名称 不能为空。")
    @Length(max = 100, message = "名称 最大长度不能超过100个字符")
    private String name;
    /**
     * 封面
     */
    @Length(max = 255, message = "封面 最大长度不能超过120个字符")
    private String cover;
    /**
     * 讲师
     */
    @Length(max = 45, message = "讲师 最大长度不能超过45个字符")
    private String lecturer;
    /**
     * 视频时长
     */
    private Double duration;
    /**
     * 摘要
     */
    @Length(max = 300, message = "摘要 最大长度不能超过300个字符")
    private String summary;
    /**
     * 介绍
     */
    @NotBlank(message = "介绍 不能为空。")
    @Length(max = 3000, message = "介绍 最大长度不能超过3000个字符")
    private String introduction;
    /**
     * 内容
     */
    @Length(max = 2000, message = "内容 最大长度不能超过2000个字符")
    private String content;
    /**
     * 购买所需积分
     */
    @NotNull(message = "购买所需积分 不能为空。")
    private Integer costScore;
    /**
     * 分类
     */
    private Integer caterogyId;
    /**
     * 媒体工作流ID
     */
    @Length(max = 32, message = "媒体工作流ID 最大长度不能超过32个字符")
    private String workflow;
    /**
     * 创建者（管理员）
     */
    private Integer creater;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 状态
     */
    private Byte state;
    /*
    * 排序
    * */
    private Integer sorting;
    /*
    * 讲师ID(userId与讲师字段无关)
    * */
    private Long lecturerId;
    /*
    * 讲师分成(%)
    * */
    private Integer lecturerProfit;
    /*
    * 浏览量
    * */
    private Integer views;

    private List<VodChapters> chapters;

    private List<VodTag> tags;


    public void setId(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return this.id;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setCover(String value) {
        this.cover = value;
    }

    public String getCover() {
        return this.cover;
    }

    public void setLecturer(String value) {
        this.lecturer = value;
    }

    public String getLecturer() {
        return this.lecturer;
    }

    public void setDuration(Double value) {
        this.duration = value;
    }

    public Double getDuration() {
        return this.duration;
    }

    public void setSummary(String value) {
        this.summary = value;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setIntroduction(String value) {
        this.introduction = value;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    public void setCostScore(Integer value) {
        this.costScore = value;
    }

    public Integer getCostScore() {
        return this.costScore;
    }

    public void setCaterogyId(Integer value) {
        this.caterogyId = value;
    }

    public Integer getCaterogyId() {
        return this.caterogyId;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public void setCreater(Integer value) {
        this.creater = value;
    }

    public Integer getCreater() {
        return this.creater;
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

    public void setState(Byte value) {
        this.state = value;
    }

    public Byte getState() {
        return this.state;
    }

    public Integer getSorting() {
        return sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public Integer getLecturerProfit() {
        return lecturerProfit;
    }

    public void setLecturerProfit(Integer lecturerProfit) {
        this.lecturerProfit = lecturerProfit;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public List<VodChapters> getChapters() {
        return chapters;
    }

    public void setChapters(List<VodChapters> chapters) {
        this.chapters = chapters;
    }

    public List<VodTag> getTags() {
        return tags;
    }

    public void setTags(List<VodTag> tags) {
        this.tags = tags;
    }
}
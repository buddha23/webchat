package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class SoftUpload implements Serializable {

    public SoftUpload(Long uuId, Integer softCategoryId, Long uploader, String title, Integer costScore) {
        this.uuId = uuId;
        this.softCategoryId = softCategoryId;
        this.uploader = uploader;
        this.title = title;
        this.costScore = costScore;
    }

    public SoftUpload() {
    }

    private Long uuId;

    private Integer softCategoryId;
    /**
     * 标题
     */
    @NotBlank(message = "标题 不能为空。")
    @Length(max = 100, message = "标题 最大长度不能超过100个字符")
    private String title;
    /**
     * 所需积分
     */
    @NotNull(message = "所需积分 不能为空。")
    private Integer costScore;

    /**
     * 浏览量
     */
    private Long views;

    /**
     * 下载量
     */
    private Long downloads;
    /**
     * 上传者
     */
    private Long uploader;
    /**
     * 上传者身份类型（用户，管理员）
     */
    private Byte uploaderType;

    private String description;

    private String specification;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private Byte state;

    // 扩展字段

    private List<SoftDoc> softs;
    private SoftCategory softCategory;
    private User user;

    public Long getUuId() {
        return uuId;
    }

    public void setUuId(Long uuId) {
        this.uuId = uuId;
    }

    public Integer getSoftCategoryId() {
        return softCategoryId;
    }

    public void setSoftCategoryId(Integer softCategoryId) {
        this.softCategoryId = softCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCostScore() {
        return costScore;
    }

    public void setCostScore(Integer costScore) {
        this.costScore = costScore;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getDownloads() {
        return downloads;
    }

    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }

    public Long getUploader() {
        return uploader;
    }

    public void setUploader(Long uploader) {
        this.uploader = uploader;
    }

    public Byte getUploaderType() {
        return uploaderType;
    }

    public void setUploaderType(Byte uploaderType) {
        this.uploaderType = uploaderType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public List<SoftDoc> getSofts() {
        return softs;
    }

    public void setSofts(List<SoftDoc> softs) {
        this.softs = softs;
    }

    public SoftCategory getSoftCategory() {
        return softCategory;
    }

    public void setSoftCategory(SoftCategory softCategory) {
        this.softCategory = softCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

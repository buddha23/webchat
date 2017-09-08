package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Doc
 */
public class Doc implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    @NotBlank(message = "标题 不能为空。")
    @Length(max = 100, message = "标题 最大长度不能超过100个字符")
    private String title;
    /**
     * 简介
     */
    @Length(max = 500, message = "简介 最大长度不能超过500个字符")
    private String summary;
    /**
     * categoryId
     */
    private Integer categoryId;
    /**
     * 所需积分
     */
    @NotNull(message = "所需积分 不能为空。")
    private Integer costScore;
    /**
     * 文件名称
     */
    @Length(max = 120, message = "文件名称 最大长度不能超过120个字符")
    private String fileName;
    /**
     * 文件地址
     */
    @Length(max = 150, message = "文件地址 最大长度不能超过150个字符")
    private String filePath;
    /**
     * 文件类型
     */
    @Length(max = 10, message = "文件类型 最大长度不能超过10个字符")
    private String fileType;
    /**
     * 文件大小（KB)
     */
    private Long fileSize;
    /**
     * 文件MD5
     */
    @Length(max = 32, message = "文件MD5 最大长度不能超过32个字符")
    private String fileMd5;
    /**
     * 文档页数
     */
    private Integer filePages;
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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态
     */
    private Byte state;

    /**
     * 已发布时间 yangb新增
     */
    private String diffTime;

    /**
     * 封面 yangb新增
     */
    private String imagePath;

    // 扩展字段
    private DocCategory docCategory;
    private User user;
    private List<DocImage> images;
    private List<String> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    public void setSummary(String value) {
        this.summary = value;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setCategoryId(Integer value) {
        this.categoryId = value;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCostScore(Integer value) {
        this.costScore = value;
    }

    public Integer getCostScore() {
        return this.costScore;
    }

    public void setFileName(String value) {
        this.fileName = value;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFilePath(String value) {
        this.filePath = value;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFileType(String value) {
        this.fileType = value;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileSize(Long value) {
        this.fileSize = value;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileMd5(String value) {
        this.fileMd5 = value;
    }

    public String getFileMd5() {
        return this.fileMd5;
    }

    public Integer getFilePages() {
        return filePages;
    }

    public void setFilePages(Integer filePages) {
        this.filePages = filePages;
    }

    public void setViews(Long value) {
        this.views = value;
    }

    public Long getViews() {
        return this.views;
    }

    public Long getDownloads() {
        return downloads;
    }

    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }

    public void setUploader(Long value) {
        this.uploader = value;
    }

    public Long getUploader() {
        return this.uploader;
    }

    public void setUploaderType(Byte value) {
        this.uploaderType = value;
    }

    public Byte getUploaderType() {
        return this.uploaderType;
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

    public DocCategory getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(DocCategory docCategory) {
        this.docCategory = docCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addImage(String path, Integer page) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(new DocImage(this.id, page, path));
    }

    public List<DocImage> getImages() {
        return images;
    }

    public void setImages(List<DocImage> images) {
        this.images = images;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(String diffTime) {
        this.diffTime = diffTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
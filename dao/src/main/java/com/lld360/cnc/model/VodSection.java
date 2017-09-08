package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.URL;

/**
 * 视频节
 */
public class VodSection implements Serializable {
    public static final byte ST_NORMAL = 1; // 正常
    public static final byte ST_NOTRANS = 2; // 待转码
    public static final byte ST_TRANSINT = 3; // 转码中
    public static final byte ST_REVIEWING = 4; // 审核中

    /**
     * id
     */
    private Long id;
    /**
     * 所属章
     */
    @NotNull(message = "所属章 不能为空。")
    private Integer chapterId;
    /**
     * 视频名称
     */
    @Length(max = 100, message = "视频名称 最大长度不能超过100个字符")
    private String vodName;
    /**
     * 名称
     */
    @NotBlank(message = "名称 不能为空。")
    @Length(max = 100, message = "名称 最大长度不能超过100个字符")
    private String name;
    /**
     * 次序
     */
    @NotNull(message = "次序 不能为空。")
    private Integer sorting;
    /**
     * 媒体ID
     */
    @NotBlank(message = "媒体ID 不能为空。")
    @Length(max = 32, message = "媒体ID 最大长度不能超过32个字符")
    private String mediaId;
    /**
     * 时长
     */
    private Double duration;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;
    /**
     * 比特率
     */
    private Double bitrate;
    /**
     * 文件大小
     */
    private Integer size;
    /**
     * 远程封面
     */
    @Length(max = 200, message = "远程封面 最大长度不能超过200个字符")
    private String cover;
    /**
     * 远程原地址
     */
    @NotBlank(message = "远程原地址 不能为空。")
    @Length(max = 200, message = "远程原地址 最大长度不能超过200个字符")
    private String url;
    /**
     * 转码后地址
     */
    @Length(max = 200, message = "转码后地址 最大长度不能超过200个字符")
    private String transUrl;
    /**
     * 后缀名
     */
    @NotBlank(message = "后缀名 不能为空。")
    @Length(max = 45, message = "后缀名 最大长度不能超过45个字符")
    private String ext;
    /**
     * 是否免费
     */
    @NotNull(message = "是否免费不可为空")
    private Boolean free;
    /**
     * 状态(1=正常,2=处理中,3=处理失败)
     */
    private Byte state;
    /*
    *  处理后地址
    **/
    private URL dealURL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSorting() {
        return sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getBitrate() {
        return bitrate;
    }

    public void setBitrate(Double bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTransUrl() {
        return transUrl;
    }

    public void setTransUrl(String transUrl) {
        this.transUrl = transUrl;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public URL getDealURL() {
        return dealURL;
    }

    public void setDealURL(URL dealURL) {
        this.dealURL = dealURL;
    }
}
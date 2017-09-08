package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class ImgFile {
//    private static final long serialVersionUID = 1L;
//    public static final String KEY = "";

    /**
     *
     */
    private Long id;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 名称
     */
    @Length(max = 45, message = "名称最大长度不能超过45个字符")
    private String name;

    /**
     * 路径
     */
    @NotBlank(message = "路径不能为空。")
    @Length(max = 250, message = "路径最大长度不能超过250个字符")
    private String path;

    /**
     * 链接
     */
    @Length(max = 250, message = "链接最大长度不能超过250个字符")
    private String link;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述最大长度不能超过200个字符")
    private String description;

    /**
     * 状态
     */
    private int status;

    /**
     * 创建时间
     */
    @NotBlank(message = "创建时间不能为空。")
    private Date createTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setType(int value) {
        this.type = value;
    }

    public int getType() {
        return this.type;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public String getPath() {
        return this.path;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public int getStatus() {
        return this.status;
    }

    public void setCreateTime(Date value) {
        this.createTime = value;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setExpireTime(Date value) {
        this.expireTime = value;
    }

    public Date getExpireTime() {
        return this.expireTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static enum imgFileType {

    }
}

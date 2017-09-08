package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * DocImage
 */
public class DocImage implements Serializable {
    /**
     * id
     */
    @NotBlank(message = "id 不能为空。")
    private Long id;
    /**
     * 文档ID
     */
    @NotBlank(message = "文档ID 不能为空。")
    private Long docId;
    /**
     * 页码
     */
    private Integer docPage;
    /**
     * 图片路径
     */
    @Length(max = 150, message = "图片路径 最大长度不能超过150个字符")
    private String path;

    public DocImage() {
    }

    public DocImage(Long docId, Integer docPage, String path) {
        this.docId = docId;
        this.docPage = docPage;
        this.path = path;
    }

    public void setDocId(Long value) {
        this.docId = value;
    }

    public Long getDocId() {
        return this.docId;
    }

    public void setDocPage(Integer value) {
        this.docPage = value;
    }

    public Integer getDocPage() {
        return this.docPage;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public String getPath() {
        return this.path;
    }
}
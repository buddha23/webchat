package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 视频章
 */
public class VodChapters implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 所属视频集
     */
    @NotNull(message = "所属视频集 不能为空。")
    private Integer volumeId;
    /**
     * 名称
     */
    @NotBlank(message = "名称 不能为空。")
    @Length(max = 100, message = "名称 最大长度不能超过100个字符")
    private String name;
    /**
     * 排序
     */
    @NotNull(message = "排序 不能为空。")
    private Integer sorting;
    /*
    * 小节列表
    * */
    private List<VodSection> vodSections;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Integer volumeId) {
        this.volumeId = volumeId;
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

    public List<VodSection> getVodSections() {
        return vodSections;
    }

    public void setVodSections(List<VodSection> vodSections) {
        this.vodSections = vodSections;
    }
}
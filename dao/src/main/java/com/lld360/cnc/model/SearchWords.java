package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * SearchWords
 */
public class SearchWords implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "名称 不能为空。")
    @Length(max = 50, message = "名称 最大长度不能超过50个字符")
    private String name;
    /**
     * 搜索次数
     */
    @NotNull(message = "搜索次数 不能为空。")
    private Integer counts;


    public void setId(Long value) {
        this.id = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setCounts(Integer value) {
        this.counts = value;
    }

    public Integer getCounts() {
        return this.counts;
    }
}
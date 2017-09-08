package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommodityCategory implements Serializable {

    private Integer id;

    private String name;

    private String icon;

    private String description;

    private Integer sorting;

    private Date createTime;

    public CommodityCategory() {

    }

    public CommodityCategory(String name, String icon, String description, Integer sorting) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.sorting = sorting;
        this.createTime = Calendar.getInstance(Locale.CHINA).getTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSorting() {
        return sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

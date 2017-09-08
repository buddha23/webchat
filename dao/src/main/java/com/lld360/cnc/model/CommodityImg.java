package com.lld360.cnc.model;

import java.io.Serializable;

public class CommodityImg implements Serializable {

    private Long id;

    private Long commodityId;

    private String path;

    public CommodityImg(Long commodityId, String path) {
        this.commodityId = commodityId;
        this.path = path;
    }

    public CommodityImg() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

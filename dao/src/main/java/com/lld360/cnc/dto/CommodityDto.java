package com.lld360.cnc.dto;

import com.lld360.cnc.model.Commodity;
import com.lld360.cnc.model.CommodityCategory;
import com.lld360.cnc.model.CommodityImg;

import java.util.List;

public class CommodityDto extends Commodity {

    private String userName;

    private String areaName;

    private CommodityCategory commodityCategory;

    private List<CommodityImg> commodityImgs;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public CommodityCategory getCommodityCategory() {
        return commodityCategory;
    }

    public void setCommodityCategory(CommodityCategory commodityCategory) {
        this.commodityCategory = commodityCategory;
    }

    public List<CommodityImg> getCommodityImgs() {
        return commodityImgs;
    }

    public void setCommodityImgs(List<CommodityImg> commodityImgs) {
        this.commodityImgs = commodityImgs;
    }
}

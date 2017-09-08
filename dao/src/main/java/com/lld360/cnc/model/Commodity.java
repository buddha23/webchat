package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Commodity implements Serializable {

    private Long id;

    @NotBlank(message = "商品名不能为空")
    @Length(max = 50, message = "商品名不能超过20字")
    private String name;

    private BigDecimal price;

    private Byte type;  //类型 1.出售 2.求购

    private Byte newDegree; //新旧 1.全新 2.二手

    private String information; //信息

    private String cover;

    private String area;

    private Long userId;

    @NotBlank(message = "联系方式不能为空")
    private String phone;

    @NotBlank(message = "微信不能为空")
    @Length(max = 50, message = "微信号过长")
    private String weixin;

    private Integer categoryId;

    private Date createTime;

    private Integer views;

    private Byte state;

    /*
    * imgPath
    * 图片路径字符串
    * */
    private String imgPath;

    private List<CommodityImg> commodityImgs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getNewDegree() {
        return newDegree;
    }

    public void setNewDegree(Byte newDegree) {
        this.newDegree = newDegree;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public List<CommodityImg> getCommodityImgs() {
        return commodityImgs;
    }

    public void setCommodityImgs(List<CommodityImg> commodityImgs) {
        this.commodityImgs = commodityImgs;
    }
}

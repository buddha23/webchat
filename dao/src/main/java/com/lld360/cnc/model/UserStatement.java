package com.lld360.cnc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserStatement implements Serializable {

    /*
    * id
    * */
    private Long id;
    /*
   * 用户ID
   * */
    private Long userId;
    /*
   * 交易创建时间
   * */
    private Date tradeTime;
    /*
   * 更新时间
   * */
    private Date updateTime;
    /*
   * 交易额
   * */
    private BigDecimal tradeAmount;
    /*
   * 状态
   * */
    private String tradeState;
    /*
   * 备注
   * */
    private String tradeRemark;
    /*
   * 内部交易号
   * */
    private String innerTradeNo;
    /*
   * 交易号
   * */
    private String tradeNo;
    /*
   * 交易类型
   * */
    private String payType;
    
    /**
     * 产品类别 1、购买积分 2、购买会员
     */
    private byte tradeType;

    /**
     * 充值购买积分
     */
    private Integer buyScore;

    /**
     * 充值赠送积分
     */
    private Integer presentScore;

    /*
    * 相关ID(视频)
    * */
    private Integer objectId;

    public Integer getBuyScore() {
        return buyScore;
    }

    public void setBuyScore(Integer buyScore) {
        this.buyScore = buyScore;
    }

    public Integer getPresentScore() {
        return presentScore;
    }

    public void setPresentScore(Integer presentScore) {
        this.presentScore = presentScore;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeRemark() {
        return tradeRemark;
    }

    public void setTradeRemark(String tradeRemark) {
        this.tradeRemark = tradeRemark;
    }

    public String getInnerTradeNo() {
        return innerTradeNo;
    }

    public void setInnerTradeNo(String innerTradeNo) {
        this.innerTradeNo = innerTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
    

    public byte getTradeType() {
		return tradeType;
	}

	public void setTradeType(byte tradeType) {
		this.tradeType = tradeType;
	}

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        return "UserStatement{" +
                "id=" + id +
                ", userId=" + userId +
                ", tradeTime=" + tradeTime +
                ", updateTime=" + updateTime +
                ", tradeAmount=" + tradeAmount +
                ", tradeState='" + tradeState + '\'' +
                ", tradeRemark='" + tradeRemark + '\'' +
                ", innerTradeNo='" + innerTradeNo + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", payType='" + payType + '\'' +
                ", tradeType='" + tradeType + '\''+
                '}';
    }
}

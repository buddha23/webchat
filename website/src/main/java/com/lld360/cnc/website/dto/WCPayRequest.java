package com.lld360.cnc.website.dto;

public class WCPayRequest {
	private String appId;
	
	private long timeStamp;
	
	private String nonceStr;
	
	private String wxPackage;
	
	private String signType;
	
	private String paySign;
	
	private String innerNo;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getWxPackage() {
		return wxPackage;
	}

	public void setWxPackage(String wxPackage) {
		this.wxPackage = wxPackage;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getInnerNo() {
		return innerNo;
	}

	public void setInnerNo(String innerNo) {
		this.innerNo = innerNo;
	}
}

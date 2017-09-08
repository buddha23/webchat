package com.lld360.cnc.model;

import java.util.Date;
import java.util.List;

public class EnterpriseCertification  {
	
	private Long id;
	
	private Long userId;
	
	private String name;
	
	private String address;
	
	private String registrationNo;
	
	private String registrationImg;
	
	private String linkman;
	
	private String mobile;
	
	private Byte state;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String[] imgPaths;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getRegistrationImg() {
		return registrationImg;
	}

	public void setRegistrationImg(String registrationImg) {
		this.registrationImg = registrationImg;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String[] getImgPaths() {
		return imgPaths;
	}

	public void setImgPaths(String[] imgPaths) {
		this.imgPaths = imgPaths;
	}
	
	
}
